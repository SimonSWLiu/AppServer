package com.onemenu.server.serviceImpl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.model.UserAnalysis;
import com.onemenu.server.protocol.req.useranalysis.InOrOutView;
import com.onemenu.server.service.UserAnalysisService;
import com.onemenu.server.util.Equipment;
import com.onemenu.server.util.StringUtil;
import com.onemenu.server.util.TimestampUtil;

/**
 * 用户行为数据采集服务实现类
 * 
 * @author file
 * @since 2015年5月31日 上午10:19:50
 * @version 1.0
 * @copyright by onemenu
 */
@Service("userAnalysisService")
public class UserAnalysisServiceImpl extends AbstractServiceImpl implements UserAnalysisService {
	
	private static final Logger logger = Logger.getLogger(UserAnalysisServiceImpl.class);
	
	@Override
	@Transactional
	public boolean saveIn(InOrOutView view) {
		UserAnalysis userAnalysis = new UserAnalysis();
		userAnalysis.setDeviceId(view.deviceId);
		userAnalysis.setViewName(view.viewControllerName);
		userAnalysis.setInTime(TimestampUtil.getCurrentTimestamp());
		userAnalysis.setEquipment(Equipment.getEquipmentByCode(view.equipment));
		if (!StringUtil.isEmpty(view.appVersion))
			userAnalysis.setAppVersion(view.appVersion);
		try {
			saveOrUpdate(userAnalysis);
		} catch (Exception e) {
			logger.error("e",e);
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean saveOut(InOrOutView view) {
		
		 Timestamp  curTime = TimestampUtil.getCurrentTimestamp();
		 
		 DetachedCriteria cri = DetachedCriteria.forClass(UserAnalysis.class);
		 cri.add(Restrictions.eq("deviceId", view.deviceId));
		 cri.add(Restrictions.eq("viewName", view.viewControllerName));
		 cri.add(Restrictions.eq("keepTime", 0));
		 if (!StringUtil.isEmpty(view.appVersion))
			 cri.add(Restrictions.eq("appVersion", view.appVersion));
		 if(StringUtil.isNumber(view.equipment)){
			 cri.add(Restrictions.eq("equipment", Equipment.getEquipmentByCode(view.equipment).getCode()));
		 }
		 
		 cri.addOrder(Order.desc("inTime"));
		 
		 List<UserAnalysis> userAnalysiss = getBaseDAO().findByCriteria(cri, 0, 1, UserAnalysis.class);
		 
		 if(userAnalysiss!=null&&!userAnalysiss.isEmpty()){
			 UserAnalysis userAnalysis = userAnalysiss.get(0);
			 userAnalysis.setOutTime(curTime);
			 int keepTime = (int) (curTime.getTime() - userAnalysis.getInTime().getTime());
			 userAnalysis.setKeepTime(keepTime);
			
			 try {
					saveOrUpdate(userAnalysis);
				} catch (Exception e) {
					logger.error("e",e);
					return false;
				}
		 }
		 
		 
		return true;
	}

}
