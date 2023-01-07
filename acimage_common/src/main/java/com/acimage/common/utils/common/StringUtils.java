package com.acimage.common.utils.common;

import cn.hutool.core.collection.CollectionUtil;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;

public class StringUtils {

    public static String concatForNotNull(@Nullable String separator, @Nullable List<String> stringList) {
        if (CollectionUtil.isEmpty(stringList)) {
            return "";
        }

        String newSeparator = separator == null ? "" : separator;
        //拼接
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stringList.size(); i++) {
            stringBuilder.append(stringList.get(i));
            if (i != stringList.size() - 1) {
                stringBuilder.append(newSeparator);
            }
        }

        return stringBuilder.toString();
    }
}
