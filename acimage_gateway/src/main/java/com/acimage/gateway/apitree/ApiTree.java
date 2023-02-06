package com.acimage.gateway.apitree;

import com.acimage.common.global.enums.MyHttpMethod;
import com.acimage.common.model.domain.sys.Api;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

@NoArgsConstructor
@Data
public class ApiTree {
    /**
     * 路径id
//     */
//    private List<Integer> ids=new ArrayList<>();
//    /**
//     * 权限id
//     */
//    private List<Integer> permissionIds=new ArrayList<>();
//    private List<MyHttpMethod> methods=new ArrayList<>();
    private List<Api> apiList=new ArrayList<>();
    private ConcurrentMap<String, ApiTree> children;
}
