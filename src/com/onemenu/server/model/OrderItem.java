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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 
 * 订单项
 * 
 * @author Simon
 * 
 */
@Entity
@Table(name = "order_item")
public class OrderItem implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long mId;

    @Column(name = "dish_id")
    private String mDishId;

    @Column(name = "dish_name")
    private String mDishName;

    @Column(name = "dish_price")
    private String mDishPrice;

    @Column(name = "dish_image_url")
    private String mDishImageUrl;

    @Column(name = "dish_amount")
    private String mDishAmount;

    @Column(name = "price")
    private BigDecimal mPrice;

    @ManyToOne
    @JoinColumn(name = "order_form_id")
    private OrderForm mOrderForm;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "mOrderItem")
    private Set<OrderItemIngredient> mOrderItemIngredientSet;

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

    public String getmDishId() {
        return mDishId;
    }

    public void setmDishId(String mDishId) {
        this.mDishId = mDishId;
    }

    public String getmDishName() {
        return mDishName;
    }

    public void setmDishName(String mDishName) {
        this.mDishName = mDishName;
    }

    public String getmDishPrice() {
        return mDishPrice;
    }

    public void setmDishPrice(String mDishPrice) {
        this.mDishPrice = mDishPrice;
    }

    public String getmDishImageUrl() {
        return mDishImageUrl;
    }

    public void setmDishImageUrl(String mDishImageUrl) {
        this.mDishImageUrl = mDishImageUrl;
    }

    public String getmDishAmount() {
        return mDishAmount;
    }

    public void setmDishAmount(String mDishAmount) {
        this.mDishAmount = mDishAmount;
    }

    public BigDecimal getmPrice() {
        return mPrice;
    }

    public void setmPrice(BigDecimal mPrice) {
        this.mPrice = mPrice;
    }

    public OrderForm getmOrderForm() {
        return mOrderForm;
    }

    public void setmOrderForm(OrderForm mOrderForm) {
        this.mOrderForm = mOrderForm;
    }

    public Date getmCreateTimestamp() {
        return mCreateTimestamp;
    }

    public void setmCreateTimestamp(Date mCreateTimestamp) {
        this.mCreateTimestamp = mCreateTimestamp;
    }

    public Set<OrderItemIngredient> getmOrderItemIngredientSet() {
        return mOrderItemIngredientSet;
    }

    public void setmOrderItemIngredientSet(Set<OrderItemIngredient> mOrderItemIngredientSet) {
        this.mOrderItemIngredientSet = mOrderItemIngredientSet;
    }

}
