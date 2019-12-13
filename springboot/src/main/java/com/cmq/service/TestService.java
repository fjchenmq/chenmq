package com.cmq.service;

import com.cmq.bean.Person;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by chenmq on 2018/8/10.
 */
public interface TestService {
    public String getName() throws Exception;

    public String getAge();

    /**
     * 经验证不知道是不是写法不正确 并不生效
     */
    @Validated
    public Person getPerson(@Valid @NotNull(message = "不能为空") Person person);
}
