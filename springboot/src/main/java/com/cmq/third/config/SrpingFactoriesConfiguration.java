package com.cmq.third.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.cmq.bean.Person;
import com.cmq.demo.async.AsyncTest;
import com.cmq.entity.Cust;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2018/11/28.
 * 不在scanBasePackages = {"com.cmq.config"} 配置的扫描路径下 而是在spring.factories 配置了加载路径
 */
//@Configuration //有没有这个注解都一样可以加载
public class SrpingFactoriesConfiguration {
    @Autowired
    AsyncTest asyncTest;

    @Bean
    public Cust cust() {
        System.out.println(" started by spring factories.....");
        asyncTest.TestAsync();
        return new Cust();
    }
}
