package com.zxy.service;

import com.zxy.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {

    public void  deleteByUserName(String userName);
}
