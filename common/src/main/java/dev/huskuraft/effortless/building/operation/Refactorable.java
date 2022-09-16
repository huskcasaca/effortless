package dev.huskuraft.effortless.building.operation;

import dev.huskuraft.effortless.building.pattern.RefactorContext;

public interface Refactorable<O> extends Trait {

    O refactor(RefactorContext source);

}
