package com.onemenu.server.service;


public interface ConfigService extends AbstractServiceInf {

	public boolean saveOrUpdate(String key, String value);

	public boolean updateAppConfig(String appVersion, String appVersionDesc, String forceUpdateVersion, String forceUpdate, String tax);

	public boolean delete(String key);
	
	public void  loadAll();

	

}
