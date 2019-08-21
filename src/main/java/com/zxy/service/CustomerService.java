package com.zxy.service;

import com.zxy.model.Customer;

public interface CustomerService {

    public void  deleteByUserName(String userName);
    public Iterable<Customer> regexpQuery(String name, String regexp);
    public Iterable<Customer> matchPhraseQuery(String name, String value);
}
