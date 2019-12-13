package com.cmq.clound;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Administrator on 2019/11/28.
 */
public interface SpringCloundService {
    public String role(String role);

    public String exception(String exception);
}
