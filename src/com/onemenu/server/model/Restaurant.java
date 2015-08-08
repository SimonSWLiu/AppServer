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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.http.util.TextUtils;

import com.onemenu.server.util.StringUtil;


/**
 *
 * Title: Restaurant.java
 *
 * @author simonliu
 *
 * @data Jun 15, 2015
 * @time 12:23:53 PM
 * 
 *       Description:
 *
 */
/**
 *
 * Title: Restaurant.java
 *
 * @author simonliu
 *
 * @data Jun 15, 2015
 * @time 12:26:55 PM
 * 
 *       Description:
 *
 */
@Entity(name = "restaurant")
public class Restaurant implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "name", length = 100, nullable = false)
    private String mName;

    /** 对name去除空格 **/
    @Column(name = "top_tags_name")
    private String topTagsName;

    @Column(name = "logo", nullable = false)
    private String mLogo;

    @Column(name = "logoUrl", nullable = false)
    private String mLogoUrl;

    @Column(name = "logoWidth", nullable = false)
    private Integer mLogoWidth;

    @Column(name = "logoHeight", nullable = false)
    private Integer mLogoHeight;

    /**
     * 纬度
     */
    @Column(name = "latitude", nullable = false)
    private Double mLatitude;

    /**
     * 经度
     */
    @Column(name = "longitude", nullable = false)
    private Double mLongitude;

    @Column(name = "address", nullable = false)
    private String mAddress;

    @Column(name = "street", nullable = false)
    private String mStreet;

    @Column(name = "city", nullable = false)
    private String mCity;

    @Column(name = "state", nullable = false)
    private String mState;

    @Column(name = "zip_code", nullable = false)
    private String mZipCode;

    /**
     * used for alerting order
     */
    @Column(name = "fax_num", nullable = false)
    private String mFaxNum;

    /**
     * used for alerting order
     */
    @Column(name = "email", nullable = false)
    private String mEmail;

    /**
     * used for alerting order
     */
    @Column(name = "phone", nullable = false)
    private String mPhone;

    @Column(name = "description", nullable = false)
    private String mDescription;

    @Column(name = "status", nullable = false)
    private Integer mStatus;

    /**
     * control restaurant support delivery or not
     */
    @Column(name = "is_delivery", nullable = false)
    private Boolean mIsDelivery;

    /**
     * control restaurant support pick out or not
     */
    @Column(name = "is_pick_out", nullable = false)
    private Boolean mIsPickOut;

    /**
     * control restaurant support pre-order or not
     */
    @Column(name = "is_pre_order", nullable = false)
    private Boolean mIsPreOrder;

    /**
     * 资源目录名称
     */
    @Column(name = "folderName", nullable = false)
    private String mFolderName;

    @Column(name = "free_delivery_limit", nullable = false)
    private BigDecimal mFreeDeliveryLimit;

    @Column(name = "delivery_fee", nullable = false)
    private BigDecimal mDeliveryFee;

    @Column(name = "delivery_distance", nullable = false)
    private Double mDeliveryDistance;

    @Column(name = "min_delivery_total", nullable = false)
    private BigDecimal mMinDeliveryTotal;

    @Column(name = "mon_Delivery_start_time", nullable = false)
    private String mMonDeliveryStartTime;

    @Column(name = "mon_Delivery_end_time", nullable = false)
    private String mMonDeliveryEndTime;

    @Column(name = "tue_Delivery_start_time", nullable = false)
    private String mTueDeliveryStartTime;

    @Column(name = "tue_Delivery_end_time", nullable = false)
    private String mTueDeliveryEndTime;

    @Column(name = "wed_Delivery_start_time", nullable = false)
    private String mWedDeliveryStartTime;

    @Column(name = "wed_Delivery_end_time", nullable = false)
    private String mWedDeliveryEndTime;

    @Column(name = "thu_Delivery_start_time", nullable = false)
    private String mThuDeliveryStartTime;

    @Column(name = "thu_Delivery_end_time", nullable = false)
    private String mThuDeliveryEndTime;

    @Column(name = "fri_Delivery_start_time", nullable = false)
    private String mFriDeliveryStartTime;

    @Column(name = "fri_Delivery_end_time", nullable = false)
    private String mFriDeliveryEndTime;

    @Column(name = "sat_Delivery_start_time", nullable = false)
    private String mSatDeliveryStartTime;

    @Column(name = "sat_Delivery_end_time", nullable = false)
    private String mSatDeliveryEndTime;

    @Column(name = "sun_Delivery_start_time", nullable = false)
    private String mSunDeliveryStartTime;

    @Column(name = "sun_Delivery_end_time", nullable = false)
    private String mSunDeliveryEndTime;

    @Column(name = "mon_open_start_time", nullable = false)
    private String mMonOpenStartTime;

    @Column(name = "mon_open_end_time", nullable = false)
    private String mMonOpenEndTime;

    @Column(name = "tue_open_start_time", nullable = false)
    private String mTueOpenStartTime;

    @Column(name = "tue_open_end_time", nullable = false)
    private String mTueOpenEndTime;

    @Column(name = "wed_open_start_time", nullable = false)
    private String mWedOpenStartTime;

    @Column(name = "wed_open_end_time", nullable = false)
    private String mWedOpenEndTime;

    @Column(name = "thu_open_start_time", nullable = false)
    private String mThuOpenStartTime;

    @Column(name = "thu_open_end_time", nullable = false)
    private String mThuOpenEndTime;

    @Column(name = "fri_open_start_time", nullable = false)
    private String mFriOpenStartTime;

    @Column(name = "fri_open_end_time", nullable = false)
    private String mFriOpenEndTime;

    @Column(name = "sat_open_start_time", nullable = false)
    private String mSatOpenStartTime;

    @Column(name = "sat_open_end_time", nullable = false)
    private String mSatOpenEndTime;

    @Column(name = "sun_open_start_time", nullable = false)
    private String mSunOpenStartTime;

    @Column(name = "sun_open_end_time", nullable = false)
    private String mSunOpenEndTime;

    @OneToOne(mappedBy = "mRestaurant")
    private Merchant mMerchant;
    
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region mRegion;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mRestaurant")
    private Set<DishCategory> mDishCategorySet;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mRestaurant")
    private Set<Coupon> mCouponSet;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mRestaurant")
    private Set<OrderForm> mOrderFormSet;

    // 关系维护方
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_rank_id")
    private RestaurantRank mRestaurantRank;

    // Common field
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_timestamp", nullable = false)
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
        if (TextUtils.isEmpty(topTagsName))
            setTopTagsName(StringUtil.removalSpace(mName));
    }

    public String getmLogo() {
        return mLogo;
    }

    public void setmLogo(String mLogo) {
        this.mLogo = mLogo;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Merchant getmMerchant() {
        return mMerchant;
    }

    public void setmMerchant(Merchant mMerchant) {
        this.mMerchant = mMerchant;
    }

    public Set<Coupon> getmCouponSet() {
        return mCouponSet;
    }

    public void setmCouponSet(Set<Coupon> mCouponSet) {
        this.mCouponSet = mCouponSet;
    }

    public String getmFolderName() {
        return mFolderName;
    }

    public void setmFolderName(String mFolderName) {
        this.mFolderName = mFolderName;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public void setmLatitude(Double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public void setmLongitude(Double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public BigDecimal getmFreeDeliveryLimit() {
        return mFreeDeliveryLimit;
    }

    public void setmFreeDeliveryLimit(BigDecimal mFreeDeliveryLimit) {
        this.mFreeDeliveryLimit = mFreeDeliveryLimit;
    }

    public BigDecimal getmDeliveryFee() {
        return mDeliveryFee;
    }

    public void setmDeliveryFee(BigDecimal mDeliveryFee) {
        this.mDeliveryFee = mDeliveryFee;
    }

    public Set<DishCategory> getmDishCategorySet() {
        return mDishCategorySet;
    }

    public void setmDishCategorySet(Set<DishCategory> mDishCategorySet) {
        this.mDishCategorySet = mDishCategorySet;
    }

    public Set<OrderForm> getmOrderFormSet() {
        return mOrderFormSet;
    }

    public void setmOrderFormSet(Set<OrderForm> mOrderFormSet) {
        this.mOrderFormSet = mOrderFormSet;
    }

    public RestaurantRank getmRestaurantRank() {
        return mRestaurantRank;
    }

    public void setmRestaurantRank(RestaurantRank mRestaurantRank) {
        this.mRestaurantRank = mRestaurantRank;
    }

    public Date getmCreateTimestamp() {
        return mCreateTimestamp;
    }

    public void setmCreateTimestamp(Date mCreateTimestamp) {
        this.mCreateTimestamp = mCreateTimestamp;
    }

    public String getmFaxNum() {
        return mFaxNum;
    }

    public void setmFaxNum(String mFaxNum) {
        this.mFaxNum = mFaxNum;
    }

    public String getmStreet() {
        return mStreet;
    }

    public void setmStreet(String mStreet) {
        this.mStreet = mStreet;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public BigDecimal getmMinDeliveryTotal() {
        return mMinDeliveryTotal;
    }

    public void setmMinDeliveryTotal(BigDecimal mMinDeliveryTotal) {
        this.mMinDeliveryTotal = mMinDeliveryTotal;
    }

    public String getmMonDeliveryStartTime() {
        return mMonDeliveryStartTime;
    }

    public void setmMonDeliveryStartTime(String mMonDeliveryStartTime) {
        this.mMonDeliveryStartTime = mMonDeliveryStartTime;
    }

    public String getmMonDeliveryEndTime() {
        return mMonDeliveryEndTime;
    }

    public void setmMonDeliveryEndTime(String mMonDeliveryEndTime) {
        this.mMonDeliveryEndTime = mMonDeliveryEndTime;
    }

    public String getmTueDeliveryStartTime() {
        return mTueDeliveryStartTime;
    }

    public void setmTueDeliveryStartTime(String mTueDeliveryStartTime) {
        this.mTueDeliveryStartTime = mTueDeliveryStartTime;
    }

    public String getmTueDeliveryEndTime() {
        return mTueDeliveryEndTime;
    }

    public void setmTueDeliveryEndTime(String mTueDeliveryEndTime) {
        this.mTueDeliveryEndTime = mTueDeliveryEndTime;
    }

    public String getmWedDeliveryStartTime() {
        return mWedDeliveryStartTime;
    }

    public void setmWedDeliveryStartTime(String mWedDeliveryStartTime) {
        this.mWedDeliveryStartTime = mWedDeliveryStartTime;
    }

    public String getmWedDeliveryEndTime() {
        return mWedDeliveryEndTime;
    }

    public void setmWedDeliveryEndTime(String mWedDeliveryEndTime) {
        this.mWedDeliveryEndTime = mWedDeliveryEndTime;
    }

    public String getmThuDeliveryStartTime() {
        return mThuDeliveryStartTime;
    }

    public void setmThuDeliveryStartTime(String mThuDeliveryStartTime) {
        this.mThuDeliveryStartTime = mThuDeliveryStartTime;
    }

    public String getmThuDeliveryEndTime() {
        return mThuDeliveryEndTime;
    }

    public void setmThuDeliveryEndTime(String mThuDeliveryEndTime) {
        this.mThuDeliveryEndTime = mThuDeliveryEndTime;
    }

    public String getmFriDeliveryStartTime() {
        return mFriDeliveryStartTime;
    }

    public void setmFriDeliveryStartTime(String mFriDeliveryStartTime) {
        this.mFriDeliveryStartTime = mFriDeliveryStartTime;
    }

    public String getmFriDeliveryEndTime() {
        return mFriDeliveryEndTime;
    }

    public void setmFriDeliveryEndTime(String mFriDeliveryEndTime) {
        this.mFriDeliveryEndTime = mFriDeliveryEndTime;
    }

    public String getmSatDeliveryStartTime() {
        return mSatDeliveryStartTime;
    }

    public void setmSatDeliveryStartTime(String mSatDeliveryStartTime) {
        this.mSatDeliveryStartTime = mSatDeliveryStartTime;
    }

    public String getmSatDeliveryEndTime() {
        return mSatDeliveryEndTime;
    }

    public void setmSatDeliveryEndTime(String mSatDeliveryEndTime) {
        this.mSatDeliveryEndTime = mSatDeliveryEndTime;
    }

    public String getmSunDeliveryStartTime() {
        return mSunDeliveryStartTime;
    }

    public void setmSunDeliveryStartTime(String mSunDeliveryStartTime) {
        this.mSunDeliveryStartTime = mSunDeliveryStartTime;
    }

    public String getmSunDeliveryEndTime() {
        return mSunDeliveryEndTime;
    }

    public void setmSunDeliveryEndTime(String mSunDeliveryEndTime) {
        this.mSunDeliveryEndTime = mSunDeliveryEndTime;
    }

    public String getmMonOpenStartTime() {
        return mMonOpenStartTime;
    }

    public void setmMonOpenStartTime(String mMonOpenStartTime) {
        this.mMonOpenStartTime = mMonOpenStartTime;
    }

    public String getmMonOpenEndTime() {
        return mMonOpenEndTime;
    }

    public void setmMonOpenEndTime(String mMonOpenEndTime) {
        this.mMonOpenEndTime = mMonOpenEndTime;
    }

    public String getmTueOpenStartTime() {
        return mTueOpenStartTime;
    }

    public void setmTueOpenStartTime(String mTueOpenStartTime) {
        this.mTueOpenStartTime = mTueOpenStartTime;
    }

    public String getmTueOpenEndTime() {
        return mTueOpenEndTime;
    }

    public void setmTueOpenEndTime(String mTueOpenEndTime) {
        this.mTueOpenEndTime = mTueOpenEndTime;
    }

    public String getmWedOpenStartTime() {
        return mWedOpenStartTime;
    }

    public void setmWedOpenStartTime(String mWedOpenStartTime) {
        this.mWedOpenStartTime = mWedOpenStartTime;
    }

    public String getmWedOpenEndTime() {
        return mWedOpenEndTime;
    }

    public void setmWedOpenEndTime(String mWedOpenEndTime) {
        this.mWedOpenEndTime = mWedOpenEndTime;
    }

    public String getmThuOpenStartTime() {
        return mThuOpenStartTime;
    }

    public void setmThuOpenStartTime(String mThuOpenStartTime) {
        this.mThuOpenStartTime = mThuOpenStartTime;
    }

    public String getmThuOpenEndTime() {
        return mThuOpenEndTime;
    }

    public void setmThuOpenEndTime(String mThuOpenEndTime) {
        this.mThuOpenEndTime = mThuOpenEndTime;
    }

    public String getmFriOpenStartTime() {
        return mFriOpenStartTime;
    }

    public void setmFriOpenStartTime(String mFriOpenStartTime) {
        this.mFriOpenStartTime = mFriOpenStartTime;
    }

    public String getmFriOpenEndTime() {
        return mFriOpenEndTime;
    }

    public void setmFriOpenEndTime(String mFriOpenEndTime) {
        this.mFriOpenEndTime = mFriOpenEndTime;
    }

    public String getmSatOpenStartTime() {
        return mSatOpenStartTime;
    }

    public void setmSatOpenStartTime(String mSatOpenStartTime) {
        this.mSatOpenStartTime = mSatOpenStartTime;
    }

    public String getmSatOpenEndTime() {
        return mSatOpenEndTime;
    }

    public void setmSatOpenEndTime(String mSatOpenEndTime) {
        this.mSatOpenEndTime = mSatOpenEndTime;
    }

    public String getmSunOpenStartTime() {
        return mSunOpenStartTime;
    }

    public void setmSunOpenStartTime(String mSunOpenStartTime) {
        this.mSunOpenStartTime = mSunOpenStartTime;
    }

    public String getmSunOpenEndTime() {
        return mSunOpenEndTime;
    }

    public void setmSunOpenEndTime(String mSunOpenEndTime) {
        this.mSunOpenEndTime = mSunOpenEndTime;
    }

    public Double getmDeliveryDistance() {
        return mDeliveryDistance;
    }

    public void setmDeliveryDistance(Double mDeliveryDistance) {
        this.mDeliveryDistance = mDeliveryDistance;
    }

    public String getmZipCode() {
        return mZipCode;
    }

    public void setmZipCode(String mZipCode) {
        this.mZipCode = mZipCode;
    }

    public String getmState() {
        return mState;
    }

    public void setmState(String mState) {
        this.mState = mState;
    }

    public String getTopTagsName() {
        return topTagsName;
    }

    public void setTopTagsName(String topTagsName) {

        if (TextUtils.isEmpty(topTagsName))
            return;
        this.topTagsName = topTagsName.toLowerCase();
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public Integer getmStatus() {
        return mStatus;
    }

    public void setmStatus(Integer mStatus) {
        this.mStatus = mStatus;
    }

    public String getmLogoUrl() {
        return mLogoUrl;
    }

    public void setmLogoUrl(String mLogoUrl) {
        this.mLogoUrl = mLogoUrl;
    }

    public Integer getmLogoWidth() {
        return mLogoWidth;
    }

    public void setmLogoWidth(Integer mLogoWidth) {
        this.mLogoWidth = mLogoWidth;
    }

    public Integer getmLogoHeight() {
        return mLogoHeight;
    }

    public void setmLogoHeight(Integer mLogoHeight) {
        this.mLogoHeight = mLogoHeight;
    }

    public Boolean getmIsDelivery() {
        return mIsDelivery;
    }

    public void setmIsDelivery(Boolean mIsDelivery) {
        this.mIsDelivery = mIsDelivery;
    }

    public Boolean getmIsPreOrder() {
        return mIsPreOrder;
    }

    public void setmIsPreOrder(Boolean mIsPreOrder) {
        this.mIsPreOrder = mIsPreOrder;
    }

    public Boolean getmIsPickOut() {
        return mIsPickOut;
    }

    public void setmIsPickOut(Boolean mIsPickOut) {
        this.mIsPickOut = mIsPickOut;
    }

	public Region getmRegion() {
		return mRegion;
	}

	public void setmRegion(Region mRegion) {
		this.mRegion = mRegion;
	}
    
    

}
