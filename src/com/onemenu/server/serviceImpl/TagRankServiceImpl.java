package com.onemenu.server.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;

import com.onemenu.server.dao.TagRankDAO;
import com.onemenu.server.service.TagRankService;


public class TagRankServiceImpl extends AbstractServiceImpl implements TagRankService {

    private TagRankDAO mTagRankDAO;
    
    @Autowired
    public void setmTagRankDAO(TagRankDAO mTagRankDAO) {
        this.mTagRankDAO = mTagRankDAO;
    }
    
    @Override
    public void addPageView(String tags){
        mTagRankDAO.addPageView(tags);
    }
    

}
