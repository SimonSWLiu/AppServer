package com.onemenu.server.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.onemenu.server.model.Config;

/**
 * 配置缓存
 * @author file
 * @since 2015-4-13 下午2:54:10
 * @version 1.0
 */
public final class ConfigCache {
	
	private static final Logger logger = Logger.getLogger(ConfigCache.class);
	//服务器启动就加载配置数据，是线程安全的
	private static final Map<String,String> _config = new HashMap<String,String>();
	
	/**
	 * 加载配置数据
	 * @param configs 配置数据列表
	 */
	public static void load(List<Config> configs){
	  if(configs==null){	
		  logger.error(" config list is null~~~");
		  return;
	  }
	  
	  for(Config config:configs){
		  load(config);
	  }
	  
	}
	/**
	 * 加载配置数据
	 * @param config  配置数据
	 */
	public static void load(Config config){
		if(config==null){
			logger.error("config is null ");
			return;
		}
		put(config.getKey(),config.getValue());
	}
	
	/**
	 * 保存或更新配置数据
	 * @param config
	 */
	public static void saveOrUpdate(Config config){
		load(config);
	}
	
	/**
	 * 缓存数据
	 * @param key  键
	 * @param value 值
	 */
	public static void put(String key,String value){
		_config.put(key, value);
	}
	/**
	 * 通过键获取值
	 * @param key 键
	 * @return 值
	 */
	public static String getValue(String key){
		return _config.get(key);
	}
	/**
	 * 通过键获取值，若值不存在则用默认值
	 * @param key  键
	 * @param defaultValue  默认值
	 * @return
	 */
	public static String getValue(String key,String defaultValue){
		String value = _config.get(key);
		return value==null?defaultValue:value;
	}
	
	/**
	 * 获取所有配置数据
	 * @return
	 */
	public static List<Config>  getConfigs(){
		List<Config>  list = new ArrayList<Config>(_config.size());
		for(Entry<String,String> entry :_config.entrySet()){
			list.add(new Config(entry.getKey(), entry.getValue()));
		}
		return list;
	}

	public static void  remove(String key){
		_config.remove(key);
	}
	
}
