package com.onemenu.server.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.onemenu.server.util.Equipment;
/**
 * 用户行为分析表
 * @author file
 * @since 2015年5月31日 上午9:50:27
 * @version 1.0
 * @copyright  by onemenu
 */
@Entity
@Table(name="user_analysis")
public class UserAnalysis implements PersistenceEntity {
	//主键
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
	private int id;
	@Column(name="deviceId",nullable=false)
	private String deviceId;
	@Column(name="appVersion")
	private String appVersion;
	@Column(name="viewName")
	private String viewName;
	@Column(name="inTime")
	private Timestamp inTime;
	@Column(name="outTime")
	private Timestamp outTime;
	@Column(name="keepTime")
	private int  keepTime;
	@Column(name="equipment")
	private byte equipment;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public Timestamp getInTime() {
		return inTime;
	}
	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}
	public Timestamp getOutTime() {
		return outTime;
	}
	public void setOutTime(Timestamp outTime) {
		this.outTime = outTime;
	}
	public int getKeepTime() {
		return keepTime;
	}
	public void setKeepTime(int keepTime) {
		this.keepTime = keepTime;
	}
	public byte getEquipment() {
		return equipment;
	}
	public void setEquipment(byte equipment) {
		this.equipment = equipment;
	}
	
	public void setEquipment(Equipment equipment) {
		this.equipment = equipment.getCode();
	}
	@Override
	public String toString() {
		return "UserAnalysis [id=" + id + ", deviceId=" + deviceId + ", appVersion=" + appVersion + ", viewName=" + viewName + ", inTime=" + inTime
				+ ", outTime=" + outTime + ", keepTime=" + keepTime + ", equipment=" + equipment + "]";
	}
	
	
	
	

}
