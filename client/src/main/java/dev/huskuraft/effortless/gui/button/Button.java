package dev.huskuraft.effortless.gui.button;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractButton;
import dev.huskuraft.effortless.text.Text;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Button extends AbstractButton {
    public static final int SMALL_WIDTH = 120;
    public static final int DEFAULT_WIDTH = 150;
    public static final int DEFAULT_HEIGHT = 20;
    protected final OnPress onPress;

    public Button(Entrance entrance, int i, int j, int k, int l, Text message, OnPress onPress) {
        super(entrance, i, j, k, l, message);
        this.onPress = onPress;
    }

    public static Builder builder(Entrance entrance, Text text, OnPress onPress) {
        return new Builder(entrance, text, onPress);
    }

    public void onPress() {
        this.onPress.onPress(this);
    }

//	protected MutableComponent createNarrationMessage() {
//		return this.createNarration.createNarrationMessage(() -> {
//			return super.createNarrationMessage();
//		});
//	}
//
//	public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
//		this.defaultButtonNarrationText(narrationElementOutput);
//	}

    public interface OnPress {
        void onPress(Button button);
    }

    public static class Builder {
        private final Text message;
        private final OnPress onPress;
        private final Entrance entrance;
        @Nullable
        private List<Text> tooltip = new ArrayList<>();
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;

        public Builder(Entrance entrance, Text text, OnPress onPress) {
            this.entrance = entrance;
            this.message = text;
            this.onPress = onPress;
        }

        public Builder pos(int i, int j) {
            this.x = i;
            this.y = j;
            return this;
        }

        public Builder width(int i) {
            this.width = i;
            return this;
        }

        public Builder size(int i, int j) {
            this.width = i;
            this.height = j;
            return this;
        }

        public Builder bounds(int i, int j, int k, int l) {
            return this.pos(i, j).size(k, l);
        }

        public Builder tooltip(Text tooltip) {
            this.tooltip = List.of(tooltip);
            return this;
        }

        public Builder tooltip(List<Text> tooltip) {
            this.tooltip = tooltip;
            return this;
        }


        public Button build() {
            var button = new Button(entrance, this.x, this.y, this.width, this.height, this.message, this.onPress);
            button.setTooltip(this.tooltip);
            return button;
        }
    }
}
