package com.onemenu.server.javabean;

import java.math.BigDecimal;

/**
 * 热门标签返回的数据
 * @author file
 * @since 2015-4-21 下午4:54:46
 * @version 1.0
 */
public class TopTagsBean {
	//标签
	public String  tag;
	//搜索总次数
	public String  count;
	
	public void setCount(BigDecimal count){
		this.count = count.toString();
	}
	
}
