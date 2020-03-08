package com.cmq.demo.ddd.domain.service;

import com.cmq.demo.ddd.domain.model.CustDto;
import com.cmq.demo.ddd.infrastruct.CustMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2020/1/14.
 */
public class CustService {
  @Autowired
  private CustMapper custMapper;

  public void createCust(CustDto custDto) {
    custMapper.createCust(custDto);
  }

  public CustDto getCust(Long id) {
    return custMapper.getCust(id);
  }

}
