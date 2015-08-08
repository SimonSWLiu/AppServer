package com.onemenu.server.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.dao.TopTagsDao;
import com.onemenu.server.javabean.TopTagsBean;
import com.onemenu.server.model.MenuTopTags;
import com.onemenu.server.model.RestaurantTopTags;
import com.onemenu.server.protocol.req.toptags.GetTopTagsData;
import com.onemenu.server.protocol.req.toptags.PostTopTagsData;
import com.onemenu.server.protocol.req.toptags.TopTagsType;
import com.onemenu.server.service.TopTagsService;

/**
 * 热门标签service实现类
 * 
 * @author file
 * @since 2015-4-21 下午4:51:45
 * @version 1.0
 */
@Service("topTagsService")
public class TopTagsServiceImpl extends AbstractServiceImpl implements TopTagsService {

	@Resource(name = "topTagsDao")
	private TopTagsDao topTagsDao;

	@Override
	public List<TopTagsBean> getTopTagsData(GetTopTagsData resData) {
		TopTagsType type = TopTagsType.getType(resData.type);
		switch (type) {
		case Menu:
			return getMenuTopTagsData(resData);
		case Restaurant:
			return getRestaurantTopTagsData(resData);
		default:
			return new ArrayList<TopTagsBean>(1);
		}
	}

	private List<TopTagsBean> getMenuTopTagsData(GetTopTagsData resData) {
		return topTagsDao.getMenuTopTagsData(resData);
	}

	private List<TopTagsBean> getRestaurantTopTagsData(GetTopTagsData resData) {
		return topTagsDao.getRestaurantTopTagsData(resData);
	}

	@Override
	public boolean postTopTags(PostTopTagsData resData) {
		TopTagsType type = TopTagsType.getType(resData.type);
		switch (type) {
		case Menu:
			return postMenuTopTags(resData);
		case Restaurant:
			return postRestaurantTopTags(resData);
		default:
			return false;
		}
	}

	@Transactional
	private boolean postMenuTopTags(PostTopTagsData resData) {

		String tags = resData.tags;

		if (TextUtils.isEmpty(tags))
			return false;

		String latitude = resData.latitude;
		String longitude = resData.longitude;
		double _latitude = 0.0;
		double _longitude = 0.0;

		if (!TextUtils.isEmpty(latitude))
			_latitude = Double.parseDouble(latitude);

		if (!TextUtils.isEmpty(longitude))
			_longitude = Double.parseDouble(longitude);

		String[] _tags = tags.split(",");

		for (String tag : _tags) {
			Map<String, Object> params = new HashMap<String, Object>(3);
			params.put("tag", tag);
			params.put("latitude", _latitude);
			params.put("longitude", _longitude);
			long rowNum = topTagsDao.getCount(MenuTopTags.class, params);

			MenuTopTags topTag = new MenuTopTags();
			topTag.setTag(tag);
			topTag.setLatitude(_latitude);
			topTag.setLongitude(_longitude);
			if (rowNum == 0) {
				topTag.setCount(1);
				// 保存
				topTagsDao.save(topTag);
			} else if (rowNum > 0) {
				// update
				topTagsDao.updateTagCount(topTag);
			}
		}

		return true;
	}
	
	@Transactional
	private boolean postRestaurantTopTags(PostTopTagsData resData) {
		String tags = resData.tags;

		if (TextUtils.isEmpty(tags))
			return false;

		String latitude = resData.latitude;
		String longitude = resData.longitude;
		double _latitude = 0.0;
		double _longitude = 0.0;

		if (!TextUtils.isEmpty(latitude))
			_latitude = Double.parseDouble(latitude);

		if (!TextUtils.isEmpty(longitude))
			_longitude = Double.parseDouble(longitude);

		String[] _tags = tags.split(",");

		for (String tag : _tags) {
			Map<String, Object> params = new HashMap<String, Object>(3);
			params.put("tag", tag);
			params.put("latitude", _latitude);
			params.put("longitude", _longitude);

			long rowNum = topTagsDao.getCount(RestaurantTopTags.class, params);

			RestaurantTopTags topTag = new RestaurantTopTags();
			topTag.setTag(tag);
			topTag.setLatitude(_latitude);
			topTag.setLongitude(_longitude);
			if (rowNum == 0) {
				topTag.setCount(1);
				// 保存
				topTagsDao.save(topTag);
			} else if (rowNum > 0) {
				// update
				topTagsDao.updateTagCount(topTag);
			}
		}

		return true;
	}

}
