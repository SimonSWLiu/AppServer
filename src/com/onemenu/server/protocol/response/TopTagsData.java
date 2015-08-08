package com.onemenu.server.protocol.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onemenu.server.javabean.TopTagsBean;

/**
 * 热门标签返回信息
 * @author file
 * @since 2015-4-21 下午4:59:04
 * @version 1.0
 */
public class TopTagsData extends BaseData {
	
	public Map<String, List<TopTagsBean>>  data = new HashMap<String, List<TopTagsBean>>(1);

}
