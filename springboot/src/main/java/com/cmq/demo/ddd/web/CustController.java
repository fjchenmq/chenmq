package com.cmq.demo.ddd.web;

import com.cmq.demo.ddd.domain.Cust;
import com.cmq.demo.ddd.application.CustAppService;
import com.cmq.demo.ddd.domain.model.CustDto;
import com.cmq.demo.ddd.CustVo;

/**
 * Created by Administrator on 2020/1/14.
 */
public class CustController {
  CustAppService custAppService;

  public void createCust() {
    CustVo custVo = new CustVo();
    CustDto custDto = custVo.toDto();

    custAppService.createCust(custDto);

    Cust cust = custAppService.getCust(1L);
    cust.getAddressEntity();

  }
}
