package com.base.properties;

import com.cmq.entity.CustContact;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by chenmq on 2018/10/19.
 */
@Configuration
@ConfigurationProperties(prefix = "my")
@PropertySource(value = {"classpath:config/my.properties"})
@Data
public class PropertiesUtils {
    private String dbType;
    private CustContact custContact;
}
