package com.cmq.demo;

/**
 * Created by chen.ming.qian on 2021/1/5.
 */

import org.apache.catalina.security.SecurityUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpClientTest {
    /**
     * 访问服务
     *
     * @param wsdl   wsdl地址
     * @param ns     命名空间
     * @param method 方法名
     * @param list   参数
     * @return
     * @throws Exception
     */
    public synchronized static String accessService(String url, String body) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();

        String soapResponseData = "";
        PostMethod postMethod = new PostMethod(url);
        // 然后把Soap请求数据添加到PostMethod中
        byte[] b = null;
        InputStream is = null;
        try {
            b = body.getBytes("utf-8");
            is = new ByteArrayInputStream(b, 0, b.length);
            RequestEntity re = new InputStreamRequestEntity(is, b.length,
                "text/xml; charset=UTF-8");
            postMethod.setRequestEntity(re);
            HttpClient httpClient = new HttpClient();
            int status = httpClient.executeMethod(postMethod);

            if (status == 200) {
                System.out.println(postMethod.getResponseBodyAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return soapResponseData;
    }

    public static String getMesage(String soapAttachment, String result) {
        if (result == null) {
            return null;
        }
        if (soapAttachment != null && soapAttachment.length() > 0) {
            int begin = soapAttachment.indexOf("<return>");
            begin = soapAttachment.indexOf(">", begin);
            int end = soapAttachment.indexOf("</return>");
            String str = soapAttachment.substring(begin + 1, end);
            str = str.replaceAll("<", "<");
            str = str.replaceAll(">", ">");
            return str;
        } else {
            return "";
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {

            String body =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.oc.ztesoft.com/\">\n"
                    + "   <soapenv:Header/>\n" + "   <soapenv:Body>\n" + "      <ser:testWb>\n"
                    + "         <!--Optional:-->\n" + "         <name>cmq</name>\n"
                    + "         <!--Optional:-->\n" + "         <id>333</id>\n"
                    + "      </ser:testWb>\n" + "   </soapenv:Body>\n" + "</soapenv:Envelope>";
            String wsdl = "http://172.21.105.101:8082/oc-service/service/ws";
            String response = accessService(wsdl, body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
