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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * 客户信息
 * 
 * @author Simon
 * 
 */
@Entity(name = "customer")
public class Customer implements PersistenceEntity {

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

    @Column(name = "company")
    private String mCompany;

    @Column(name = "profession")
    private String mProfession;

    @Column(name = "phone")
    private String mPhone;

    @Column(name = "address")
    private String mAddress;

    @Column(name = "foler_name")
    private String mFolderName;

    @OneToOne(mappedBy = "mCustomer")
    private Account mAccount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mCustomer")
    private Set<Review> mReviewSet;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mCustomer")
    private Set<Trade> mTradesSet;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name="customer_coupon", 
        joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "id"),
        inverseJoinColumns=@JoinColumn(name = "coupon_id", referencedColumnName = "id")
    )
    private Set<Coupon> mCouponSet;

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

    public String getmCompany() {
        return mCompany;
    }

    public void setmCompany(String mCompany) {
        this.mCompany = mCompany;
    }

    public String getmProfession() {
        return mProfession;
    }

    public void setmProfession(String mProfession) {
        this.mProfession = mProfession;
    }

    public String getmFolderName() {
        return mFolderName;
    }

    public void setmFolderName(String mFolderName) {
        this.mFolderName = mFolderName;
    }

    public Set<Review> getmReviewSet() {
        return mReviewSet;
    }

    public void setmReviewSet(Set<Review> mReviewSet) {
        this.mReviewSet = mReviewSet;
    }

    public Set<Trade> getmTradesSet() {
        return mTradesSet;
    }

    public void setmTradesSet(Set<Trade> mTradesSet) {
        this.mTradesSet = mTradesSet;
    }

    public Set<Coupon> getmCouponSet() {
        return mCouponSet;
    }

    public void setmCouponSet(Set<Coupon> mCouponSet) {
        this.mCouponSet = mCouponSet;
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

}
