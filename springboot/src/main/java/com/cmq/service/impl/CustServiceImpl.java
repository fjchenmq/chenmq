package com.cmq.service.impl;

import com.base.sequence.DbUtil;
import com.base.service.impl.BaseServiceImpl;
import com.cmq.entity.Cust;
import com.cmq.mapper.CustMapper;
import com.cmq.service.CustService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administratoron 2018/11/29.
 */
@Service
@Transactional
public class CustServiceImpl  extends BaseServiceImpl<Cust, Long> implements CustService {
    @Autowired
    private CustMapper custMapper;

    public Cust getOne(Long id) {
        return custMapper.getOne(id);
    }

    public void create(Cust Cust) {
        custMapper.create(Cust);
        String nullStr = null;
//        nullStr.indexOf("1");
    }

    public PageInfo<Cust> pageQuery() {
        System.out.println("get a sequence : "+ DbUtil.getSequenceNumber("SEQ_MAINORD_PROD_PROD_ID"));
        Page<Cust> page = PageHelper.startPage(1, 3, true);
        custMapper.pageQuery(null);

        return page.toPageInfo();
    }

    public PageInfo<Cust> queryCustOrderList(Long id) {

        Page<Cust> page = PageHelper.startPage(1, 3, true);
        custMapper.queryCustOrderList(id);

        return page.toPageInfo();
    }
}
