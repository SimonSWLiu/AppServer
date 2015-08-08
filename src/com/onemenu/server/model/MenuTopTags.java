package com.onemenu.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 菜单热门标签
 * @author file
 * @since 2015-4-21 下午3:53:17
 * @version 1.0
 */
@Entity
@Table(name="menu_top_tags")
public class MenuTopTags implements PersistenceEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private long id;
	@Column(name="tag",nullable=false)
	private String tag;
	@Column(name="`count`")
	private long count;
	/** 纬度 **/
    @Column(name = "latitude")
	private double latitude;
    /** 经度 **/
    @Column(name = "longitude")
	private double longitude;
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	@Override
	public String toString() {
		return "MenuTopTags [id=" + id + ", tag=" + tag + ", count=" + count + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}    
}
