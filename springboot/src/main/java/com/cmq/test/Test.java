package com.cmq.test;

import java.io.File;

/**
 * Created by Administrator on 2019/6/24.
 */
public class Test {
    // 仓库根目录
    static String root = "E:\\asiainfo\\EBoss\\repository";

    public static void main(String[] args) {

        String str="账号管理,认证,会话管理,权限管理,数据安全,加解密,日志审计,服务接口,HTTP安全,XSS-应采用适当的输入校验和输出编码机制防止XSS攻击,注入,文件上传/下载,越权访问,敏感信息泄露,CSRF,XXE,不安全的重定向,使用已知漏洞组件,安全配置错误,SSRF,不安全的java反序列化,点击劫持clickjacking,用户枚举,记录上次登录用户,HTTP内容混杂,不安全的资源加载,1. 加密,2. 话单脱敏,3. 数据库脱敏,4. 日志不得打印密码,5. 日志个人数据脱敏,6. 分层分级管理,7. 新建角色默认最小权限,9. 密码复杂度管理,10. 安全转移,12. 数据采集声明,13. 数据采集授权,15. 最小化采集,16. 个人数据存储加密,17. GDPR日志,18. GDPR日志内容,19. GDPR日志保存,33. 测试/开发环境";
        String str1="1714493,1714484,1714483,1714482,1714481,1714480,1714479,1714477,1714476,1714474,1714473,1714472,1714471,1714502,1714491,1714488,1714486,1714475,1714505,1714495";




        File file = new File(root);

        File[] _files = file.listFiles();
        if (_files != null && _files.length > 0) {
            for (File _file : _files) {
                validate(_file);
            }
        }

    }

    public static boolean validate(File file) {
        boolean isHaveJar = false;
        File[] _files = file.listFiles();
        if (_files != null && _files.length > 0) {
            // 判断是否有*jar 是否是有文件夹
            for (File _file : _files) {
                if (_file.getName().endsWith(".jar")) {
                    isHaveJar = true;
                }
                if (_file.isDirectory()) {
                    boolean isNextHaveJar = validate(_file);
                    if (isNextHaveJar) {
                        isHaveJar = true;
                    }
                }
            }
        }
        if (!isHaveJar) {
            delete(file);
        }
        return isHaveJar;
    }

    public static void delete(File file) {
        File[] _files = file.listFiles();
        if (_files != null && _files.length > 0) {
            for (File _file : _files) {
                if (_file.isDirectory()) {
                    delete(_file);
                }
                _file.delete();
            }
        } else {
            file.delete();
        }
    }

}
