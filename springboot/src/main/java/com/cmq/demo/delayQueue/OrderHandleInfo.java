package com.cmq.demo.delayQueue;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import static org.apache.coyote.http11.Constants.a;

/**
 * Created by chen.ming.qian on 2021/1/15.
 */
public class OrderHandleInfo implements Serializable, Delayed {
    private static final long serialVersionUID = 1L;
    private String orderNbr;// 订单号
    private String handleClassPath;// 订单状态
    private Long   expTime;// 订单过期时间
    private Date   beginTime;//订单处理开始时间
    /**
     * 用于延时队列内部比较排序：当前订单的过期时间 与 队列中对象的过期时间 比较
     */
    @Override
    public int compareTo(Delayed o) {
        Long nowThreadtime = 0L;
        Long queueThreadtime = 0L;
        nowThreadtime = System.currentTimeMillis();
        queueThreadtime = expTime;
        return nowThreadtime.compareTo(queueThreadtime);
    }

    /**
     * 时间单位：秒
     * 延迟关闭时间 = 过期时间 - 当前时间
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return this.expTime - System.currentTimeMillis();
    }

    public String getOrderNbr() {
        return orderNbr;
    }

    public void setOrderNbr(String orderNbr) {
        this.orderNbr = orderNbr;
    }

    public Long getExpTime() {
        return expTime;
    }

    public void setExpTime(Long overTime) {
        this.expTime = overTime;
    }

    public String getHandleClassPath() {
        return handleClassPath;
    }

    public void setHandleClassPath(String handleClassPath) {
        this.handleClassPath = handleClassPath;
    }
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
    @Override

    public boolean equals(Object o){
        if(o instanceof OrderHandleInfo){
            OrderHandleInfo c = (OrderHandleInfo)o;

            return  this.getOrderNbr().equals(c.getOrderNbr());

        }

        return false;

    }


}
