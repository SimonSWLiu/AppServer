package com.onemenu.server.protocol.req.toptags;

import com.onemenu.server.protocol.req.BaseRequestData;
/**
 * 获取热门标签数据（request）
 * @author file
 * @since 2015-4-21 下午4:33:01
 * @version 1.0
 */
public class GetTopTagsData extends BaseRequestData {
	/**
	 * 类型（1.menu，2.restaurant）
	 */
	public String type = "1";
	/**标签前缀（后面支持前匹配）**/
	public String preTag = "";
	
	
	public void setType(String type) {
		this.type = type;
	}
	public void setPreTag(String preTag) {
		this.preTag = preTag;
	}
	
	
}
