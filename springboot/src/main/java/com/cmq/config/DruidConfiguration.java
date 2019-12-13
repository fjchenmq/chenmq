package com.cmq.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2018/11/28.
 * 初始化数据库连接池
 * spring.datasource.password
 */
@Configuration
public class DruidConfiguration {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean(name="dataSource")
    @Order(value = 10000)
    public DataSource dataSource() {
        return new DruidDataSource();
    }
}
