package dev.huskuraft.effortless.building;

public enum TracingResult {
    SUCCESS_FULFILLED,
    SUCCESS_PARTIAL,
    PASS,
    FAILED;

    public boolean isSuccess() {
        return this == SUCCESS_FULFILLED || this == SUCCESS_PARTIAL;
    }
}
