package com.onemenu.server.serviceImpl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.dao.ReviewDAO;
import com.onemenu.server.dao.TradeDAO;
import com.onemenu.server.javabean.IngredientBean;
import com.onemenu.server.javabean.NMenuItemBean;
import com.onemenu.server.javabean.NMenuItemDishBean;
import com.onemenu.server.javabean.NMenuItemReviewCustomerBean;
import com.onemenu.server.javabean.NMenuItemReviewItemBean;
import com.onemenu.server.javabean.NMyTasteDishBean;
import com.onemenu.server.javabean.NMyTasteReviewBean;
import com.onemenu.server.javabean.OrderFormBean;
import com.onemenu.server.javabean.OrderItemBean;
import com.onemenu.server.javabean.TradeBean;
import com.onemenu.server.model.Customer;
import com.onemenu.server.model.OrderForm;
import com.onemenu.server.model.OrderItem;
import com.onemenu.server.model.OrderItemIngredient;
import com.onemenu.server.model.Restaurant;
import com.onemenu.server.model.Review;
import com.onemenu.server.model.ReviewItem;
import com.onemenu.server.model.Trade;
import com.onemenu.server.service.TradeService;
import com.onemenu.server.util.AwsS3Utils;
import com.onemenu.server.util.FormatUtils;
import com.onemenu.server.util.GenerateIdUtil;
import com.onemenu.server.util.TimestampUtil;
import com.onemenu.server.util.TimestampUtil.DateFormatType;

public class TradeServiceImpl extends AbstractServiceImpl implements TradeService {

	private TradeDAO	mTradeDAO;

	private ReviewDAO	mReviewDAO;

	@Autowired
	public void setmReviewDAO(ReviewDAO mReviewDAO) {
		this.mReviewDAO = mReviewDAO;
	}

	@Autowired
	public void setmTradeDAO(TradeDAO mTradeDAO) {
		this.mTradeDAO = mTradeDAO;
	}

	@Override
	public List<OrderItemBean> listOrderItemByCustomerId(long customerId) {

		List<OrderItemBean> orderItemBeanList = new ArrayList<OrderItemBean>();

		List<OrderItem> orderItemList = mTradeDAO.getOrderItemListByCustomerId(customerId);

		for (OrderItem orderItem : orderItemList) {

			OrderItemBean orderItemBean = new OrderItemBean();

			if (orderItem.getmDishId() != null)
				orderItemBean.dishId = String.valueOf(orderItem.getmDishId());
			if (orderItem.getmDishName() != null)
				orderItemBean.dishName = orderItem.getmDishName();
			if (orderItem.getmDishImageUrl() != null)
				orderItemBean.dishImageUrl = AwsS3Utils.getAwsS3RootResUrl() + orderItem.getmDishImageUrl();
			if (orderItem.getmCreateTimestamp() != null)
				orderItemBean.createTimestamp = orderItem.getmCreateTimestamp();
			if (orderItem.getmCreateTimestamp() != null)
				orderItemBean.timestamp = TimestampUtil.formatTimestamp(orderItem.getmCreateTimestamp());// modify
																											// by
																											// file
																											// 统一使用新的时间工具类

			orderItemBeanList.add(orderItemBean);
		}

		return orderItemBeanList;
	}

	@Transactional
	public Trade saveTradeBean(TradeBean tradeBean) {

		Timestamp curTimestamp = TimestampUtil.getCurrentTimestamp(); // modify
																		// by
																		// file
																		// 统一使用新的时间工具类

		Trade trade = new Trade();

		if (!tradeBean.customer.customerId.equals("")) {
			Customer customer = findById(Customer.class, new Long(tradeBean.customer.customerId));
			trade.setmCustomer(customer);
		}

		trade.setmCode(genTradeCode());
		trade.setmGetType(tradeBean.tradeGetType);
		trade.setmCouponId(tradeBean.tradeCouponId);
		trade.setmCouponDesc(tradeBean.tradeCouponDesc);
		trade.setmPaymentType(tradeBean.tradePaymentType);
		trade.setmPrice(new BigDecimal(tradeBean.tradePrice));
		trade.setmCustomerName(tradeBean.tradeCustomerName);
		trade.setmCustomerPhone(tradeBean.tradeCustomerPhone);
		trade.setmCustomerAddress(tradeBean.tradeCustomerAddress);
		trade.setmCustomerApt(tradeBean.tradeCustomerApt);
		trade.setmCustomerStreet(tradeBean.tradeCustomerStreet);
		trade.setmCustomerCity(tradeBean.tradeCustomerCity);
		trade.setmCustomerZipCode(tradeBean.tradeCustomerZipCode);
		trade.setmCreateTimestamp(curTimestamp);

		Set<OrderForm> orderFormSet = new HashSet<OrderForm>();
		for (OrderFormBean orderFormBean : tradeBean.orderFormArray) {

			OrderForm orderForm = new OrderForm();

			orderForm.setmCode(genOrderFormCode());
			orderForm.setmRemark(orderFormBean.customerRemark);
			orderForm.setmCouponId(orderFormBean.orderFormCouponId);
			orderForm.setmCouponDesc(orderFormBean.orderFormCouponDesc);
			orderForm.setmTaxFee(new BigDecimal(orderFormBean.taxFee));
			orderForm.setmTipsFee(new BigDecimal(orderFormBean.tipsFee));
			orderForm.setmDeliveryFee(new BigDecimal(orderFormBean.deliveryFee));
			orderForm.setmDiscountFee(new BigDecimal(orderFormBean.discountFee));
			orderForm.setmSubtotalFee(new BigDecimal(orderFormBean.orderFormSubtotal));
			orderForm.setmTotalFee(new BigDecimal(orderFormBean.orderFormTotal));
			orderForm.setmPrice(new BigDecimal(orderFormBean.orderFormPrice));
			orderForm.setmCreateTimestamp(curTimestamp);
			
			orderForm.setmIsOneMenu(1);
			
			orderForm.setmTrade(trade);
			orderForm.setmStatus(ParameterConstant.ORDER_FORM_STATUS_RESTAURANT_PENDING);

			// restaurant
			Restaurant restaurant = findById(Restaurant.class, new Long(orderFormBean.restaurant.restaurantId));
			orderForm.setmRestaurant(restaurant);

			Set<OrderItem> orderItemSet = new HashSet<OrderItem>();
			for (OrderItemBean orderItemBean : orderFormBean.orderItemArray) {

				OrderItem orderItem = new OrderItem();

				orderItem.setmDishId(orderItemBean.dishId);
				orderItem.setmDishName(orderItemBean.dishName);
				orderItem.setmDishImageUrl(orderItemBean.dishImageUrl.replace(AwsS3Utils.getAwsS3RootResUrl(), ""));
				orderItem.setmDishAmount(orderItemBean.dishAmount);
				orderItem.setmDishPrice(orderItemBean.dishPrice);
				orderItem.setmPrice(new BigDecimal(orderItemBean.orderItemPrice));
				orderItem.setmCreateTimestamp(curTimestamp);
				orderItem.setmOrderForm(orderForm);

				Set<OrderItemIngredient> orderItemIngredientSet = new HashSet<OrderItemIngredient>();
				for (IngredientBean ingredientBean : orderItemBean.orderItemIngredientArray) {

					OrderItemIngredient orderItemIngredient = new OrderItemIngredient();

					orderItemIngredient.setmName(ingredientBean.ingredientName);
					orderItemIngredient.setmPrice(new BigDecimal(ingredientBean.ingredientPrice));
					orderItemIngredient.setmCreateTimestamp(curTimestamp);

					orderItemIngredient.setmOrderItem(orderItem);

					orderItemIngredientSet.add(orderItemIngredient);
				}

				orderItem.setmOrderItemIngredientSet(orderItemIngredientSet);
				orderItemSet.add(orderItem);
			}

			orderForm.setmOrderItemSet(orderItemSet);
			orderFormSet.add(orderForm);
		}

		trade.setmOrderFormSet(orderFormSet);
		save(trade);

		return trade;
	}

	private String genTradeCode() {

		long serial = mTradeDAO.findTodayOrderFormRowCount();

		String dateStr = TimestampUtil.dateToStr(new Date(), DateFormatType.DateNoHyphen);// modify
																							// by
																							// file
																							// 统一使用新的时间工具类
		String serialStr = FormatUtils.formatNumStr(serial, "000000");

		return dateStr + serialStr;
	}

	private String genOrderFormCode() {

		return GenerateIdUtil.getOneMenuOrderCode();

		/*
		 * 
		 * long serial = mTradeDAO.findTodayOrderFormRowCount();
		 * 
		 * String dateStr = TimestampUtil.dateToStr(new Date(),
		 * DateFormatType.DateNoHyphen);//modify by file 统一使用新的时间工具类 String
		 * serialStr = FormatUtils.formatNumStr(serial, "000000");
		 * 
		 * return dateStr + serialStr;
		 */
	}

	@Override
	public void payOrderByBraintree(TradeBean tradeBean, String nonce, String amount) {

	}

	@Override
	public List<NMenuItemDishBean> orderItemsByCustomerId(long customerId) {

		List<NMenuItemDishBean> orderItemBeanList = new ArrayList<NMenuItemDishBean>();
		List<OrderItem> orderItemList = mTradeDAO.getOrderItemListByCustomerId(customerId);
		for (OrderItem orderItem : orderItemList) {
			NMenuItemDishBean orderItemBean = new NMenuItemDishBean();
			orderItemBean.dishId = String.valueOf(orderItem.getmDishId());
			orderItemBean.dishName = orderItem.getmDishName();
			orderItemBean.dishImageUrl = AwsS3Utils.getAwsS3RootResUrl() + orderItem.getmDishImageUrl();
			orderItemBeanList.add(orderItemBean);
		}
		return orderItemBeanList;
	}

	@Override
	public int getAllOrderCount() {
		DetachedCriteria tradeCri = DetachedCriteria.forClass(Trade.class);
		tradeCri.setProjection(Projections.rowCount());
		List<?> results = getBaseDAO().findByCriteria(tradeCri, Trade.class);
		return ((Long) results.get(0)).intValue();
	}

	@Override
	public List<NMenuItemBean> getTastesByCustomerId(long customerId) {

		List<NMenuItemBean> tastes = new ArrayList<NMenuItemBean>();

		List<OrderItem> orderItemList = mTradeDAO.getOrderItemListByCustomerId(customerId);
		for (OrderItem orderItem : orderItemList) {

			NMyTasteDishBean taste = new NMyTasteDishBean();
			taste.type = "D";
			taste.dishId = String.valueOf(orderItem.getmDishId());
			taste.dishName = orderItem.getmDishName();
			taste.dishImageUrl = AwsS3Utils.getAwsS3RootResUrl() + orderItem.getmDishImageUrl();
			taste.timestamp = TimestampUtil.formatTimestamp(orderItem.getmCreateTimestamp());// modify
																								// by
																								// file
																								// 统一使用新的时间工具类
			// taste.desc = orderItem.getmOrderForm().
			tastes.add(taste);
		}

		List<Review> reviewList = mReviewDAO.findByCustomerId(customerId);

		for (Review review : reviewList) {

			NMyTasteReviewBean reviewBean = new NMyTasteReviewBean();
			
			reviewBean.type="R";

			reviewBean.reviewId = String.valueOf(review.getmId());
			reviewBean.summary = review.getmSummary();
			reviewBean.customer = new NMenuItemReviewCustomerBean();

			reviewBean.customer.setAvatarUrl(AwsS3Utils.getAwsS3CustomerResUrl() + review.getmCustomer().getmFolderName() + "/"
					+ review.getmCustomer().getmAvatar());

			reviewBean.customer.setCustomerName(review.getmCustomer().getmName()); 
			
			reviewBean.reviewItemArray = new ArrayList<NMenuItemReviewItemBean>();
			

			for (ReviewItem reviewItem : review.getmReviewItemSet()) {

				NMenuItemReviewItemBean reviewItemBean = new NMenuItemReviewItemBean();

				reviewItemBean.dishImageUrl = AwsS3Utils.getAwsS3RootResUrl() + reviewItem.getmImageUrl();

				reviewBean.reviewItemArray.add(reviewItemBean);
			}
			
			reviewBean.timestamp = TimestampUtil.formatTimestamp(review.getmCreateTimestamp());

			tastes.add(reviewBean);
		}
		
		Collections.sort(tastes, new Comparator<NMenuItemBean>() {

            public int compare(NMenuItemBean arg0, NMenuItemBean arg1) {
            	String time0 = "";
            	if(arg0 instanceof NMyTasteDishBean){
            		time0 = ((NMyTasteDishBean) arg0).timestamp;
            	}else if(arg0 instanceof NMyTasteReviewBean){
            		time0 = ((NMyTasteReviewBean) arg0).timestamp;
            	}
            	
            	String time1 = "";
            	if(arg1 instanceof NMyTasteDishBean){
            		time1 = ((NMyTasteDishBean) arg1).timestamp;
            	}else if(arg1 instanceof NMyTasteReviewBean){
            		time1 = ((NMyTasteReviewBean) arg1).timestamp;
            	}
            	
                return time1.compareTo(time0);
            }
        });

		return tastes;
	}
}
