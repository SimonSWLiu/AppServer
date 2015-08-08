package com.onemenu.server.daoImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.dao.TradeDAO;
import com.onemenu.server.model.Customer;
import com.onemenu.server.model.OrderForm;
import com.onemenu.server.model.OrderItem;
import com.onemenu.server.model.Trade;


public class TradeDAOImpl extends BaseDAOSupport implements TradeDAO {

    protected HibernateTemplate mHibernateTemplate;

    @Autowired
    public void setmHibernateTemplate(HibernateTemplate mHibernateTemplate) {
        this.mHibernateTemplate = mHibernateTemplate;
    }

    @Override
    @Transactional
    public List<OrderItem> getOrderItemListByCustomerId(long customerId) {

        List<OrderItem> orderItemList = new ArrayList<OrderItem>();

        Customer customer = mHibernateTemplate.get(Customer.class, customerId);

        for (Trade trade : customer.getmTradesSet()) {

            if (trade.getmCreateTimestamp() != null) {

                for (OrderForm orderForm : trade.getmOrderFormSet()) {

                    if (orderForm.getmCreateTimestamp() != null) {

                        for (OrderItem orderItem : orderForm.getmOrderItemSet()) {

                            if (orderItem.getmCreateTimestamp() != null) {
                                orderItemList.add(orderItem);
                            }
                        }

                    }


                }
            }
        }

        return orderItemList;
    }

    @Override
    public Long findTodayTradeRowCount() {

        DetachedCriteria cri = DetachedCriteria.forClass(Trade.class);
        cri.setProjection(Projections.rowCount());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date begin = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date end = cal.getTime();
        cri.add(Restrictions.between("mCreateTimestamp", begin, end));

        List results = mHibernateTemplate.findByCriteria(cri);

        // Integer count = Integer.valueOf((String)results.get(0));
        Long count = (Long) results.get(0);
        return count;

    }
    
    @Override
    public Long findTodayOrderFormRowCount() {

        DetachedCriteria cri = DetachedCriteria.forClass(OrderForm.class);
        cri.setProjection(Projections.rowCount());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date begin = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date end = cal.getTime();
        cri.add(Restrictions.between("mCreateTimestamp", begin, end));

        List results = mHibernateTemplate.findByCriteria(cri);

        // Integer count = Integer.valueOf((String)results.get(0));
        Long count = (Long) results.get(0);
        return count;

    }

}
