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

@Entity
@Table(name = "customer_feedback")
public class CustomerFeedback implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "customer_id")
    private long mCustomerId;

    @Column(name = "customer_name")
    private String mCustomerName;

    @Column(name = "customer_phone")
    private String mCustomerPhone;

    @Column(name = "customer_email")
    private String mCustomerEmail;

    @Column(name = "content")
    private String mContent;

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

    public long getmCustomerId() {
        return mCustomerId;
    }

    public void setmCustomerId(long mCustomerId) {
        this.mCustomerId = mCustomerId;
    }

    public String getmCustomerName() {
        return mCustomerName;
    }

    public void setmCustomerName(String mCustomerName) {
        this.mCustomerName = mCustomerName;
    }

    public String getmCustomerPhone() {
        return mCustomerPhone;
    }

    public void setmCustomerPhone(String mCustomerPhone) {
        this.mCustomerPhone = mCustomerPhone;
    }

    public String getmCustomerEmail() {
        return mCustomerEmail;
    }

    public void setmCustomerEmail(String mCustomerEmail) {
        this.mCustomerEmail = mCustomerEmail;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public Date getmCreateTimestamp() {
        return mCreateTimestamp;
    }

    public void setmCreateTimestamp(Date mCreateTimestamp) {
        this.mCreateTimestamp = mCreateTimestamp;
    }

}
