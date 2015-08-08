package com.onemenu.server.protocol.req.toptags;

import com.onemenu.server.protocol.req.BaseRequestData;
/**
 * 提交搜索热门标签
 * @author file
 * @since 2015-4-21 下午5:23:30
 * @version 1.0
 */
public class PostTopTagsData extends BaseRequestData {
	//类型（1.menu，2.restaurant）
	public String type;
	//标签
	public String tags;
	
	
	public void setType(String type) {
		this.type = type;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}

	
	
}
