package com.onemenu.server.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * 商家和客户、第三方登录的账户信息
 * 
 * @author Simon
 *
 */
@Entity
@Table(name = "tag_rank")
public class TagRank implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "tag", unique = true)
    private String mTag;

    @Column(name = "pageView")
    private long mPageView;

    // Common field
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_timestamp")
    private Date mCreateTimestamp;

    public Date getmCreateTimestamp() {
        return mCreateTimestamp;
    }

    public void setmCreateTimestamp(Date mCreateTimestamp) {
        this.mCreateTimestamp = mCreateTimestamp;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmTag() {
        return mTag;
    }

    public void setmTag(String mTag) {
        this.mTag = mTag;
    }

    public long getmPageView() {
        return mPageView;
    }

    public void setmPageView(long mPageView) {
        this.mPageView = mPageView;
    }

}
