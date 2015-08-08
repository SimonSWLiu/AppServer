package com.onemenu.server.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onemenu.server.dao.ReviewDAO;
import com.onemenu.server.javabean.MenuSearchConditionBean;
import com.onemenu.server.javabean.NMenuItemBean;
import com.onemenu.server.javabean.NMenuItemReviewBean;
import com.onemenu.server.javabean.NMenuItemReviewCustomerBean;
import com.onemenu.server.javabean.NMenuItemReviewItemBean;
import com.onemenu.server.javabean.ReviewBean;
import com.onemenu.server.javabean.ReviewItemBean;
import com.onemenu.server.model.Customer;
import com.onemenu.server.model.Dish;
import com.onemenu.server.model.Review;
import com.onemenu.server.model.ReviewItem;
import com.onemenu.server.model.ReviewRank;
import com.onemenu.server.service.ReviewService;
import com.onemenu.server.util.AwsS3Utils;
import com.onemenu.server.util.ImageUtils;
import com.onemenu.server.util.MimeUtils;
import com.onemenu.server.util.TimestampUtil;


public class ReviewServiceImpl extends AbstractServiceImpl implements ReviewService {
	
	private static final Logger logger = Logger.getLogger(ReviewServiceImpl.class);
	
    private ReviewDAO mReviewDAO;

    @Autowired
    public void setmReviewDAO(ReviewDAO mReviewDAO) {
        this.mReviewDAO = mReviewDAO;
    }
    
    @Override
    public void addPageView(long reviewId) {
        mReviewDAO.addPageView(reviewId);
    }


    @Override
    public List<ReviewBean> listReviewMenuItemByCondition(MenuSearchConditionBean condition) {

        List<Review> reviewList = mReviewDAO.getReviewListByCondition(condition);

        List<ReviewBean> reviewBeanlList = new ArrayList<ReviewBean>();
        for (int i = 0; i < reviewList.size(); i++) {
            Review review = reviewList.get(i);

            ReviewBean reviewBean = new ReviewBean();

            reviewBean.reviewId = String.valueOf(review.getmId());
            if (review.getmSummary() != null)
                reviewBean.summary = review.getmSummary();
            if (review.getmCustomer().getmAvatar() != null
                    && review.getmCustomer().getmFolderName() != null)
                reviewBean.customer.avatarUrl =
                        AwsS3Utils.getAwsS3CustomerResUrl()
                                + review.getmCustomer().getmFolderName() + "/"
                                + review.getmCustomer().getmAvatar();
            if (review.getmCustomer().getmName() != null)
                reviewBean.customer.customerName = review.getmCustomer().getmName();
            if (review.getmCustomer().getmProfession() != null)
                reviewBean.customer.customerProfession = review.getmCustomer().getmProfession();
            if (review.getmCustomer().getmCompany() != null)
                reviewBean.customer.customerCompany = review.getmCustomer().getmCompany();

            for (ReviewItem reviewItem : review.getmReviewItemSet()) {

                ReviewItemBean reviewItemBean = new ReviewItemBean();
                if (reviewItem.getmComment() != null)
                    reviewItemBean.comment = reviewItem.getmComment();
                if (reviewItem.getmDishId() != null)
                    reviewItemBean.dishId = reviewItem.getmDishId();
                if (reviewItem.getmDishName() != null)
                    reviewItemBean.dishName = reviewItem.getmDishName();
                if (reviewItem.getmImageUrl() != null)
                    reviewItemBean.dishImageUrl =
                            AwsS3Utils.getAwsS3RootResUrl() + reviewItem.getmImageUrl();

                reviewBean.reviewItemArray.add(reviewItemBean);
            }

            int pageView = 0;
            ReviewRank reviewRank = review.getmReviewRank();
            if (reviewRank != null) {
                // ranking
                pageView = (int) review.getmReviewRank().getmPageView();
            }

            reviewBean.rank = (double) (pageView);

            if (review.getmCreateTimestamp() != null) {
                reviewBean.createTimestamp = review.getmCreateTimestamp();
                reviewBean.timestamp = TimestampUtil.formatTimestamp(review.getmCreateTimestamp());//modify by file  统一使用新的时间工具类
            }

            reviewBeanlList.add(reviewBean);
        }

        return reviewBeanlList;
    }

    @Override
    public List<ReviewBean> listReviewBeanByCustomerId(long customerId) {
        
        List<Review> reviewList = mReviewDAO.findByCustomerId(customerId);

        List<ReviewBean> reviewBeanList = new ArrayList<ReviewBean>();
        for (Review review : reviewList) {

            ReviewBean reviewBean = new ReviewBean();

            reviewBean.reviewId = String.valueOf(review.getmId());
            if (review.getmSummary() != null)
                reviewBean.summary = review.getmSummary();
            if (review.getmCustomer().getmAvatar() != null
                    && review.getmCustomer().getmFolderName() != null)
                reviewBean.customer.avatarUrl =
                        AwsS3Utils.getAwsS3CustomerResUrl()
                                + review.getmCustomer().getmFolderName() + "/"
                                + review.getmCustomer().getmAvatar();
            if (review.getmCustomer().getmName() != null)
                reviewBean.customer.customerName = review.getmCustomer().getmName();
            if (review.getmCustomer().getmProfession() != null)
                reviewBean.customer.customerProfession = review.getmCustomer().getmProfession();
            if (review.getmCustomer().getmCompany() != null)
                reviewBean.customer.customerCompany = review.getmCustomer().getmCompany();

            for (ReviewItem reviewItem : review.getmReviewItemSet()) {

                ReviewItemBean reviewItemBean = new ReviewItemBean();
                
                if (reviewItem.getmComment() != null)
                    reviewItemBean.comment = reviewItem.getmComment();
                if (reviewItem.getmDishId() != null)
                    reviewItemBean.dishId = reviewItem.getmDishId();
                if (reviewItem.getmDishName() != null)
                    reviewItemBean.dishName = reviewItem.getmDishName();
                if (reviewItem.getmImageUrl() != null)
                    reviewItemBean.dishImageUrl =
                            AwsS3Utils.getAwsS3RootResUrl() + reviewItem.getmImageUrl();

                reviewBean.reviewItemArray.add(reviewItemBean);
            }

            // TODO
            if (review.getmCreateTimestamp() != null) {
                reviewBean.createTimestamp = review.getmCreateTimestamp();
                reviewBean.timestamp = TimestampUtil.formatTimestamp(review.getmCreateTimestamp());//modify by file  统一使用新的时间工具类
            }

            reviewBeanList.add(reviewBean);
        }

        return reviewBeanList;
    }

    @Override
    public ReviewBean getReviewBean(long id) {

        Review review = mReviewDAO.findByReviewId(id);

        ReviewBean reviewBean = new ReviewBean();

        if (review != null) {

            reviewBean.reviewId = String.valueOf(review.getmId());
            if (review.getmSummary() != null)
                reviewBean.summary = review.getmSummary();
            if (review.getmCustomer().getmAvatar() != null
                    && review.getmCustomer().getmFolderName() != null)
                reviewBean.customer.avatarUrl =
                        AwsS3Utils.getAwsS3CustomerResUrl()
                                + review.getmCustomer().getmFolderName() + "/"
                                + review.getmCustomer().getmAvatar();
            if (review.getmCustomer().getmName() != null)
                reviewBean.customer.customerName = review.getmCustomer().getmName();
            if (review.getmCustomer().getmProfession() != null)
                reviewBean.customer.customerProfession = review.getmCustomer().getmProfession();
            if (review.getmCustomer().getmCompany() != null)
                reviewBean.customer.customerCompany = review.getmCustomer().getmCompany();

            for (ReviewItem reviewItem : review.getmReviewItemSet()) {

                ReviewItemBean reviewItemBean = new ReviewItemBean();
                if (reviewItem.getmComment() != null)
                    reviewItemBean.comment = reviewItem.getmComment();
                if (reviewItem.getmDishId() != null)
                    reviewItemBean.dishId = reviewItem.getmDishId();
                if (reviewItem.getmDishName() != null)
                    reviewItemBean.dishName = reviewItem.getmDishName();
                if (reviewItem.getmImageUrl() != null)
                    reviewItemBean.dishImageUrl =
                            AwsS3Utils.getAwsS3RootResUrl() + reviewItem.getmImageUrl();

                reviewBean.reviewItemArray.add(reviewItemBean);
            }
        }

        if (review.getmCreateTimestamp() != null) {
            reviewBean.createTimestamp = review.getmCreateTimestamp();
            reviewBean.timestamp = TimestampUtil.formatTimestamp(review.getmCreateTimestamp());  //modify by file  统一使用新的时间工具类
        }

        return reviewBean;
    }

    @Override
    @Transactional
    public Review saveReviewBean(ReviewBean reviewBean) {

        Review review = new Review();

        try {

            Timestamp curTimestamp = TimestampUtil.getCurrentTimestamp(); //modify by file  统一使用新的时间工具类

            review.setmSummary(reviewBean.summary);
            review.setmCreateTimestamp(curTimestamp);

            Customer customer = findById(Customer.class, new Long(reviewBean.customer.customerId));
            review.setmCustomer(customer);

            Set<ReviewItem> reviewItemSet = new HashSet<ReviewItem>();
            for (ReviewItemBean reviewItemBean : reviewBean.reviewItemArray) {

                ReviewItem reviewItem = new ReviewItem();
                reviewItem.setmComment(reviewItemBean.comment);
                reviewItem.setmDishId(reviewItemBean.dishId);
                reviewItem.setmDishName(reviewItemBean.dishName);
                reviewItem.setmCreateTimestamp(curTimestamp);

                if (reviewItemBean.dishImageUrl.equals("")) {

                    String base64Str = reviewItemBean.base64Str;
                    String widthStr =
                            String.valueOf((int) Double.parseDouble(reviewItemBean.width));
                    String heightStr =
                            String.valueOf((int) Double.parseDouble(reviewItemBean.height));

                    if (!base64Str.equals("")) {

                        // Get folder
                        String folderName = customer.getmFolderName();

                        // Generate image name
                        String mimetype = AwsS3Utils.getMineType(base64Str);
                        String extension = MimeUtils.guessExtensionFromMimeType(mimetype);
                        String imageName =
                                AwsS3Utils.generateImageName(widthStr, heightStr, extension);

                        // upload image to s3
                        InputStream is = ImageUtils.getInputStreamFromBase64(base64Str);
                        ObjectMetadata metaddata = new ObjectMetadata();
                        metaddata.setContentType(mimetype);

                        metaddata.setContentLength(is.available());

                        AwsS3Utils.uploadFileToAwsS3CustomerRes(folderName, imageName, is,
                                metaddata);
                        reviewItemBean.dishImageUrl =
                                AwsS3Utils.getAwsS3CustomerResUrl() + folderName + "/" + imageName;
                    }
                    reviewItem.setmImageUrl(reviewItemBean.dishImageUrl.replace(
                            AwsS3Utils.getAwsS3RootResUrl(), ""));
                }

                reviewItem.setmImageUrl(reviewItemBean.dishImageUrl.replace(
                        AwsS3Utils.getAwsS3RootResUrl(), ""));
                reviewItem.setmReview(review);
                reviewItemSet.add(reviewItem);
            }

            review.setmReviewItemSet(reviewItemSet);
            save(review);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return review;
    }

	@Override
	public List<NMenuItemBean> menuItemReviewsByCondition(MenuSearchConditionBean condition) {
		List<NMenuItemReviewBean>  list = mReviewDAO.getReviewsByCondition(condition);
		List<NMenuItemBean> _list = new ArrayList<NMenuItemBean>();
		for(NMenuItemReviewBean menuItemReviewBean:list){
			if(logger.isDebugEnabled())
				logger.debug("menuItemReviewBean:"+menuItemReviewBean);
			DetachedCriteria detachedCriteria  = DetachedCriteria.forClass(ReviewItem.class);
			detachedCriteria.add(Restrictions.eq("mReview.mId", Long.parseLong(menuItemReviewBean.reviewId)));
			List<ReviewItem> reviewItems = mReviewDAO.findByCriteria(detachedCriteria  , ReviewItem.class);
			List<NMenuItemReviewItemBean>  urls = new ArrayList<NMenuItemReviewItemBean>(reviewItems.size());
			for(ReviewItem reviewItem:reviewItems){
				Dish dish = mReviewDAO.findById(Dish.class, Long.valueOf(reviewItem.getmDishId()));
				NMenuItemReviewItemBean bean = new NMenuItemReviewItemBean(AwsS3Utils.getAwsS3RootResUrl() + reviewItem.getmImageUrl());
				bean.comment = reviewItem.getmComment();
				bean.dishId = reviewItem.getmDishId();
				bean.dishName = dish.getmName();
				bean.dishPrice = String.valueOf(dish.getmPrice());
				urls.add(bean);
			}
			menuItemReviewBean.reviewItemArray = urls;
			
			NMenuItemReviewCustomerBean customer = new NMenuItemReviewCustomerBean();
			customer.setAvatarUrl(AwsS3Utils.getAwsS3CustomerResUrl() +  menuItemReviewBean.avatarUrl);
			//customer.setCustomerCompany(menuItemReviewBean.customerCompany);
			//customer.setCustomerName(menuItemReviewBean.customerName);
			//customer.setCustomerProfession(menuItemReviewBean.customerProfession);
			
			//
			menuItemReviewBean.avatarUrl=null;
			menuItemReviewBean.customerCompany=null;
			menuItemReviewBean.customerName =null;
			menuItemReviewBean.customerProfession =null;
			//menuItemReviewBean.summary=null;
			
			menuItemReviewBean.customer = customer;
			
			if(logger.isDebugEnabled())
				logger.debug("menuItemReviewBean:"+menuItemReviewBean);
			_list.add(menuItemReviewBean);
		}
		return _list;
	}


}
