package com.onemenu.server.serviceImpl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.dao.DishDAO;
import com.onemenu.server.dao.DishRankDAO;
import com.onemenu.server.dao.ReviewDAO;
import com.onemenu.server.javabean.DishBean;
import com.onemenu.server.javabean.DishValidationConditionBean;
import com.onemenu.server.javabean.MenuSearchConditionBean;
import com.onemenu.server.javabean.NMenuItemBean;
import com.onemenu.server.javabean.NMenuItemDishBean;
import com.onemenu.server.javabean.NMenuItemReviewBean;
import com.onemenu.server.javabean.NMenuItemReviewCustomerBean;
import com.onemenu.server.javabean.NMenuItemReviewItemBean;
import com.onemenu.server.javabean.RestaurantDeliveryTimeBean;
import com.onemenu.server.model.Dish;
import com.onemenu.server.model.DishRank;
import com.onemenu.server.model.Restaurant;
import com.onemenu.server.model.ReviewItem;
import com.onemenu.server.service.DishService;
import com.onemenu.server.util.AwsS3Utils;
import com.onemenu.server.util.FormatUtils;
import com.onemenu.server.util.JsonFormatUtils;
import com.onemenu.server.util.MapUtils;
import com.onemenu.server.util.StringUtil;
import com.onemenu.server.util.TimestampUtil;
import com.onemenu.server.util.TimestampUtil.DateFormatType;

public class DishServiceImpl extends AbstractServiceImpl implements DishService {

    private final static Logger logger = Logger.getLogger(DishServiceImpl.class);

    private DishDAO mDishDAO;

    private ReviewDAO mReviewDAO;

    private DishRankDAO mdDishRankDAO;

    @Autowired
    public void setmReviewDAO(ReviewDAO mReviewDAO) {
        this.mReviewDAO = mReviewDAO;
    }

    @Autowired
    public void setmDishDAO(DishDAO mDishDAO) {
        this.mDishDAO = mDishDAO;
    }

    @Autowired
    public void setMdDishRankDAO(DishRankDAO mdDishRankDAO) {
        this.mdDishRankDAO = mdDishRankDAO;
    }

    @Override
    public void addPageView(long dishId) {
        mdDishRankDAO.addPageView(dishId);
    }

    @Override
    public List<DishBean> validateDish(DishValidationConditionBean condition, List<Long> dishIdList)
            throws ParseException {

        List<DishBean> dishBeanList = new ArrayList<DishBean>();

        List<Dish> dishList = mDishDAO.validateDish(condition, dishIdList);

        for (Dish dish : dishList) {

            DishBean dishBean = new DishBean();

            dishBean.dishId = String.valueOf(dish.getmId());
            dishBean.dishName = String.valueOf(dish.getmName());

            dishBeanList.add(dishBean);
        }

        return dishBeanList;
    }

    @Override
    public List<DishBean> listDishItemByCondition(MenuSearchConditionBean condition) {

        List<Dish> dishList = this.listDishByCondition(condition);

        List<DishBean> jsonList = new ArrayList<DishBean>();
        for (int i = 0; i < dishList.size(); i++) {
            Dish dish = dishList.get(i);
            DishBean json = new DishBean();
            json.dishId = String.valueOf(dish.getmId());
            json.dishName = dish.getmName();
            json.dishPrice = String.valueOf(dish.getmPrice());
            json.dishImageUrl =
                    getDishImageUrl(dish.getmImageName(), dish.getmDishCategory().getmRestaurant());
            json.type = ParameterConstant.MENU_ITEM_TYPE_DISH;

            jsonList.add(json);
        }

        return jsonList;
    }

    @Override
    public DishBean getDishBeanById(MenuSearchConditionBean condition, long id) {

        Dish dish = getBaseDAO().findById(Dish.class, id);
        Restaurant restaurant = dish.getmDishCategory().getmRestaurant();
        DishBean dishBean = new DishBean();
        String curTime = TimestampUtil.dateToStr(new Date(), DateFormatType.Time24);
        if (dish != null) {

            dishBean.dishId = String.valueOf(dish.getmId());
            dishBean.dishPrice = String.valueOf(dish.getmPrice());
            if (dish.getmName() != null)
                dishBean.dishName = dish.getmName();
            if (dish.getmDescription() != null) {
                dishBean.desc = dish.getmDescription();
            }
            if (dish.getmCustomization() != null) {
                dishBean.customization = JsonFormatUtils.getCustomization(dish.getmCustomization());
            }
            if (dish.getmDishCategory().getmRestaurant().getmFolderName() != null
                    && dish.getmImageName() != null)
                dishBean.dishImageUrl =
                        AwsS3Utils.getAwsS3RestaurantResUrl()
                                + dish.getmDishCategory().getmRestaurant().getmFolderName() + "/"
                                + dish.getmImageName();

            dishBean.restaurant.restaurantId =
                    String.valueOf(dish.getmDishCategory().getmRestaurant().getmId());
            if (dish.getmDishCategory().getmRestaurant().getmName() != null)
                dishBean.restaurant.restaurantName =
                        dish.getmDishCategory().getmRestaurant().getmName();
            dishBean.restaurant.freeDeliveryLimit =
                    String.valueOf(dish.getmDishCategory().getmRestaurant().getmFreeDeliveryLimit());
            dishBean.restaurant.deliveryFee =
                    String.valueOf(dish.getmDishCategory().getmRestaurant().getmDeliveryFee());
            if (dish.getmDishCategory().getmRestaurant().getmFolderName() != null
                    && dish.getmDishCategory().getmRestaurant().getmLogo() != null)
                dishBean.restaurant.restaurantLogoUrl =
                        AwsS3Utils.getAwsS3RestaurantResUrl()
                                + dish.getmDishCategory().getmRestaurant().getmFolderName() + "/"
                                + dish.getmDishCategory().getmRestaurant().getmLogo();

            // set different open time and delivery through different week
            int week = FormatUtils.getCurrentWeek();
            RestaurantDeliveryTimeBean restaurantDeliveryTimeBean =
                    new RestaurantDeliveryTimeBean();
            switch (week) {

                case 0:
                    if (restaurant.getmSunOpenStartTime() != null)
                        dishBean.restaurant.openingStartTime = restaurant.getmSunOpenStartTime();
                    if (restaurant.getmSunOpenEndTime() != null)
                        dishBean.restaurant.openingEndTime = restaurant.getmSunOpenEndTime();
                    if (restaurant.getmSunDeliveryStartTime() != null)
                        restaurantDeliveryTimeBean.startTime =
                                restaurant.getmSunDeliveryStartTime();
                    if (restaurant.getmSunDeliveryEndTime() != null)
                        restaurantDeliveryTimeBean.endTime = restaurant.getmSunDeliveryEndTime();
                    break;

                case 1:
                    if (restaurant.getmMonOpenStartTime() != null)
                        dishBean.restaurant.openingStartTime = restaurant.getmMonOpenStartTime();
                    if (restaurant.getmMonOpenEndTime() != null)
                        dishBean.restaurant.openingEndTime = restaurant.getmMonOpenEndTime();
                    if (restaurant.getmMonDeliveryStartTime() != null)
                        restaurantDeliveryTimeBean.startTime =
                                restaurant.getmMonDeliveryStartTime();
                    if (restaurant.getmMonDeliveryEndTime() != null)
                        restaurantDeliveryTimeBean.endTime = restaurant.getmMonDeliveryEndTime();
                    break;

                case 2:
                    if (restaurant.getmTueOpenStartTime() != null)
                        dishBean.restaurant.openingStartTime = restaurant.getmTueOpenStartTime();
                    if (restaurant.getmTueOpenEndTime() != null)
                        dishBean.restaurant.openingEndTime = restaurant.getmTueOpenEndTime();
                    if (restaurant.getmTueDeliveryStartTime() != null)
                        restaurantDeliveryTimeBean.startTime =
                                restaurant.getmTueDeliveryStartTime();
                    if (restaurant.getmTueDeliveryEndTime() != null)
                        restaurantDeliveryTimeBean.endTime = restaurant.getmTueDeliveryEndTime();
                    break;

                case 3:
                    if (restaurant.getmWedOpenStartTime() != null)
                        dishBean.restaurant.openingStartTime = restaurant.getmWedOpenStartTime();
                    if (restaurant.getmWedOpenEndTime() != null)
                        dishBean.restaurant.openingEndTime = restaurant.getmWedOpenEndTime();
                    if (restaurant.getmWedDeliveryStartTime() != null)
                        restaurantDeliveryTimeBean.startTime =
                                restaurant.getmWedDeliveryStartTime();
                    if (restaurant.getmWedDeliveryEndTime() != null)
                        restaurantDeliveryTimeBean.endTime = restaurant.getmWedDeliveryEndTime();
                    break;

                case 4:
                    if (restaurant.getmThuOpenStartTime() != null)
                        dishBean.restaurant.openingStartTime = restaurant.getmThuOpenStartTime();
                    if (restaurant.getmThuOpenEndTime() != null)
                        dishBean.restaurant.openingEndTime = restaurant.getmThuOpenEndTime();
                    if (restaurant.getmThuDeliveryStartTime() != null)
                        restaurantDeliveryTimeBean.startTime =
                                restaurant.getmThuDeliveryStartTime();
                    if (restaurant.getmThuDeliveryEndTime() != null)
                        restaurantDeliveryTimeBean.endTime = restaurant.getmThuDeliveryEndTime();
                    break;

                case 5:
                    if (restaurant.getmFriOpenStartTime() != null)
                        dishBean.restaurant.openingStartTime = restaurant.getmFriOpenStartTime();
                    if (restaurant.getmFriOpenEndTime() != null)
                        dishBean.restaurant.openingEndTime = restaurant.getmFriOpenEndTime();
                    if (restaurant.getmFriDeliveryStartTime() != null)
                        restaurantDeliveryTimeBean.startTime =
                                restaurant.getmFriDeliveryStartTime();
                    if (restaurant.getmFriDeliveryEndTime() != null)
                        restaurantDeliveryTimeBean.endTime = restaurant.getmFriDeliveryEndTime();
                    break;

                case 6:
                    if (restaurant.getmSatOpenStartTime() != null)
                        dishBean.restaurant.openingStartTime = restaurant.getmSatOpenStartTime();
                    if (restaurant.getmSatOpenEndTime() != null)
                        dishBean.restaurant.openingEndTime = restaurant.getmSatOpenEndTime();
                    if (restaurant.getmSatDeliveryStartTime() != null)
                        restaurantDeliveryTimeBean.startTime =
                                restaurant.getmSatDeliveryStartTime();
                    if (restaurant.getmSatDeliveryEndTime() != null)
                        restaurantDeliveryTimeBean.endTime = restaurant.getmSatDeliveryEndTime();
                    break;

                default:
                    break;
            }

            // 营业开始时间
            String beginHour = dishBean.restaurant.openingStartTime;
            // 营业结束时间
            String endHour = dishBean.restaurant.openingEndTime;
            // 如果超过则设置为非营业时间（默认是 营业时间）
            if (curTime.compareTo(beginHour) < 0 || curTime.compareTo(endHour) > 0)
                dishBean.restaurant.isOutOfBusinessHour = "1";

            // 送餐时间
            String beginTime = restaurantDeliveryTimeBean.startTime;
            String endTime = restaurantDeliveryTimeBean.endTime;
            if (curTime.compareTo(beginTime) < 0 || curTime.compareTo(endTime) > 0)
                dishBean.restaurant.isOutOfDeliveryTime = "1";



            dishBean.restaurant.deliveryTimeArray.add(restaurantDeliveryTimeBean);

            if (dish.getmStatus() != null) {
                dishBean.dishStatus = dish.getmStatus();
            }

            if (dish.getmDishCategory().getmRestaurant().getmStatus() != null) {
                dishBean.restaurant.restaurantStatus =
                        String.valueOf(dish.getmDishCategory().getmRestaurant().getmStatus());
            }

            dishBean.restaurant.minDeliveryTotal =
                    String.valueOf(restaurant.getmMinDeliveryTotal());
            dishBean.restaurant.deliveryDistance =
                    String.valueOf(restaurant.getmDeliveryDistance());

            if (condition.latitude == null || condition.longitude == null) {
                dishBean.restaurant.distance = "999999";

            } else {
                dishBean.restaurant.distance =
                        String.valueOf(MapUtils.getDistance(condition.latitude,
                                condition.longitude, dish.getmDishCategory().getmRestaurant()
                                        .getmLatitude(), dish.getmDishCategory().getmRestaurant()
                                        .getmLongitude()));

                if (dishBean.restaurant.distance.compareTo(dishBean.restaurant.deliveryDistance) <= 0)
                    dishBean.restaurant.isOutOfRange = "0";
            }



        }

        return dishBean;
    }



    @Override
    public List<DishBean> listDishMenuItemByCondition(MenuSearchConditionBean condition) {

        List<Dish> dishList = mDishDAO.getDishMenuItemListByConditon(condition);

        List<DishBean> dishBeanlList = new ArrayList<DishBean>();
        for (Dish dish : dishList) {
            DishBean dishBean = new DishBean();

            dishBean.dishId = String.valueOf(dish.getmId());
            if (dish.getmName() != null)
                dishBean.dishName = dish.getmName();
            if (dish.getmPrice() != null)
                dishBean.dishPrice = FormatUtils.formatPrice(dish.getmPrice());
            if (dish.getmImageName() != null && dish.getmDishCategory().getmRestaurant() != null)
                dishBean.dishImageUrl =
                        getDishImageUrl(dish.getmImageName(), dish.getmDishCategory()
                                .getmRestaurant());

            int pageView = 0;
            int purchaseVolume = 0;
            DishRank dishRank = dish.getmDishRank();
            if (dishRank != null) {
                pageView = (int) dishRank.getmPageView();
                purchaseVolume = (int) dishRank.getmPurchaseVolume();
            }

            dishBean.rank = (double) (pageView + purchaseVolume);

            dishBeanlList.add(dishBean);
        }

        // Collections.sort(dishBeanlList, new Comparator<DishBean>() {
        //
        // public int compare(DishBean arg0, DishBean arg1) {
        // return arg0.rank.compareTo(arg1.rank);
        // }
        // });

        return dishBeanlList;
    }

    public String getDishImageUrl(Dish dish) {

        if (dish.getmImageName() == null || dish.getmImageName() == "") {
            return "";
        }

        if (dish.getmDishCategory() == null) {
            return "";
        }

        if (dish.getmDishCategory().getmRestaurant() == null) {
            return "";
        }

        return AwsS3Utils.getAwsS3RestaurantResUrl()
                + dish.getmDishCategory().getmRestaurant().getmFolderName() + "/"
                + dish.getmImageName();
    }


    private String getDishImageUrl(String dishImageName, Restaurant restaurant) {

        if (restaurant == null) {
            return "";
        }

        if (dishImageName == null || dishImageName == "") {
            return "";
        }

        return AwsS3Utils.getAwsS3RestaurantResUrl() + restaurant.getmFolderName() + "/"
                + dishImageName;
    }

    private List<Dish> listDishByCondition(MenuSearchConditionBean condition) {

        DetachedCriteria cri = DetachedCriteria.forClass(Dish.class);
        if (!TextUtils.isEmpty(condition.keyWord)) {
            String[] keyWords = condition.keyWord.split(",");
            Junction junction = Restrictions.disjunction();

            for (int i = 0; i < keyWords.length; i++) {
                String keyWord = keyWords[i];
                junction.add(Restrictions.ilike("mTags", keyWord, MatchMode.ANYWHERE));
                junction.add(Restrictions.ilike("mName", keyWord, MatchMode.ANYWHERE));
            }
            cri.add(junction);
        }

        List<Dish> list =
                getBaseDAO().findByCriteria(cri, condition.startNum, condition.range, Dish.class);

        return list;
    }

    @Override
    public List<NMenuItemBean> menuItemDishsByCondition(MenuSearchConditionBean condition) {

        List<NMenuItemDishBean> list = mDishDAO.getDishMenuItemsByConditon(condition);

        List<NMenuItemBean> _list = new ArrayList<NMenuItemBean>(list.size());

        for (NMenuItemDishBean bean : list) {
            if (logger.isDebugEnabled())
                logger.debug("NMenuItemDishBean:" + bean);
            bean.dishImageUrl = AwsS3Utils.getAwsS3RestaurantResUrl() + bean.dishImageUrl;
            _list.add(bean);
            if (logger.isDebugEnabled())
                logger.debug("NMenuItemDishBean:" + bean);
        }

        return _list;
    }

    @Override
    public List<NMenuItemBean> menuItemDishAndReviewsByCondition(MenuSearchConditionBean condition) {

        Map<String, String> results = mDishDAO.getDishAndReviewsMenuItemsByConditon(condition);
        List<NMenuItemBean> list = new ArrayList<NMenuItemBean>(results.size());
        for (String key : results.keySet()) {
            String type = key.substring(0, 1);
            String id = key.substring(1);
            NMenuItemBean bean = null;
            if (ParameterConstant.MENU_ITEM_TYPE_DISH.equals(type)) {
                NMenuItemDishBean _bean = mDishDAO.getDishMenuItemById(id);
                if (StringUtil.isEmpty(_bean.dishImageUrl))
                    continue;
                _bean.dishImageUrl = AwsS3Utils.getAwsS3RestaurantResUrl() + _bean.dishImageUrl;
                bean = _bean;
            } else {
                NMenuItemReviewBean _bean = mReviewDAO.getReviewMenuItemById(id);
                DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ReviewItem.class);
                detachedCriteria
                        .add(Restrictions.eq("mReview.mId", Long.parseLong(_bean.reviewId)));
                List<ReviewItem> reviewItems =
                        mReviewDAO.findByCriteria(detachedCriteria, ReviewItem.class);
                List<NMenuItemReviewItemBean> urls =
                        new ArrayList<NMenuItemReviewItemBean>(reviewItems.size());
                for (ReviewItem reviewItem : reviewItems) {
                	
                	Dish dish = mReviewDAO.findById(Dish.class, Long.valueOf(reviewItem.getmDishId()));
    				NMenuItemReviewItemBean itemBean = new NMenuItemReviewItemBean(AwsS3Utils.getAwsS3RootResUrl() + reviewItem.getmImageUrl());
    				itemBean.comment = reviewItem.getmComment();
    				itemBean.dishId = reviewItem.getmDishId();
    				itemBean.dishName = dish.getmName();
    				itemBean.dishPrice = String.valueOf(dish.getmPrice());
    				urls.add(itemBean);
                }
                _bean.reviewItemArray = urls;
                NMenuItemReviewCustomerBean customer = new NMenuItemReviewCustomerBean();
                customer.setAvatarUrl(AwsS3Utils.getAwsS3CustomerResUrl() + _bean.avatarUrl);
                //customer.setCustomerCompany(_bean.customerCompany);
                //customer.setCustomerName(_bean.customerName);
                //customer.setCustomerProfession(_bean.customerProfession);

                //
                _bean.avatarUrl = null;
                _bean.customerCompany = null;
                _bean.customerName = null;
                _bean.customerProfession = null;
                //_bean.summary= null;

                _bean.customer = customer;

                bean = _bean;
            }
            list.add(bean);
        }
        return list;

    }

    @Override
    public List<NMenuItemDishBean> validateDishs(DishValidationConditionBean condition,
            List<Long> dishIdList) throws Exception {

        List<NMenuItemDishBean> dishBeanList = new ArrayList<NMenuItemDishBean>();

        List<Dish> dishList = mDishDAO.validateDish(condition, dishIdList);

        for (Dish dish : dishList) {

            NMenuItemDishBean dishBean = new NMenuItemDishBean();

            dishBean.dishId = String.valueOf(dish.getmId());
            dishBean.dishName = String.valueOf(dish.getmName());

            dishBeanList.add(dishBean);
        }

        return dishBeanList;
    }

}
