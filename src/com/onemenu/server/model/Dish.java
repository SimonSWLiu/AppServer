package com.onemenu.server.model;

import java.math.BigDecimal;
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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.http.util.TextUtils;

import com.onemenu.server.util.StringUtil;

/**
 * 
 * 菜式
 * 
 * @author Simon
 *
 */
@Entity(name = "dish")
public class Dish implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "code")
    private String mCode;
    
    @Column(name = "name")
    private String mName;
    
    /**对name去除空格**/
    @Column(name = "top_tags_name")
    private String topTagsName;

    @Column(name = "other_name")
    private String mOtherName;

    @Column(name = "description")
    private String mDescription;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal mPrice;

    @Column(name = "image_name")
    private String mImageName;

    @Column(name = "discountability")
    private String mDiscountability;

    @Column(name = "status")
    private String mStatus;

    /*** json字符串，Dish的标签 ****/
    @Lob
    @Column(name = "tags")
    private String mTags;

    /** 用户自选配料，保存了json字符串 **/
    @Lob
    @Column(name = "customization")
    private String mCustomization;

    @ManyToOne
    @JoinColumn(name = "dish_category_id")
    private DishCategory mDishCategory;

    // 关系维护方
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_rank_id")
    private DishRank mDishRank;

    @ManyToMany(mappedBy = "mCouponTargetDishsSet", fetch = FetchType.LAZY)
    private Set<Coupon> mCouponTargetDishsSet;

    @ManyToMany(mappedBy = "mCouponExtraCriDishsSet", fetch = FetchType.LAZY)
    private Set<Coupon> mCouponExtraCriDishsSet;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mDish")
    private Set<DishOptionType> mDishOptionTypeSet;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mDish")
    private Set<DishIngredient> mDishIngredientSet;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mDish")
    private Set<DishImage> mDishImageSet;
    
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
        if(TextUtils.isEmpty(topTagsName))
        	setTopTagsName(StringUtil.removalSpace(mName));
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmDiscountability() {
        return mDiscountability;
    }

    public void setmDiscountability(String mDiscountability) {
        this.mDiscountability = mDiscountability;
    }

    public DishCategory getmDishCategory() {
        return mDishCategory;
    }

    public void setmDishCategory(DishCategory mDishCategory) {
        this.mDishCategory = mDishCategory;
    }

    public String getmTags() {
        return mTags;
    }

    public void setmTags(String mTags) {
        this.mTags = mTags;
    }

    public String getmCustomization() {
        return mCustomization;
    }

    public void setmCustomization(String mCustomization) {
        this.mCustomization = mCustomization;
    }

    public String getmImageName() {
        return mImageName;
    }

    public void setmImageName(String mImageName) {
        this.mImageName = mImageName;
    }

    public BigDecimal getmPrice() {
        return mPrice;
    }

    public void setmPrice(BigDecimal mPrice) {
        this.mPrice = mPrice;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public DishRank getmDishRank() {
        return mDishRank;
    }

    public void setmDishRank(DishRank mDishRank) {
        this.mDishRank = mDishRank;
    }

    public Set<Coupon> getmCouponTargetDishsSet() {
        return mCouponTargetDishsSet;
    }

    public void setmCouponTargetDishsSet(Set<Coupon> mCouponTargetDishsSet) {
        this.mCouponTargetDishsSet = mCouponTargetDishsSet;
    }

    public Set<Coupon> getmCouponExtraCriDishsSet() {
        return mCouponExtraCriDishsSet;
    }

    public void setmCouponExtraCriDishsSet(Set<Coupon> mCouponExtraCriDishsSet) {
        this.mCouponExtraCriDishsSet = mCouponExtraCriDishsSet;
    }
    
    public String getTopTagsName() {
		return topTagsName;
	}

	public void setTopTagsName(String topTagsName) {
		if(TextUtils.isEmpty(topTagsName))
			return;
		this.topTagsName = topTagsName.toLowerCase();
	}

    public String getmOtherName() {
        return mOtherName;
    }

    public void setmOtherName(String mOtherName) {
        this.mOtherName = mOtherName;
    }

    public Set<DishOptionType> getmDishOptionTypeSet() {
        return mDishOptionTypeSet;
    }

    public void setmDishOptionTypeSet(Set<DishOptionType> mDishOptionTypeSet) {
        this.mDishOptionTypeSet = mDishOptionTypeSet;
    }

    public Set<DishIngredient> getmDishIngredientSet() {
        return mDishIngredientSet;
    }

    public void setmDishIngredientSet(Set<DishIngredient> mDishIngredientSet) {
        this.mDishIngredientSet = mDishIngredientSet;
    }

}
