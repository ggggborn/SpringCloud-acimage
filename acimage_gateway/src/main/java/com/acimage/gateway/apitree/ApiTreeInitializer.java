package com.acimage.gateway.apitree;

import org.springframework.stereotype.Component;

@Component
public class ApiTreeInitializer {

    ApiTree apiTree;

    public ApiTree getApiTree() {
        return apiTree;
    }

    public void setApiTree(ApiTree apiTree) {
        this.apiTree = apiTree;
    }
}
