package com.cmq.config;

import com.cmq.base.security.CustomAuthenticationEntryPoint;
import com.cmq.base.security.CustomAuthenticationFailureHandler;
import com.cmq.base.security.CustomAuthenticationSuccessHandler;
import com.cmq.base.security.MyPasswordEncoder;
import com.cmq.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by Administrator on 2019/12/25.
 */

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired
    CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    CustomAuthenticationFailureHandler authenticationFailureHandler;

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("**"); // 首页不登录
        // 忽略URL
        //web.ignoring().antMatchers("/**/*.js", "/**/*.png");
    }

    //屏蔽掉 security 登陆验证 默认login 入口org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
    protected void configure(HttpSecurity http) throws Exception {
       /* http.authorizeRequests()
            .anyRequest().permitAll();*/

        http.authorizeRequests()
            .antMatchers("/sb/favicon.ico", "/timeout", "/**/index*.*", "/login", "/myError",
                "/test/*", "my-spring-clound//*", "/**/*.js").permitAll().anyRequest()
            .authenticated().and().formLogin()
            //指定登录页的路径 ,
            .loginPage("/index")
            //指定自定义form表单请求的路径  如果与界面提交的路径不一致会失败  spring mvc是否有这个方法好像没有影响
            .loginProcessingUrl("/login").failureUrl("/myError")
            .defaultSuccessUrl("/success")// successHandler 后面的覆盖前面
            .successHandler(authenticationSuccessHandler)
            .failureHandler(authenticationFailureHandler)
            //必须允许所有用户访问我们的登录页（例如未验证的用户，否则验证流程就会进入死循环）
            //这个formLogin().permitAll()方法允许所有用户基于表单登录访问/login这个page。
            .permitAll().and().rememberMe().tokenValiditySeconds(60)//记住我功能，cookies有限期是一周
            .rememberMeParameter("remember-me")//登陆时是否激活记住我功能的参数名字，在登陆页面有展示
            .rememberMeCookieName("workspace");//cookies的名字，登陆后可以通过浏览器查看cookies名字;
        // 指定未登录入口点 后面的设置覆盖上面loginPage
        http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);
        //默认都会产生一个hiden标签 里面有安全相关的验证 防止请求伪造 这边我们暂时不需要 可禁用掉
        //关闭跨域保护

        http.csrf().disable().cors();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailServiceImpl())//自定义登陆用户服务
            .passwordEncoder(new MyPasswordEncoder());//在此处应用自定义PasswordEncoder
        //.withUser("admin").password("123456").roles("USER");
    }

}

