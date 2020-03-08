package com.cmq.demo.ddd.domain.service;

import com.cmq.demo.ddd.domain.model.CustAddressDto;
import com.cmq.demo.ddd.infrastruct.CustAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2020/1/14.
 */
public class CustAddressService {
  @Autowired
  private CustAddressMapper custAddressMapper;

  public void createCustAddress(CustAddressDto custAddressDto) {
    custAddressMapper.createCustAddress(custAddressDto);
  }

  public CustAddressDto getCustAddress(Long custId) {
    return custAddressMapper.getCustAddress(custId);
  }


}
