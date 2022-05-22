package javaweb.remember.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Time     : 2022-05-21 19:51:56
 * Author   : 王豪
 * E-mail   : 1964085132@qq.com
 * Remarks  : 从HttpServletRequest中获取ip地址
 * File     : IpAddress.java
 * Project  : I-Remember
 * Software : IntelliJ IDEA
 * Copyright © 2022 王豪. All rights reserved.
 */
public class GetIpAddress {
    /**
     * 获取客户端ip地址
     * @param request Http请求
     * @return ip地址或者false
     */
    public static String getIp(HttpServletRequest request){
        String[] ipAgent = new String[]{
                "X-Real-IP",            // nginx服务代理
                "X-Forwarded-For",      // Squid 服务代理
                "Proxy-Client-IP",      // apache 服务代理
                "WL-Proxy-Client-IP",   // weblogic 服务代理
                "HTTP_CLIENT_IP"       // 有些服务代理
        };
        String ip = null;
        String ipAddresses = null;
        int i = 0;
        // 依次按照代理获取ip地址
        // 要先按照代理来获取，如果有代理但直接获取getRemoteAddr就会获取到代理的ip，127.0.0.1
        while(i < ipAgent.length && (null == ipAddresses || 0 == ipAddresses.length() || "unknown".equals(ipAddresses))){
            ipAddresses = request.getHeader(ipAgent[i]);
            i++;
        }
        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }
        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (null == ip || 0 == ip.length() || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        // 还是获取不到就返回false
        if (null == ip || 0 == ip.length() || "unknown".equalsIgnoreCase(ipAddresses)){
            ip = "false";
        }
        return ip;
    }


}
