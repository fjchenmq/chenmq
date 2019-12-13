package com.cmq.mapper;

import com.base.mapper.common.BaseMapper;
import com.cmq.entity.Cust;

import java.util.List;

/**
 * Created by Administrator on 2018/11/29.
 */
public  interface CustMapper extends BaseMapper<Cust> {
    public Cust getOne(Long id);
    public void create(Cust cust);
    public List<Cust> pageQuery(Cust cust);
    public List<Cust> queryCustOrderList(Long id);
}
