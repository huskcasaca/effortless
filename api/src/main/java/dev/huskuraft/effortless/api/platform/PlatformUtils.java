package dev.huskuraft.effortless.api.platform;

public final class PlatformUtils {

    public static OperatingSystem getOS() {
        return ContentFactory.getInstance().getOperatingSystem();
    }

    public static boolean isWindows() {
        return getOS() == OperatingSystem.WINDOWS;
    }

    public static boolean isMacOS() {
        return getOS() == OperatingSystem.MACOS;
    }

}
