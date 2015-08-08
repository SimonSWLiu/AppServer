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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * Driver信息
 * 
 * @author Simon
 * 
 */
@Entity(name = "driver")
public class Driver implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "name")
    private String mName;

    @Column(name = "avatar")
    private String mAvatar;

    @Column(name = "avatarUrl")
    private String mAvatarUrl;

    @Column(name = "avatarWidth")
    private Integer mAvatarWidth;

    @Column(name = "avatarHeight")
    private Integer mAvatarHeight;

    @Column(name = "phone")
    private String mPhone;

    @Column(name = "address")
    private String mAddress;

    @Column(name = "email")
    private String mEmail;

    @Column(name = "status")
    private Integer mStatus;

    @Column(name = "device_token")
    private String deviceToken;
    
    /**
     * 纬度
     */
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    /**
     * 经度
     */
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @OneToOne(mappedBy = "mDriver")
    private Account mAccount;
    
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region mRegion;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mDriver")
    private Set<OrderForm> mOrderFormSet;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name="driver_delivery_time_sheet", 
        joinColumns = @JoinColumn(name = "driver_id", referencedColumnName = "id"),
        inverseJoinColumns=@JoinColumn(name = "delivery_time_sheet_id", referencedColumnName = "id")
    )
    private Set<DeliveryTimeSheet> mDeliveryTimeSheetSet;
    
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

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmAvatar() {
        return mAvatar;
    }

    public void setmAvatar(String mAvatar) {
        this.mAvatar = mAvatar;
    }

    public Account getmAccount() {
        return mAccount;
    }

    public void setmAccount(Account mAccount) {
        this.mAccount = mAccount;
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

    public Integer getmStatus() {
        return mStatus;
    }

    public void setmStatus(Integer mStatus) {
        this.mStatus = mStatus;
    }


    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Set<DeliveryTimeSheet> getmDeliveryTimeSheetSet() {
        return mDeliveryTimeSheetSet;
    }

    public void setmDeliveryTimeSheetSet(Set<DeliveryTimeSheet> mDeliveryTimeSheetSet) {
        this.mDeliveryTimeSheetSet = mDeliveryTimeSheetSet;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmAvatarUrl() {
        return mAvatarUrl;
    }

    public void setmAvatarUrl(String mAvatarUrl) {
        this.mAvatarUrl = mAvatarUrl;
    }

    public Integer getmAvatarWidth() {
        return mAvatarWidth;
    }

    public void setmAvatarWidth(Integer mAvatarWidth) {
        this.mAvatarWidth = mAvatarWidth;
    }

    public Integer getmAvatarHeight() {
        return mAvatarHeight;
    }

    public void setmAvatarHeight(Integer mAvatarHeight) {
        this.mAvatarHeight = mAvatarHeight;
    }

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Region getmRegion() {
		return mRegion;
	}

	public void setmRegion(Region mRegion) {
		this.mRegion = mRegion;
	}
	

}
