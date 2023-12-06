package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.text.Text;

import java.util.stream.Stream;

public interface Transformer {
    // TODO: 12/9/23
    //    private final boolean enabled;

    BatchOperation transform(TransformableOperation operation);

    Text getName();

    Transformers getType();

    Stream<Text> getSearchableTags();

    boolean isValid();

}