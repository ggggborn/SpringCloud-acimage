package com.acimage.gateway.apitree;

import com.acimage.common.model.domain.sys.Api;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ApiTreeUtils {

    public ApiTree constructApiTree(List<Api> apiList) {
        //统一去掉斜杆开头
        for (Api api : apiList) {
            String path = api.getPath();
            if (path.startsWith("/")) {
                api.setPath(path.substring(1));
            }
        }
        apiList.sort(Comparator.comparing(Api::getPath));

        //按/分割路径
        List<List<String>> splits = new ArrayList<>();
        for (Api api : apiList) {
            splits.add(Arrays.asList(api.getPath().split("/")));
        }
        ApiTree apiTree = new ApiTree();

        for (int i = 0; i < splits.size(); i++) {
            ApiTree head = apiTree;
            List<String> pathSplits = splits.get(i);
            for (int j = 0; j < pathSplits.size(); j++) {
                if (head.getChildren() == null) {
                    head.setChildren(new ConcurrentHashMap<>());
                }
                ConcurrentMap<String, ApiTree> children = head.getChildren();
                ApiTree child = children.get(pathSplits.get(j));
                if (child == null) {
                    children.put(pathSplits.get(j), new ApiTree());
                }
                child = children.get(pathSplits.get(j));
                if (j == splits.size() - 1) {
                    child.getIds().add(apiList.get(i).getId());
                    child.getMethods().add(apiList.get(i).getMethod());
                    child.getPermissionIds().add(apiList.get(i).getPermissionId());
                } else {
                    head = child;
                }
            }
        }

        return null;
    }
}
