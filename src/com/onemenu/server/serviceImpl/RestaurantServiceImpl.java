package com.onemenu.server.serviceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.dao.DishDAO;
import com.onemenu.server.dao.RestaurantDAO;
import com.onemenu.server.dao.RestaurantRankDAO;
import com.onemenu.server.javabean.NRestaurantBean;
import com.onemenu.server.javabean.NRestaurantCategoryBean;
import com.onemenu.server.javabean.NRestaurantDetailBean;
import com.onemenu.server.javabean.NRestaurantDishBean;
import com.onemenu.server.javabean.NRestaurantItemBean;
import com.onemenu.server.javabean.RestaurantBean;
import com.onemenu.server.javabean.RestaurantSearchConditionBean;
import com.onemenu.server.model.Dish;
import com.onemenu.server.model.DishCategory;
import com.onemenu.server.model.Restaurant;
import com.onemenu.server.service.RestaurantService;
import com.onemenu.server.util.AwsS3Utils;
import com.onemenu.server.util.MapUtils;
import com.onemenu.server.util.StringUtil;
import com.onemenu.server.util.TimestampUtil;

public class RestaurantServiceImpl extends AbstractServiceImpl implements RestaurantService {

	private RestaurantDAO mRestaurantDAO;
	private DishDAO mDishDAO;
	private RestaurantRankDAO mRestaurantRankDAO;

	@Autowired
	public void setmRestaurantRankDAO(RestaurantRankDAO mRestaurantRankDAO) {
		this.mRestaurantRankDAO = mRestaurantRankDAO;
	}

	@Autowired
	public void setmRestaurantDAO(RestaurantDAO mRestaurantDAO) {
		this.mRestaurantDAO = mRestaurantDAO;
	}

	@Autowired
	public void setmDishDAO(DishDAO mDishDAO) {
		this.mDishDAO = mDishDAO;
	}

	@Override
	public List<RestaurantBean> listRestaurantBean(RestaurantSearchConditionBean condition) {

		List<RestaurantBean> resulatArray = new ArrayList<RestaurantBean>();

		Double latitude = condition.latitude;
		Double longitude = condition.longitude;

		if (latitude == null)
			condition.latitude = 0.0;
		if (longitude == null)
			condition.longitude = 0.0;

		List<Restaurant> restaurantList = mRestaurantDAO.getRestaurantListByConditon(condition);

		for (Restaurant restaurant : restaurantList) {

			RestaurantBean restaurantBean = new RestaurantBean();
			restaurantBean.restaurantId = String.valueOf(restaurant.getmId());
			if (restaurant.getmName() != null)
				restaurantBean.restaurantName = restaurant.getmName();
			double distance = MapUtils.getDistance(condition.latitude, condition.longitude, restaurant.getmLatitude(), restaurant.getmLongitude());
			restaurantBean.distance = (latitude == null || longitude == null) ? "N/A" : String.valueOf(distance) + " miles";
			restaurantBean.distanceOrder = distance;
			if (restaurant.getmFolderName() != null && restaurant.getmLogo() != null)
				restaurantBean.restaurantLogoUrl = AwsS3Utils.getAwsS3RestaurantResUrl() + restaurant.getmFolderName() + "/" + restaurant.getmLogo();

			// 获取对应Dish的图片
			List<Dish> dishList = mDishDAO.getDishListByRestaturantId(restaurant.getmId());

			List<String> imageUrlList = new ArrayList<String>();
			if (dishList.size() > 3) {
				for (int j = 0; j < 3; j++) {
					Dish dish = dishList.get(j);
					String imageUrl = AwsS3Utils.getAwsS3ImageUrl(AwsS3Utils.getAwsS3RestaurantResUrl(), dish.getmDishCategory().getmRestaurant().getmFolderName(),
							dish.getmImageName());
					imageUrlList.add(imageUrl);
				}
			}
			restaurantBean.dishImageArray = imageUrlList;

			resulatArray.add(restaurantBean);
		}

		Collections.sort(resulatArray, new Comparator<RestaurantBean>() {
			public int compare(RestaurantBean arg0, RestaurantBean arg1) {
				return arg0.distanceOrder.compareTo(arg1.distanceOrder);
			}
		});

		return resulatArray;
	}

	public String getRestaurantLogoUrl(Restaurant restaurant) {

		if (restaurant == null) {
			return "";
		}

		if (restaurant.getmFolderName() == null) {
			return "";
		}

		if (restaurant.getmLogo() == null) {
			return "";
		}

		return AwsS3Utils.getAwsS3RestaurantResUrl() + restaurant.getmFolderName() + "/" + restaurant.getmLogo();
	}

	@Override
	public void addPageView(long restaurantId) {
		mRestaurantRankDAO.addPageView(restaurantId);
	}

	@Override
	public List<NRestaurantItemBean> restaurantItems(RestaurantSearchConditionBean condition) {

		List<NRestaurantItemBean> resulatArray = new ArrayList<NRestaurantItemBean>();

		Double latitude = condition.latitude;
		Double longitude = condition.longitude;

		if (latitude == null)
			condition.latitude = 0.0;
		if (longitude == null)
			condition.longitude = 0.0;

		List<Restaurant> restaurantList = mRestaurantDAO.getRestaurantListByConditon(condition);

		for (Restaurant restaurant : restaurantList) {

			NRestaurantItemBean restaurantBean = new NRestaurantItemBean();
			restaurantBean.restaurantId = String.valueOf(restaurant.getmId());
			restaurantBean.setRestaurantName(restaurant.getmName());

			if (latitude == null || longitude == null) {
				restaurantBean.distance = "N/A";
			} else {
				double distance = MapUtils.getDistance(latitude, longitude, restaurant.getmLatitude(), restaurant.getmLongitude());
				restaurantBean.distance = String.format("%.2f", distance) + " miles";
			}

			if (restaurant.getmFolderName() != null && restaurant.getmLogo() != null)
				restaurantBean.restaurantLogoUrl = AwsS3Utils.getAwsS3RestaurantResUrl() + restaurant.getmFolderName() + "/" + restaurant.getmLogo();
			else
				restaurantBean.restaurantLogoUrl = "";

			// 获取对应Dish的图片
			List<Dish> dishList = mDishDAO.getDishListByRestaturantId(restaurant.getmId());
			List<String> imageUrlList = new ArrayList<String>(3);

			List<Dish> hasImageDishs = new ArrayList<Dish>();
			for (Dish dish : dishList) {
				if (StringUtil.isEmpty(dish.getmImageName()) || "null".equals(dish.getmImageName()))
					continue;
				hasImageDishs.add(dish);
			}

			int minSize = hasImageDishs.size() > 3 ? 3 : hasImageDishs.size();

			for (int j = 0; j < minSize; j++) {
				Dish dish = hasImageDishs.get(j);

				String imageUrl = AwsS3Utils.getAwsS3ImageUrl(AwsS3Utils.getAwsS3RestaurantResUrl(), dish.getmDishCategory().getmRestaurant().getmFolderName(),
						dish.getmImageName());
				imageUrlList.add(imageUrl);
			}

			restaurantBean.dishImageArray = imageUrlList;

			resulatArray.add(restaurantBean);
		}

		Collections.sort(resulatArray, new Comparator<NRestaurantItemBean>() {
			public int compare(NRestaurantItemBean arg0, NRestaurantItemBean arg1) {
				if ("N/A".equals(arg0.distance)) {
					return -1;
				}
				return arg0.distance.compareTo(arg1.distance);
			}
		});

		return resulatArray;
	}

	@Override
	@Transactional
	public NRestaurantDetailBean getRestaurantDetailInfo(long restaurantId) {

		Restaurant restaurant = mRestaurantDAO.findById(Restaurant.class, restaurantId);
		if (restaurant == null)
			return null;

		NRestaurantDetailBean bean = new NRestaurantDetailBean();
		NRestaurantBean restBean = new NRestaurantBean();
		bean.restaurant = restBean;
		if (restaurant.getmFolderName() != null && restaurant.getmLogo() != null)
			restBean.restLogoImageUrl = AwsS3Utils.getAwsS3RestaurantResUrl() + restaurant.getmFolderName() + "/" + restaurant.getmLogo();
		else
			restBean.restLogoImageUrl = "";

		restBean.restAddress = restaurant.getmAddress();
		restBean.restDescription = restaurant.getmDescription();
		restBean.restLatitude = String.valueOf(restaurant.getmLatitude());
		restBean.restLongitude = String.valueOf(restaurant.getmLongitude());

		int curWeek = TimestampUtil.getCurrentWeek();

		switch (curWeek) {
		case 0:
			restBean.restOpenHourToday = restaurant.getmSunOpenStartTime() + "-" + restaurant.getmSunOpenEndTime();
			restBean.restDeliveryHourToday = restaurant.getmSunDeliveryStartTime() + "-" + restaurant.getmSunDeliveryEndTime();
			break;
		case 1:
			restBean.restOpenHourToday = restaurant.getmMonOpenStartTime() + "-" + restaurant.getmMonOpenEndTime();
			restBean.restDeliveryHourToday = restaurant.getmMonDeliveryStartTime() + "-" + restaurant.getmMonDeliveryEndTime();
			break;
		case 2:
			restBean.restOpenHourToday = restaurant.getmTueOpenStartTime() + "-" + restaurant.getmTueOpenEndTime();
			restBean.restDeliveryHourToday = restaurant.getmTueDeliveryStartTime() + "-" + restaurant.getmTueDeliveryEndTime();
			break;
		case 3:
			restBean.restOpenHourToday = restaurant.getmWedOpenStartTime() + "-" + restaurant.getmWedOpenEndTime();
			restBean.restDeliveryHourToday = restaurant.getmWedDeliveryStartTime() + "-" + restaurant.getmWedDeliveryEndTime();
			break;
		case 4:
			restBean.restOpenHourToday = restaurant.getmThuOpenStartTime() + "-" + restaurant.getmThuOpenEndTime();
			restBean.restDeliveryHourToday = restaurant.getmThuDeliveryStartTime() + "-" + restaurant.getmThuDeliveryEndTime();
			break;
		case 5:
			restBean.restOpenHourToday = restaurant.getmFriOpenStartTime() + "-" + restaurant.getmFriOpenEndTime();
			restBean.restDeliveryHourToday = restaurant.getmFriDeliveryStartTime() + "-" + restaurant.getmFriDeliveryEndTime();
			break;
		case 6:
			restBean.restOpenHourToday = restaurant.getmSatOpenStartTime() + "-" + restaurant.getmSatOpenEndTime();
			restBean.restDeliveryHourToday = restaurant.getmSatDeliveryStartTime() + "-" + restaurant.getmSatDeliveryEndTime();
			break;
		default:
			break;
		}

		Set<DishCategory> dishCategorys = restaurant.getmDishCategorySet();

		if (dishCategorys == null || dishCategorys.isEmpty()) {
			bean.categoryArray = new ArrayList<NRestaurantCategoryBean>(1);
		} else {
			bean.categoryArray = new ArrayList<NRestaurantCategoryBean>(dishCategorys.size());

			for (DishCategory category : dishCategorys) {

				NRestaurantCategoryBean _bean = new NRestaurantCategoryBean();
				_bean.categoryName = category.getmName();

				Set<Dish> dishs = category.getmDishSet();

				_bean.dishWithOutUrlArray = new ArrayList<NRestaurantDishBean>();
				_bean.dishWithUrlArray = new ArrayList<NRestaurantDishBean>();
				for (Dish dish : dishs) {
					NRestaurantDishBean dishBean = new NRestaurantDishBean();
					dishBean.dishId = String.valueOf(dish.getmId());
					dishBean.dishName = dish.getmName();
					dishBean.dishPrice = String.valueOf(dish.getmPrice());

					if(StringUtil.isEmpty(restaurant.getmFolderName())||StringUtil.isEmpty(dish.getmImageName())){
						_bean.dishWithOutUrlArray.add(dishBean);
					}else{
						dishBean.dishImageUrl =AwsS3Utils.getAwsS3ImageUrl(AwsS3Utils.getAwsS3RestaurantResUrl(), restaurant.getmFolderName(),
								dish.getmImageName()); 
						//dishBean.dishImageWidth = dish.getm
						_bean.dishWithUrlArray.add(dishBean);
					}

				}

			}
		}

		return bean;
	}

}
