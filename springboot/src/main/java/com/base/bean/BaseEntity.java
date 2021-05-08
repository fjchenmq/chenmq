package com.base.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;

import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 实体基类.
 *
 * @param <ID> 主键类型
 */
@Slf4j
public abstract class BaseEntity<ID extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * ID泛型时Srping的BeanUtils.copyProteries无法拷贝，所以先都改为String.
     */
    private ID id;

    @Id
    public ID getId() {
        return id;
    }

    @OrderBy("desc")
    public void setId(ID id) {
        this.id = id;
    }



    /**
     * 状态时间.
     */
    @Setter
    @Getter
    @Transient
    private java.util.Date statusDate;

    /**
     * 创建时间.
     */
    @Setter
    @Getter
    @Transient
    private java.util.Date createDate;

    /**
     * 创建人.
     */
    @Setter
    @Getter
    @Transient
    private Long createStaff;
    /**
     * 创建人.
     */
    @Setter
    @Getter
    @Transient
    private String statusCd;


    /**
     * 修改时间.
     */
    @Setter
    @Getter
    @Transient
    private java.util.Date updateDate;

    /**
     * 修改人.
     */
    @Setter
    @Getter
    @Transient
    private Long updateStaff;


    // add by wangzhy 用于属性 hiberante lazy loading 判断
    /**
     * 是否是新实体，还没有保存到数据库中.
     */
    @JsonIgnore
    @Transient
    @org.codehaus.jackson.annotate.JsonIgnore
    private Boolean newEntity = false;

    /**
     * 判断是否新实体.
     * @return
     */
    @JsonIgnore
    @org.codehaus.jackson.annotate.JsonIgnore
    @Transient
    public boolean isNewEntity() {
        return (getId() == null || newEntity);

    }
    
    public void setNewEntity(boolean isNewEntity) {
        this.newEntity = isNewEntity;
    }
    
    


    /**
     * 是否已经保存到数据库.
     */
    @JsonIgnore
    @Transient
    @org.codehaus.jackson.annotate.JsonIgnore
    private boolean saved = false;


    /**
     * 已经加载的属性集合.
     */
    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @JsonIgnore
    @org.codehaus.jackson.annotate.JsonIgnore
    @Transient
    Set<String> setPropertyLoaded = new HashSet<String>();

}
