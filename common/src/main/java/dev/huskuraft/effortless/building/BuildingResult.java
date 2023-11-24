package dev.huskuraft.effortless.building;

public enum BuildingResult {
    COMPLETED,
    PARTIAL,
    CANCELED; // state inconsistent or become idle

    public boolean isSuccess() {
        return this == COMPLETED || this == PARTIAL;
    }

}
