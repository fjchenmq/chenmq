package com.base.util;

import com.base.properties.PropertiesUtils;
import com.cmq.bean.CertInfo;
import com.cmq.bean.Person;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射工具类
 * .
 *
 * @since 1.0.0
 */
public class ReflectUtils {

    private static final ConcurrentHashMap<String, Class> genericsCache = new ConcurrentHashMap<String, Class>();

    private static final ConcurrentHashMap<TypeFieldKey, Field> fieldCache = new ConcurrentHashMap<TypeFieldKey, Field>();
    public final static  String                                 LEFT       = "{{";
    public final static  String                                 RIGHT      = "}}";

    public static final Field NONE_FIELD = null;

    /**
     * 获取泛型类型
     * .
     *
     * @param clazz 类型
     * @param index 参数索引，从0开始
     * @return orderCustCert
     *

     

     */
    public static Class getSuperClassGenericType(Class clazz, int index) {
        String cacheKey = clazz.getName() + "_" + index;
        if (!genericsCache.containsKey(cacheKey)) {
            Type genType = clazz.getGenericSuperclass();// 得到泛型父类

            // 如果没有实现ParameterizedType接口，即不支持泛型，直接返回Object.class
            if (!(genType instanceof ParameterizedType)) {
                throw new RuntimeException("不支持泛型");
            }

            // 返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class, 如BuyerServiceBean extends
            // DaoSupport<Buyer,Contact>就返回Buyer和Contact类型
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if (index >= params.length || index < 0) {

                throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
            }
            if (!(params[index] instanceof Class)) {

                throw new RuntimeException("泛型参数不是Class");
            }

            genericsCache.put(cacheKey, (Class) params[index]);
        }

        return genericsCache.get(cacheKey);
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        TypeFieldKey fieldKey = new TypeFieldKey(clazz, fieldName);

        if (!fieldCache.containsKey(fieldCache)) {
            Class<?> target = clazz;
            Field result = null;

            while (target != null) {
                Field[] fields = target.getDeclaredFields();
                if (fields != null && fields.length > 0) {
                    for (Field field : fields) {
                        if (fieldName.equals(field.getName())) {
                            result = field;
                            break;
                        }
                    }

                    if (result != null) {
                        fieldCache.put(fieldKey, result);
                        break;
                    }
                }
                target = target.getSuperclass();
            }

            if (result == null) { // 找不到也缓存NULL
                fieldCache.put(fieldKey, NONE_FIELD);
            }
        }

        return fieldCache.get(fieldKey);
    }

    static class TypeFieldKey {
        Class<?> clazz;

        String fieldName;

        TypeFieldKey(Class<?> clazz, String fieldName) {
            this.clazz = clazz;
            this.fieldName = fieldName;
        }

        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof TypeFieldKey)) {
                return false;
            }

            TypeFieldKey otherKey = (TypeFieldKey) other;

            return otherKey.clazz == this.clazz && otherKey.fieldName.equals(this.fieldName);
        }

        public int hashCode() {
            return this.clazz.hashCode() * 11 + this.fieldName.hashCode();
        }
    }

    public static void invoke(String classPath, String methodName) throws Exception {
        Object instance = Class.forName(classPath).newInstance();
        Method[] methods = instance.getClass().getDeclaredMethods();
        Method method = null;
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                method = m;
            }
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        Person p = new Person();
        p.setName("cmq");
        method.invoke(instance, p);
    }

    /**
     * @param null
     * @return
     * @author chenmq
     * @version 2020-07-15 16:03:46
     * @description
     */
    private static String parseTemplate(Object obj, String template, String rlt) {
        String code = "";
        String val = "";
        if (template.indexOf(LEFT) != -1 && template.indexOf(RIGHT) != -1) {
            code = getSubStr(template, LEFT, RIGHT);
            val = getVal(obj, code).toString();
            template = template.replace(LEFT + code + RIGHT, "");
            rlt = rlt.replace(LEFT + code + RIGHT, LEFT + val + RIGHT);
        }
        if (template.indexOf(LEFT) != -1 && template.indexOf(RIGHT) != -1) {
            rlt = parseTemplate(obj, template, rlt);
        }
        return rlt;
    }

    public static String getSubStr(String source, String start, String end) {
        if (source == null) {
            return source;
        }
        String temp = source.substring(source.indexOf(start) + start.length());
        temp = temp.substring(0, temp.indexOf(end));
        return temp.trim();
    }

    /**
     * @param null
     * @return
     * @author chenmq
     * @version 2020-07-15 17:22:58
     * @description
     */
    public static Object getVal(Object obj, String path) {
        Object rlt = null;
        String code;
        String[] arrays = path.split("\\.");
        for (int i = 0; i < arrays.length; i++) {
            code = arrays[i];
            try {
                rlt = PropertyUtils.getProperty(obj, code);
                obj = rlt;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return rlt;
    }

    public static void main(String[] args) throws Exception {
        String temp = "{{name}}{{certInfo.certName}}{{certInfo.certNo}}";
        Person person = new Person();
        person.setName("chen");
        CertInfo certInfo = new CertInfo();
        certInfo.setCertName("身份证");
        certInfo.setCertNo("0000");
        person.setCertInfo(certInfo);
        String rlt = "";
        rlt = parseTemplate(person, temp, temp);
        System.out.println(rlt);

    }

}
