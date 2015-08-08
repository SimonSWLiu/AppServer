package com.onemenu.server.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;

import com.onemenu.server.dao.CustomerDAO;
import com.onemenu.server.service.CustomerService;


public class CustomerServiceImpl extends AbstractServiceImpl implements CustomerService {

    CustomerDAO mCustomerDAO;

    @Autowired
    public void setmCustomerDAO(CustomerDAO mCustomerDAO) {
        this.mCustomerDAO = mCustomerDAO;
    }
    


    

}
