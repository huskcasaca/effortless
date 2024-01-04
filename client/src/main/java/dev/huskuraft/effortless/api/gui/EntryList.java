package dev.huskuraft.effortless.api.gui;

public interface EntryList {

    void moveUp(Widget widget);

    void moveDown(Widget widget);

    void moveUpNoClamp(Widget widget);

    void moveDownNoClamp(Widget widget);

    boolean isEditable();

    Entry getSelected();

    interface Entry {

        void onPositionChange(int from, int to);

        void onSelected();

        void onDeselected();

    }
}
