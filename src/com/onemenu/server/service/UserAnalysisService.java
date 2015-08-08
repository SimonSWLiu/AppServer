package com.onemenu.server.service;

import com.onemenu.server.protocol.req.useranalysis.InOrOutView;

/**
 * 用户数据分析服务
 * @author file
 * @since 2015年5月31日 上午10:16:05
 * @version 1.0
 * @copyright  by onemenu
 */
public interface UserAnalysisService extends AbstractServiceInf {
	/**
	 * 保存进入界面采集
	 * @param view
	 * @return
	 */
	public boolean saveIn(InOrOutView view);
	/**
	 * 保存退出界面数据采集
	 * @param view
	 * @return
	 */
	public boolean saveOut(InOrOutView view);

}
