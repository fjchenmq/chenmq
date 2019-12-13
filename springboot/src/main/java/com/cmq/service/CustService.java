package com.cmq.service;

import com.base.service.BaseService;
import com.cmq.entity.Cust;
import com.github.pagehelper.PageInfo;

/**
 * Created by Administrator on 2018/11/29.
 */
public interface CustService  extends BaseService<Cust, Long> {
    public Cust getOne(Long id);

    public void create(Cust cust);

    public PageInfo<Cust> pageQuery();

    public PageInfo<Cust> queryCustOrderList(Long id);
}
