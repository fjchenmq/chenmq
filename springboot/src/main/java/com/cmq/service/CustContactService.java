package com.cmq.service;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by chenmq on 2018/8/10.
 */
public interface CustContactService {
    public  String getName();
    public  String getAge();
}
