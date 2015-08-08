package com.onemenu.server.serviceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.dao.AccountDAO;
import com.onemenu.server.model.Account;
import com.onemenu.server.model.DeviceToken;
import com.onemenu.server.service.AccountService;
import com.onemenu.server.util.StringUtil;
import com.onemenu.server.util.TimestampUtil;

public class AccountServiceImpl extends AbstractServiceImpl implements AccountService {

	AccountDAO	mAccountDAO;

	@Autowired
	public void setmAccountDAO(AccountDAO mAccountDAO) {
		this.mAccountDAO = mAccountDAO;
	}

	@Override
	public Account login(Account account) {

		DetachedCriteria accountCri = DetachedCriteria.forClass(Account.class);

		accountCri.add(Restrictions.eq("mEmail", account.getmEmail()));
		accountCri.add(Restrictions.eq("mPassword", account.getmPassword()));

		List<?> list = getBaseDAO().findByCriteria(accountCri, Account.class);

		if (list.size() == 1) {
			return (Account) list.get(0);
		}

		return null;
	}

	@Override
	public Account findByEmail(String email) {

		DetachedCriteria accountCri = DetachedCriteria.forClass(Account.class);

		accountCri.add(Restrictions.eq("mEmail", email));

		List<?> list = getBaseDAO().findByCriteria(accountCri, Account.class);

		if (list.size() == 1) {
			return (Account) list.get(0);
		}

		return null;
	}

	@Override
	public boolean signUp(Account account) {
		getBaseDAO().save(account);

		return true;
	}

	// Return true if Email is not exist.
	public boolean validateEmail(String email) {

		long count = findRowCount(Account.class, "mEmail", email);

		if (count == 0) {
			return true;
		}

		return false;
	}

	@Transactional
	public Account changePasswordByEmail(String email, String oriPassword, String curPassword) {

		Account account = new Account();
		account.setmEmail(email);
		account.setmPassword(oriPassword);
		account = login(account);

		if (account != null) {
			account.setmPassword(curPassword);
			updateTrd(account);
		}

		return account;
	}

	@Transactional
	public boolean resetPasswrodByEamil(String email) {

		boolean result = false;

		DetachedCriteria accountCri = DetachedCriteria.forClass(Account.class);
		accountCri.add(Restrictions.eq("mEmail", email));
		List<Account> accountList = getBaseDAO().findByCriteria(accountCri, Account.class);

		if (accountList.size() == 1) {
			Account account = accountList.get(0);
			account.setmPassword("12345678");
			updateTrd(account);
			result = true;
		}

		return result;
	}

	@Override
	@Transactional
	public int getAllAcountCount() {
		DetachedCriteria accountCri = DetachedCriteria.forClass(Account.class);
		accountCri.setProjection(Projections.rowCount());
		List<?> results = getBaseDAO().findByCriteria(accountCri, Account.class);
		return ((Long) results.get(0)).intValue();
	}

	@Override
	@Transactional
	public void deleteAllToken() {
		mAccountDAO.deleteAllToken();
	}

	@Override
	@Transactional
	public Account loginUpdateToken(String email, String password, Integer device, String token,Integer userType) {

		DetachedCriteria accountCri = DetachedCriteria.forClass(Account.class);

		accountCri.add(Restrictions.eq("mEmail", email));
		accountCri.add(Restrictions.eq("mPassword", password));

		List<Account> list = getBaseDAO().findByCriteria(accountCri, Account.class);
		if (list == null || list.isEmpty())
			return null;

		Account account = list.get(0);
		
		if(userType == ParameterConstant.LOGIN_DRIVER && account.getmDriver() == null){
			return null;
		}else if(userType == ParameterConstant.LOGIN_MERCHANT && account.getmMerchant()==null){
			return null;
		}else if(userType == ParameterConstant.LOGIN_USER && account.getmCustomer() ==null){
			return null;
		}
		

		Set<DeviceToken> tokens = account.getDeviceTokenSet();
		if (tokens == null) {
			tokens = new HashSet<DeviceToken>();
			account.setDeviceTokenSet(tokens);
		}
		boolean isExist = false;
		for (DeviceToken deviceToken : tokens) {
			if (deviceToken.getToken().equals(token) && deviceToken.getDevice().equals(device)) {
				isExist = true;
				deviceToken.setLogin(ParameterConstant.LOGIN);
			}
		}

		// 设备不存在，加入
		if (!isExist && !StringUtil.isEmpty(token)) {
			DeviceToken _token = new DeviceToken();
			_token.setDevice(device);
			_token.setLogin(ParameterConstant.LOGIN);
			_token.setToken(token);
			_token.setmCreateTimestamp(TimestampUtil.getCurrentTimestamp());
			_token.setmAccount(account);
			tokens.add(_token);
		}
		
		account.setLastLoginTime(TimestampUtil.getCurrentTimestamp());
		update(account);
		
		return account;

	}

	@Override
	@Transactional
	public void logoutUpdateToken(long accountId, Integer device, String token, Integer userType) {
		
		Account account = findById(Account.class, accountId);
		
		if(account==null)
			return;
		
		if(userType == ParameterConstant.LOGIN_DRIVER && account.getmDriver() == null){
			return;
		}else if(userType == ParameterConstant.LOGIN_MERCHANT && account.getmMerchant()==null){
			return ;
		}else if(userType == ParameterConstant.LOGIN_USER && account.getmCustomer() ==null){
			return ;
		}
		

		Set<DeviceToken> tokens = account.getDeviceTokenSet();
		if (tokens == null) {
			return ;
		}
		boolean isExist = false;
		for (DeviceToken deviceToken : tokens) {
			if (deviceToken.getToken().equals(token) && deviceToken.getDevice().equals(device)) {
				isExist = true;
				deviceToken.setLogin(ParameterConstant.LOGOUT);
			}
		}

		// 设备存在，更新
		if (isExist) {
			update(account);
		}
		
		
	}

}
