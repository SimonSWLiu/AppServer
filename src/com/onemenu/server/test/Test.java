package com.onemenu.server.test;


import java.util.HashMap;
import java.util.Map;

import com.onemenu.server.util.HttpClientUtil;

public class Test {
	
	public static void main(String[] args) {
		//development-wbsssi2pys.elasticbeanstalk.com  http://production-xy3vy7dxqv.elasticbeanstalk.com/
		//toptags/listMenu，restaurant/listRestaurantItem
		//String url =\"http://localhost:8080/OneMenu/app/menu/listMenuItem\";coupon/getCouponMarket
		//String url =\"http://development-wbsssi2pys.elasticbeanstalk.com/app/menu/listMenuItem\";
		String url ="http://localhost:8080/OneMenu/app/userAnalysis/out";
		//String url ="http://production-xy3vy7dxqv.elasticbeanstalk.com/app/menu/listMenuItem";
		Map<String,String>  params = new HashMap<String,String>();
		params.put("type", "D");
		params.put("startNum", "0");
		params.put("range", "20");
		params.put("tags", "kungfutea");//
		params.put("latitude", "40.802823");
		params.put("longitude", "-77.889503");
		params.put("dishIdListStr", "3095");
		params.put("amount", "22.79");
		params.put("customerId", "68");
		params.put("restaurantId", "19");
		params.put("deviceId", "123456");
		params.put("viewControllerName", "dish");
		params.put("appVersion", "1.0.3.0");
		//params.put("tradeJsonStr", tradeJsonStr1);
		
		String result = HttpClientUtil.doPostMethod(url, params);
		
		System.out.println(result);
				
		
	}
	
	static String  tradeJsonStr="{\"tradePaymentType\":\"3\", \"tradeCouponDesc\": \"\", \"tradeCustomerApt\": \"\", \"tradeCustomerAddress\": \"\", \"customer\": {\"customerId\": \"68\"  },\"tradeGetType\": \"1\",\"orderFormArray\": [{\"customerRemark\": \"\",\"orderFormPrice\": \"11.16\",\"orderFormTotal\": \"11.16\",\"discountFee\": \"0.75\",\"orderItemArray\": [{\"dishImageUrl\": \"https://s3-us-west-2.amazonaws.com/s3-onemenu/resources/restaurant-res/864C-1422826667702/1424993390053_540_810.jpeg\",\"dishName\": \"Longan Red Date Tea 桂圆红枣茶\",\"dishId\": \"3095\",\"dishAmount\": \"3\",\"orderItemIngredientArray\": [{\"ingredientName\": \"Large\",\"ingredientPrice\": \"0.75\"},{\"ingredientName\": \"Less(70%)\",\"ingredientPrice\": \"0\"},{\"ingredientName\": \"Less Ice\",\"ingredientPrice\": \"0\"}],\"dishPrice\": \"3.00\",\"orderItemPrice\": \"10.5\"}],\"taxFee\": \"0.66\",\"deliveryFee\": \"0\",\"orderFormCouponDesc\": \"\",\"tipsFee\": \"-1\",\"restaurant\": { \"restaurantId\": \"12\"}, \"orderFormCouponId\": \"6\",\"orderFormSubtotal\": \"11.25\"}],\"tradeCouponId\": \"\",\"tradeCustomerZipCode\": \"\",\"tradeCustomerCity\": \"\",\"tradeCustomerStreet\": \"\",\"tradeCustomerPhone\": \"1231231231\",\"tradePrice\": \"11.16\",\"tradeCustomerName\": \"\"	}";
	static String tradeJsonStr1="{\"tradePaymentType\":\"3\",\"tradeCouponDesc\":\"\",\"tradeCustomerApt\":\"\",\"tradeCustomerAddress\":\"test,test,12312\", \"customer\": {\"customerId\" : \"68\"},\"tradeGetType\" : \"2\",\"orderFormArray\" : [{\"customerRemark\" : \"\",\"orderFormPrice\" : \"11.16\",\"orderFormTotal\" : \"11.16\",\"discountFee\" : \"0.75\",\"orderItemArray\" : [{\"dishImageUrl\" : \"https://s3-us-west-2.amazonaws.com/s3-onemenu/resources/restaurant-res/864C-1422826667702/1424993390053_540_810.jpeg\",\"dishName\" : \"Longan Red Date Tea 桂圆红枣茶\",\"dishId\" : \"3095\",\"dishAmount\" : \"3\",\"orderItemIngredientArray\" : [{\"ingredientName\" : \"Large\",\"ingredientPrice\" : \"0.75\"},{\"ingredientName\" : \"Less(70%)\",\"ingredientPrice\" : \"0\" },{\"ingredientName\" : \"Less Ice\",\"ingredientPrice\" : \"0\"}],\"dishPrice\" : \"3.00\",\"orderItemPrice\" : \"10.5\"}],\"taxFee\" : \"0.66\",\"deliveryFee\" : \"0\",\"orderFormCouponDesc\" : \"\",\"tipsFee\" : \"-1\",\"restaurant\" : {\"restaurantId\" : \"12\"},\"orderFormCouponId\" : \"6\",\"orderFormSubtotal\" : \"11.25\"},{\"customerRemark\" : \"\",\"orderFormPrice\" :\"11.63\",\"orderFormTotal\" : \"11.63\",\"discountFee\" : \"2\",\"orderItemArray\" : [{\"dishImageUrl\" : \"https://s3-us-west-2.amazonaws.com/s3-onemenu/resources/restaurant-res/6282-1422908479458/1424967165229_540_367.jpeg\",\"dishName\" : \"2. Shrimp Salad\",\"dishId\" : \"3181\",\"dishAmount\" : \"1\",\"orderItemIngredientArray\" : [],\"dishPrice\" : \"10.95\",\"orderItemPrice\" : \"10.95\"}],\"taxFee\" : \"0.68\",\"deliveryFee\" : \"2\",\"orderFormCouponDesc\" : \"save 2 - bill\",\"tipsFee\" : \"-1\",\"restaurant\" : {\"restaurantId\" : \"15\"},\"orderFormCouponId\" : \"7\",\"orderFormSubtotal\" : \"10.95\" }],\"tradeCouponId\" : \"\",\"tradeCustomerZipCode\" : \"12312\",\"tradeCustomerCity\" : \"test\",\"tradeCustomerStreet\" : \"test\",\"tradeCustomerPhone\" : \"1231231231\",\"tradePrice\" : \"22.79\",\"tradeCustomerName\" : \"\"}";
}
