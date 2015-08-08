package com.onemenu.server.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

import net.interfax.outbound.InterFaxLocator;
import net.interfax.outbound.InterFaxSoapStub;
import net.interfax.outbound.Sendfax;
import net.interfax.outbound.SendfaxResponse;

import org.apache.http.util.TextUtils;


import org.apache.log4j.Logger;

import com.onemenu.server.cache.ConfigCache;
import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.constant.ParameterConstant.ConfigKey;
import com.onemenu.server.model.OrderForm;
import com.onemenu.server.model.OrderItem;
import com.onemenu.server.model.OrderItemIngredient;

public class FaxUtils {

    private static Logger LOGGER = Logger.getLogger(FaxUtils.class);

    /******** Begin settings ********/
    private static String USERNAME = "onemenu"; // Enter your Interfax username here
    // private static String USERNAME = "onemenu.developer";
    private static String PASSWORD = "onemenu31415926"; // Enter your Interfax password here

    /******** End settings ********/

    public static boolean sendOrderFormToRestaurant(OrderForm orderForm) throws URISyntaxException {

        // String FAX_NUMBER = "18143088309"; // Enter the destination fax number here, e.g.
        // +497116589658
        // +8143088309
        // 18559357319
        String FAX_NUMBER = orderForm.getmRestaurant().getmFaxNum();

        if (TextUtils.isEmpty(FAX_NUMBER)) {
            return false;
        }
        
        if("false".equals(ConfigCache.getValue(ConfigKey.IS_SEND_FAX, "true"))){
        	LOGGER.error("无需发送fax");
        	return false;
        }
        
        try {

            InterFaxSoapStub theBinding =
                    (InterFaxSoapStub) new InterFaxLocator().getInterFaxSoap();
            theBinding.setTimeout(60000);
            LOGGER.info("Sending Fax using Sendfax()");

            // Read file data into a byte[].
            byte[] fileData = getOrderFormHtml(orderForm).getBytes("utf-8");
            
            if(LOGGER.isDebugEnabled())
            	LOGGER.debug("Sending Fax using sendFax().  Document size: " + fileData.length);

            Sendfax theParams = new Sendfax(USERNAME, PASSWORD, FAX_NUMBER, fileData, "html");
            SendfaxResponse theResponse = theBinding.sendfax(theParams);
            if(LOGGER.isDebugEnabled())
            	LOGGER.debug("Sendfax() call returned with code: " + theResponse.getSendfaxResult());

            return true;
            
        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return false;
    }

    public static String getOrderFormHtml(OrderForm orderForm) throws IOException,
            URISyntaxException {

        String classPath =
                Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
        String webRootPath = classPath.substring(0, classPath.indexOf("WEB-INF/classes/"));
        String orderFormHtmlPath = webRootPath + "template/OrderFormForFax.html";
        if(LOGGER.isDebugEnabled())
        	LOGGER.debug(classPath);
        if(LOGGER.isDebugEnabled())
        	LOGGER.debug(webRootPath);
        if(LOGGER.isDebugEnabled())
        	LOGGER.debug(orderFormHtmlPath);

        String templateContent = "";

        FileInputStream fileinputstream = new FileInputStream(orderFormHtmlPath);// 读取模板文件
        int lenght = fileinputstream.available();
        byte bytes[] = new byte[lenght];
        fileinputstream.read(bytes);
        fileinputstream.close();

        templateContent = new String(bytes, "utf-8");

        String paymentType = "";
        if (orderForm.getmTrade().getmPaymentType().equals(ParameterConstant.TRADE_PAYMENT_TYPE_CASH)) {
            paymentType = ParameterConstant.DESC_TRADE_PAYMENT_TYPE_CASH;
        } else if (orderForm.getmTrade().getmPaymentType()
                .equals(ParameterConstant.TRADE_PAYMENT_TYPE_CREDIT_CARD)) {
            paymentType = ParameterConstant.DESC_TRADE_PAYMENT_TYPE_CREDIT_CARD;
        } else if (orderForm.getmTrade().getmPaymentType()
                .equals(ParameterConstant.TRADE_PAYMENT_TYPE_PAYPAL)) {
            paymentType = ParameterConstant.DESC_TRADE_PAYMENT_TYPE_PAYPAL;
        } 
        
        String getType = "";
        if (orderForm.getmTrade().getmGetType().equals(ParameterConstant.TRADE_GET_TYPE_TAKEOUT)) {
            getType = ParameterConstant.DESC_TRADE_GET_TYPE_TAKEOUT;
        } else if (orderForm.getmTrade().getmGetType()
                .equals(ParameterConstant.TRADE_GET_TYPE_DELIVERY)) {
            getType = ParameterConstant.DESC_TRADE_GET_TYPE_DELIVERY;
        }

        String orderCode = "";
        if (orderForm.getmCode() != null)
            orderCode = orderForm.getmCode();
        
        String sendTime = "";
        if (orderForm.getmTrade().getmCustomerName() != null)
            sendTime = TimestampUtil.formatTimestamp();  //modify by file  统一使用新的时间工具类

        String orderTime = "";
        if (orderForm.getmTrade().getmCustomerName() != null)
            orderTime = TimestampUtil.formatTimestamp(orderForm.getmTrade().getmCreateTimestamp()); //modify by file  统一使用新的时间工具类
        String customerName = "";
        if (orderForm.getmTrade().getmCustomerName() != null)
            customerName = orderForm.getmTrade().getmCustomerName();
        String address = "";
        if (orderForm.getmTrade().getmCustomerAddress() != null)
            address = orderForm.getmTrade().getmCustomerAddress();

        String phone = "";
        if (orderForm.getmTrade().getmCustomerPhone() != null)
            phone = orderForm.getmTrade().getmCustomerPhone();

        templateContent = templateContent.replaceAll("###paymentType###", paymentType);
        
        templateContent = templateContent.replaceAll("###getType###", getType);
        templateContent = templateContent.replaceAll("###orderCode###", orderCode);
        templateContent = templateContent.replaceAll("###sendingTime###", sendTime);

        templateContent = templateContent.replaceAll("###orderTime###", orderTime);
        templateContent = templateContent.replaceAll("###customerName###", customerName);
        templateContent = templateContent.replaceAll("###customerAddress###", address);
        templateContent = templateContent.replaceAll("###customerPhone###", phone);

        // Bill

        BigDecimal subtotal = new BigDecimal(0);

        BigDecimal discount = new BigDecimal(0);
        if (orderForm.getmPrice() != null)
            discount = orderForm.getmDiscountFee();

        BigDecimal delivery = new BigDecimal(0);
        if (orderForm.getmPrice() != null)
            delivery = orderForm.getmDeliveryFee();

        BigDecimal tax = new BigDecimal(0);
        if (orderForm.getmPrice() != null)
            tax = orderForm.getmTaxFee();

        BigDecimal total = new BigDecimal(0);
        if (orderForm.getmPrice() != null)
            total = orderForm.getmPrice();

        BigDecimal tips = new BigDecimal(0);
        if (orderForm.getmPrice() != null)
            tips = orderForm.getmTipsFee();
        // modify by file  本处不用使用StringBuffer，Stringbuffer 是线程安全的，性能不如StringBuilder（所有本身是线程安全的代码都不应该使用StringBuffer）
        //StringBuffer billStringBuffer = new StringBuffer();
        StringBuilder billBuilder = new StringBuilder();
        for (OrderItem orderItem : orderForm.getmOrderItemSet()) {

            subtotal = subtotal.add(orderItem.getmPrice());

            String dishAmount = orderItem.getmDishAmount();
            String dishName = orderItem.getmDishName();
            String price = FormatUtils.formatPrice(orderItem.getmPrice()).toString();

            // dish tr
            Integer amount = Integer.parseInt(dishAmount);
            if (amount == 1) {
                billBuilder.append("<tr>").append("<td style='text-align: center'><h3>")
                        .append(dishAmount).append("</h3></td>").append("<td>").append(dishName)
                        .append("</td>").append("<td></td>")
                        .append("<td style='text-align: right'>").append(price).append("</td>")
                        .append("</tr>");
            } else if (amount > 1) {
                billBuilder.append("<tr>")
                        .append("<td style='text-align: center'><h3><font face='bold' size='6'>")
                        .append(dishAmount).append("</font></h3></td>").append("<td>")
                        .append(dishName).append("</td>").append("<td></td>")
                        .append("<td style='text-align: right'>").append(price).append("</td>")
                        .append("</tr>");
            }

            // ingredient tr
            for (OrderItemIngredient ingredient : orderItem.getmOrderItemIngredientSet()) {

                String ingredientName = ingredient.getmName();
                billBuilder.append("<tr>").append("<td></td>").append("<td></td>")
                        .append("<td>").append(ingredientName).append("</td>").append("<td></td>")
                        .append("</tr>");
            }

        }

        // hr
        billBuilder.append("<tr><td colspan='4'><hr /></td></tr>");

        // Subtotal
        billBuilder.append("<tr>").append("<td></td>").append("<td></td>")
                .append("<td style='text-align: right'>Subtotal</td>")
                .append("<td style='text-align: right'>").append(FormatUtils.formatPrice(subtotal))
                .append("</td>").append("</tr>");

        // Discount
        billBuilder.append("<tr>").append("<td></td>").append("<td></td>")
                .append("<td style='text-align: right'>Discount</td>")
                .append("<td style='text-align: right'>-")
                .append(FormatUtils.formatPrice(discount)).append("</td>").append("</tr>");

        // Delivery
        billBuilder.append("<tr>").append("<td></td>").append("<td></td>")
                .append("<td style='text-align: right'>Delivery</td>")
                .append("<td style='text-align: right'>").append(FormatUtils.formatPrice(delivery))
                .append("</td>").append("</tr>");

        // Tax
        billBuilder.append("<tr>").append("<td></td>").append("<td></td>")
                .append("<td style='text-align: right'>Tax</td>")
                .append("<td style='text-align: right'>").append(FormatUtils.formatPrice(tax))
                .append("</td>").append("</tr>");

        // hr
        billBuilder.append("<tr>").append("<td></td>").append("<td></td>")
                .append("<td colspan='2'><hr /></td>").append("</tr>");

        // Total
        billBuilder.append("<tr>").append("<td></td>").append("<td></td>")
                .append("<td style='text-align: right'>Total</td>")
                .append("<td style='text-align: right'>").append(FormatUtils.formatPrice(total))
                .append("</td>").append("</tr>");

        // blank row
        billBuilder.append("<tr><td colspan='4'><br/></td></tr>");


        // Tips
        if (FormatUtils.formatPrice(tips).equals(FormatUtils.formatPrice(-1))) {
            billBuilder.append("<tr>").append("<td></td>").append("<td></td>")
                    .append("<td style='text-align: right'>Tips</td>")
                    .append("<td style='text-align: right'>Cash</td>").append("</tr>");
        } else {
            billBuilder.append("<tr>").append("<td></td>").append("<td></td>")
                    .append("<td style='text-align: right'>Tips</td>")
                    .append("<td style='text-align: right'>").append(FormatUtils.formatPrice(tips))
                    .append("</td>").append("</tr>");
        }

        String bill = billBuilder.toString();
        templateContent = templateContent.replaceAll("###bill###", bill);

        if(LOGGER.isDebugEnabled())
        	LOGGER.info("new html:\n" + templateContent);

        return templateContent;

    }
}
