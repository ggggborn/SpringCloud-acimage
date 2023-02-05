package com.acimage.gateway.apitree;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

@NoArgsConstructor
@Data
public class ApiTree {
    /**
     * 路径id
     */
    Integer id;
    /**
     * 权限id
     */
    Integer permissionId;
    List<HttpMethod> methods;
    ConcurrentMap<String, ApiTree> children;
}
