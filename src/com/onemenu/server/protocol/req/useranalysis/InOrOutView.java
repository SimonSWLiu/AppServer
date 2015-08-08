package com.onemenu.server.protocol.req.useranalysis;

import com.onemenu.server.protocol.req.BaseRequestData;
/**
 * 用户行为分析请求数据
 * @author file
 * @since 2015年5月31日 上午10:08:13
 * @version 1.0
 * @copyright  by onemenu
 */
public class InOrOutView extends BaseRequestData {
	//界面
	public String viewControllerName;
	//设备
	public String equipment;
	

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public void setViewControllerName(String viewControllerName) {
		this.viewControllerName = viewControllerName;
	}
	
	

}
