package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.text.Text;

import java.util.stream.Stream;

public abstract class Transformer {
    // TODO: 12/9/23
    //    private final boolean enabled;

    public abstract BatchOperation transform(TransformableOperation operation);

    public abstract Text getName();

    public abstract Transformers getType();

    public abstract Stream<Text> getSearchableTags();

    public abstract boolean isValid();

}