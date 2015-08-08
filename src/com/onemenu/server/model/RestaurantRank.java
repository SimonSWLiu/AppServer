package com.onemenu.server.model;

import java.sql.Timestamp;
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
 * 餐馆
 * 
 * @author Simon
 *
 */
@Entity(name = "restaurant_rank")
public class RestaurantRank implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "rank")
    private long mRank;

    @Column(name = "page_view")
    private long mPageView;

    @OneToOne(mappedBy = "mRestaurantRank")
    private Restaurant mRestaurant;
    
    // Common field
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_timestamp")
    private Date mCreateTimestamp;

    public long getmId() {
        return mId;
    }

    public Date getmCreateTimestamp() {
        return mCreateTimestamp;
    }

    public void setmCreateTimestamp(Date mCreateTimestamp) {
        this.mCreateTimestamp = mCreateTimestamp;
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

    public Restaurant getmRestaurant() {
        return mRestaurant;
    }

    public void setmRestaurant(Restaurant mRestaurant) {
        this.mRestaurant = mRestaurant;
    }

}
