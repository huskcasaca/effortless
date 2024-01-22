package dev.huskuraft.effortless.api.platform;

public final class PlatformUtils {

    public static Platform.OperatingSystem getOS() {
        return Entrance.getInstance().getContentFactory().getOperatingSystem();
    }

}
