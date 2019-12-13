package com.cmq.service.impl;

import com.cmq.service.EmailService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2019/11/28.
 */
@Service("emailService")
@Profile("prod") //开发环境的时候.
public class ProdEmailServiceImpl implements EmailService {

    @Override
    public void send() {
        System.out.println("ProdEmailServiceImpl.send().开发环境不执行邮件的发送.");
    }
}
