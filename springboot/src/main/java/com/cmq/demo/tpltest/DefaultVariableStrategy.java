package com.cmq.demo.tpltest;

import org.apache.commons.lang.StringUtils;


public class DefaultVariableStrategy implements VariableStrategy {

    private int counter = 0;


    @Override
    public String getVariable(String content) {
        if (!StringUtils.isEmpty(content)) {
            if (content.endsWith("s")
                && content.length() > 1) {
                return content.substring(0, content.length() - 1);
            }
            else if (content.endsWith("List")
                && content.length() > 4) {
                return content.substring(0, content.length() - 4);
            }
            else if (content.endsWith("Array")
                && content.length() > 5) {
                return content.substring(0, content.length() - 5);
            }
            else {
                return "item" + counter++;
            }
        }

        return null;
    }
}
