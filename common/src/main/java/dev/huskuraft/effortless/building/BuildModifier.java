package dev.huskuraft.effortless.building;

public interface BuildModifier {

    boolean isIntermediate();

    BuildStage getStage();

}