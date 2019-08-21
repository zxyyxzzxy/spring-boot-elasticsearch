package com.zxy.service.impl;

import com.zxy.model.Customer;
import com.zxy.repository.CustomerRepository;
import com.zxy.service.CustomerService;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void deleteByUserName(String userName) {
        Customer customer = customerRepository.findByUserName(userName);
        customerRepository.delete(customer);
    }

    @Override
    public Iterable<Customer> regexpQuery(String name, String regexp) {
        QueryBuilder queryBuilder = QueryBuilders.regexpQuery(name, regexp);
        return customerRepository.search(queryBuilder);
    }

    @Override
    public Iterable<Customer> matchPhraseQuery(String name, String value) {
        QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery(name, value);
        return customerRepository.search(queryBuilder);
    }
}
