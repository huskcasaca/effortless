package dev.huskuraft.effortless.config;

import dev.huskuraft.effortless.core.Position;
import dev.huskuraft.effortless.math.MathUtils;

public class PreviewConfiguration implements Configuration {

    public static final int MIN_SHADER_DISSOLVE_TIME_MULTIPLIER = 1;
    public static final int MAX_SHADER_DISSOLVE_TIME_MULTIPLIER = 40;

    private int itemUsagePosition = Position.RIGHT.ordinal();
    private int buildInfoPosition = Position.RIGHT.ordinal();
    private boolean alwaysShowBlockPreview = false;
    private int shaderDissolveTimeMultiplier = 10;

    public PreviewConfiguration() {
    }

    public boolean isShowingBuildInfo() {
        return buildInfoPosition != Position.DISABLED.ordinal();
    }

    public Position getBuildInfoPosition() {
        return Position.values()[buildInfoPosition];
    }

    public void setBuildInfoPosition(Position position) {
        this.buildInfoPosition = position.ordinal();
    }

    public boolean isShowItemUsage() {
        return itemUsagePosition != Position.DISABLED.ordinal();
    }

    public Position getItemUsagePosition() {
        return Position.values()[itemUsagePosition];
    }

    public void setItemUsagePosition(Position position) {
        this.itemUsagePosition = position.ordinal();
    }

    public boolean isAlwaysShowBlockPreview() {
        return alwaysShowBlockPreview;
    }

    public void setAlwaysShowBlockPreview(boolean alwaysShowBlockPreview) {
        this.alwaysShowBlockPreview = alwaysShowBlockPreview;
    }

    public int getShaderDissolveTimeMultiplier() {
        return shaderDissolveTimeMultiplier;
    }

    public void setShaderDissolveTimeMultiplier(int shaderDissolveTimeMultiplier) {
        this.shaderDissolveTimeMultiplier = shaderDissolveTimeMultiplier;
    }

    @Override
    public boolean isValid() {
        return shaderDissolveTimeMultiplier >= MIN_SHADER_DISSOLVE_TIME_MULTIPLIER &&
                shaderDissolveTimeMultiplier <= MAX_SHADER_DISSOLVE_TIME_MULTIPLIER;
    }

    @Override
    public void validate() {
        shaderDissolveTimeMultiplier = MathUtils.max(MIN_SHADER_DISSOLVE_TIME_MULTIPLIER, MathUtils.min(shaderDissolveTimeMultiplier, MAX_SHADER_DISSOLVE_TIME_MULTIPLIER));
    }
}
