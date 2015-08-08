package com.onemenu.server.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.onemenu.server.dao.ThirdPartyAccountDAO;
import com.onemenu.server.model.ThirdPartyAccount;
import com.onemenu.server.service.ThirdPartyAccountService;


public class ThirdPartyAccountServiceImpl extends AbstractServiceImpl implements
        ThirdPartyAccountService {

    ThirdPartyAccountDAO mThirdPartyAccountDAO;

    @Autowired
    public void setmThirdPartyAccountDAO(ThirdPartyAccountDAO mThirdPartyAccountDAO) {
        this.mThirdPartyAccountDAO = mThirdPartyAccountDAO;
    }

    @Override
    public boolean isSignUp(String thirdPartyAccountId, String loginType) {

        Map<String, Object> criMap = new HashMap<String, Object>();
        criMap.put("mThirdPartyId", thirdPartyAccountId);
        criMap.put("mLoginType", loginType);
        long count = findRowCount(ThirdPartyAccount.class, criMap);

        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public ThirdPartyAccount login(String thirdPartyAccountId, String loginType) {

        Map<String, Object> criMap = new HashMap<String, Object>();
        criMap.put("mThirdPartyId", thirdPartyAccountId);
        criMap.put("mLoginType", loginType);

        DetachedCriteria cri = DetachedCriteria.forClass(ThirdPartyAccount.class);
        cri.add(Restrictions.allEq(criMap));
        List<ThirdPartyAccount> thirdPartyAccountList =
                getBaseDAO().findByCriteria(cri, ThirdPartyAccount.class);

        if (thirdPartyAccountList.size() != 0) {
            return thirdPartyAccountList.get(0);
        } else {
            return null;
        }
    }
}
