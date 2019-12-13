package com.base.filter;

import com.cmq.bean.LoginInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2019/2/21.
 */

/**
 * @ServletComponentScan时会扫描标注@WebFilter的自定义filter, 并且加载到项目中,
 * 这边采用的不是注解方式 而是 registration.setFilter(new MyFilter());
 */
//@WebFilter
//@ServletComponentScan("com.base.filter")
public class MyFilter extends OncePerRequestFilter {
    protected static List<Pattern> patterns = new ArrayList<Pattern>();

    private static final Logger log = LoggerFactory.getLogger(MyFilter.class);

    static {
        patterns.add(Pattern.compile(".*login.do"));
        patterns.add(Pattern.compile(".*login.html"));
        patterns.add(Pattern.compile(".*timeout.html"));
        //|| (\.ico)
        patterns.add(Pattern.compile(".*[(\\.js)||(\\.css)||(\\.png)||(\\.tff) || (\\.ico)]"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws ServletException, IOException {
        String requestUrl = "";
        LoginInfo loginInfo = null;
        try {
            loginInfo = (LoginInfo) request.getSession().getAttribute("loginSession");
            requestUrl = request.getRequestURL().toString();
            log.info("{}  is enter filter....", requestUrl);

            //服务先不拦截
            if (requestUrl.indexOf(".do") != -1) {
                chain.doFilter(request, response);
                return;
            }
            if (loginInfo == null && isFilter(requestUrl)) {
                request.getSession().setAttribute("beforeUrl", requestUrl);
                //request.getRequestDispatcher("timeout.html").forward(request, response);
                response.sendRedirect("timeout.html");
                return;
            }

            chain.doFilter(request, response);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /*
     * 是否需要过滤
     * @param url
     * @return
     */

    private boolean isFilter(String url) {
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.matches()) {
                return false;
            }
        }
        return true;
    }

}
