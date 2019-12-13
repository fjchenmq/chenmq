package com.cmq.config;

import com.base.properties.PropertiesUtils;
import com.base.sequence.DbUtil;
import com.cmq.bean.Person;
import com.cmq.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by chenmq on 2018/10/26.
 */

/**
 * @Configuration一般被用来初始化配置，有两种方法可以使带有@Configuration的类被初始化， 一为让把类所在包的路径纳入scanBasePackages，这样就进入了Spring的扫描范围；
 * 还有一种方法就是在spring.factories中用org.springframework.boot.autoconfigure.EnableAutoConfiguration=类的全路径名,
 * 这样在项目启动的时候SpringFactoriesLoader会初始化spring.factories（包括引入的jar包中的）中配置的类。
 * 在spring.factories配置的好处就是，如果我们想开发一个jar把供其他人使用，
 * 那么我们就在自己工程的spring.factories中配置@@Configuration类，
 * 这样只要其他人在他们项目的POM中加入了我们开发的jar包作为依赖，在他们项目启动的时候就会初始化我们开发的工程中的类。
 * ---------------------
 */
@Configuration
/**
 * @ComponentScan 如果扫描到有@Component @Controller@Service等这些注解的类，则把这些类注册为bean
 */
@ConditionalOnProperty(prefix = "sys", value = {"enable"}, havingValue = "true"
    // ,matchIfMissing = true
    //matchIfMissing = true指如果没有配置这个参数，默认条件仍然成立
)
public class MyCondition {
    @Autowired
    PropertiesUtils propertiesUtils;

    @ConditionalOnMissingBean(TestService.class)
    @Bean
    public String missBean() {
        System.err.println("ConditionalOnBean is missing");
        return "";
    }

    /**
     * 当 testServive存在时 加载person bean  参数testService自动注入
     *
     * @param testService
     * @return
     */
    @ConditionalOnBean(TestService.class)
    @Bean
    public Person onBean(TestService testService) {
        String age = testService.getAge();
        System.err.println("ConditionalOnBean is OK");
        Person person = new Person();
        person.setName("chenm");
        return person;
    }

    /**
     * Created by chenmq on 2018/8/10.
     */
    @Configuration
   /* @ComponentScan(basePackages = {
        "com.base","com.cmq"
    })
    @ConditionalOnProperty(
        prefix = "sys",
        value = {"enable"},
        havingValue = "true"
      // ,matchIfMissing = true
        //matchIfMissing = true指如果没有配置这个参数，默认条件仍然成立
    )*/

    public class ConditionApplication {
        {
            System.out.println("loading...." + propertiesUtils.getDbType());
            DbUtil.setDbType(propertiesUtils.getDbType());
        }

    }
}
