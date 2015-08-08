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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * 优惠券
 * 
 * @author Simon
 *
 */
@Entity(name = "coupon")
public class Coupon implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "name")
    private String mName;

    @Column(name = "type")
    private String mType;

    @Column(name = "type_amount")
    private BigDecimal mTypeAmount;

    @Column(name = "target_tpye")
    private String mTargetType;
    
    @Column(name = "extra_cri_type")
    private String mExtraCriType;

    @Column(name = "extra_cri_amount")
    private BigDecimal mExtraCriAmount;
    
    @Column(name = "description")
    private String mDescription;

    @Column(name = "quantity")
    private Integer mQuantity;

    @Column(name = "total_quantity")
    private Integer mTotalQuantity;

    @Column(name = "available_start_time")
    private String mAvailableStartTime;
    
    @Column(name = "available_end_time")
    private String mAvailableEndTime;
    
    @Column(name = "effective_date")
    private String mEffectiveDate;

    @Column(name = "maturity_date")
    private String mMaturityDate;
    
    @Column(name = "weeks")
    private String mWeeks;
    
    @Column(name = "image_url")
    private String mImageUrl;

    @Column(name = "status")
    private String mStatus;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant mRestaurant;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name="coupon_target_dish", 
        joinColumns = @JoinColumn(name = "coupon_id", referencedColumnName = "id"),
        inverseJoinColumns=@JoinColumn(name = "dish_id", referencedColumnName = "id")
    )
    private Set<Dish> mCouponTargetDishsSet;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name="coupon_extra_cri_dish", 
        joinColumns = @JoinColumn(name = "coupon_id", referencedColumnName = "id"),
        inverseJoinColumns=@JoinColumn(name = "dish_id", referencedColumnName = "id")
    )
    private Set<Dish> mCouponExtraCriDishsSet;
    
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "mCoupon")
//    private Set<CouponTargetDish> mCouponTargetDishsSet;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "mCoupon")
//    private Set<CouponExtraCriDish> mCouponExtraCriDishsSet;

    @ManyToMany(mappedBy = "mCouponSet", fetch = FetchType.LAZY)
    private Set<Customer> mCustomerSet;
    
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

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Integer getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(Integer mQuantity) {
        this.mQuantity = mQuantity;
    }

    public Integer getmTotalQuantity() {
        return mTotalQuantity;
    }

    public void setmTotalQuantity(Integer mTotalQuantity) {
        this.mTotalQuantity = mTotalQuantity;
    }

    public String getmEffectiveDate() {
        return mEffectiveDate;
    }

    public void setmEffectiveDate(String mEffectiveDate) {
        this.mEffectiveDate = mEffectiveDate;
    }

    public String getmMaturityDate() {
        return mMaturityDate;
    }

    public void setmMaturityDate(String mMaturityDate) {
        this.mMaturityDate = mMaturityDate;
    }

    public Restaurant getmRestaurant() {
        return mRestaurant;
    }

    public void setmRestaurant(Restaurant mRestaurant) {
        this.mRestaurant = mRestaurant;
    }

    public String getmTargetType() {
        return mTargetType;
    }

    public void setmTargetType(String mTargetType) {
        this.mTargetType = mTargetType;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getmExtraCriType() {
        return mExtraCriType;
    }

    public void setmExtraCriType(String mExtraCriType) {
        this.mExtraCriType = mExtraCriType;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public BigDecimal getmTypeAmount() {
        return mTypeAmount;
    }

    public void setmTypeAmount(BigDecimal mTypeAmount) {
        this.mTypeAmount = mTypeAmount;
    }

    public BigDecimal getmExtraCriAmount() {
        return mExtraCriAmount;
    }

    public void setmExtraCriAmount(BigDecimal mExtraCriAmount) {
        this.mExtraCriAmount = mExtraCriAmount;
    }

    public Date getmCreateTimestamp() {
        return mCreateTimestamp;
    }

    public void setmCreateTimestamp(Date mCreateTimestamp) {
        this.mCreateTimestamp = mCreateTimestamp;
    }

    public Set<Customer> getmCustomerSet() {
        return mCustomerSet;
    }

    public void setmCustomerSet(Set<Customer> mCustomerSet) {
        this.mCustomerSet = mCustomerSet;
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

    public String getmWeeks() {
        return mWeeks;
    }

    public void setmWeeks(String mWeeks) {
        this.mWeeks = mWeeks;
    }

    public Set<Dish> getmCouponTargetDishsSet() {
        return mCouponTargetDishsSet;
    }

    public void setmCouponTargetDishsSet(Set<Dish> mCouponTargetDishsSet) {
        this.mCouponTargetDishsSet = mCouponTargetDishsSet;
    }

    public Set<Dish> getmCouponExtraCriDishsSet() {
        return mCouponExtraCriDishsSet;
    }

    public void setmCouponExtraCriDishsSet(Set<Dish> mCouponExtraCriDishsSet) {
        this.mCouponExtraCriDishsSet = mCouponExtraCriDishsSet;
    }
    
}
