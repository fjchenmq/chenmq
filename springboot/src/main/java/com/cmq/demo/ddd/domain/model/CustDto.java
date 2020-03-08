package com.cmq.demo.ddd.domain.model;

import lombok.Data;

/**
 * Created by Administrator on 2020/1/14.
 */
@Data
public class CustDto {
  private String         custName;
  private Long           custId;
  private CustAddressDto custAddress;

}
