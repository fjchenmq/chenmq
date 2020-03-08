package com.cmq.demo.ddd.domain;

import com.cmq.demo.ddd.domain.model.CustAddressDto;
import com.cmq.demo.ddd.domain.model.CustDto;
import com.cmq.demo.ddd.domain.service.CustAddressService;
import com.cmq.demo.ddd.domain.service.CustService;
import lombok.Getter;

/**
 * Created by Administrator on 2020/1/14.
 */
public class Cust {
    CustService        custService;
    CustAddressService custAddressService;
    @Getter
    CustDto custDto;

    public Cust createCust(CustDto custDto) {
        custService.createCust(custDto);
        this.custDto = custDto;
        custAddressService.createCustAddress(custDto.getCustAddress());
        return this;
    }

    public Cust getCust(Long id) {
        custDto = custService.getCust(id);
        return this;
    }

    public CustAddressDto getAddressEntity() {
        return custAddressService.getCustAddress(custDto.getCustId());
    }

    public static Cust newInstance() {
        Cust instance = new Cust();
        return instance;
    }
}
