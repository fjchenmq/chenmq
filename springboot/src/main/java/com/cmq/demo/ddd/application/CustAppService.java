package com.cmq.demo.ddd.application;

import com.cmq.demo.ddd.domain.Cust;
import com.cmq.demo.ddd.domain.model.CustDto;

/**
 * Created by Administrator on 2020/1/15.
 */
public class CustAppService {

    public Cust createCust(CustDto custDto) {
        return Cust.newInstance().createCust(custDto);
    }

    public Cust getCust(Long id) {
        return Cust.newInstance().getCust(id);
    }

}
