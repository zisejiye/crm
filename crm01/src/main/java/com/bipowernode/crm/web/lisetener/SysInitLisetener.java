package com.bipowernode.crm.web.lisetener;

import com.bipowernode.crm.settings.domain.DicValue;
import com.bipowernode.crm.settings.service.DicService;
import com.bipowernode.crm.settings.service.impl.DicServiceImpl;
import com.bipowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitLisetener implements ServletContextListener {
//该方法是用来监听上下文域对象的方法，当服务器启动，上下文域对象创建对象完毕后，马上执行该方法
    //event：改参数能够取得监听的对象
          /*  监听的是什么对象，就可用改参数获取什么对象，
            我们用的上下文的对象，就可以同过该参数取得上下文对象，*/
    public void contextInitialized(ServletContextEvent event) {
             System.out.println("上下文对象创建了");
        ServletContext application = event.getServletContext();
        //获取Dic业务层对象
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        //应该向业务层要
       /* 7个list
            可以打包成为一个map，业务层应该是这样来保存数据的：
                map.put("appellationList",dvList1)
                map.put("clueStateList",dvList2)*/

        Map<String, List<DicValue>> map = ds.getAll();
        //将map解析为上下文中的键值对
        Set<String> set = map.keySet();
        for(String  key:set){
            application.setAttribute(key,map.get(key));
        }
//        application.setAttribute("key",数据字典);


        //数据字典处理完毕后，处理Stage2Possibility文件
        /*
        * 处理Stage2Possibility文件步骤：
        *       解析该文件，将改属性文件中的键值对关系处理成java中键值对关系（map）
        *       Map<String(阶段stage),String(可能性possibility)> pmap =....
        *       pMap.put("01资质审查",10)
        *       pMap.put("02需求分析"，25）
        *       pMap.put("07..",...)
        *
        *       map保存值后放在服务器缓存中
        *       appliction.setAtribute("pMap",pMap);
        * */

        //解析properties文件
        Map<String,String> pMap = new HashMap<String, String>();
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e =rb.getKeys();
        while(e.hasMoreElements()){
            //阶段
            String key=e.nextElement();
            //可能性
            String value=rb.getString(key);
            pMap.put(key,value);
        }
        //将pMap保存到服务器缓存中
        application.setAttribute("pMap",pMap);
    }


}
