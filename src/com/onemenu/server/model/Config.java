package com.onemenu.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 配置表
 * @author file
 * @since 2015-4-13 下午2:48:22
 * @version 1.0
 */
@Entity
@Table(name="config")
public class Config implements PersistenceEntity {
	
	public Config() {
	}
	
	public Config(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@Id
	@Column(name="`key`",nullable=false)
	private String key;
	@Column(name="`value`",nullable=false)
	private String value = "";	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Config [key=" + key + ", value=" + value + "]";
	}
		
}
