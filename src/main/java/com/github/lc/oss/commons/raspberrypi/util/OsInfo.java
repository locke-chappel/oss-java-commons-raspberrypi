package com.github.lc.oss.commons.raspberrypi.util;

public class OsInfo {
    private static final String UNKNOWN = "unknown";

    private static String os;
    private static String arch;

    public static boolean isLinux() {
        if (OsInfo.os == null) {
            OsInfo.os = System.getProperty("os.name");
            if (OsInfo.os == null || OsInfo.os.trim().equals("")) {
                OsInfo.os = OsInfo.UNKNOWN;
            }
            OsInfo.os = OsInfo.os.toLowerCase();
        }
        return "linux".equals(OsInfo.os);
    }

    public static boolean isArm() {
        if (OsInfo.arch == null) {
            OsInfo.arch = System.getProperty("os.arch");
            if (OsInfo.arch == null || OsInfo.arch.trim().equals("")) {
                OsInfo.arch = OsInfo.UNKNOWN;
            }
            OsInfo.arch = OsInfo.arch.toLowerCase();
        }
        return "arm".equals(OsInfo.arch);
    }

    public static boolean isRaspbian() {
        return OsInfo.isLinux() && OsInfo.isArm();
    }

    private OsInfo() {
    }
}
