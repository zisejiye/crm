package com.bjpowernode.settings;

import com.bipowernode.crm.utils.DateTimeUtil;
import com.bipowernode.crm.utils.MD5Util;

public class test01 {
    public static void main(String[] args) {
       //验证失效时间
       /* String expireTime = "2022-09-08 15:15:15";
        String str = DateTimeUtil.getSysTime();
         int count =expireTime.compareTo(str);
        System.out.println(count);*/

  /*     String localState= "0";
       if ("0".equals(localState)){
           System.out.println("账户状态已锁定");
       }*/

/*        //浏览器端ip地址
        String ip ="192.168.0.1";
        //允许访问的ip地址群
        String allowIps = "192.168.0.1,192.168.0.3";
        if (allowIps.contains(ip) ) {
            System.out.println("ip地址有效，允许访问");

        }else {
            System.out.println("ip地址受限,联系管理员");
        }*/

        String pwd = "Song,666666";
        pwd=MD5Util.getMD5(pwd);
        System.out.println(pwd);






    }
}
