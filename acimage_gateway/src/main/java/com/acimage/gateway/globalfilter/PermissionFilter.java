package com.acimage.gateway.globalfilter;


import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.sys.Api;
import com.acimage.gateway.apitree.ApiTreeFactory;
import com.acimage.gateway.apitree.ApiTreeUtils;
import com.acimage.gateway.config.RoleConfig;
import com.acimage.gateway.serivce.AuthorizeQueryService;
import com.acimage.gateway.serivce.RoleQueryService;
import com.acimage.gateway.serivce.UserRoleQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Order(30)
@Component
public class PermissionFilter implements GlobalFilter {
    @Autowired
    private ApiTreeFactory apiTreeFactory;
    @Autowired
    private AuthorizeQueryService authorizeQueryService;
    @Autowired
    private UserRoleQueryService userRoleQueryService;
    @Autowired
    private RoleConfig roleConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getURI().getPath();
        HttpMethod httpMethod = request.getMethod();
        if (httpMethod != HttpMethod.GET) {
            log.info("{} {} ip:{} user:{}", url, httpMethod, UserContext.getIp(), UserContext.getUsername());
        }
        //获取匹配的api树
        Api api = ApiTreeUtils.getMatchApi(apiTreeFactory.getApiTree(), url, httpMethod);

        //api不存在
        if (api == null) {
            log.info(url + " 不存在");
            UserContext.remove();
            return exchange.getResponse().setComplete();
        }
        Map<Integer, List<Integer>> map = authorizeQueryService.getRolePermissionIdsMap();

        //获取访客权限
        List<Integer> permissionIds = map.get(roleConfig.getVisitorId());
        if (permissionIds != null && permissionIds.contains(api.getPermissionId())) {
            log.debug(api.getPath() + api.getMethod() + "通过");
            UserContext.remove();
            return chain.filter(exchange);
        }

        //获取用户权限
        if (UserContext.getUserId() != null) {
            permissionIds = map.get(roleConfig.getUserId());
            if (permissionIds != null && permissionIds.contains(api.getPermissionId())) {
                log.debug(api.getPath() + api.getMethod() + "通过");
                UserContext.remove();
                return chain.filter(exchange);
            }
        }

        if (UserContext.getUserId() != null) {
            //获取用户具体角色的权限
            List<Integer> roleIds = userRoleQueryService.listRoleIds(UserContext.getUserId());
            for (Integer roleId : roleIds) {
                permissionIds = map.get(roleId);
                if (permissionIds != null && permissionIds.contains(api.getPermissionId())) {
                    log.debug(api.getPath() + api.getMethod() + "通过");
                    UserContext.remove();
                    return chain.filter(exchange);
                }
            }
        }

        log.info("{} {} 权限不足 ip:{}", api.getPath(), api.getMethod(), UserContext.getIp());
        UserContext.remove();
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }


}
