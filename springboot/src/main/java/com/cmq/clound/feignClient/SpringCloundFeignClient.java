package com.cmq.clound.feignClient;

import com.cmq.entity.Cust;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Administrator on 2019/11/29.
 */
//这里的value对应调用服务的spring.applicatoin.name
    //fallback需要开启 开启Hystrix断路器 feign.hystrix.enabled=true
@FeignClient(value = "my-spring-clound"/*,fallback = SpringCloundFeignFallback.class*/
)
public interface SpringCloundFeignClient {
    //集合了SpringCloundServiceImpl  SpringCloundProviderController 的接口
    @RequestMapping(value = "/provider/get")
    String get(@RequestParam("model") String model);

    @RequestMapping(value = "/provider/postCust")
    Cust postCust(@RequestBody Cust cust);

    @RequestMapping(value = "/springCloundService/role")
    public String role(@RequestParam("role") String role);

    @RequestMapping(value = "/springCloundService/exception")
    public String exception(@RequestParam("exception") String exception);

}
