package dev.huskuraft.effortless.api.gui;

public interface EntryList extends Widget {

    void moveUp(Widget widget);

    void moveDown(Widget widget);

    void moveUpNoClamp(Widget widget);

    void moveDownNoClamp(Widget widget);

    boolean isEditable();

    Entry getSelected();

    interface Entry extends Widget {

        void onPositionChange(int from, int to);

        void onSelected();

        void onDeselected();

    }
}
