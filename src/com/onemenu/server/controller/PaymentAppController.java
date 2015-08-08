package com.onemenu.server.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.Transaction.GatewayRejectionReason;
import com.braintreegateway.ValidationError;
import com.braintreegateway.ValidationErrors;
import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.javabean.TradeBean;
import com.onemenu.server.model.OrderForm;
import com.onemenu.server.model.Trade;
import com.onemenu.server.protocol.ErrorCode;
import com.onemenu.server.protocol.response.ObjectBaseData;
import com.onemenu.server.service.CouponService;
import com.onemenu.server.service.OrderFormService;
import com.onemenu.server.service.TradeService;
import com.onemenu.server.util.BraintreeUtils;
import com.onemenu.server.util.EmailUtils;
import com.onemenu.server.util.FaxUtils;
import com.onemenu.server.util.JsonFormatUtils;
import com.onemenu.server.util.ResponseUtils;

@Controller
@RequestMapping("/payment")
public class PaymentAppController {
	
	private static final Logger logger = Logger.getLogger(PaymentAppController.class);

    private TradeService mTradeService;
    
    private OrderFormService mOrderFormService;

	@Autowired
	public void setmOrderFormService(OrderFormService mOrderFormService) {
		this.mOrderFormService = mOrderFormService;
	}

    @Autowired
    public void setmTradeService(TradeService mTradeService) {
        this.mTradeService = mTradeService;
    }
    
    private CouponService mCouponService;

    @Autowired
    public void setmCouponService(CouponService mCouponService) {
        this.mCouponService = mCouponService;
    }

    @RequestMapping(value = "/getBraintreeClientToken", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object getBraintreeClientToken(String customerId) {

    	ObjectBaseData<String> _data = new ObjectBaseData<String>();
    	/*
        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
		*/
        String clientToken = BraintreeUtils.getClientToken();

        _data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
        _data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
        _data.data.put("clientToken", clientToken);

        return _data.toJson();
    }

    @RequestMapping(value = "/braintreePurchases", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object braintreePurchases(String nonce, String amount, String tradeJsonStr) {

        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(TextUtils.isEmpty(tradeJsonStr)){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("tradeJsonStr");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }else if(TextUtils.isEmpty(nonce)){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("nonce");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }else if(TextUtils.isEmpty(amount)){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("amount");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        } 
        

        TradeBean tradeBean = JsonFormatUtils.getTradeBean(tradeJsonStr);

        if (tradeBean != null) {
        	
        	mTradeService.payOrderByBraintree(tradeBean,nonce,amount);
        	
            // pay
            Result<Transaction> result = BraintreeUtils.purchases(nonce, amount);

            if (result.isSuccess()) {
                // save trade

                Trade trade = mTradeService.saveTradeBean(tradeBean);
                
                //add by file on 20150509
                
                /**if(StringUtil.isNumber(tradeBean.customer.customerId) && StringUtil.isNumber(tradeBean.tradeCouponId)){            	
                	if(!mCouponService.delCouponFromCustomer(Long.valueOf(tradeBean.customer.customerId), Long.valueOf(tradeBean.tradeCouponId))){
                		logger.error("remove CouponId from customer error");
                	}
                }**/
                
                mCouponService.delCouponsFromCustomer(tradeBean);
                //交易监听器
                mOrderFormService.addOrderListener(trade);
                
                

                // send Fax
                for (OrderForm orderForm : trade.getmOrderFormSet()) {
                    try {
                        EmailUtils.sendOrderFormToRestaurant(orderForm);
                        FaxUtils.sendOrderFormToRestaurant(orderForm);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }

                statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
                msg = ParameterConstant.MSG_OPERATION_SUCCESS;
            } else {
            	
            	logger.error("BraintreeUtils  payment failed :"+ result);
            	
                StringBuffer msgBuffer = new StringBuffer();

                ValidationErrors errors = result.getErrors();

                if (errors != null) {

                    msgBuffer.append("Error : ").append(errors).append(";\n");
                }

                for (ValidationError error : errors.getAllDeepValidationErrors()) {

                    msgBuffer.append("Error Code: ").append(error.getCode()).append(";\n");
                    msgBuffer.append("Error Message: ").append(error.getMessage()).append(";\n");
                }


                Transaction target = result.getTarget();
                if (target != null) {

                    msgBuffer.append("Target Status : ").append(target.getStatus()).append(";\n");
                    msgBuffer.append("Processor Response Code : ")
                            .append(target.getProcessorResponseCode()).append(";\n");
                    msgBuffer.append("Processor Response Text : ")
                            .append(target.getProcessorResponseText()).append(";\n");
                }

                Transaction transaction = result.getTransaction();
                if (transaction != null) {
                	GatewayRejectionReason reason =  transaction.getGatewayRejectionReason();
                    msgBuffer.append("Transaction Status : ").append(transaction.getStatus())
                            .append(";\n");
                    msgBuffer.append("Gateway Rejection Reason : ")
                            .append(reason).append(";\n");

                    if (reason!=null&&reason.toString().equalsIgnoreCase("CCV")) {
                        msgBuffer.append("CCV is invalid.");
                    }
                }

                msg = msgBuffer.toString();
            }
        } else {

            msg = "Trade format is invalid.";
        }

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }

    @RequestMapping(value = "/cashPurchases", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object cashPurchases(String tradeJsonStr) {

        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(TextUtils.isEmpty(tradeJsonStr)){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("tradeJsonStr");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }

        // save trade
        TradeBean tradeBean = JsonFormatUtils.getTradeBean(tradeJsonStr);
        if (tradeBean != null) {

            Trade trade = mTradeService.saveTradeBean(tradeBean);
            
            //add by file on 20150509
            mCouponService.delCouponsFromCustomer(tradeBean);
            
            //交易监听器
            mOrderFormService.addOrderListener(trade);
            
            
            // send Fax
            for (OrderForm orderForm : trade.getmOrderFormSet()) {
                try {
                    EmailUtils.sendOrderFormToRestaurant(orderForm);
                    FaxUtils.sendOrderFormToRestaurant(orderForm);
                } catch (Exception e) {
                	logger.error("", e);
                }
            }

            statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
            msg = ParameterConstant.MSG_OPERATION_SUCCESS;
        } else {
            msg = "Trade format is invalid.";
        }

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }

}
