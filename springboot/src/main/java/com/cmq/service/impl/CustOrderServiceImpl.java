package com.cmq.service.impl;

import com.cmq.entity.CustOrder;
import com.cmq.mapper.CustOrderMapper;
import com.cmq.service.CustOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Service;

/**
 * Created by Administratoron 2018/11/29.
 */
@Service
public class CustOrderServiceImpl implements CustOrderService {
    @Autowired
    private CustOrderMapper custOrderMapper;

    public CustOrder getOne(Long id) {
        return custOrderMapper.getOne(id);
    }

    public CustOrder create(CustOrder custOrder) {
        return custOrderMapper.create(custOrder);
    }

    public PageInfo<CustOrder> pageQuery() {

        Page<CustOrder> page = PageHelper.startPage(1, 3, true);
        custOrderMapper.pageQuery(null);

        return page.toPageInfo();
    }
}
