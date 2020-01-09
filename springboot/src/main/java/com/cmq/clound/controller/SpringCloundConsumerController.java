package com.cmq.clound.controller;

import com.cmq.clound.SpringCloundService;
import com.cmq.clound.feignClient.SpringCloundFeignClient;
import com.cmq.entity.Cust;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Administrator on 2019/11/28.
 */
@RestController
@RequestMapping("/consumer")

public class SpringCloundConsumerController {
    //SpringCloundFeignClient
    @Autowired
    RestTemplate            restTemplate;
    @Autowired
    SpringCloundFeignClient springCloundFeignClient;

    @Autowired
    SpringCloundService springCloundService;

    @GetMapping(value = "get")
    public String get() {
        String str = "I'm consumer";
        System.out.println(str);

        Cust cust = new Cust();
        //RestTemplate方式调用
        str = restTemplate
            .getForEntity("http://my-spring-clound/provider/get?model=RestTemplate",
                String.class).getBody();

        cust.setCustName("restTemplate");
        ResponseEntity<Cust> responseEntity = restTemplate
            .postForEntity("http://my-spring-clound/provider/postCust", cust, Cust.class);
        System.out.println(responseEntity.getBody().getCustName());

        //SpringCloundFeignClient  方式调用

        String aa = springCloundFeignClient.get("SpringCloundFeignClient");
        System.out.println(aa);

        cust.setCustName("springCloundFeignClient");
        cust = springCloundFeignClient.postCust(cust);
        System.out.println(cust.getCustName());
        System.out.println(springCloundFeignClient.role("SpringCloundFeignClient"));

        //异常 fallback
       // System.out.println(springCloundFeignClient.exception("exception"));

        //spring方式调用
        System.out.println(springCloundService.role("spring"));




        return str;
    }
}
