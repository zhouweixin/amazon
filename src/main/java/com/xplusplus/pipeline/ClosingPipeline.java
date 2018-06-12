package com.xplusplus.pipeline;

import com.xplusplus.domain.Closing;
import com.xplusplus.service.ClosingService;
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
 * @Date: Created in 19:32 2018/4/3
 * @Modified By:
 */
@Component
public class ClosingPipeline implements Pipeline {
    @Autowired
    private ClosingService closingService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Closing closing = resultItems.get("closing");

        if(closing != null) {
            ClosingService closingService = (ClosingService) ApplicationContextProvider.getApplicationContext().getBean("closingService");
            try {
                closingService.save(closing);
            }catch (Exception e){
                // 重复约束
                if(e.getMessage().contains("Duplicate entry")){

                }
            }
        }
    }
}
