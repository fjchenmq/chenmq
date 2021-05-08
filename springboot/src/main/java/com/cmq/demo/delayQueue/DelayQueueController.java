package com.cmq.demo.delayQueue;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Random;

import static com.cmq.demo.delayQueue.OrderPay.getTime;

/**
 * Created by chenmq on 2018/8/10.
 * http://my:9082/delayQueue/test
 */
@Controller
@RequestMapping("/delayQueue")
public class DelayQueueController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() throws Exception {
        String str = "Success";
        long createTime = System.currentTimeMillis();
        String currentTime = getTime(createTime);
        Long overTime = createTime + 10 * 1000;// 1000*10 十秒后超时
        for (int i = 0; i < 10000; i++) {
            String orderNo = String.valueOf(new Random().nextLong());
            OrderHandleInfo order = new OrderHandleInfo();
            order.setOrderNbr(orderNo);
            order.setExpTime(overTime);
            order.setHandleClassPath("com.cmq.demo.delayQueue.OrderOverTimeClose.cancelOrder");
            order.setBeginTime(new Date(createTime));
            OrderHandleQueueConsumer.getInstance().putHandleQueue(order);
        }
        return str;
    }

}
