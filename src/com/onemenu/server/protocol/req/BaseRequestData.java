package com.onemenu.server.protocol.req;
/**
 * 公共的请求数据
 * @author file
 * @since 2015-4-21 下午4:22:03
 * @version 1.0
 */
public class BaseRequestData {
	//设备id
	public String deviceId;
	//当前版本号
	public String appVersion;
	//经度
	public String longitude;
	//纬度 
	public String latitude;
	//设备名称
	public String deviceName;
	//推送通知设备token
	public String notificationDeviceToken;
	//设备标识
	public String device;//ios-1,android-2
	

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	public void setNotificationDeviceToken(String notificationDeviceToken) {
		this.notificationDeviceToken = notificationDeviceToken;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setDevice(String device) {
		this.device = device;
	}
	
	
	
	
	
	
}
