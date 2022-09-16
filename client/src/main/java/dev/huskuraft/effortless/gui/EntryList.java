package dev.huskuraft.effortless.gui;

public interface EntryList extends ContainerWidget {

    void moveUp(Widget widget);

    void moveDown(Widget widget);

    boolean isEditable();

}
