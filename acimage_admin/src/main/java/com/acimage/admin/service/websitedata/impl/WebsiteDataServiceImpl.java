package com.acimage.admin.service.websitedata.impl;

import com.acimage.admin.model.vo.WebsiteDataVo;
import com.acimage.admin.service.websitedata.WebsiteDataService;
import com.acimage.common.global.consts.SysKeyConstants;
import com.acimage.common.utils.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebsiteDataServiceImpl implements WebsiteDataService {
    @Autowired
    RedisUtils redisUtils;

    @Override
    public WebsiteDataVo getWebsiteData() {
        WebsiteDataVo websiteDataVo = new WebsiteDataVo();
        Long pageViewLong = redisUtils.sizeForHyperLogLog(SysKeyConstants.LOGK_PAGE_VIEW);
        Integer apiAccessCount = redisUtils.getForString(SysKeyConstants.STRINGK_INTERFACE_TOTAL, Integer.class);
        if (pageViewLong != null) {
            websiteDataVo.setPageView(pageViewLong.intValue());
        }

        websiteDataVo.setApiAccessCount(apiAccessCount);
        return websiteDataVo;
    }
}
