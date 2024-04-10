package dev.huskuraft.effortless.api.gui.button;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import dev.huskuraft.effortless.api.gui.AbstractButton;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;

public class Button extends AbstractButton {

    public static final int QUARTER_WIDTH = Dimens.Buttons.QUARTER_WIDTH;
    public static final int HALF_WIDTH = Dimens.Buttons.HALF_WIDTH;
    public static final int FULL_WIDTH = Dimens.Buttons.FULL_WIDTH;

    public static final int TAB_WIDTH = Dimens.Buttons.TAB_WIDTH;

    public static final int DEFAULT_HEIGHT = Dimens.Buttons.HEIGHT;

    public static final int VERTICAL_PADDING = Dimens.Buttons.VERTICAL_SPACING;
    public static final int HORIZONTAL_PADDING = Dimens.Buttons.HORIZONTAL_SPACING;

    public static final int MARGIN = Dimens.Buttons.VERTICAL_PADDING;


    private OnPress onPress;

    public Button(Entrance entrance, int x, int y, int width, int height, Text message) {
        this(entrance, x, y, width, height, message, null);
    }

    public Button(Entrance entrance, int x, int y, int width, int height, Text message, OnPress onPress) {
        super(entrance, x, y, width, height, message);
        this.onPress = onPress;
    }

    public static Builder builder(Entrance entrance, Text text, OnPress onPress) {
        return new Builder(entrance, text, onPress);
    }

    protected void onPress() {
        if (onPress != null) {
            this.onPress.onPress(this);
        }
    }

    public void setOnPressListener(OnPress onPress) {
        this.onPress = onPress;
    }

    //	protected MutableComponent createNarrationMessage() {
//		return this.createNarration.createNarrationMessage(() -> {
//			return super.createNarrationMessage();
//		});
//	}
//
//	public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
//		this.defaultButtonNarrationText(narrationElementOutput);x
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

        public Builder setPos(int i, int j) {
            this.x = i;
            this.y = j;
            return this;
        }

        public Builder setWidth(int i) {
            this.width = i;
            return this;
        }

        public Builder setSize(int i, int j) {
            this.width = i;
            this.height = j;
            return this;
        }

        public Builder setBounds(int x, int y, int width, int height) {
            return this.setPos(x, y).setSize(width, height);
        }

        public Builder setBoundsGrid(int width, int height, float row, float col, float size) {
            return setBounds((int) (width / 2 - Button.QUARTER_WIDTH * 2 - HORIZONTAL_PADDING * 3 / 2 + col * 4 * (Button.QUARTER_WIDTH + HORIZONTAL_PADDING)), (int) (height - Button.DEFAULT_HEIGHT - Button.MARGIN - row * (DEFAULT_HEIGHT + VERTICAL_PADDING)), (int) (Button.QUARTER_WIDTH * size * 4 + HORIZONTAL_PADDING * (size * 4 - 1)), DEFAULT_HEIGHT);
        }

        public Builder setTooltip(Text tooltip) {
            this.tooltip = List.of(tooltip);
            return this;
        }

        public Builder setTooltip(List<Text> tooltip) {
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
