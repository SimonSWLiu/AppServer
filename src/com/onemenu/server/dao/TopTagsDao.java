package com.onemenu.server.dao;

import java.util.List;
import java.util.Map;

import com.onemenu.server.javabean.TopTagsBean;
import com.onemenu.server.model.MenuTopTags;
import com.onemenu.server.model.PersistenceEntity;
import com.onemenu.server.model.RestaurantTopTags;
import com.onemenu.server.protocol.req.toptags.GetTopTagsData;

/**
 * 热门标签数据库操作
 * @author file
 * @since 2015-4-22 下午4:08:26
 * @version 1.0
 */
public interface TopTagsDao extends BaseDAOInf {
	
	/**
	 *  根据条件获取菜单的热门标签
	 * @param resData 请求条件
	 * @return
	 */
	public List<TopTagsBean> getMenuTopTagsData(GetTopTagsData resData);
	/**
	 * 根据条件获取餐馆的热门标签
	 * @param resData  请求条件
	 * @return
	 */
	public List<TopTagsBean> getRestaurantTopTagsData(GetTopTagsData resData); 
	
	
	
	public <T extends PersistenceEntity> long getCount(Class<T> clazz,Map<String,Object> params);
	
	
	public int updateTagCount(MenuTopTags topTag);
	
	public int updateTagCount(RestaurantTopTags topTag);
	
	
}
