package com.onemenu.server.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author simonliu
 *
 */

@Entity(name = "review_rank")
public class ReviewRank implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "rank")
    private long mRank;

    @Column(name = "page_view")
    private long mPageView;

    @OneToOne(mappedBy = "mReviewRank")
    private Review mReview;

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

    public long getmRank() {
        return mRank;
    }

    public void setmRank(long mRank) {
        this.mRank = mRank;
    }

    public long getmPageView() {
        return mPageView;
    }

    public void setmPageView(long mPageView) {
        this.mPageView = mPageView;
    }

    public Review getmReview() {
        return mReview;
    }

    public void setmReview(Review mReview) {
        this.mReview = mReview;
    }

}
