package com.onemenu.server.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.TextUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.dao.RestaurantDAO;
import com.onemenu.server.javabean.RestaurantSearchConditionBean;
import com.onemenu.server.model.Restaurant;
import com.onemenu.server.util.MapUtils;


public class RestaurantDAOImpl extends BaseDAOSupport implements RestaurantDAO {

    protected HibernateTemplate mHibernateTemplate;

    @Autowired
    public void setmHibernateTemplate(HibernateTemplate mHibernateTemplate) {
        this.mHibernateTemplate = mHibernateTemplate;
    }

    @Override
    @Transactional
    public List<Restaurant> getRestaurantListByConditon(RestaurantSearchConditionBean condition) {

        List<Restaurant> restaurantList = new ArrayList<Restaurant>();

        DetachedCriteria restarantCri = DetachedCriteria.forClass(Restaurant.class);
        
        /* modify by file on 2015-07-01
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.eq("mStatus", ParameterConstant.RESTAURANT_STATUS_ENABLE));
        disjunction.add(Restrictions.eq("mStatus", ParameterConstant.RESTAURANT_STATUS_DISABLE));
        restarantCri.add(disjunction);*/
        //只查enable的餐厅
        restarantCri.add(Restrictions.eq("mStatus", ParameterConstant.RESTAURANT_STATUS_ENABLE));
        
        // 计算出 distance km 所对应的经纬度范围：
        double range = 180 / Math.PI * condition.distance / MapUtils.EARTH_RADIUS; // 单位km
        double lngR = range / Math.cos(condition.latitude * Math.PI / 180.0);
        double maxLat = condition.latitude + range;
        double minLat = condition.latitude - range;
        double maxLng = condition.longitude + lngR;
        double minLng = condition.longitude - lngR;

        // 搜索tag  
        if (!TextUtils.isEmpty(condition.tags)) {
            String[] keyWords = condition.tags.split(",");
            Junction junction = Restrictions.disjunction();

            for (int i = 0; i < keyWords.length; i++) {
                String keyWord = keyWords[i];
                junction.add(Restrictions.ilike("topTagsName", keyWord, MatchMode.ANYWHERE));
            }
            restarantCri.add(junction);
        }

        // 搜索范围
        restarantCri.add(Restrictions.conjunction()
                .add(Restrictions.between("mLatitude", minLat, maxLat))
                .add(Restrictions.between("mLongitude", minLng, maxLng)));

        /**
         * 通过数据库计算距离然后返回的数据
         */
        restaurantList =
                mHibernateTemplate
                        .findByCriteria(restarantCri, condition.startNum, condition.range);

        return restaurantList;
    }

}
