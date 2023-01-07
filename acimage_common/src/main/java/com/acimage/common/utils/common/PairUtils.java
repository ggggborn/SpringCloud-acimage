package com.acimage.common.utils.common;

import cn.hutool.core.lang.Pair;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PairUtils {

    public static <T, R> List<Pair<T, R>> combine(@NotNull List<T> list1, @NotNull List<R> list2) {
        if (list1 == null) {
            throw new IllegalArgumentException("参数1为空");
        }else if (list2 == null) {
            throw new IllegalArgumentException("参数2为空");
        }else if (list1.size() != list2.size()) {
            throw new IllegalArgumentException(String.format("参数1长度[%s] 参数2长度[%s] 参数长度不一致！", list1.size(), list2.size()));
        }else if (list1.size() == 0) {
            return new ArrayList<>();
        }

        int len = list1.size();
        List<Pair<T, R>> pairList = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            pairList.add(new Pair<>(list1.get(i), list2.get(i)));
        }
        return pairList;
    }
}
