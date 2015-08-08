package com.onemenu.server.service;

import com.onemenu.server.model.Account;

/**
 * 
 * @author lin
 *
 */
public interface AccountService extends AbstractServiceInf {

	/**
	 * 登录
	 * 
	 * @param account
	 * @return
	 */
	public Account login(Account account);

	public Account findByEmail(String email);

	/**
	 * 注册账号
	 * 
	 * @param account
	 * @return
	 */
	public boolean signUp(Account account);

	public boolean validateEmail(String email);

	public Account changePasswordByEmail(String email, String oriPassword, String curPassword);

	public boolean resetPasswrodByEamil(String email);

	public int getAllAcountCount();

	public void deleteAllToken();

	public Account loginUpdateToken(String email, String password, Integer device, String token,Integer userType);
	
	public void logoutUpdateToken(long accountId,Integer device, String token,Integer userType);
}
