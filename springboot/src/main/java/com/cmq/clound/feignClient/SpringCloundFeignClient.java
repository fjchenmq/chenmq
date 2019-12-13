package com.cmq.clound.feignClient;

import com.cmq.entity.Cust;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Administrator on 2019/11/29.
 */
//这里的name对应调用服务的spring.applicatoin.name
    //fallback需要开启 开启Hystrix断路器 feign.hystrix.enabled=true
@FeignClient(value = "my-spring-clound"/*,fallback = SpringCloundFeignFallback.class*/
)
public interface SpringCloundFeignClient {
    @RequestMapping(value = "/provider/get.do")
    String get(@RequestParam("model") String model);

    @RequestMapping(value = "/provider/postCust.do")
    Cust postCust(@RequestBody Cust cust);

    @RequestMapping(value = "/springCloundService/role.do")
    public String role(@RequestParam("role") String role);

    @RequestMapping(value = "/springCloundService/exception.do")
    public String exception(@RequestParam("exception") String exception);

}
