package com.acimage.gateway.schedule;

import com.acimage.gateway.apitree.ApiTree;
import com.acimage.gateway.apitree.ApiTreeFactory;
import com.acimage.gateway.apitree.ApiTreeUtils;
import com.acimage.gateway.serivce.ApiQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class RefreshApiTreeSchedule {

    @Autowired
    private ApiQueryService apiQueryService;
    @Autowired
    private ApiTreeFactory apiTreeFactory;
    public static final int FIX_RATE=10*1000;

    /**
     * 10分钟刷新一次api tree
     */
    @Scheduled(cron ="0 */10 * * * ?")
    public void refreshApiTree(){
        log.info("开始刷新api tree");
        ApiTree apiTree= ApiTreeUtils.buildApiTreeFrom(apiQueryService.listEnableApis());
        apiTreeFactory.setApiTree(apiTree);
        log.info("刷新api tree完成");
    }
}
