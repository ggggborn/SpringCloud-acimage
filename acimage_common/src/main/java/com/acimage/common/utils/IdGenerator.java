package com.acimage.common.utils;

import cn.hutool.core.util.IdUtil;

public class IdGenerator {
    public static long getSnowflakeNextId(){
        return IdUtil.getSnowflakeNextId();
    }
}
