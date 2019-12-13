package com.cmq.utils;

import com.cmq.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by chenmq on 2018/11/9.
 */
public class BatchUtil {
    private static JdbcTemplate        jdbcTemplate = SpringUtils.getBean(JdbcTemplate.class);
    private static Logger              logger       = LoggerFactory.getLogger(BatchUtil.class);
    private static Map<String, String> ignoreField  = new HashMap();

    static {
        ignoreField.put("TNAME", "");
        ignoreField.put("serialVersionUID", "");

    }

    public static String buildInsertSql(String tableName, String keyFieldName,
        List<String> columnNameList) {
        StringBuilder sql = new StringBuilder(" INSERT INTO ");
        sql.append(tableName).append("(");
        for (int i = 0; i < columnNameList.size(); i++) {
            String col = columnNameList.get(i);
            sql.append(col.toUpperCase());
            if (i < columnNameList.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(") values (  ");
        for (int i = 0; i < columnNameList.size(); i++) {
            String col = columnNameList.get(i);
            sql.append("?");
            if (i < columnNameList.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(")");
        return sql.toString();
    }

    public static <T> List<Object[]> buildArgs(String tableName, String keyFieldName,
        List<String> columnNameList, List<T> dataList) {
        List<Object[]> batchArgs = new ArrayList<>();
        for (T dto : dataList) {
            Object[] objects = new Object[columnNameList.size()];
            for (int i = 0; i < columnNameList.size(); i++) {
                String key = columnNameList.get(i);
                Object e = null;
                try {
                    String getName = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
                    Method fieldGetMet = dto.getClass().getMethod(getName, new Class[] {});
                    e = fieldGetMet.invoke(dto, new Object[] {});
                } catch (Exception var6) {
                    //logger.error("", var6);
                }

                objects[i] = e;
            }
            batchArgs.add(objects);
        }
        return batchArgs;
    }

    /**
     * @param dataList
     * @param keyFieldName 数据库主键字段名称
     * @throws Exception
     */
    public static <T> void batchInsert(List<T> dataList, String keyFieldName, Class cls)
        throws Exception {
        try {
            if (dataList == null || dataList.isEmpty()) {
                return;
            }

            Set<String> filedSet = new HashSet<String>();
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                if (!ignoreField.containsKey(field.getName())) {
                    if (!field.getName().equals("custOrders")) {
                        filedSet.add(field.getName());
                    }
                }
            }
            String tableName = HumpToUnderline(cls.getSimpleName());
            List<String> fieldNameList = new ArrayList<>();
            List<String> columnNameList = new ArrayList<>();

            Iterator<String> iterator = filedSet.iterator();
            while (iterator.hasNext()) {
                String name = iterator.next();
                fieldNameList.add(name);
                columnNameList.add(HumpToUnderline(name));
            }
            String insertSql = buildInsertSql(tableName, keyFieldName, columnNameList);
            List<Object[]> batchArgs = buildArgs(tableName, keyFieldName, fieldNameList, dataList);
            jdbcTemplate.batchUpdate(insertSql, batchArgs);
        } catch (Exception e) {
            logger.error("batchInsertFromTemp", e);
            throw e;
        }
    }

    /***
     * 下划线命名转为驼峰命名
     *
     * @param para
     *        下划线命名的字符串
     */

    public static String UnderlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String a[] = para.split("_");
        for (String s : a) {
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    public static String HumpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;//定位
        for (int i = 0; i < para.length(); i++) {
            if (Character.isUpperCase(para.charAt(i)) && i != 0) {
                sb.insert(i + temp, "_");
                temp += 1;
            }
        }
        return sb.toString().toUpperCase();
    }

}
