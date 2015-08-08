package com.onemenu.server.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.cache.ConfigCache;
import com.onemenu.server.constant.ParameterConstant.ConfigKey;
import com.onemenu.server.model.Config;
import com.onemenu.server.service.ConfigService;

@Service("configService")
public class ConfigServiceImpl extends AbstractServiceImpl implements ConfigService {

	private static final Logger logger = Logger.getLogger(ConfigServiceImpl.class);

	@Override
	@Transactional
	public boolean saveOrUpdate(String key, String value) {

		Config config = new Config(key, value);
		try {
			saveOrUpdate(config);
			ConfigCache.saveOrUpdate(config);
			return true;
		} catch (Exception e) {
			logger.error("", e);
		}
		return false;
	}

	@Override
	@Transactional
	public boolean updateAppConfig(String appVersion, String appVersionDesc, String forceUpdateVersion, String forceUpdate, String tax) {
		boolean appVerIsNull = true;
		boolean appUpVerIsNull = true;
		if (!TextUtils.isEmpty(appVersion)) {
			appVerIsNull = false;
			saveOrUpdate("appVersion", appVersion);
		}
		if (!TextUtils.isEmpty(appVersionDesc)) {
			saveOrUpdate("appVersionDesc", appVersionDesc);
		}
		if (!TextUtils.isEmpty(forceUpdateVersion)) {
			appUpVerIsNull = false;
			saveOrUpdate("forceUpdateVersion", forceUpdateVersion);
		}

		if (TextUtils.isEmpty(forceUpdate)) {
			if (!appVerIsNull && !appUpVerIsNull) {
				// appVersion > forceUpdateVersion TODO 暂时用字符串比较，以后可能用需要用版本号的比较
				if (appVersion.compareTo(forceUpdateVersion) > 0) {
					forceUpdate = "0";
				} else
					forceUpdate = "1";
			}
		}

		if (!TextUtils.isEmpty(forceUpdate)) {
			saveOrUpdate("forceUpdate", forceUpdate);
		}

		if (TextUtils.isEmpty(tax)) {
			tax = "6.25";
		}

		saveOrUpdate("tax", tax);

		return true;
	}

	@Override
	@Transactional
	public boolean delete(String key) {
		delete(new Config(key, null));
		ConfigCache.remove(key);
		return true;
	}

	@Override
	@Transactional
	public void loadAll() {
		//从数据库加载
		List<Config> configs = findAll(Config.class);
		//数据库没数据的时候，从配置文件加载
		if (configs == null || configs.isEmpty()) {
			// 从配置文件加载
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("AppConfig.properties");
			Properties props = new Properties();
			try {
				props.load(is);
				configs = new ArrayList<Config>(props.size()+1);
				for (Entry<Object, Object> entry : props.entrySet()) {
					configs.add(new Config(entry.getKey().toString(), entry.getValue().toString()));
				}
			} catch (IOException e) {// 加载失败
				logger.error("", e);
			}
			configs.add(new Config("server_key", "oneMenuKey"));
			for (Config config : configs)
				save(config);
		}
		
		ConfigCache.load(configs);
		
		if(ConfigCache.getValue(ConfigKey.GMAIL_USER_NAME)==null){
			saveOrUpdate(ConfigKey.GMAIL_USER_NAME, "onemenu@onemenu.mobi");
			ConfigCache.put(ConfigKey.GMAIL_USER_NAME, "onemenu@onemenu.mobi");
		}
		
		if(ConfigCache.getValue(ConfigKey.IS_SEND_FAX)==null){
			saveOrUpdate(ConfigKey.IS_SEND_FAX, "true");
			ConfigCache.put(ConfigKey.IS_SEND_FAX, "true");
		}
		
		if(ConfigCache.getValue(ConfigKey.GMAIL_PASSWORD)==null){
			saveOrUpdate(ConfigKey.GMAIL_PASSWORD, "onemenu31415926");
			ConfigCache.put(ConfigKey.GMAIL_PASSWORD, "onemenu31415926");
		}
		
		
		if(ConfigCache.getValue(ConfigKey.IS_SAND_BOX)==null){
			saveOrUpdate(ConfigKey.IS_SAND_BOX, "true");
			ConfigCache.put(ConfigKey.IS_SAND_BOX, "true");
		}
		
		if(ConfigCache.getValue(ConfigKey.APPLICATION_NAME)==null){
			saveOrUpdate(ConfigKey.APPLICATION_NAME, "One-Menu");
			ConfigCache.put(ConfigKey.APPLICATION_NAME, "One-Menu");
		}
		
		
	}

}
