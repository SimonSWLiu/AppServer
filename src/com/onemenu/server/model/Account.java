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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * 商家和客户、第三方登录的账户信息
 * 
 * @author Simon
 * 
 */
@Entity
@Table(name = "account")
public class Account implements PersistenceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private long mId;

	@Column(name = "email", unique = true)
	private String mEmail;

	@Column(name = "password")
	// , nullable = false)
	private String mPassword;

	@Column(name = "status")
	private String mStatus;

	// 关系维护方
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	private Customer mCustomer;

	// 关系维护方
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "merchant_id")
	private Merchant mMerchant;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "driver_id")
	private Driver mDriver;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mAccount")
	private Set<ThirdPartyAccount> mThirdPartyAccountSet;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mAccount")
	private Set<DeviceToken> deviceTokenSet;

	// Common field
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_timestamp")
	private Date mCreateTimestamp;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login_timestamp")
	private Date lastLoginTime;

	public long getmId() {
		return mId;
	}

	public void setmId(long mId) {
		this.mId = mId;
	}

	public String getmEmail() {
		return mEmail;
	}

	public void setmEmail(String mEmail) {
		this.mEmail = mEmail;
	}

	public String getmPassword() {
		return mPassword;
	}

	public void setmPassword(String mPassword) {
		this.mPassword = mPassword;
	}

	public Customer getmCustomer() {
		return mCustomer;
	}

	public void setmCustomer(Customer mCustomer) {
		this.mCustomer = mCustomer;
	}

	public Merchant getmMerchant() {
		return mMerchant;
	}

	public void setmMerchant(Merchant mMerchant) {
		this.mMerchant = mMerchant;
	}

	public Set<ThirdPartyAccount> getmThirdPartyAccountSet() {
		return mThirdPartyAccountSet;
	}

	public void setmThirdPartyAccountSet(Set<ThirdPartyAccount> mThirdPartyAccountSet) {
		this.mThirdPartyAccountSet = mThirdPartyAccountSet;
	}

	public Date getmCreateTimestamp() {
		return mCreateTimestamp;
	}

	public void setmCreateTimestamp(Date mCreateTimestamp) {
		this.mCreateTimestamp = mCreateTimestamp;
	}

	public String getmStatus() {
		return mStatus;
	}

	public void setmStatus(String mStatus) {
		this.mStatus = mStatus;
	}

	public Driver getmDriver() {
		return mDriver;
	}

	public void setmDriver(Driver mDriver) {
		this.mDriver = mDriver;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Set<DeviceToken> getDeviceTokenSet() {
		return deviceTokenSet;
	}

	public void setDeviceTokenSet(Set<DeviceToken> deviceTokenSet) {
		this.deviceTokenSet = deviceTokenSet;
	}

}
