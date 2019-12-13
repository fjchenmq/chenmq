package com.cmq.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

/**
 * Created by Administrator on 2019/11/27.
 */
 @Configuration
// 注意，由于MapperScannerConfigurer执行的比较早，所以必须有下面的注解
//@AutoConfigureAfter(MybatisConfiguration.class)
public class MyBatisMapperScannerConfig {
    @Bean
    @Order(value = 50000)
    //等MybatisConfiguration加载完再加载 否则会报错 同AutoConfigureAfter
    @ConditionalOnBean(value = MybatisConfiguration.class)
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //BaseMapper 不能被扫描到 否则会报错-
        mapperScannerConfigurer.setBasePackage("com.cmq.**.mapper,com.**.dao");
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setMarkerInterface(com.base.mapper.common.EmptyMapper.class);
        Properties properties = new Properties();
        properties.setProperty("enableMethodAnnotation", "true");
        properties.setProperty("ORDER", "BEFORE");
        properties.setProperty("com.base.mapper.common.BaseMapper", "");
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }
}
