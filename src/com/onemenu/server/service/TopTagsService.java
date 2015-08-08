package com.onemenu.server.service;

import java.util.List;

import com.onemenu.server.javabean.TopTagsBean;
import com.onemenu.server.protocol.req.toptags.GetTopTagsData;
import com.onemenu.server.protocol.req.toptags.PostTopTagsData;

/**
 * 热门标签service 接口
 * @author file
 * @since 2015-4-21 下午4:50:01
 * @version 1.0
 */
public interface TopTagsService extends AbstractServiceInf {
	/**
	 * 根据请求条件获取热门标签
	 * @param resData
	 * @return
	 */
	List<TopTagsBean> getTopTagsData(GetTopTagsData resData);
	/**
	 * 提交搜索标签
	 * @param resData
	 * @return
	 */
	boolean postTopTags(PostTopTagsData resData);

}
