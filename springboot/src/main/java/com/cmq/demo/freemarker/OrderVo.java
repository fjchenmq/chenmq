package com.cmq.demo.freemarker;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen.ming.qian on 2020/8/17.
 */
@Data
public class OrderVo {
    private String orderId;
    private String buyer;
    private List<OrderAttrVo> orderAttrList =new ArrayList<>();
}
