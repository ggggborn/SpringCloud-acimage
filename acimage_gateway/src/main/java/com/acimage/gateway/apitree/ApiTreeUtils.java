package com.acimage.gateway.apitree;

import com.acimage.common.model.domain.sys.Api;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class ApiTreeUtils {

    public ApiTree constructApiTree(List<Api> apiList){
        //统一去掉斜杆开头
        for(Api api:apiList){
            String path=api.getPath();
            if(path.startsWith("/")){
                api.setPath(path.substring(1));
            }
        }
        ApiTree apiTree=new ApiTree();
        apiList.sort(Comparator.comparing(Api::getPath));
        return null;
    }
}
