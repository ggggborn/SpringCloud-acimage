package com.acimage.common.model.page;


import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPage<T> {
    int totalCount;
    List<T> dataList;

    public static<T> MyPage<T> from(IPage<T> page){
        return new MyPage<>((int)page.getTotal(),page.getRecords());
    }
}
