package com.onemenu.server.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 *
 * Title: DeliveryTimeSheet.java
 *
 * @author simonliu
 *
 * @data Jun 10, 2015
 * @time 11:41:52 AM
 * 
 * Description: 
 *
 */

@Entity(name = "delivery_time_sheet")
public class DeliveryTimeSheet implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "available_date")
    private String mAvailableDate;

    @Column(name = "available_start_time")
    private String mAvailableStartTime;

    @Column(name = "available_end_time")
    private String mAvailableEndTime;

    @Column(name = "available_quantity")
    private Integer mAvailableQuantity;

    @Column(name = "quantity")
    private Integer mQuantity;

    @ManyToMany(mappedBy = "mDeliveryTimeSheetSet", fetch = FetchType.LAZY)
    private Set<Driver> mDriverSet;

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

    public String getmAvailableStartTime() {
        return mAvailableStartTime;
    }

    public void setmAvailableStartTime(String mAvailableStartTime) {
        this.mAvailableStartTime = mAvailableStartTime;
    }

    public String getmAvailableEndTime() {
        return mAvailableEndTime;
    }

    public void setmAvailableEndTime(String mAvailableEndTime) {
        this.mAvailableEndTime = mAvailableEndTime;
    }

    public Integer getmAvailableQuantity() {
        return mAvailableQuantity;
    }

    public void setmAvailableQuantity(Integer mAvailableQuantity) {
        this.mAvailableQuantity = mAvailableQuantity;
    }

    public Integer getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(Integer mQuantity) {
        this.mQuantity = mQuantity;
    }

    public Set<Driver> getmDriverSet() {
        return mDriverSet;
    }

    public void setmDriverSet(Set<Driver> mDriverSet) {
        this.mDriverSet = mDriverSet;
    }

    public Date getmCreateTimestamp() {
        return mCreateTimestamp;
    }

    public void setmCreateTimestamp(Date mCreateTimestamp) {
        this.mCreateTimestamp = mCreateTimestamp;
    }

    public String getmAvailableDate() {
        return mAvailableDate;
    }

    public void setmAvailableDate(String mAvailableDate) {
        this.mAvailableDate = mAvailableDate;
    }

}
