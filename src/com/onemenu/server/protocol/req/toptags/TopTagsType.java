package com.onemenu.server.protocol.req.toptags;
/**
 * 热门标签类型
 * @author file
 * @since 2015-4-21 下午4:34:58
 * @version 1.0
 */
public enum TopTagsType {
	Menu("1"),
	Restaurant("2");
	String value;
	private TopTagsType(String value) {
		this.value = value;
	}
	/**
	 * 根据类型值获取热门标签类型
	 * @param value  类型值
	 * @return 热门标签类型（默认返回Menu）
	 */
	public static TopTagsType getType(String value){
		for(TopTagsType type:values())
			if(type.value.equals(value))
				return type;
		return Menu;
	}
	
	public String getValue(){
		return value;
	}
	
}
