package com.cmq.test.controller;

import com.base.bean.GridQo;
import com.base.controller.BaseController;
import com.base.properties.PropertiesUtils;
import com.cmq.bean.Book;
import com.cmq.bean.LoginInfo;
import com.cmq.bean.Person;
import com.cmq.entity.Cust;
import com.cmq.mapper.QhccMapper;
import com.cmq.service.CustService;
import com.cmq.service.EmailService;
import com.cmq.service.SpiTestService;
import com.cmq.service.TestService;
import com.cmq.stock.EasyMoneyService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import static com.cmq.utils.SpringUtils.getBean;

/**
 * Created by chenmq on 2018/8/10.
 * http://my:9082/springboot/stock.html
 * http://my:9082/springboot/get
 */

/***
 * @RestController注解，相当于@Controller+@ResponseBody两个注解的结合，
 * 返回json数据不需要在方法前面加@ResponseBody注解了，
 * 但使用@RestController这个注解，就不能返回jsp,html页面，视图解析器无法解析jsp,html页面
 */

@RestController
@RequestMapping("/test")
public class TestController extends BaseController<Cust, Long> {
    private static Logger logger = LoggerFactory.getLogger(TestController.class);
    @Autowired
    TestService      testService;
    @Autowired
    EasyMoneyService easyMoneyService;
    @Autowired
    CustService      custService;
    @Autowired
    QhccMapper       qhccMapper;
    @Autowired
    PropertiesUtils  propertiesUtils;
    @Autowired
    EmailService     emailService;
    //redis 默认操作类
    @Resource
    private RedisTemplate<String, Cust> redisTemplate;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    @ResponseBody
    public List home(@RequestParam("date") String date) throws Exception {
        System.out.println(date);
        easyMoneyService.syncQHCC(true);
        return easyMoneyService.sortNetAmount();
    }

    @RequestMapping(value = "/lucky", method = {RequestMethod.POST})
    @ResponseBody
    @Validated
    public Person lucky(HttpServletRequest request, HttpServletResponse response,
        @RequestBody @Validated Person person) throws Exception {
        testService.getPerson(null);

        return person;
    }

    @RequestMapping(value = "/data", method = {RequestMethod.POST})
    @ResponseBody
    public PageInfo<Cust> data(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        PageInfo<Cust> pageInfo = custService.pageQuery();
        return pageInfo;
    }

    @RequestMapping(value = "/goBefore", method = {RequestMethod.POST})
    @ResponseBody
    public void go(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("go before...................");
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    @ResponseBody
    public Cust me(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("log...................");
     /*   Map<String, String> map = new HashMap<>();
        map.put("type", "if1902");
        map.put("createDate", "2019-02-14");
        Qhcc qhcc = qhccMapper.getOne(map);*/
        Cust cust = new Cust();
        cust.setCustName("李文");
        cust.setCustId(116L);
        cust.setCustType("1100");
        custService.create(cust);
        custService.insert(cust);
        Cust cust1 = null;
        cust1 = redisTemplate.opsForValue().get("cust");
        return cust1;
    }

    @GetMapping(value = "get")
    @ResponseBody
    public String get() {
        ServiceLoader<SpiTestService> loaders = ServiceLoader.load(SpiTestService.class);
        for (SpiTestService spi : loaders) {
            spi.helloSpi(" miss ");
        }

        Book book = getBean(Book.class);
        JdbcTemplate jdbcTemplate = SpringUtils.getBean("jdbcTemplate");
        System.out.println(book.getName() + ":" + book.getId());
        System.out.println(propertiesUtils.getCustContact().getContactName());
        Cust cust = custService.getOne(11L);

        PageInfo<Cust> pageInfo = custService.pageQuery();

        //测试base service
        Cust cc = custService.selectByPrimaryKey(16L);
        Cust custQuery = new Cust();
        custQuery.setCustName("11111的订单");
        List<Cust> custList = custService.selectPageList(custQuery, 1, 10);
        GridQo query = new GridQo();
        query.setPageNum(1);
        query.setPageSize(20);
        PageInfo<Cust> custPageInfo = custService.selectGridData(query);
        //end

        //写入redis
        redisTemplate.opsForValue().set("cust", cust);

        emailService.send();
        return cust.getCustName();
    }

    public void add() {
        System.out.println("add method");
    }

    @RequestMapping(value = "login", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String login(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setStaffName("张三");
        loginInfo.setStaffCode("zs");
        request.getSession().setAttribute("loginSession", loginInfo);
        String url;
        //返回登陆前的URL
        url = (String) request.getSession().getAttribute("beforeUrl");
        if (url == null) {
            url = "/main";
        }
        return url;
    }

    @RequestMapping(value = "sessions", method = RequestMethod.GET)
    @ResponseBody
    public Object sessions(HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();
        map.put("get loginSession", request.getSession().getAttribute("loginSession"));
        return map;
    }

}

//@Controller
