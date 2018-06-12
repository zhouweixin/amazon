package com.xplusplus.pipeline;

import com.xplusplus.domain.SimpleClosing;
import com.xplusplus.service.SimpleClosingService;
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
 * @Date: Created in 20:33 2018/4/4
 * @Modified By:
 */
@Component
public class SimpleClosingPipeline implements Pipeline {
    @Autowired
    private SimpleClosingService simpleClosingService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<SimpleClosing> simpleClosings = resultItems.get("simpleClosings");

        if(simpleClosings != null) {
            SimpleClosingService simpleClosingService = (SimpleClosingService) ApplicationContextProvider.getApplicationContext().getBean("simpleClosingService");
            simpleClosingService.save(simpleClosings);
        }
    }
}
