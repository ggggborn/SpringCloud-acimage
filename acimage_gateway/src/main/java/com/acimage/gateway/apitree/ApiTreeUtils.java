package com.acimage.gateway.apitree;

import cn.hutool.core.collection.CollectionUtil;
import com.acimage.common.global.enums.MyHttpMethod;
import com.acimage.common.model.domain.sys.Api;
import com.acimage.gateway.global.consts.NotationConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class ApiTreeUtils {

    public static Api getMatchApi(ApiTree apiTree, String path, HttpMethod method) {
        if (apiTree == null) {
            return null;
        }
        if (path.startsWith("/")) {
            if (path.length() == 1) {
                return null;
            }
            path = path.substring(1);

        }
        List<String> splits = Arrays.asList(path.split("/"));
        if (CollectionUtil.isEmpty(splits)) {
            return null;
        }
        return recursiveMatch(apiTree, splits, 0, method);

    }

    private static Api recursiveMatch(ApiTree apiTree, List<String> splits, int beginIndex, HttpMethod method) {
        if (beginIndex == splits.size() || apiTree == null||apiTree.getChildren()==null) {
            return null;
        }

        for (int i = beginIndex; i < splits.size(); i++) {
            String s = splits.get(i);
            ConcurrentMap<String, ApiTree> children = apiTree.getChildren();
            //待匹配字符串
            List<String> matchStrings = Arrays.asList(s, NotationConstants.ASTERISK, NotationConstants.DOUBLE_ASTERISK);
            for (String matchString : matchStrings) {
                ApiTree child = children.get(matchString);
                //刚好匹配到根节点或匹配到**
                if (child != null && (i == splits.size() - 1 || matchString.equals(NotationConstants.DOUBLE_ASTERISK))) {
                    int matchIndex = -1;
                    List<Api> apiList = child.getApiList();
                    //找到匹配的api
                    for (int j = 0; j < apiList.size(); j++) {
                        if (apiList.get(j).getMethod().equals(MyHttpMethod.from(method))) {
                            matchIndex = j;
                            break;
                        } else if (apiList.get(j).getMethod().equals(MyHttpMethod.ALL)) {
                            matchIndex = j;
                        }
                    }
                    //匹配到则返回
                    if (matchIndex != -1) {
                        return apiList.get(matchIndex);
                    }

                    //再看看下个结点有没有**的子节点
                    if (i == splits.size() - 1 && child.getChildren() != null) {
                        for (String str : Arrays.asList(NotationConstants.ASTERISK, NotationConstants.DOUBLE_ASTERISK)) {
                            ApiTree tempChild=child.getChildren().get(str);
                            if(tempChild==null){
                                continue;
                            }
                            matchIndex = -1;
                            apiList =tempChild.getApiList();
                            //找到匹配的api
                            for (int j = 0; j < apiList.size(); j++) {
                                if (apiList.get(j).getMethod().equals(MyHttpMethod.from(method))) {
                                    matchIndex = j;
                                    break;
                                } else if (apiList.get(j).getMethod().equals(MyHttpMethod.ALL)) {
                                    matchIndex = j;
                                }
                            }
                            //匹配到则返回
                            if (matchIndex != -1) {
                                return apiList.get(matchIndex);
                            }
                        }

                    }

                } else if (child != null) {
                    //递归匹配
                    Api result = recursiveMatch(child, splits, i + 1, method);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    public static ApiTree buildApiTreeFrom(List<Api> apiList) {
        //按/分割路径
        //统一去掉斜杆开头
        for (Api api : apiList) {
            String path = api.getPath();
            if (path.startsWith("/")) {
                api.setPath(path.substring(1));
            }
        }
        apiList.sort(ApiTreeUtils::comparator);

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

                if (j == pathSplits.size() - 1) {
                    child.getApiList().add(apiList.get(i));
                }

                head = child;
            }
        }

        return apiTree;
    }

    private static int comparator(Api next, Api cur) {
        List<String> nextList = Arrays.asList(next.getPath().split("/"));
        List<String> curList = Arrays.asList(cur.getPath().split("/"));
        for (int i = 0; i < nextList.size() && i < curList.size(); i++) {
            String sNext = nextList.get(i);
            String sCur = curList.get(i);

            if (sNext.compareTo(sCur) == 0) {
                continue;
            } else if (orderOf(sNext) != orderOf(sCur)) {
                return orderOf(sNext) - orderOf(sCur);
            }
            {
                return sNext.compareTo(sCur);
            }
        }
        return curList.size() - nextList.size();
    }

    private static int orderOf(String s) {
        if (NotationConstants.DOUBLE_ASTERISK.equals(s)) {
            return 2;
        }
        if (NotationConstants.ASTERISK.equals(s)) {
            return 1;
        }
        return 0;
    }


}
