package com.cmq.config;

import com.base.util.ListUtil;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Administrator on 2019/11/27.
 */
@Configuration
public class MybatisConfiguration {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(ListUtil.class);
    @Autowired
    DataSource dataSource;

    @Bean("sqlSessionFactory")
    @Order(value = 20000)//值越小 优先级越高
    public SqlSessionFactoryBean sqlSessionFactoryBean() {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        try {
            sqlSessionFactoryBean.setDataSource(dataSource);
            String mapperLocations = "classpath*:mapping/*.xml";
            // 加载MyBatis配置文件
            PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            // 能加载多个，所以可以配置通配符(如：classpath*:mapper/**/*.xml)
            sqlSessionFactoryBean
                .setMapperLocations(resourcePatternResolver.getResources(mapperLocations));
            // 配置mybatis的config文件
            // sqlSessionFactoryBean.setConfigLocation("mybatis-config.xml");
        } catch (IOException e) {
            logger.error("sqlSessionFactoryBean", e);
        }
        return sqlSessionFactoryBean;
    }


}
