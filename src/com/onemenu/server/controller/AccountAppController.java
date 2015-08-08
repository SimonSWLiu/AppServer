package com.onemenu.server.controller;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onemenu.server.cache.LoginTokenCache;
import com.onemenu.server.cache.SignUpTokenCache;
import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.entity.LoginTokenObj;
import com.onemenu.server.entity.SignUpTokenObj;
import com.onemenu.server.javabean.NEmailLoginBean;
import com.onemenu.server.javabean.NMenuItemReviewCustomerBean;
import com.onemenu.server.model.Account;
import com.onemenu.server.model.Customer;
import com.onemenu.server.model.CustomerFeedback;
import com.onemenu.server.model.ThirdPartyAccount;
import com.onemenu.server.protocol.ErrorCode;
import com.onemenu.server.protocol.response.ObjectBaseData;
import com.onemenu.server.protocol.response.OnlyObjectBaseData;
import com.onemenu.server.service.AccountService;
import com.onemenu.server.service.CustomerFeedbackService;
import com.onemenu.server.service.CustomerService;
import com.onemenu.server.service.ThirdPartyAccountService;
import com.onemenu.server.util.AwsS3Utils;
import com.onemenu.server.util.EmailUtils;
import com.onemenu.server.util.FbUtils;
import com.onemenu.server.util.ImageUtils;
import com.onemenu.server.util.MD5Utils;
import com.onemenu.server.util.MimeUtils;
import com.onemenu.server.util.ResponseUtils;
import com.onemenu.server.util.TimestampUtil;

@Controller
@RequestMapping("/account")
public class AccountAppController {

    private AccountService mAccountService;
    private CustomerService mCustomerService;
    private ThirdPartyAccountService mThirdPartyAccountService;
    private CustomerFeedbackService mCustomerFeedbackService;

    @Autowired
    public void setAccountService(AccountService service) {
        this.mAccountService = service;
    }

    @Autowired
    public void setmCustomerService(CustomerService mCustomerService) {
        this.mCustomerService = mCustomerService;
    }

    @Autowired
    public void setmThirdPartyAccountService(ThirdPartyAccountService mThirdPartyAccountService) {
        this.mThirdPartyAccountService = mThirdPartyAccountService;
    }

    @Autowired
    public void setmCustomerFeedbackService(CustomerFeedbackService mCustomerFeedbackService) {
        this.mCustomerFeedbackService = mCustomerFeedbackService;
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object signUp(String email, String password) {

//        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
//        String msg = ParameterConstant.MSG_OPERATION_ERROR;
//        Map<String, Object> data = new HashMap<String, Object>();
    	
    	ObjectBaseData<NMenuItemReviewCustomerBean> _data = new ObjectBaseData<NMenuItemReviewCustomerBean>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(TextUtils.isEmpty(email)){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg  = ErrorCode.ParamError.getMsgForName("email");
        	return _data.toJson();
        }else if(TextUtils.isEmpty(password)){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg  = ErrorCode.ParamError.getMsgForName("password");
        	return _data.toJson();
        }
        

        if (mAccountService.validateEmail(email)) {

            Account account = new Account();
            account.setmEmail(email);
            account.setmPassword(password);
            account.setmCreateTimestamp(TimestampUtil.getCurrentTimestamp());  //modify by file  统一使用新的时间工具类
            // Create customer
            Customer customer = new Customer();
            String folderName =
                    MD5Utils.getHashCodePrefix(String.valueOf(System.currentTimeMillis()))
                            + System.currentTimeMillis();
            customer.setmFolderName(folderName);
            // customer.setmCreateTimestamp(FormatUtils.getCurrentTimestamp());
            account.setmCustomer(customer);

            mAccountService.saveTrd(account);

            EmailUtils.sendActiveEmail(account);

            NMenuItemReviewCustomerBean customerBean = new NMenuItemReviewCustomerBean();
            customerBean.customerId = String.valueOf(customer.getmId());
            
            _data.data.put("customer", customerBean);
            _data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
            _data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
        }

        return _data.toJson();
    }

    @RequestMapping(value = "/emailLogin", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object emailLogin(String email, String password) {

//        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
//        String msg = ParameterConstant.MSG_OPERATION_ERROR;
//        Map<String, Object> data = new HashMap<String, Object>();
    	
    	OnlyObjectBaseData<NEmailLoginBean> _data = new OnlyObjectBaseData<NEmailLoginBean>();
    	
        
        //校验参数为空    add  by file on 2015-04-26
        if(TextUtils.isEmpty(email)){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg  = ErrorCode.ParamError.getMsgForName("email");
        	return _data.toJson();
        }else if(TextUtils.isEmpty(password)){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg  = ErrorCode.ParamError.getMsgForName("password");
        	return _data.toJson();
        }
        

        String loginToken = "";
        Account account = new Account();
        account.setmEmail(email);
        account.setmPassword(password);
        account = mAccountService.login(account);

        if (account != null) {

            // if (account.getmStatus() != null &&
            // account.getmStatus().equals(ParameterConstant.ACCOUNT_STATUS_ACTIVE)) {
        	
        	NEmailLoginBean bean = new NEmailLoginBean();
        	
            loginToken = LoginTokenCache.generateLoginToken(account.getmId());
            LoginTokenObj loginTokenObj = new LoginTokenObj();
            loginTokenObj.account = account;
            loginTokenObj.loginTimestamp = TimestampUtil.getCurrentTimestamp();  //modify by file  统一使用新的时间工具类
            LoginTokenCache.loginTokenMap.put(loginToken, loginTokenObj);
            
            bean.loginToken = loginToken;

            Customer customer = account.getmCustomer();
            NMenuItemReviewCustomerBean customerBean = new NMenuItemReviewCustomerBean();
            customerBean.customerId = String.valueOf(customer.getmId());
            customerBean.setCustomerName(customer.getmName());
            customerBean.setAvatarUrl(AwsS3Utils.getAwsS3CustomerResUrl() + customer.getmFolderName() + "/"
                    + customer.getmAvatar()); 
            
            bean.customer= customerBean;
            
            _data.data = bean; 

            _data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
            _data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
            // }
            // else {
            //
            // msg = "This account is inactice.";
            // }

        } else {

            _data.msg = "Account or password error.";
        }

        return _data.toJson();
    }

    @RequestMapping(value = "/thirdPartyLogin", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object thirdPartyLogin(String thirdPartyToken, String thirdPartyAccountId,
            String loginType) {

//        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
//        String msg = ParameterConstant.MSG_OPERATION_ERROR;
//        Map<String, Object> data = new HashMap<String, Object>();
    	
    	OnlyObjectBaseData<NEmailLoginBean> _data = new OnlyObjectBaseData<NEmailLoginBean>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(TextUtils.isEmpty(thirdPartyToken)){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg  = ErrorCode.ParamError.getMsgForName("thirdPartyToken");
        	return _data.toJson();
        }else if(TextUtils.isEmpty(thirdPartyAccountId)){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg  = ErrorCode.ParamError.getMsgForName("thirdPartyAccountId");
        	return _data.toJson();
        }else if(TextUtils.isEmpty(loginType)){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg  = ErrorCode.ParamError.getMsgForName("loginType");
        	return _data.toJson();
        }
        
        
        

        // accountType类型
        if (ParameterConstant.LOGIN_TYPE_FACEBOOK.equals(loginType)) {

            String fbTokenContent = new String();
            // 请求facebook的token，查看userId
            try {

                fbTokenContent = FbUtils.getFacebookTokenContent(thirdPartyToken);

                if (fbTokenContent.equals("ERROR")) {
                    _data.status = ParameterConstant.STATUS_CODE_ERROR;
                    _data.msg = "Fail to get FB access token.";
                } else {
                	//modify by file  统一使用新的时间工具类
                	Timestamp curTimestamp = TimestampUtil.getCurrentTimestamp();
                    LoginTokenObj loginTokenObj = new LoginTokenObj();
                    loginTokenObj.thirdPartyToken = thirdPartyToken;
                    loginTokenObj.thirdPartyTokenContent = fbTokenContent;
                    loginTokenObj.loginTimestamp = curTimestamp;

                    if (mThirdPartyAccountService.isSignUp(thirdPartyAccountId, loginType)) {

                        // get Account by fbId and set account into loginTokenObj
                        ThirdPartyAccount thirdPartyAccount =
                                mThirdPartyAccountService.login(thirdPartyAccountId, loginType);
                        loginTokenObj.account = thirdPartyAccount.getmAccount();
                        loginTokenObj.loginTimestamp = curTimestamp;
                        String loginToken =
                                LoginTokenCache.generateLoginToken(loginTokenObj.account.getmId());
                        LoginTokenCache.loginTokenMap.put(loginToken, loginTokenObj);
                        NEmailLoginBean bean = new NEmailLoginBean();
                        bean.loginToken = loginToken;

                        Customer customer = thirdPartyAccount.getmAccount().getmCustomer();
                        NMenuItemReviewCustomerBean customerBean = new NMenuItemReviewCustomerBean();
                        customerBean.customerId = String.valueOf(customer.getmId());
                        customerBean.setCustomerName(customer.getmName());
                        customerBean.setAvatarUrl(AwsS3Utils.getAwsS3CustomerResUrl() + customer.getmFolderName() + "/"
                                + customer.getmAvatar()); 
                        
                        bean.customer= customerBean;
                        
                        _data.data = bean; 

                        _data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
                        _data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
                    } else {


                        // 新建一个新的账户
                        Account account = new Account();
                        account.setmCreateTimestamp(curTimestamp);
                        Customer customer = new Customer();
                        customer.setmCreateTimestamp(curTimestamp);
                        // 生成保存的文件夹
                        String folderName =
                                MD5Utils.getHashCodePrefix(String.valueOf(System
                                        .currentTimeMillis())) + System.currentTimeMillis();
                        customer.setmFolderName(folderName);
                        account.setmCustomer(customer);
                        account.setmStatus(ParameterConstant.ACCOUNT_STATUS_ACTIVE);
                        mAccountService.saveTrd(account);

                        // save third party account
                        ThirdPartyAccount thirdPartyAccount = new ThirdPartyAccount();
                        thirdPartyAccount.setmThirdPartyId(thirdPartyAccountId);
                        thirdPartyAccount.setmLoginType(loginType);
                        thirdPartyAccount.setmCreateTimestamp(curTimestamp);
                        // link to one menu account
                        thirdPartyAccount.setmAccount(account);
                        mThirdPartyAccountService.saveTrd(thirdPartyAccount);

                        loginTokenObj.account = account;
                        loginTokenObj.loginTimestamp = curTimestamp;
                        String loginToken = LoginTokenCache.generateLoginToken(account.getmId());
                        LoginTokenCache.loginTokenMap.put(loginToken, loginTokenObj);
                        NEmailLoginBean bean = new NEmailLoginBean();
                        bean.loginToken = loginToken;

                        NMenuItemReviewCustomerBean customerBean = new NMenuItemReviewCustomerBean();
                        customerBean.customerId = String.valueOf(customer.getmId());
                        customerBean.setCustomerName(customer.getmName());
                        customerBean.setAvatarUrl(AwsS3Utils.getAwsS3CustomerResUrl() + customer.getmFolderName() + "/"
                                + customer.getmAvatar()); 
                        
                        bean.customer= customerBean;
                        
                        _data.data = bean; 

                        _data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
                        _data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
                    }
                }
            } catch (Exception e) {
                _data.status = ParameterConstant.STATUS_CODE_ERROR;
                _data.msg = "Fail to get account for facebook login.";
                e.printStackTrace();
            }

        }

        return _data.toJson();
    }

    @RequestMapping(value = "/autoLogin", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object autoLogin(String loginToken) {
    	
    	

//        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
//        String msg = ParameterConstant.MSG_OPERATION_ERROR;
//        Map<String, Object> data = new HashMap<String, Object>();
    	
    	OnlyObjectBaseData<NEmailLoginBean> _data = new OnlyObjectBaseData<NEmailLoginBean>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(TextUtils.isEmpty(loginToken)){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg  = ErrorCode.ParamError.getMsgForName("loginToken");
        	return _data.toJson();
        }

        if (LoginTokenCache.loginTokenMap.containsKey(loginToken)) {

            LoginTokenObj loginTokenObj =
                    (LoginTokenObj) LoginTokenCache.loginTokenMap.get(loginToken);
            LoginTokenCache.loginTokenMap.remove(loginToken);

            Timestamp loginTimestamp = loginTokenObj.loginTimestamp;
            Timestamp currentTimestamp = TimestampUtil.getCurrentTimestamp();  //modify by file  统一使用新的时间工具类

            // check token valid time
            int days = 7;
            if ((currentTimestamp.getTime() - loginTimestamp.getTime()) < days * 24 * 60 * 60
                    * 1000) {
            	
            	NEmailLoginBean bean = new NEmailLoginBean();
            	
                Account account = loginTokenObj.account;

                String newLoginToken = LoginTokenCache.generateLoginToken(account.getmId());
                LoginTokenCache.loginTokenMap.put(newLoginToken, loginTokenObj);

                _data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
                _data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
                bean.loginToken= newLoginToken;

                Customer customer = account.getmCustomer();
                NMenuItemReviewCustomerBean customerBean = new NMenuItemReviewCustomerBean();
                customerBean.customerId = String.valueOf(customer.getmId());
                customerBean.setCustomerName(customer.getmName());
                customerBean.setAvatarUrl(AwsS3Utils.getAwsS3CustomerResUrl() + customer.getmFolderName() + "/"
                        + customer.getmAvatar()); 
                
                bean.customer= customerBean;
                
                _data.data = bean; 
            }

        } else {

            _data.status = ParameterConstant.STATUS_CODE_DATA_NOT_EXIST;
            _data.msg = ParameterConstant.MSG_DATA_NOT_EXIST;
        }

        return _data.toJson();
    }

    @RequestMapping(value = "/validateEmail", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object validateEmail(String email) {

        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(TextUtils.isEmpty(email)){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg  = ErrorCode.ParamError.getMsgForName("email");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }

        if (mAccountService.validateEmail(email)) {
            statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
            msg = ParameterConstant.MSG_OPERATION_SUCCESS;
        }

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object changePassword(String email, String oriPassword, String curPassword) {
//        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
//        String msg = ParameterConstant.MSG_OPERATION_ERROR;
//        Map<String, Object> data = new HashMap<String, Object>();
    	
    	ObjectBaseData<String> _data = new ObjectBaseData<String>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(TextUtils.isEmpty(email)){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg  = ErrorCode.ParamError.getMsgForName("email");
        	return _data.toJson();
        }else if(TextUtils.isEmpty(oriPassword)){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg  = ErrorCode.ParamError.getMsgForName("oriPassword");
        	return _data.toJson();
        }else if(TextUtils.isEmpty(curPassword)){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg  = ErrorCode.ParamError.getMsgForName("curPassword");
        	return _data.toJson();
        }
        
        
        

        if (mAccountService.validateEmail(email)) {

        	_data.status = ParameterConstant.STATUS_CODE_DATA_NOT_EXIST;
        	_data.msg = ParameterConstant.MSG_DATA_NOT_EXIST;
        } else {

            Account account =
                    mAccountService.changePasswordByEmail(email, oriPassword, curPassword);
            if (account != null) {
            	_data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
            	_data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
            	_data.data.put("loginToken", LoginTokenCache.generateLoginToken(account.getmId()));
            } else {
            	_data.msg = "Wrong Password";
            }
        }

        return _data.toJson();
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object resetPassword(String email) {
        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(TextUtils.isEmpty(email)){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg  = ErrorCode.ParamError.getMsgForName("email");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }

        if (mAccountService.validateEmail(email)) {

            statusCode = ParameterConstant.STATUS_CODE_DATA_NOT_EXIST;
            msg = ParameterConstant.MSG_DATA_NOT_EXIST;
        } else {

            if (mAccountService.resetPasswrodByEamil(email)) {
                statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
                msg = ParameterConstant.MSG_OPERATION_SUCCESS;
            }
        }

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }

    @RequestMapping(value = "/editCustomerName", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object editCustomerName(long customerId, String customerName) {
        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(customerId==0l){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg  = ErrorCode.ParamError.getMsgForName("customerId");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }else if(TextUtils.isEmpty(customerName)){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg  = ErrorCode.ParamError.getMsgForName("customerName");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }
        

        Customer customer = mCustomerService.findById(Customer.class, customerId);
        customer.setmName(customerName);
        mCustomerService.updateTrd(customer);

        statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
        msg = ParameterConstant.MSG_OPERATION_SUCCESS;

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }

    @RequestMapping(value = "/editCustomerAvatar", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object editCustomerAvatar(long customerId, String base64Str, double width, double height) {

        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(customerId==0l){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg  = ErrorCode.ParamError.getMsgForName("customerId");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }else if(TextUtils.isEmpty(base64Str)){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg  = ErrorCode.ParamError.getMsgForName("base64Str");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }else if(width==0.0){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg  = ErrorCode.ParamError.getMsgForName("width");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }else if(height==0.0){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg  = ErrorCode.ParamError.getMsgForName("height");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }
        
        

        String widthStr = String.valueOf((int) width);
        String heightStr = String.valueOf((int) height);

        try {
            Customer customer = mCustomerService.findById(Customer.class, customerId);

            if (!base64Str.equals("")) {

                // Get folder
                String folderName = customer.getmFolderName();

                // Generate image name
                String mimetype = AwsS3Utils.getMineType(base64Str);
                String extension = MimeUtils.guessExtensionFromMimeType(mimetype);
                String avatarName = AwsS3Utils.generateImageName(widthStr, heightStr, extension);

                // upload image to s3
                InputStream is = ImageUtils.getInputStreamFromBase64(base64Str);
                ObjectMetadata metaddata = new ObjectMetadata();
                metaddata.setContentType(mimetype);
                metaddata.setContentLength(is.available());
                AwsS3Utils.uploadFileToAwsS3CustomerRes(folderName, avatarName, is, metaddata);

                customer.setmAvatar(avatarName);
                mCustomerService.updateTrd(customer);

                String avatarUrl = AwsS3Utils.getAwsS3CustomerResUrl() + folderName + avatarName;

                data.put("avatarUrl", avatarUrl);
                statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
                msg = ParameterConstant.MSG_OPERATION_SUCCESS;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }

    @RequestMapping(value = "/addCustomerFeedback", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object addCustomerFeedback(long customerId, String content) {

        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(customerId==0l){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg  = ErrorCode.ParamError.getMsgForName("customerId");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }else if(TextUtils.isEmpty(content)){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg  = ErrorCode.ParamError.getMsgForName("content");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }

        CustomerFeedback customerFeedback = new CustomerFeedback();
        customerFeedback.setmCustomerId(customerId);
        customerFeedback.setmContent(content);
        customerFeedback.setmCreateTimestamp(TimestampUtil.getCurrentTimestamp());   //modify by file  统一使用新的时间工具类
        mCustomerFeedbackService.saveTrd(customerFeedback);

        EmailUtils.sendFeedbackEmail(customerFeedback);
        
        statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
        msg = ParameterConstant.MSG_OPERATION_SUCCESS;

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }

    @RequestMapping(value = "/activeEmail",
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object activeEmail(String signUpToken) {

        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(TextUtils.isEmpty(signUpToken)){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg  = ErrorCode.ParamError.getMsgForName("signUpToken");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }

        if (SignUpTokenCache.signUpTokenMap.containsKey(signUpToken)) {

            SignUpTokenObj signUpTokenObj = SignUpTokenCache.signUpTokenMap.get(signUpToken);

            Timestamp signUpTimestamp = signUpTokenObj.signUpTimestamp;
            Timestamp currentTimestamp = TimestampUtil.getCurrentTimestamp();   //modify by file  统一使用新的时间工具类

            int mins = 30;
            if ((currentTimestamp.getTime() - signUpTimestamp.getTime()) < mins * 60 * 1000) {

                Account account = signUpTokenObj.account;
                account.setmStatus(ParameterConstant.ACCOUNT_STATUS_ACTIVE);
                mAccountService.updateTrd(account);

                statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
                msg = ParameterConstant.MSG_OPERATION_SUCCESS;
            } else {

                msg = "Token over time.";
            }

            SignUpTokenCache.signUpTokenMap.remove(signUpToken);

        } else {

            msg = "Invalid token.";
        }

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }

    @RequestMapping(value = "/sendActiveEmail", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object sendActiveEmail(String email) {

        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(TextUtils.isEmpty(email)){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg  = ErrorCode.ParamError.getMsgForName("email");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }

        Account account = mAccountService.findByEmail(email);

        if (account != null) {

            if (account.getmStatus() != null
                    && account.getmStatus().equals(ParameterConstant.ACCOUNT_STATUS_ACTIVE)) {
                
                msg = "This account is already active.";
            } else {
                
                EmailUtils.sendActiveEmail(account);

                statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
                msg = ParameterConstant.MSG_OPERATION_SUCCESS;
            }

        } else {
            statusCode = ParameterConstant.STATUS_CODE_DATA_NOT_EXIST;
            msg = "This account is not exist.";
        }

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }

}
