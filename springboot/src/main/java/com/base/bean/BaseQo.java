package com.base.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseQo<T> extends PagerQo implements Serializable {
    private static final long serialVersionUID = 1L;

    T entity;
}
