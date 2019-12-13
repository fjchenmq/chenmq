package com.cmq.service;

import com.cmq.entity.Cust;
import com.cmq.entity.CustOrder;
import com.github.pagehelper.PageInfo;

/**
 * Created by Administrator on 2018/11/29.
 */
public interface CustOrderService {
    public CustOrder getOne(Long id);

    public CustOrder create(CustOrder custOrder);

    public PageInfo<CustOrder> pageQuery();
}
