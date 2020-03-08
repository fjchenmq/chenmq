package com.cmq.demo.ddd.infrastruct;

import com.cmq.demo.ddd.domain.model.CustAddressDto;

/**
 * Created by Administrator on 2020/1/14.
 */
public interface CustAddressMapper {
  public void createCustAddress(CustAddressDto custAddressDto);

  public CustAddressDto getCustAddress(Long custId);
}
