package com.cmq.demo.ognl;

import com.cmq.bean.CertInfo;
import com.cmq.bean.Person;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.ibatis.ognl.OgnlException;

/**
 * Created by chen.ming.qian on 2020/12/28.
 */
public class TestOgnl {
    public static void  main(String [] args) throws OgnlException {
        // 构建一个OgnlContext对象
        OgnlContext context = new OgnlContext();

        Person person = new Person();
        person.setName("cmq");
        CertInfo certInfo = new CertInfo();
        certInfo.setCertName("sfz");
        person.setCertInfo(certInfo);

        // 将上述部门和员工对象放入Ognl上下文环境中
        context.put("person", person);

        // 将员工设置为根对象
        context.setRoot(person);

        // 构建Ognl表达式的树状表示,用来获取
        Object expression = Ognl.parseExpression("#person.name");
        Object name = Ognl.getValue(expression, context, context.getRoot());

        // 解析树状表达式，返回结果
        System.out.println(name);

        // 构建Ognl表达式的树状表示,用来获取
         expression = Ognl.parseExpression("#person.certInfo.certName");
         name = Ognl.getValue(expression, context, context.getRoot());

        // 解析树状表达式，返回结果
        System.out.println(name);
    }
}
