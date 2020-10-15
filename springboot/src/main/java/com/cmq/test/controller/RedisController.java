package com.cmq.test.controller;

import com.base.bean.GridQo;
import com.base.properties.PropertiesUtils;
import com.cmq.base.BaseController;
import com.cmq.bean.Book;
import com.cmq.bean.LoginInfo;
import com.cmq.bean.Person;
import com.cmq.demo.json2tree.Json2TreeUtil;
import com.cmq.demo.json2tree.NodeVo;
import com.cmq.demo.schema.abs.Audi;
import com.cmq.demo.schema.abs.Car;
import com.cmq.entity.Cust;
import com.cmq.mapper.QhccMapper;
import com.cmq.service.CustService;
import com.cmq.service.EmailService;
import com.cmq.service.SpiTestService;
import com.cmq.service.TestService;
import com.cmq.stock.EasyMoneyService;
import com.cmq.utils.HttpClientUtil;
import com.cmq.utils.SpringUtils;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import static com.cmq.utils.SpringUtils.getBean;

/**
 * Created by chenmq on 2018/8/10.
 * http://my:9082/springboot/stock.html
 * http://my:9082/redis/get
 */

/***
 * @RestController注解，相当于@Controller+@ResponseBody两个注解的结合，
 * 返回json数据不需要在方法前面加@ResponseBody注解了，
 * 但使用@RestController这个注解，就不能返回jsp,html页面，视图解析器无法解析jsp,html页面
 */

@RestController
@RequestMapping("/redis")
public class RedisController extends BaseController<Cust, Long> {
    private static Logger logger = LoggerFactory.getLogger(RedisController.class);
    @Autowired
    PropertiesUtils propertiesUtils;
    //redis 默认操作类
    @Resource
    private RedisTemplate<String, Cust> redisTemplate;

    @RequestMapping(value = "/put", method = RequestMethod.GET)
    @ResponseBody
    public String put() throws Exception {
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
        //rightPush 将数据添加到key对应的现有数据的左边，也就是头部，rightPush 是将现有数据添加到现有数据的右边，也就是尾部
        redisTemplate.opsForList().rightPush("custList", cust);
        redisTemplate.opsForList().rightPush("custList", cust2);
        redisTemplate.opsForList().rightPush("custList", cust3);
        return "success";
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Object get() throws Exception {
        List<Cust> custList = redisTemplate.opsForList().range("custList", 0, -1);//所有数据
        //redisTemplate.opsForList().size("custList") > 0
      /*  while (true) {
            Cust cust = redisTemplate.opsForList().rightPop("custList");
            if(cust==null){
                break;
            }
        }*/
        while (true) {
            List<Cust> tempList = redisTemplate.opsForList().range("custList", 0, 0);
            if (tempList == null || tempList.isEmpty()) {
                break;
            }
            System.out.println(tempList.get(0).getCustId());
            if (true) {
                //操作成功 删除元素 从头部移除
                Cust cust = redisTemplate.opsForList().leftPop("custList");
                System.out.println(cust.getCustId());

            }
        }
        return custList;
    }

}

//@Controller
