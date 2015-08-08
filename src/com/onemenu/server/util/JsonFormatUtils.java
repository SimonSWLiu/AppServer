package com.onemenu.server.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.onemenu.server.javabean.IngredientBean;
import com.onemenu.server.javabean.IngredientTypeBean;
import com.onemenu.server.javabean.OrderFormBean;
import com.onemenu.server.javabean.OrderItemBean;
import com.onemenu.server.javabean.ReviewBean;
import com.onemenu.server.javabean.ReviewItemBean;
import com.onemenu.server.javabean.TradeBean;


public class JsonFormatUtils {
	private static final Logger logger = Logger.getLogger(JsonFormatUtils.class);
	
    public static List<IngredientTypeBean> getCustomization(String customization) {

        List<IngredientTypeBean> result = new ArrayList<IngredientTypeBean>();

        try {

            JSONArray customizationJsonArray = new JSONArray(customization);
            for (int i = 0; i < customizationJsonArray.length(); i++) {
                JSONObject ingredientTypeJsonObject = customizationJsonArray.getJSONObject(i);
                IngredientTypeBean ingredientTypeBean = new IngredientTypeBean();
                ingredientTypeBean.type = ingredientTypeJsonObject.get("type").toString();
                ingredientTypeBean.isMandatory =
                        ingredientTypeJsonObject.get("isMandatory").toString();
                ingredientTypeBean.isSingle = ingredientTypeJsonObject.get("isSingle").toString();

                String ingredientListStr = ingredientTypeJsonObject.get("ingredient").toString();

                List<IngredientBean> ingredientList = new ArrayList<IngredientBean>();
                JSONArray ingredientJsonArray = new JSONArray(ingredientListStr);
                for (int j = 0; j < ingredientJsonArray.length(); j++) {
                    JSONObject ingredientJsonObject = ingredientJsonArray.getJSONObject(j);
                    IngredientBean ingredientBean = new IngredientBean();
                    //ingredientBean.ingredientId = ingredientJsonObject.getString("ingredientId");
                    ingredientBean.ingredientName =
                            ingredientJsonObject.get("ingredientName").toString();
                    ingredientBean.ingredientPrice =
                            ingredientJsonObject.get("ingredientPrice").toString();
                    ingredientList.add(ingredientBean);
                }
                ingredientTypeBean.ingredientArray = ingredientList;
                result.add(ingredientTypeBean);
            }

        } catch (JSONException e) {
           logger.error("", e);
        }

        return result;
    }

    public static ReviewBean getReview(long customerId, String reviewJsonStr) {

        ReviewBean reviewBean = new ReviewBean();

        try {

            JSONObject reviewJson = new JSONObject(reviewJsonStr);
            
            reviewBean.customer.customerId = String.valueOf(customerId);//TODO
            reviewBean.summary = reviewJson.getString("summary");

            String reviewItemArrayJsonStr = reviewJson.get("reviewItemArray").toString();
            JSONArray reviewItemJsonArray = new JSONArray(reviewItemArrayJsonStr);
            for (int i = 0; i < reviewItemJsonArray.length(); i++) {

                JSONObject reviewItemJsonObject = reviewItemJsonArray.getJSONObject(i);
                ReviewItemBean reviewItemBean = new ReviewItemBean();
                reviewItemBean.comment = reviewItemJsonObject.getString("comment");
                reviewItemBean.dishId = reviewItemJsonObject.getString("dishId");
                reviewItemBean.dishName = reviewItemJsonObject.getString("dishName");
                reviewItemBean.dishImageUrl = reviewItemJsonObject.getString("dishImageUrl");
                reviewItemBean.base64Str = reviewItemJsonObject.getString("base64Str");
                reviewItemBean.width = reviewItemJsonObject.getString("width");
                reviewItemBean.height = reviewItemJsonObject.getString("height");

                reviewBean.reviewItemArray.add(reviewItemBean);
            }

        } catch (JSONException e) {
            logger.error("", e);
        }

        return reviewBean;
    }

    public static TradeBean getTradeBean(String tradeJsonStr) {

        TradeBean tradeBean = new TradeBean();

        if (tradeJsonStr != null) {

            try {

                JSONObject tradeJson = new JSONObject(tradeJsonStr);

                // customer
                JSONObject customerJson = (JSONObject) tradeJson.get("customer");
                tradeBean.customer.customerId = customerJson.getString("customerId");

                tradeBean.tradeGetType = tradeJson.getString("tradeGetType");
                tradeBean.tradeCouponId = tradeJson.getString("tradeCouponId");
                tradeBean.tradeCouponDesc = tradeJson.getString("tradeCouponDesc");
                tradeBean.tradePaymentType = tradeJson.getString("tradePaymentType");
                tradeBean.tradePrice = tradeJson.getString("tradePrice");
                tradeBean.tradeCustomerName = tradeJson.getString("tradeCustomerName");
                tradeBean.tradeCustomerPhone = tradeJson.getString("tradeCustomerPhone");
                tradeBean.tradeCustomerAddress = tradeJson.getString("tradeCustomerAddress");
                tradeBean.tradeCustomerApt = tradeJson.getString("tradeCustomerApt");
                tradeBean.tradeCustomerStreet = tradeJson.getString("tradeCustomerStreet");
                tradeBean.tradeCustomerCity = tradeJson.getString("tradeCustomerCity");
                tradeBean.tradeCustomerZipCode = tradeJson.getString("tradeCustomerZipCode");
                
                //order form
                String orderFormArrayJsonStr = tradeJson.get("orderFormArray").toString();
                JSONArray orderFormJsonArray = new JSONArray(orderFormArrayJsonStr);
                for (int i = 0; i < orderFormJsonArray.length(); i++) {

                    JSONObject orderFormJsonObject = orderFormJsonArray.getJSONObject(i);
                    OrderFormBean orderFormBean = new OrderFormBean();
                    orderFormBean.customerRemark = orderFormJsonObject.getString("customerRemark");
                    orderFormBean.orderFormCouponId =
                            orderFormJsonObject.getString("orderFormCouponId");
                    orderFormBean.orderFormCouponDesc =
                            orderFormJsonObject.getString("orderFormCouponDesc");
                    orderFormBean.taxFee = orderFormJsonObject.getString("taxFee");
                    orderFormBean.tipsFee = orderFormJsonObject.getString("tipsFee");
                    orderFormBean.deliveryFee = orderFormJsonObject.getString("deliveryFee");
                    orderFormBean.discountFee = orderFormJsonObject.getString("discountFee");
                    orderFormBean.orderFormSubtotal = orderFormJsonObject.getString("orderFormSubtotal");
                    orderFormBean.orderFormTotal = orderFormJsonObject.getString("orderFormTotal");
                    orderFormBean.orderFormPrice = orderFormJsonObject.getString("orderFormPrice");

                    // restaurant
                    JSONObject restaurantJson = (JSONObject) orderFormJsonObject.get("restaurant");
                    orderFormBean.restaurant.restaurantId =
                            restaurantJson.getString("restaurantId");

                    //order item
                    String orderItemArrayJsonStr =
                            orderFormJsonObject.get("orderItemArray").toString();
                    JSONArray orderItemJsonArray = new JSONArray(orderItemArrayJsonStr);
                    for (int j = 0; j < orderItemJsonArray.length(); j++) {

                        JSONObject orderItemJsonObject = orderItemJsonArray.getJSONObject(j);
                        OrderItemBean orderItemBean = new OrderItemBean();
                        orderItemBean.dishId = orderItemJsonObject.getString("dishId");
                        orderItemBean.dishName = orderItemJsonObject.getString("dishName");
                        orderItemBean.dishImageUrl = orderItemJsonObject.getString("dishImageUrl");
                        orderItemBean.dishAmount = orderItemJsonObject.getString("dishAmount");
                        orderItemBean.dishPrice = orderItemJsonObject.getString("dishPrice");
                        orderItemBean.orderItemPrice =
                                orderItemJsonObject.getString("orderItemPrice");

                        //ingredient
                        String orderItemIngredientArrayJsonStr =
                                orderItemJsonObject.get("orderItemIngredientArray").toString();
                        JSONArray orderItemIngredientJsonArray =
                                new JSONArray(orderItemIngredientArrayJsonStr);
                        for (int k = 0; k < orderItemIngredientJsonArray.length(); k++) {

                            JSONObject orderItemIngredientJsonObject =
                                    orderItemIngredientJsonArray.getJSONObject(k);
                            IngredientBean ingredientBean = new IngredientBean();
                            ingredientBean.ingredientName =
                                    orderItemIngredientJsonObject.getString("ingredientName");
                            ingredientBean.ingredientPrice =
                                    orderItemIngredientJsonObject.getString("ingredientPrice");

                            orderItemBean.orderItemIngredientArray.add(ingredientBean);
                        }

                        orderFormBean.orderItemArray.add(orderItemBean);
                    }


                    tradeBean.orderFormArray.add(orderFormBean);
                }

            } catch (JSONException e) {
            	logger.error("", e);
                tradeBean = null;
            }
        }

        return tradeBean;
    }

}
