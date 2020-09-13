package com.cmq.demo.freemarker;

import lombok.Data;

import java.util.List;

/**
 * Created by chen.ming.qian on 2020/8/17.
 */
@Data
public class RequestVo {
    private HeadVo        head;
    private List<OrderVo> orders;
}
