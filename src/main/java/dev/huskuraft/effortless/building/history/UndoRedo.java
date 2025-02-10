package dev.huskuraft.effortless.building.history;

import dev.huskuraft.effortless.building.Option;

public enum UndoRedo implements Option {
    UNDO("undo"),
    REDO("redo"),
    ;

    private final String name;

    UndoRedo(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategory() {
        return "history";
    }

}
