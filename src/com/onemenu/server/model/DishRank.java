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
 * 菜式
 * 
 * @author Simon
 *
 */
@Entity(name = "dish_rank")
public class DishRank implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "rank")
    private long mRank;

    @Column(name = "page_view")
    private long mPageView;

    @Column(name = "purchase_volume")
    private long mPurchaseVolume;

    @OneToOne(mappedBy = "mDishRank")
    private Dish mDish;

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

    public Dish getmDish() {
        return mDish;
    }

    public void setmDish(Dish mDish) {
        this.mDish = mDish;
    }

    public long getmPurchaseVolume() {
        return mPurchaseVolume;
    }

    public void setmPurchaseVolume(long mPurchaseVolume) {
        this.mPurchaseVolume = mPurchaseVolume;
    }

}
