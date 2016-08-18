package com.zhaimi.koper.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author caie
 */
public class SysUtil {

    /**
     * 获取本机IP
     *
     * @return 本机IP
     */
    public static String getHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取当前进程ID
     *
     * @return 进程ID
     */
    public static Long getPid() {
        final String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        final String pId = processName.substring(0, processName.indexOf('@'));
        return Long.valueOf(pId);
    }

}
