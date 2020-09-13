package com.cmq.demo.freemarker;

import com.alibaba.fastjson.JSON;
import com.cmq.utils.DateUtils;
import lombok.Data;

import java.util.Date;

import static ch.qos.logback.classic.ClassicConstants.REQUEST_METHOD;
import static com.alibaba.fastjson.JSON.parseObject;

/**
 * Created by chen.ming.qian on 2020/8/17.
 */
@Data
public class HeadVo {
    private String tranId;
    private Date   reqTime;

    public static void main(String[] args) {
        String json = "{" + "        \"reqTime\": \"2020-02-20 10:10:10\","
            + "        \"tranId\": \"00001\"" + "    }";

        HeadVo headVo = JSON.parseObject(json, HeadVo.class);
        System.out.println(DateUtils.formatDate(headVo.getReqTime(),DateUtils.TIME_FORMAT));
    }
}
