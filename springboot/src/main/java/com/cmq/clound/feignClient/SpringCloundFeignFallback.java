package com.cmq.clound.feignClient;

import com.cmq.entity.Cust;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Administrator on 2019/12/2.
 */
@Service
public class SpringCloundFeignFallback implements SpringCloundFeignClient {
    @Override
    public String get(String model) {
        return null;
    }

    @Override
    public Cust postCust(Cust cust) {
        return null;
    }

    @Override
    public String role(String role) {
        return null;
    }

    @Override
    public String exception(  String exception) {
        return "网络异常请重试";
    }
}
