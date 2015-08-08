package com.onemenu.server.daoImpl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.dao.ReviewDAO;
import com.onemenu.server.javabean.MenuSearchConditionBean;
import com.onemenu.server.javabean.NMenuItemReviewBean;
import com.onemenu.server.model.Review;
import com.onemenu.server.model.ReviewItem;
import com.onemenu.server.model.ReviewRank;


public class ReviewDAOImpl extends BaseDAOSupport implements ReviewDAO {
	
	private static final Logger logger = Logger.getLogger(ReviewDAOImpl.class);
	
    protected HibernateTemplate mHibernateTemplate;

    @Autowired
    public void setmHibernateTemplate(HibernateTemplate mHibernateTemplate) {
        this.mHibernateTemplate = mHibernateTemplate;
    }

    @Override
    @Transactional
    public Review findByReviewId(long reviewId) throws RuntimeException {

        Review review = mHibernateTemplate.get(Review.class, reviewId);

        Set<ReviewItem> reviewItemSet = new HashSet<ReviewItem>();

        for (ReviewItem reviewItem : review.getmReviewItemSet()) {

            reviewItemSet.add(reviewItem);
        }

        review.setmReviewItemSet(reviewItemSet);

        return review;
    }

    @Override
    @Transactional
    public List<Review> findByCustomerId(long customerId) throws RuntimeException {

        DetachedCriteria cri = DetachedCriteria.forClass(Review.class);
        cri.add(Restrictions.eq("mCustomer.mId", customerId));
        // cri.add(Restrictions.isNotNull("mCreateTimestamp"));
        cri.addOrder(Order.desc("mCreateTimestamp"));

        List<Review> reviewList = mHibernateTemplate.findByCriteria(cri);

        for (Review review : reviewList) {

            Set<ReviewItem> reviewItemSetTest = review.getmReviewItemSet();

            Set<ReviewItem> reviewItemSet = new HashSet<ReviewItem>();

            for (ReviewItem reviewItem : review.getmReviewItemSet()) {

                reviewItemSet.add(reviewItem);
            }

            review.setmReviewItemSet(reviewItemSet);
        }

        return reviewList;
    }

    @Override
    @Transactional
    public void addPageView(long reviewId) throws RuntimeException {

        Review review = mHibernateTemplate.get(Review.class, reviewId);
        if (review != null) {
            ReviewRank reviewRank = review.getmReviewRank();
            if (reviewRank == null) {
                reviewRank = new ReviewRank();
                reviewRank.setmPageView(1);
                review.setmReviewRank(reviewRank);
                mHibernateTemplate.save(reviewRank);
            } else {
                long pageView = reviewRank.getmPageView();
                pageView++;
                reviewRank.setmPageView(pageView);
                mHibernateTemplate.update(reviewRank);
            }
        }
    }

    @Override
    @Transactional
    public List<Review> getReviewListByCondition(MenuSearchConditionBean condition) {

        DetachedCriteria cri = DetachedCriteria.forClass(Review.class);
        // 构造tags搜索相关的条件
        if (!TextUtils.isEmpty(condition.keyWord)) {
            String[] keyWords = condition.keyWord.split(",");
            Junction junction = Restrictions.disjunction();

            for (int i = 0; i < keyWords.length; i++) {
                String keyWord = keyWords[i];
                junction.add(Restrictions.ilike("mSummary", keyWord, MatchMode.ANYWHERE));
            }
            cri.add(junction);

        }

        List<Review> reviewList = mHibernateTemplate.findByCriteria(cri);

        for (Review review : reviewList) {

            Set<ReviewItem> reviewItemSetTest = review.getmReviewItemSet();

            Set<ReviewItem> reviewItemSet = new HashSet<ReviewItem>();

            for (ReviewItem reviewItem : review.getmReviewItemSet()) {

                reviewItemSet.add(reviewItem);
            }

            review.setmReviewItemSet(reviewItemSet);
        }

        return reviewList;
    }
    //,rr.page_view AS pageView
    final String sql = "SELECT 'R' as type, r.id AS reviewId,r.summary AS summary,CONCAT(c.foler_name,'/',c.avatar) AS avatarUrl,c.company AS customerCompany,c.name AS customerName,c.profession AS customerProfession  "
        + "FROM review r  LEFT OUTER JOIN  customer c  ON r.customer_id=c.id  LEFT OUTER JOIN   review_rank rr  ON r.review_rank_id=rr.id  %s    ORDER BY rr.`page_view` DESC" ;
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<NMenuItemReviewBean> getReviewsByCondition(MenuSearchConditionBean condition) {
		 StringBuilder sqlBuilder =new StringBuilder(" ");
		 Map<String,String> params = null;
		 // 构造tags搜索相关的条件
        if (!TextUtils.isEmpty(condition.keyWord)) {
            String[] keyWords = condition.keyWord.split(",");
            sqlBuilder = new StringBuilder("where  ");
            int len = keyWords.length;
            params = new HashMap<String,String>(len); 
            for (int i = 0; i < len-1; i++) {
            	String paramName = "p"+i;
                sqlBuilder.append("r.summary like :"+paramName).append("  or  ");
                params.put(paramName, "%"+keyWords[i]+"%");
            }
            
            String paramName = "p"+(len-1);
            sqlBuilder.append("r.summary like :"+paramName).append(" ");
            params.put(paramName, "%"+keyWords[len-1]+"%");
        }
        String _sql = String.format(sql, sqlBuilder.toString());
        
        if(logger.isDebugEnabled())
        	logger.debug("sql:"+_sql);
        
        Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();
        try{
        	Query query = session.createSQLQuery(_sql);
        	
        	if(params!=null){
        		for(Entry<String,String> entry:params.entrySet())
        			query.setParameter(entry.getKey(), entry.getValue());
        	}
        	query.setFirstResult(condition.startNum).setMaxResults(condition.range)        		
        		 .setResultTransformer(new AliasToBeanResultTransformer(NMenuItemReviewBean.class));
        	
        	return query.list();
        }catch(Exception e){
        	logger.error("", e);
        }finally{
        	//session.close();
        }
        
        
		
		
		return null;
	}

	 final String sqlById = "SELECT 'R' as type, r.id AS reviewId,r.summary AS summary,CONCAT(c.foler_name,'/',c.avatar) AS avatarUrl,c.company AS customerCompany,c.name AS customerName,c.profession AS customerProfession  "
		        + "FROM review r  LEFT OUTER JOIN  customer c  ON r.customer_id=c.id  where r.id=:id " ;
	@Override
	@Transactional
	public NMenuItemReviewBean getReviewMenuItemById(String id) {
		Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();
		try {
			Query query = session.createSQLQuery(sqlById);
			query.setParameter("id", id);
			query.setResultTransformer(new AliasToBeanResultTransformer(NMenuItemReviewBean.class));
			return (NMenuItemReviewBean) query.uniqueResult();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			// session.close();
		}
		return null;
	}
}
