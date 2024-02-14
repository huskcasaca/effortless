package dev.huskuraft.effortless.api.platform;

public record PlatformVersion(
        int major,
        int minor,
        int patch
) {

    public static final PlatformVersion UNAVAILABLE = new PlatformVersion(-1, -1, -1);
}
