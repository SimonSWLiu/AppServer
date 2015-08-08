package com.onemenu.server.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author simonliu
 *
 */

@Entity(name = "review")
public class Review implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "summary")
    private String mSummary;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer mCustomer;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mReview")
    private Set<ReviewItem> mReviewItemSet;

    // 关系维护方
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "review_rank_id")
    private ReviewRank mReviewRank;

    // Common field
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_timestamp")
    private Date mCreateTimestamp;

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmSummary() {
        return mSummary;
    }

    public void setmSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    public Customer getmCustomer() {
        return mCustomer;
    }

    public void setmCustomer(Customer mCustomer) {
        this.mCustomer = mCustomer;
    }

    public Set<ReviewItem> getmReviewItemSet() {
        return mReviewItemSet;
    }

    public void setmReviewItemSet(Set<ReviewItem> mReviewItemSet) {
        this.mReviewItemSet = mReviewItemSet;
    }

    public ReviewRank getmReviewRank() {
        return mReviewRank;
    }

    public void setmReviewRank(ReviewRank mReviewRank) {
        this.mReviewRank = mReviewRank;
    }

    public Date getmCreateTimestamp() {
        return mCreateTimestamp;
    }

    public void setmCreateTimestamp(Date mCreateTimestamp) {
        this.mCreateTimestamp = mCreateTimestamp;
    }

}
