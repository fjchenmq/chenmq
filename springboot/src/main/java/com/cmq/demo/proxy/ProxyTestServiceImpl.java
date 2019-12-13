package com.cmq.demo.proxy;

import com.cmq.entity.Cust;
import com.cmq.service.CustService;
import com.github.pagehelper.PageInfo;

/**
 * Created by Administrator on 2019/11/20.
 */
public class ProxyTestServiceImpl implements ProxyTestService {
    @Override
    public void printName(String name) {
        System.out.println(name);
    }

    @Override
    public void printAge(String age) {
        System.out.println(age);
    }

}
