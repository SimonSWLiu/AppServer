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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * 菜式
 * 
 * @author Simon
 *
 */
@Entity(name = "dish_option_type")
public class DishOptionType implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "name")
    private String mName;
    
    @Column(name = "is_mandatory")
    private String mIsMandatory;
    
    @Column(name = "is_single")
    private String mIsSingle;
    
    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish mDish;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mDishOptionType")
    private Set<DishOptionItem> mDishOptionItemSet;

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

    public Dish getmDish() {
        return mDish;
    }

    public void setmDish(Dish mDish) {
        this.mDish = mDish;
    }

    public Date getmCreateTimestamp() {
        return mCreateTimestamp;
    }

    public void setmCreateTimestamp(Date mCreateTimestamp) {
        this.mCreateTimestamp = mCreateTimestamp;
    }

    public String getmIsMandatory() {
        return mIsMandatory;
    }

    public void setmIsMandatory(String mIsMandatory) {
        this.mIsMandatory = mIsMandatory;
    }

    public String getmIsSingle() {
        return mIsSingle;
    }

    public void setmIsSingle(String mIsSingle) {
        this.mIsSingle = mIsSingle;
    }

    public Set<DishOptionItem> getmDishOptionItemSet() {
        return mDishOptionItemSet;
    }

    public void setmDishOptionItemSet(Set<DishOptionItem> mDishOptionItemSet) {
        this.mDishOptionItemSet = mDishOptionItemSet;
    }

}




