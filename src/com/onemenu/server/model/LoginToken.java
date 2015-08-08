package com.onemenu.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "login_token")
public class LoginToken implements PersistenceEntity{
	//token id
	@Id
	@Column(name = "token_id", nullable = false)
	private String  tokenId;
	//token 内容
	@Column(name="`content`")
	private String  content;
	
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "LoginToken [tokenId=" + tokenId + ", content=" + content + "]";
	}
	
}
