package com.cmq.mapper;

import com.base.mapper.common.BaseMapper;
import com.cmq.entity.Cust;
import com.cmq.entity.CustOrder;

import java.util.List;

/**
 * Created by Administrator on 2018/11/29.
 */
public  interface CustOrderMapper extends BaseMapper<CustOrder> {
    public CustOrder getOne(Long id);
    public CustOrder create(CustOrder custOrder);
    public List<CustOrder> pageQuery(CustOrder custOrder);
}
