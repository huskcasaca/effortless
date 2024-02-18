package dev.huskuraft.effortless.api.platform;

public final class PlatformUtils {

    public static OperatingSystem getOS() {
        return ContentFactory.getInstance().getOperatingSystem();
    }

}
