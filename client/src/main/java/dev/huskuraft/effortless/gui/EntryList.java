package dev.huskuraft.effortless.gui;

public interface EntryList {

    void moveUp(Widget widget);

    void moveDown(Widget widget);

    void moveUpNoClamp(Widget widget);

    void moveDownNoClamp(Widget widget);

    boolean isEditable();

    Entry getSelected();

}
