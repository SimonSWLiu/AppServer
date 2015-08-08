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
 * 菜式分类
 * 
 * @author Simon
 *
 */
@Entity(name = "dish_category")
public class DishCategory implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "name")
    private String mName;

    @Column(name = "sequence")
    private Integer mSequence;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "mDishCategory")
    private Set<Dish> mDishSet;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant mRestaurant;

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

    public Set<Dish> getmDishSet() {
        return mDishSet;
    }

    public void setmDishSet(Set<Dish> mDishSet) {
        this.mDishSet = mDishSet;
    }

    public Restaurant getmRestaurant() {
        return mRestaurant;
    }

    public void setmRestaurant(Restaurant mRestaurant) {
        this.mRestaurant = mRestaurant;
    }

    public Integer getmSequence() {
        return mSequence;
    }

    public void setmSequence(Integer mSequence) {
        this.mSequence = mSequence;
    }

}
