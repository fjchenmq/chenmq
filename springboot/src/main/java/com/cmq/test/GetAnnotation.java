package com.cmq.test;

import org.apache.commons.httpclient.util.DateUtil;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

public class GetAnnotation {

    public static void main(String[] args)
        throws NoSuchFieldException, SecurityException, IllegalArgumentException,
        IllegalAccessException {

        Field[] fields = SampleClass.class.getDeclaredFields();
        for (Field field : fields) {
            // field.setAccessible(true);
            String fieldName = field.getName();
            String fieldTypeName = field.getType().getTypeName();
            DateFormat customAnnotation = field.getAnnotation(DateFormat.class);
            System.out.println(customAnnotation.value());
        }

    }
}

class SampleClass {

    @DateFormat(value = "Sample Field Annotation")
      String sampleField;

    public String getSampleField() {
        return sampleField;
    }

    public void setSampleField(String sampleField) {
        this.sampleField = sampleField;
    }
}

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {FIELD, METHOD})
@interface DateFormat {

    /**
     * 默认推荐使用
     */
    String value() default "yyyyyyyyyy";

}
