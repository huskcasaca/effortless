package dev.huskuraft.effortless.screen.settings;

import java.util.function.Consumer;

import dev.huskuraft.effortless.api.gui.EntryList;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.container.AbstractEntryList;
import dev.huskuraft.effortless.api.gui.container.EditableEntryList;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;

public class SettingButtonsList extends AbstractEntryList<SettingButtonsList.Entry<?>> {

    public SettingButtonsList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    @Override
    public boolean isRenderSelection() {
        return false;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getWidth() / 2 + 160;
    }

    public Button addTab(Text title, Consumer<Button> consumer) {
        return addEntry(new ButtonEntry(getEntrance(), this, title, Button.TAB_WIDTH, consumer)).getButton();
    }

    private static final class ButtonEntry extends Entry<Void> {

        private Consumer<Button> consumer;
        private int width;
        private Button button;

        public ButtonEntry(Entrance entrance, EntryList entryList, Text title, int width, Consumer<Button> consumer) {
            super(entrance, entryList, null);
            setMessage(title);
            this.consumer = consumer;
            this.width = width;
        }

        @Override
        public void onCreate() {
            super.onCreate();

            this.button = addWidget(new Button(getEntrance(), getX(), getY(), getWidth(), Button.DEFAULT_HEIGHT, getMessage()));
            this.button.setOnPressListener(button -> {
                consumer.accept(button);
            });

        }

        public Button getButton() {
            return button;
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return 24;
        }

    }

    public abstract static class Entry<T> extends EditableEntryList.Entry<T> {

        protected Entry(Entrance entrance, T item) {
            super(entrance, item);
        }

        protected Entry(Entrance entrance, EntryList entryList, T item) {
            super(entrance, entryList, item);
        }
    }


}
