package com.cmq.demo.ddd.infrastruct;

import com.cmq.demo.ddd.domain.model.CustDto;

/**
 * Created by Administrator on 2020/1/14.
 */
public interface CustMapper {
  public void createCust(CustDto custDto);

  public CustDto getCust(Long id);
}
