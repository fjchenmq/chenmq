package com.base.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/5.
 */
public class MyListUtil {
    static Logger logger     = org.slf4j.LoggerFactory.getLogger(MyListUtil.class);
    static String SPLIT_CHAR = ",";

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static <T> List<T> newList() {
        return new ArrayList<T>();
    }

    public static <T> Map<String, T> list2Map(List<T> list, String keyField) {
        Map map = new HashMap();
        if (!isEmpty(list)) {
            for (T t : list) {
                try {
                    Object val = PropertyUtils.getProperty(t, keyField);
                    String value = val == null ? "" : val.toString();
                    map.put(value, t);
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }
        return map;
    }

    /**
     * 提取list 里面某个字段
     *
     * @param list
     * @param keyField
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K> List<K> extractList(List<T> list, String keyField) {
        List<K> rlt = MyListUtil.newList();
        if (!isEmpty(list)) {
            for (T t : list) {
                try {
                    K value = (K) PropertyUtils.getProperty(t, keyField);
                    rlt.add(value);
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }
        return rlt;
    }

    /*
    根据des list在src中排除重复数值，返回排除重复后的list
     */
    public static <T> List<Long> excludeRepeat(List<Long> src, List<T> des,
        String compareFieldName) {
        List<Long> list = MyListUtil.newList();
        if (MyListUtil.isEmpty(des)) {
            return src;
        }
        Map<String, T> map = MyListUtil.list2Map(des, compareFieldName);
        for (Long el : src) {
            if (!map.containsKey(el)) {
                list.add(el);
            }
        }
        return list;
    }

    /**
     * list 转为string 以,隔开
     *
     * @param des
     * @param fieldName
     * @param <T>
     * @return
     */
    public static <T> String list2String(List<T> list, String fieldName) {
        StringBuilder sb = new StringBuilder("");
        if (!MyListUtil.isEmpty(list)) {

            if (fieldName == null || fieldName.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    try {
                        sb.append(list.get(i));
                        if (!(i == (list.size() - 1))) {
                            sb.append(SPLIT_CHAR);
                        }
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            } else {
                for (int i = 0; i < list.size(); i++) {
                    try {
                        Object val = PropertyUtils.getProperty(list.get(i), fieldName);
                        String key = val == null ? "" : val.toString();
                        if (key != null && !key.isEmpty()) {
                            sb.append(key);
                            if (!(i == (list.size() - 1))) {
                                sb.append(SPLIT_CHAR);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            }

        }
        return sb.toString();
    }

    /**
     * 判断2个list 指定的字段是否一致
     *
     * @param src
     * @param des
     * @param compareFieldName
     * @param <T>
     * @return
     */
    public static <T, K> boolean equals(List<K> src, List<T> des, String compareFieldName) {
        return MyListUtil.list2String(src, compareFieldName)
            .equals(MyListUtil.list2String(des, compareFieldName));
    }

    /**
     * @param list
     * @param keyField
     * @param <T>
     * @return
     */
    public static <T> boolean hasNullValue(List<T> list, String keyField) {
        if (!isEmpty(list)) {
            for (T t : list) {
                try {
                    Object value = PropertyUtils.getProperty(t, keyField);
                    if (value == null) {
                        return true;
                    }
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
            return false;
        }
        return true;
    }

}
