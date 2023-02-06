package com.acimage.common.global.context;

import com.acimage.common.global.enums.TokenStatus;


public class UserContext {
    private static final ThreadLocal<Long> userId = new ThreadLocal<>();
    private static final ThreadLocal<String> username = new ThreadLocal<>();
    private static final ThreadLocal<String> photoUrl = new ThreadLocal<>();
    private static final ThreadLocal<TokenStatus> tokenStatus = new ThreadLocal<>();
    private static final ThreadLocal<String> ip = new ThreadLocal<>();

    public static void saveCurrentUserInfo(Long userId, String username, String photoUrl) {
        UserContext.userId.set(userId);
        UserContext.username.set(username);
        UserContext.photoUrl.set(photoUrl);
    }

    /**
     * 移除登录用户信息，在拦截器方法afterCompletion中，应移除当前用户对象
     */
    public static void remove() {
        userId.remove();
        username.remove();
        photoUrl.remove();
        tokenStatus.remove();
        ip.remove();
    }

    public static void setTokenStatus(TokenStatus tokenStatus) {
        UserContext.tokenStatus.set(tokenStatus);
    }
    public static void setUserId(Long userId){
        UserContext.userId.set(userId);
    }
    public static void setUsername(String username){
        UserContext.username.set(username);
    }

    public static void setIp(String ip) {
        UserContext.ip.set(ip);
    }

    public static Long getUserId() {
        return userId.get();
    }

    public static String getUsername() {
        return username.get();
    }

    public static String getPhotoUrl() {
        return photoUrl.get();
    }

    public static TokenStatus getTokenStatus() {
        return tokenStatus.get();
    }

    public static String getIp() {
        return ip.get();
    }
}
