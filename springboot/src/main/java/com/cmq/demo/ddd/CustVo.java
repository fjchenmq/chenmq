package com.cmq.demo.ddd;

import com.cmq.demo.ddd.domain.model.CustDto;

/**
 * Created by Administrator on 2020/1/14.
 */
public class CustVo {
  public static CustDto toDto() {
    return new CustDto();
  }
}
