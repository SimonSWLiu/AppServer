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
 * 
 * 商家和客户、第三方登录的账户信息
 * 
 * @author Simon
 *
 */
@Entity
@Table(name = "third_party_account")
public class ThirdPartyAccount implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "third_party_id")
    private String mThirdPartyId;

    @Column(name = "login_type")
    private String mLoginType;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account mAccount;

    // Common field
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_timestamp")
    private Date mCreateTimestamp;

    public long getmId() {
        return mId;
    }

    public Date getmCreateTimestamp() {
        return mCreateTimestamp;
    }

    public void setmCreateTimestamp(Date mCreateTimestamp) {
        this.mCreateTimestamp = mCreateTimestamp;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmThirdPartyId() {
        return mThirdPartyId;
    }

    public void setmThirdPartyId(String mThirdPartyId) {
        this.mThirdPartyId = mThirdPartyId;
    }

    public String getmLoginType() {
        return mLoginType;
    }

    public void setmLoginType(String mLoginType) {
        this.mLoginType = mLoginType;
    }

    public Account getmAccount() {
        return mAccount;
    }

    public void setmAccount(Account mAccount) {
        this.mAccount = mAccount;
    }

}
