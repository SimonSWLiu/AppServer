package com.onemenu.server.javabean.delivery;
/**
 * 位置
 * @author file
 * @since 2015-7-1 下午3:52:28
 * @version 1.0
 */
public class DLocationBean {

	// 经度
	public String longitude;
	// 纬度
	public String latitude;
	
	
	public void setLongitude(double longitude) {
		this.longitude = String.valueOf(longitude);
	}
	
	public void setLatitude(double latitude) {
		this.latitude = String.valueOf(latitude);
	}
	
	

}
