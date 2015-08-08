package com.onemenu.server.model;

import java.math.BigDecimal;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 
 * 交易记录
 * 
 * @author Simon
 * 
 */
@Entity(name = "trade")
public class Trade implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "code", unique = true)
    private String mCode;

    @Column(name = "get_type")
    private String mGetType;

    @Column(name = "payment_type")
    private String mPaymentType;

    @Column(name = "customer_name")
    private String mCustomerName;

    @Column(name = "customer_phone")
    private String mCustomerPhone;

    @Column(name = "coupon_id")
    private String mCouponId;

    @Column(name = "coupon_desc")
    private String mCouponDesc;

    @Column(name = "customer_address")
    private String mCustomerAddress;

    @Column(name = "customer_apt")
    private String mCustomerApt;

    @Column(name = "customer_street")
    private String mCustomerStreet;

    @Column(name = "customer_city")
    private String mCustomerCity;
    
    @Column(name = "customer_zip_code")
    private String mCustomerZipCode;

    @Column(name = "price")
    private BigDecimal mPrice;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer mCustomer;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "mTrade")
    private Set<OrderForm> mOrderFormSet;

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

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }

    public String getmPaymentType() {
        return mPaymentType;
    }

    public void setmPaymentType(String mPaymentType) {
        this.mPaymentType = mPaymentType;
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

    public String getmCustomerAddress() {
        return mCustomerAddress;
    }

    public void setmCustomerAddress(String mCustomerAddress) {
        this.mCustomerAddress = mCustomerAddress;
    }


    public BigDecimal getmPrice() {
        return mPrice;
    }

    public void setmPrice(BigDecimal mPrice) {
        this.mPrice = mPrice;
    }

    public Customer getmCustomer() {
        return mCustomer;
    }

    public void setmCustomer(Customer mCustomer) {
        this.mCustomer = mCustomer;
    }

    public Set<OrderForm> getmOrderFormSet() {
        return mOrderFormSet;
    }

    public void setmOrderFormSet(Set<OrderForm> mOrderFormSet) {
        this.mOrderFormSet = mOrderFormSet;
    }

    public Date getmCreateTimestamp() {
        return mCreateTimestamp;
    }

    public void setmCreateTimestamp(Date mCreateTimestamp) {
        this.mCreateTimestamp = mCreateTimestamp;
    }

    public String getmCouponId() {
        return mCouponId;
    }

    public void setmCouponId(String mCouponId) {
        this.mCouponId = mCouponId;
    }

    public String getmCouponDesc() {
        return mCouponDesc;
    }

    public void setmCouponDesc(String mCouponDesc) {
        this.mCouponDesc = mCouponDesc;
    }

    public String getmGetType() {
        return mGetType;
    }

    public void setmGetType(String mGetType) {
        this.mGetType = mGetType;
    }

    public String getmCustomerApt() {
        return mCustomerApt;
    }

    public void setmCustomerApt(String mCustomerApt) {
        this.mCustomerApt = mCustomerApt;
    }

    public String getmCustomerStreet() {
        return mCustomerStreet;
    }

    public void setmCustomerStreet(String mCustomerStreet) {
        this.mCustomerStreet = mCustomerStreet;
    }

    public String getmCustomerCity() {
        return mCustomerCity;
    }

    public void setmCustomerCity(String mCustomerCity) {
        this.mCustomerCity = mCustomerCity;
    }

    public String getmCustomerZipCode() {
        return mCustomerZipCode;
    }

    public void setmCustomerZipCode(String mCustomerZipCode) {
        this.mCustomerZipCode = mCustomerZipCode;
    }

}
