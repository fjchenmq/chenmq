/**
 * Copyright 2009-2016 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.base.mapper;

import java.lang.annotation.*;

/**
 * 固定条件放在实体类定义上面，由于动态属性表对应多个实体表的情况，增加固定值区分是那种类型的实例属性
 * 放在实体类上面比如 @MapperFixedConditions("ORG_ROLE_TYPE='1000'")
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MapperFixedConditions {
    /**
     * 固定条件
     * @return
     */
    String value();
}
