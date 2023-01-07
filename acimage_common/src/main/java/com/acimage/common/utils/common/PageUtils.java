package com.acimage.common.utils.common;

public class PageUtils {
    public static int startIndexOf(int pageNo, int pageSize) {
        return (pageNo - 1) * pageSize;
    }

    public static int endIndexOf(int pageNo, int pageSize) {
        return pageNo * pageSize - 1;
    }
}
