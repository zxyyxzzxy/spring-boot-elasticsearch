package com.zxy.service.impl;

import com.zxy.model.Customer;
import com.zxy.repository.CustomerRepository;
import com.zxy.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void deleteByUserName(String userName) {
        Customer customer = customerRepository.findByUserName(userName);
        customerRepository.delete(customer);
    }

}
