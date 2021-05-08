package com.cmq.config;

import com.base.filter.MyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

//import static org.springframework.security.config.http.MatcherType.mvc;

/**
 * Created by Administrator on 2019/2/21.
 */
@Configuration
@EnableWebMvc
public class MyWebMvcConfigurerAdapter implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        InterceptorRegistration interceptorRegistration = registry
            .addInterceptor(new MyInterceptor());
//        List<String> exclude = new ArrayList<>();
//        exclude.add("/**/fonts/*");
//        exclude.add("/**/*.css");
//        exclude.add("/**/*.js");
//        exclude.add("**/*.png");
//        exclude.add("/**/*.gif");
//        exclude.add("/**/*.jpg");
//        exclude.add("/**/*.jpeg");
//        interceptorRegistration.excludePathPatterns(exclude);
        interceptorRegistration.addPathPatterns("/*");
        //super.addInterceptors(registry);
    }

    /**
     * 通过重写addResourceHandlers方法映射文件路径，使能够访问静态资源
     * 该 resolver 的作用是将 url 为 /webjars/** 的请求映射到 classpath:/META-INF/resources/webjars/。
     * 比如请求 http://localhost:8080/webjars/jquery/3.1.0/jquery.js 时，
     * Spring MVC 会查找路径为 classpath:/META-INF/resources/webjars/jquery/3.1.0/jquery.js 的资源文件。
     * addResourceLocations后面多个路径会按照顺序优先加载
     * http://my:9082/springboot/login.html 直接访问web路径 不过好像配法问题 也会导致访问不了
     * http://my:9082/sb/login.html 通过spring mvc访问
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/sb/**")
            .addResourceLocations("/webjars/springboot/", "classpath:/webjars/springboot/");
        /**添加这个解决 springmvc 返回view 在界面引入js 访问不到问题
         * 如果没有添加这个 通过springmvc 视图返回的界面 （访问http://my:9082/success）是无法加载到src="./js/jquery-1.10.2.min.js"
         * 但是可以加载到src="./sb/js/jquery-1.10.2.min.js"
         *
          */

        registry.addResourceHandler("/js/**")
            .addResourceLocations("/webjars/springboot/js/", "classpath:/webjars/springboot/js/");
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return converter;
    }

/*

    //解决mvc 返回中文乱码问题
    spring boot2无需设置这个
    @Bean
    public HttpMessageConverter<String> stringHttpMessageConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(
            Charset.forName("UTF-8"));
        return converter;
    }
*/

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //converters.add(stringHttpMessageConverter());
        converters.add(mappingJackson2HttpMessageConverter());
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }
    //end 乱码

}
