package com.cmq.service.impl;

import com.cmq.service.EmailService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2019/11/28.
 */
@Service("emailService")
/**
 * 开发环境的时候.根据profile.active决定哪个启动,也可以层级如类下 的方法也可以指定profile
 * 也可以用于@bean的方法上 决定是否启动加载某个功能
 */
@Profile({"dev"})
public class DevEmailServiceImpl implements EmailService {

    @Override
    public void send() {
        System.out.println("DevEmailServiceImpl.send().开发环境不执行邮件的发送.");
    }
}
