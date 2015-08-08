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
 * 订单
 * 
 * @author Simon
 * 
 */
@Entity(name = "order_form")
public class OrderForm implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "code", unique = true)
    private String mCode;
    
    @Column(name = "remark")
    private String mRemark;

    @Column(name = "coupon_id")
    private String mCouponId;
    
    @Column(name = "coupon_desc")
    private String mCouponDesc;

    @Column(name = "status")
    private String mStatus;

    @Column(name = "price")
    private BigDecimal mPrice;

    @Column(name = "tips_fee")
    private BigDecimal mTipsFee;
    
    @Column(name="tips_type")
    private String tipsType;
    
    @Column(name="driver_bill_price")
    private BigDecimal driverBillPrice;

    @Column(name = "delivery_fee")
    private BigDecimal mDeliveryFee;

    @Column(name = "discount_fee")
    private BigDecimal mDiscountFee;

    @Column(name = "tax_fee")
    private BigDecimal mTaxFee;
    
    @Column(name = "subtotal_fee")
    private BigDecimal mSubtotalFee;
    
    @Column(name = "total_fee")
    private BigDecimal mTotalFee;
    
    @Column(name = "payment_type")
    private String mPaymentType;

    @ManyToOne
    @JoinColumn(name = "trade_id")
    private Trade mTrade;
    
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant mRestaurant;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "mOrderForm")
    private Set<OrderItem> mOrderItemSet;

    @Column(name = "is_one_menu")
    private Integer mIsOneMenu;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "rest_confirmed_timestamp")
    private Date mRestConfirmedTimestamp;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "cancel_timestamp")
    private Date mCancelTimestamp;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "call_delivery_timestamp")
    private Date mCallDeliveryTimestamp;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "driver_pending_timestamp")
    private Date mDriverPendingTimestamp;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "driver_confirmed_timestamp")
    private Date mDriverConfirmedTimestamp;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "driver_pickedup_timestamp")
    private Date mDriverPickedUpTimestamp;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "arrived_timestamp")
    private Date mArrivedTimestamp;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver mDriver;
    
    @Column(name = "receipt_image_url")
    private String mReceiptImageUrl;

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

    public String getmRemark() {
        return mRemark;
    }

    public void setmRemark(String mRemark) {
        this.mRemark = mRemark;
    }

    public String getmCouponDesc() {
        return mCouponDesc;
    }

    public void setmCouponDesc(String mCouponDesc) {
        this.mCouponDesc = mCouponDesc;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public Trade getmTrade() {
        return mTrade;
    }

    public void setmTrade(Trade mTrade) {
        this.mTrade = mTrade;
    }

    public Restaurant getmRestaurant() {
        return mRestaurant;
    }

    public void setmRestaurant(Restaurant mRestaurant) {
        this.mRestaurant = mRestaurant;
    }

    public Set<OrderItem> getmOrderItemSet() {
        return mOrderItemSet;
    }

    public void setmOrderItemSet(Set<OrderItem> mOrderItemSet) {
        this.mOrderItemSet = mOrderItemSet;
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

    public BigDecimal getmPrice() {
        return mPrice;
    }

    public void setmPrice(BigDecimal mPrice) {
        this.mPrice = mPrice;
    }

    public BigDecimal getmTipsFee() {
        return mTipsFee;
    }

    public void setmTipsFee(BigDecimal mTipsFee) {
        this.mTipsFee = mTipsFee;
    }

    public BigDecimal getmDeliveryFee() {
        return mDeliveryFee;
    }

    public void setmDeliveryFee(BigDecimal mDeliveryFee) {
        this.mDeliveryFee = mDeliveryFee;
    }

    public BigDecimal getmDiscountFee() {
        return mDiscountFee;
    }

    public void setmDiscountFee(BigDecimal mDiscountFee) {
        this.mDiscountFee = mDiscountFee;
    }

    public BigDecimal getmTaxFee() {
        return mTaxFee;
    }

    public void setmTaxFee(BigDecimal mTaxFee) {
        this.mTaxFee = mTaxFee;
    }

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }

    public BigDecimal getmSubtotalFee() {
        return mSubtotalFee;
    }

    public void setmSubtotalFee(BigDecimal mSubtotalFee) {
        this.mSubtotalFee = mSubtotalFee;
    }

    public BigDecimal getmTotalFee() {
        return mTotalFee;
    }

    public void setmTotalFee(BigDecimal mTotalFee) {
        this.mTotalFee = mTotalFee;
    }

    public Integer getmIsOneMenu() {
        return mIsOneMenu;
    }

    public void setmIsOneMenu(Integer mIsOneMenu) {
        this.mIsOneMenu = mIsOneMenu;
    }

    public Date getmRestConfirmedTimestamp() {
        return mRestConfirmedTimestamp;
    }

    public void setmRestConfirmedTimestamp(Date mRestConfirmedTimestamp) {
        this.mRestConfirmedTimestamp = mRestConfirmedTimestamp;
    }

    public Date getmCancelTimestamp() {
        return mCancelTimestamp;
    }

    public void setmCancelTimestamp(Date mCancelTimestamp) {
        this.mCancelTimestamp = mCancelTimestamp;
    }

    public Date getmCallDeliveryTimestamp() {
        return mCallDeliveryTimestamp;
    }

    public void setmCallDeliveryTimestamp(Date mCallDeliveryTimestamp) {
        this.mCallDeliveryTimestamp = mCallDeliveryTimestamp;
    }

    public Date getmDriverConfirmedTimestamp() {
        return mDriverConfirmedTimestamp;
    }

    public void setmDriverConfirmedTimestamp(Date mDriverConfirmedTimestamp) {
        this.mDriverConfirmedTimestamp = mDriverConfirmedTimestamp;
    }

    public Date getmArrivedTimestamp() {
        return mArrivedTimestamp;
    }

    public void setmArrivedTimestamp(Date mArrivedTimestamp) {
        this.mArrivedTimestamp = mArrivedTimestamp;
    }

    public Driver getmDriver() {
        return mDriver;
    }

    public void setmDriver(Driver mDriver) {
        this.mDriver = mDriver;
    }

	public String getTipsType() {
		return tipsType;
	}

	public void setTipsType(String tipsType) {
		this.tipsType = tipsType;
	}

	public BigDecimal getDriverBillPrice() {
		return driverBillPrice;
	}

	public void setDriverBillPrice(BigDecimal driverBillPrice) {
		this.driverBillPrice = driverBillPrice;
	}

	public String getmPaymentType() {
		return mPaymentType;
	}

	public void setmPaymentType(String mPaymentType) {
		this.mPaymentType = mPaymentType;
	}

	public Date getmDriverPickedUpTimestamp() {
		return mDriverPickedUpTimestamp;
	}

	public void setmDriverPickedUpTimestamp(Date mDriverPickedUpTimestamp) {
		this.mDriverPickedUpTimestamp = mDriverPickedUpTimestamp;
	}

	public String getmReceiptImageUrl() {
		return mReceiptImageUrl;
	}

	public void setmReceiptImageUrl(String mReceiptImageUrl) {
		this.mReceiptImageUrl = mReceiptImageUrl;
	}

	public Date getmDriverPendingTimestamp() {
		return mDriverPendingTimestamp;
	}

	public void setmDriverPendingTimestamp(Date mDriverPendingTimestamp) {
		this.mDriverPendingTimestamp = mDriverPendingTimestamp;
	}
	
	
}
