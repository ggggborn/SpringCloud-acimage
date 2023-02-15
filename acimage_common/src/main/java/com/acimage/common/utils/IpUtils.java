package com.acimage.common.utils;

import cn.hutool.core.util.StrUtil;
import com.acimage.common.global.consts.HeaderKeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;


import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

@Slf4j
public class IpUtils {

    //获取请求客户端IP地址
    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    ExceptionUtils.printIfDev(e);
                }
                assert inet != null;
                ip = inet.getHostAddress();
            }
        }

        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    private final static String IP_UTILS_FLAG = ",";
    // 未知IP
    private final static String UNKNOWN = "unknown";
    // 本地 IP
    private final static String LOCALHOST_IP = "0:0:0:0:0:0:0:1";
    private final static String LOCALHOST_IP1 = "127.0.0.1";


    public static String getUserIp(ServerHttpRequest request) {
        // 多次反向代理后会有多个ip值 的分割符
        // 根据 HttpHeaders 获取 请求 IP地址
        String ip = request.getHeaders().getFirst("X-Forwarded-For");
//        if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
//            ip = request.getHeaders().getFirst("x-forwarded-for");
//            if (ip != null && ip.length() != 0 && !UNKNOWN.equalsIgnoreCase(ip)) {
//                // 多次反向代理后会有多个ip值，第一个ip才是真实ip
//                if (ip.contains(IP_UTILS_FLAG)) {
//                    ip = ip.split(IP_UTILS_FLAG)[0];
//                }
//            }
//        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("X-Real-IP");
        }
        //兼容k8s集群获取ip
        if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
            if (LOCALHOST_IP1.equalsIgnoreCase(ip) || LOCALHOST_IP.equalsIgnoreCase(ip)) {
                //根据网卡取本机配置的IP
                InetAddress iNet = null;
                try {
                    iNet = InetAddress.getLocalHost();
                    ip = iNet.getHostAddress();
                } catch (UnknownHostException e) {
                    log.error("getClientIp error:{}", e.getMessage());
                }
            }
        }

        return ip;
    }


    public static String getIp(HttpServletRequest request) {
        String originUserIp = request.getHeader(HeaderKeyConstants.FEIGN_X_USER_IP);
        if (!StrUtil.isEmpty(originUserIp)) {
            return originUserIp;
        } else {
            return getClientIpAddr(request);
        }
    }

}
