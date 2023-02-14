package com.acimage.gateway.apitree;

import com.acimage.common.model.domain.sys.Api;
import com.acimage.gateway.serivce.ApiQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ApiTreeTest {

    @Autowired
    ApiQueryService apiQueryService;

    @Test
    public void testApiTree() {
        ApiTree apiTree = ApiTreeUtils.buildApiTreeFrom(apiQueryService.listEnableApis());
        List<String> paths = Arrays.asList("/api/community/topics/operate",
                "/api/community/topics/query",
                "/api/topics/community/query",
                "/api/community/topics/jkhakhsd",
                "/api/community/topics/xxx");
        for (String path : paths) {
            System.out.println(path + ": " + ApiTreeUtils.getMatchApi(apiTree, path, HttpMethod.GET));
        }
    }

    @Test
    public void testApiTree2() {
        ApiTree apiTree = ApiTreeUtils.buildApiTreeFrom(apiQueryService.listEnableApis());
        List<String> paths = Arrays.asList(
                "/api/community/topics/operate");

        for (String path : paths) {
            Api api = ApiTreeUtils.getMatchApi(apiTree, path, HttpMethod.GET);
            System.out.println(api);
        }

    }
}
