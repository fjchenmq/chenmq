package com.cmq.clound.impl;

import com.cmq.clound.SpringCloundService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2019/11/28.
 * spring  与spring clound混合使用
 */
@Service
@RestController
@RequestMapping(value = "/springCloundService")
public class SpringCloundServiceImpl implements SpringCloundService {
    //可以直接调用 http://my:9082/springCloundService/role?role=1
    @Override
    @RequestMapping(value = "/role")
    public String role(@RequestParam String role) {
        return "I'm  " + role;
    }

    @Override
    @RequestMapping(value = "/exception")
    public String exception(@RequestParam("exception") String exception) {
        throw  new NullPointerException();
        //return "I'm exception "  ;
    }
}
