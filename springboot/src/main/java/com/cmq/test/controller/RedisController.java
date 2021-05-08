package com.cmq.test.controller;

import com.base.properties.PropertiesUtils;
import com.cmq.base.BaseController;
import com.cmq.entity.Cust;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenmq on 2018/8/10.
 * http://my:9082/springboot/stock.html
 * http://my:9082/redis/get
 */

/***
 * @RestController注解，相当于@Controller+@ResponseBody两个注解的结合，
 * 返回json数据不需要在方法前面加@ResponseBody注解了，
 * 但使用@RestController这个注解，就不能返回jsp,html页面，视图解析器无法解析jsp,html页面
 * http://my:9082/redis/get
 * http://my:9082/redis/putHash
 */

@RestController
@RequestMapping("/redis")
public class RedisController extends BaseController<Cust, Long> {
    private static Logger logger = LoggerFactory.getLogger(RedisController.class);
    @Autowired
    PropertiesUtils propertiesUtils;
    //redis 默认操作类
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(value = "/put", method = RequestMethod.GET)
    @ResponseBody
    public String put() throws Exception {
      /*  for (int i = 0; i < 10000; i++) {
             redisTemplate.opsForValue().set("cancelOrder:" + i,i, 1000, TimeUnit.MILLISECONDS);
            redisTemplate.opsForValue().set("alarmOrder:" + i,i, 2000, TimeUnit.MILLISECONDS);

            //redisTemplate.opsForValue().set("cancelOrder:" + i, i, 1, TimeUnit.SECONDS);
            //redisTemplate.opsForValue().set("alarmOrder:" + i, i, 2, TimeUnit.SECONDS);
        }*/

        redisTemplate.opsForValue().set("cancelOrder:" + "0001", "111");
        redisTemplate.expire("cancelOrder:" + "0001", 2000, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set("alarmOrder:001", 1, 2000, TimeUnit.MILLISECONDS);

        Object val = redisTemplate.opsForValue().get("myKey");
        Cust cust = new Cust();
        cust.setCustName("李1");
        cust.setCustId(1L);
        cust.setCustType("1100");
        //写入redis
        redisTemplate.opsForValue().set("cust", cust);
        List<Cust> custList = new ArrayList();
        Cust cust2 = new Cust();
        cust2.setCustName("李2");
        cust2.setCustId(2L);
        cust2.setCustType("1100");
        //redisTemplate.boundHashOps("salesOrderCreatedList").delete("cust","cust2","cust3");
        Cust cust3 = new Cust();
        cust3.setCustName("李3");
        cust3.setCustId(3L);
        cust3.setCustType("1100");
        //rightPush 将数据添加到key对应的现有数据的左边，也就是头部，rightPush 是将现有数据添加到现有数据的右边，也就是尾部
        BoundListOperations custListOpration = redisTemplate.boundListOps("salesOrderCreatedList");
        custListOpration.rightPush(cust);
        custListOpration.rightPush(cust2);
        custListOpration.rightPush(cust3);

      /*  redisTemplate.opsForList().rightPush("custList", cust);
        redisTemplate.opsForList().rightPush("custList", cust2);
        redisTemplate.opsForList().rightPush("custList", cust3);*/
        return "success";
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Object get() throws Exception {
        BoundListOperations custListOpration = redisTemplate.boundListOps("salesOrderCreatedList");
        //List<Cust> custList = redisTemplate.opsForList().range("custList", 0, -1);//所有数据
        List<Cust> custList = custListOpration.range(0, -1);//所有数据
        //redisTemplate.opsForList().size("custList") > 0
      /*  while (true) {
            Cust cust = redisTemplate.opsForList().rightPop("custList");
            if(cust==null){
                break;
            }
        }*/
        while (true) {
            // List<Cust> tempList = redisTemplate.opsForList().range("custList", 0, 0);
            List<Cust> tempList = custListOpration.range(0, 0);
            if (tempList == null || tempList.isEmpty()) {
                break;
            }
            System.out.println(tempList.get(0).getCustId());
            if (true) {
                //操作成功 删除元素 从头部移除
                //Cust cust = redisTemplate.opsForList().leftPop("custList");
                Cust cust = (Cust) custListOpration.leftPop();
                System.out.println(cust.getCustId());

            }
        }
        return custList;
    }

    @RequestMapping(value = "/putHash", method = RequestMethod.GET)
    @ResponseBody
    public String putHash() throws Exception {
        Cust cust = new Cust();
        cust.setCustName("李1");
        cust.setCustId(1L);
        cust.setCustType("1100");
        //写入redis
        redisTemplate.opsForValue().set("cust", cust);
        List<Cust> custList = new ArrayList();
        Cust cust2 = new Cust();
        cust2.setCustName("李2");
        cust2.setCustId(2L);
        cust2.setCustType("1100");

        Cust cust3 = new Cust();
        cust3.setCustName("李3");
        cust3.setCustId(3L);
        cust3.setCustType("1100");
        BoundHashOperations custMapOpration = redisTemplate.boundHashOps("salesOrderCreatedHash");
        custMapOpration.put("cust", cust);
        custMapOpration.put("cust2", cust2);
        custMapOpration.put("cust3", cust3);
        return "success";
    }

    @RequestMapping(value = "/getHash", method = RequestMethod.GET)
    @ResponseBody
    public Object getHash() throws Exception {
        BoundHashOperations custMapOpration = redisTemplate.boundHashOps("salesOrderCreatedHash");
        System.out.println(custMapOpration.get("cust"));
        System.out.println(custMapOpration.get("cust2"));
        System.out.println(custMapOpration.get("cust3"));
        return custMapOpration.entries();
    }

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    @ResponseBody
    public Object top() throws Exception {
        Object obj = null;
        redisTemplate.opsForZSet().add("myTop", "a", 1);
        redisTemplate.opsForZSet().add("myTop", "b", 2);
        redisTemplate.opsForZSet().add("myTop", "c", 2);
        redisTemplate.opsForZSet().add("myTop", "d", 3);
        redisTemplate.opsForZSet().add("myTop", "e", 4);
        redisTemplate.opsForZSet().add("myTop", "f", 52);
        redisTemplate.opsForZSet().add("myTop", "g", 60);
        redisTemplate.opsForZSet().rangeByScore("myTop", 50, 400);//方法根据设置的score获取区间值
        redisTemplate.opsForZSet().rangeWithScores("myTop", 0, 3);//   索引start<=index<=end的元素子集， 正序
        redisTemplate.opsForZSet()
            .reverseRangeWithScores("myTop", 0, 3);//   索引start<=index<=end的元素子集， 倒序
        obj = redisTemplate.opsForZSet().count("myTop", 1, 2);//Smin<=score<=Smax的元素个数
        redisTemplate.opsForZSet().score("myTop", "b");//键为K的集合，value为obj的元素分数


        return obj;
    }

    @RequestMapping(value = "/set", method = RequestMethod.GET)
    @ResponseBody
    public Object set() throws Exception {
        Object obj = new Object();
        obj = redisTemplate.opsForSet().add("myset", "aa");
        redisTemplate.opsForSet().add("myset", "ab");
        redisTemplate.opsForSet().add("myset", "cc");
        redisTemplate.opsForSet().add("myset", "dc");
        redisTemplate.opsForSet().members("myset").forEach((v) -> {
            // System.out.println(v);
        });
        /**
         * 127.0.0.1:6379> ZSCAN myTop 0 match "*a*"
         1) "0"
         2) 1) "\"a\""
            2) "1"
         */
        ScanOptions options = ScanOptions.scanOptions().match("*a*").count(200).build();//正则 无效？
        Cursor<Object> cursor = redisTemplate.opsForSet().scan("myset", options);
        while (cursor.hasNext()) {
            System.out.println("scan result:" + cursor.next());
        }
        try {
            cursor.close();
        } catch (IOException e) {
            // do something meaningful
        }
        return obj;
    }
}

//@Controller
