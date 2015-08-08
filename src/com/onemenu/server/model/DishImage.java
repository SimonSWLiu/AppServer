package com.onemenu.server.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * 菜式
 * 
 * @author Simon
 *
 */
@Entity(name = "dish_image")
public class DishImage implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "name")
    private String mName;

    @Column(name = "url")
    private String mUrl;

    @Column(name = "width")
    private Integer mWidth;

    @Column(name = "height")
    private Integer mHeight;

    @Column(name = "description")
    private String mDescription;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish mDish;

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

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public Integer getmWidth() {
        return mWidth;
    }

    public void setmWidth(Integer mWidth) {
        this.mWidth = mWidth;
    }

    public Integer getmHeight() {
        return mHeight;
    }

    public void setmHeight(Integer mHeight) {
        this.mHeight = mHeight;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Dish getmDish() {
        return mDish;
    }

    public void setmDish(Dish mDish) {
        this.mDish = mDish;
    }

    public Date getmCreateTimestamp() {
        return mCreateTimestamp;
    }

    public void setmCreateTimestamp(Date mCreateTimestamp) {
        this.mCreateTimestamp = mCreateTimestamp;
    }

}
