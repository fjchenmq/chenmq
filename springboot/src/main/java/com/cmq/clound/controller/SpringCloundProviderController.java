package com.cmq.clound.controller;

import com.cmq.entity.Cust;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2019/11/28.
 */
@RestController
@RequestMapping("/provider")
public class SpringCloundProviderController {
    @GetMapping(value = "get")
    public String get(@RequestParam("model") String model){
        String str ="You get me by "+model;
        System.out.println("I'm provider");
        return str;
    }

    @PostMapping(value = "postCust")
    public Cust  postCust(@RequestBody Cust cust){
        String newName = "SpringCloundProviderController : "+cust.getCustName();
        cust.setCustName(newName);
        //System.out.println(newName);
        return  cust;
    }


}
