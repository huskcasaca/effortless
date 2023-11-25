package dev.huskuraft.effortless.gui;

public interface EntryList {

    void moveUp(Widget widget);

    void moveDown(Widget widget);

    boolean isEditable();

    Entry getSelected();

}
