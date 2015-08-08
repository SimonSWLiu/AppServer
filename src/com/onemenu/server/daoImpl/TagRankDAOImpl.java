package com.onemenu.server.daoImpl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.dao.TagRankDAO;
import com.onemenu.server.model.ReviewRank;
import com.onemenu.server.model.TagRank;


public class TagRankDAOImpl extends BaseDAOSupport implements TagRankDAO {
    protected HibernateTemplate mHibernateTemplate;

    @Autowired
    public void setmHibernateTemplate(HibernateTemplate mHibernateTemplate) {
        this.mHibernateTemplate = mHibernateTemplate;
    }

    @Override
    @Transactional
    public void addPageView(String tags) throws RuntimeException {

        String[] tagArray = tags.split(",");
        for (String tag : tagArray) {

            DetachedCriteria tagRankCri = DetachedCriteria.forClass(TagRank.class);
            tagRankCri.add(Restrictions.eq("mTag", tag));
            List<TagRank> tagRankList = mHibernateTemplate.findByCriteria(tagRankCri);

            if (tagRankList.size() == 0) {
                TagRank tagRank = new TagRank();
                tagRank.setmPageView(1);
                mHibernateTemplate.save(tagRank);
            } else if (tagRankList.size() == 1) {
                TagRank tagRank = tagRankList.get(0);
                long pageView = tagRank.getmPageView();
                pageView++;
                tagRank.setmPageView(pageView);
                mHibernateTemplate.update(tagRank);
            }
        }
    }
}
