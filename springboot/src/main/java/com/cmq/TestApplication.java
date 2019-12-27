package com.cmq;

import com.base.filter.MyFilter;
import org.springframework.boot.Banner;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * 默认还会扫描 ConditionApplication类所在包路径 也就是com.cmq
 */

/**
 * @SpringBootApplication 扫描 @Configuration
 * excludeName 、 exclude 排除启动某些类
 */
@SpringBootApplication(scanBasePackages = {"com.cmq.config"}
    ,exclude = {SecurityAutoConfiguration.class,ManagementWebSecurityAutoConfiguration.class }//EnableWebSecurity 会覆盖此配置
   // excludeName = {"org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration",
     //   "org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration"}
    )
@PropertySource(value = {"classpath:config/system.properties", "classpath:config/jdbc.properties"})
public class TestApplication extends SpringBootServletInitializer
    implements WebServerFactoryCustomizer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return configureApplication(builder);
    }

    @Override
    public void customize(WebServerFactory webServerFactory) {
    }

    @Bean
    public WebServerFactoryCustomizer webServerFactoryCustomizer() {
        return new WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>() {
            @Override
            public void customize(ConfigurableServletWebServerFactory factory) {
                factory.setPort(9082);
            }
        };
    }

    public static void main(String[] args) {
        configureApplication(new SpringApplicationBuilder()).run(args);
    }

    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
        return builder.sources(TestApplication.class).bannerMode(Banner.Mode.OFF)
            .properties("is.enable=true");
    }

    /**
     * servlet相关的用这种register bean的方式
     * spring mvc相关的拦截器继承WebMvcConfigurerAdapter
     */

    @Bean
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.getUrlMappings().clear();
        /**url增加带.do后缀映射 如http://my:9082/springboot/get.do
         * 但是如果只配*.do 只会拦截带.do的方法 其他的不会走到spring mvc的servlet  像静态资源 也就拦截不到 addResourceHandlers就不会生效
          */
       /* registration.addUrlMappings("*.do");
        registration.addUrlMappings("*.html");
        registration.addUrlMappings("*.css");
        registration.addUrlMappings("*.js");
        registration.addUrlMappings("*.png");
        registration.addUrlMappings("*.gif");
        registration.addUrlMappings("*.ico");
        registration.addUrlMappings("*.jpeg");
        registration.addUrlMappings("*.jpg");*/
        //registration.addUrlMappings("/*");

        registration.setLoadOnStartup(1);
        return registration;
    }

    //用spring security 自己模拟的登陆filter 先屏蔽
     //@Bean
    public FilterRegistrationBean addFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new MyFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("MyFilter");
        registration.setOrder(1);
        return registration;
    }
    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        /**
         * 如果配置需要与addResourceHandlers里面配置的相对路径保持一致
         */
        resolver.setPrefix("/");
        resolver.setSuffix(".html");
        return resolver;
    }
}
