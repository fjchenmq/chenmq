package com.base.util;

/**
 * Created by Administrator on 2019/11/22.
 */
public class StrUtil {
    public static String strnull(String strn) {
        return strnull(strn, "");
    }
    public static String strnull(String str, String rpt) {
        return str != null && !str.equals("null") && !str.equals("") && str.trim() != null?str.trim():rpt;
    }
}
