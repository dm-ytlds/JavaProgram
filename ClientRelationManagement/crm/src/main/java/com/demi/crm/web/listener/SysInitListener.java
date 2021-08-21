package com.demi.crm.web.listener;

import com.demi.crm.settings.domain.DicValue;
import com.demi.crm.settings.service.DicService;
import com.demi.crm.settings.service.impl.DicServiceImpl;
import com.demi.crm.utils.ServiceFactory;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {
    // 重写创建和销毁方法
    /*
    * 该方法用来监听上细纹作用域对象的方法，当服务器启动，上下文作用域对象也就创建了
    * 对象创建完毕后，马上执行该方法
    * event：改参数能够取得监听的对象是什么。
    *   监听的是什么对象，就可以通过改参数就能取得什么对象。
    *   例如：这里监听的是上下文与对象，event参数就用来获取上下文域对象
    * */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("上下文作用域对象已成功创建");
        ServletContext application = event.getServletContext();
        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());
        /*
        * 应该管业务层要7个list(因为 typecode 有七种类型)
        *   map.put("appellationList", dvList01);
        *   map.put("appellationList", dvList02);
        *   map.put("appellationList", dvList03);
        *   ...
        * */
        Map<String, List<DicValue>> map  = dicService.getAll();

        // 取出所有的key
        Set<String> keys = map.keySet();
        for (String key : keys) {
            // 将map解析为上下文作用域对象中保存的键值对
            application.setAttribute(key, map.get(key));
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }
}
