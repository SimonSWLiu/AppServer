package com.onemenu.server.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * 设备token管理
 * @author file
 * @since 2015-7-3 下午5:20:50
 * @version 1.0
 */
@Entity
@Table(name = "device_token")
public class DeviceToken {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private long id;

	@Column(name = "device")
	private Integer device;
	
	@Column(name = "token")
	private String token;

	@Column(name = "login")
	private Integer login;

	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account mAccount;

	// Common field
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_timestamp")
	private Date mCreateTimestamp;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getDevice() {
		return device;
	}

	public void setDevice(Integer device) {
		this.device = device;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getLogin() {
		return login;
	}

	public void setLogin(Integer login) {
		this.login = login;
	}

	public Account getmAccount() {
		return mAccount;
	}

	public void setmAccount(Account mAccount) {
		this.mAccount = mAccount;
	}

	public Date getmCreateTimestamp() {
		return mCreateTimestamp;
	}

	public void setmCreateTimestamp(Date mCreateTimestamp) {
		this.mCreateTimestamp = mCreateTimestamp;
	}
	
	

}
