package com.cmq.redisson;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by chen.ming.qian on 2021/1/15.
 */
@Data
public class OrderHandleInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String orderNbr;// 订单号
    private String handleClassPath;// 订单状态
    private Long   expTime;// 订单过期时间
    private Long   beginTime;//订单处理开始时间

}
