package com.cmq.demo.IKExpression;

import org.apache.commons.lang.StringUtils;

/**
 * IK-Expression 函数定制
 */
@Deprecated
public class MyExpFunction {

  /**
   * in
   */
  public boolean in(String src, String target) {
    boolean srcFlag = StringUtils.isEmpty(src);
    boolean targetFlag = StringUtils.isEmpty(target);
    if (srcFlag && targetFlag) {
      return true;
    }
    if (targetFlag) {
      return false;
    }
    if (srcFlag) {
      return false;
    }
    String[] ss = target.split(",");
    for (String s : ss) {
      if (src.equals(s)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 比较两个字符串
   */
  public boolean matches(String src, String target) {
    boolean srcFlag = StringUtils.isEmpty(src);
    boolean targetFlag = StringUtils.isEmpty(target);
    if (srcFlag && targetFlag) {
      return true;
    }
    if (srcFlag) {
      return false;
    }
    if (targetFlag) {
      return false;
    }
    return src.equals(target);
  }

}
