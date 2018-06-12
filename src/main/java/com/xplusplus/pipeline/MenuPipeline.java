package com.xplusplus.pipeline;

import com.xplusplus.domain.Menu;
import com.xplusplus.service.MenuService;
import com.xplusplus.utils.ApplicationContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 19:46 2018/4/4
 * @Modified By:
 */
@Component
public class MenuPipeline implements Pipeline {
    @Autowired
    private MenuService menuService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<Menu> menus = resultItems.get("menus");

        if(menus != null) {
            MenuService menuService = (MenuService) ApplicationContextProvider.getApplicationContext().getBean("menuService");
            menuService.addBatch(menus);
        }
    }
}
