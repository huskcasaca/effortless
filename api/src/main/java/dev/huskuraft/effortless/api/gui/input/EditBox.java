package dev.huskuraft.effortless.api.gui.input;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.RenderLayers;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;

public class EditBox extends AbstractWidget {
    private static final int BACKWARDS = -1;
    private static final int FORWARDS = 1;
    private static final int DEFAULT_TEXT_COLOR = 0x00e0e0e0;
    private static final int CURSOR_INSERT_WIDTH = 1;
    private static final int CURSOR_INSERT_COLOR = 0xffd0d0d0;
    private static final String CURSOR_APPEND_CHARACTER = "_";
    private static final int BORDER_COLOR_FOCUSED = 0xffffffff;
    private static final int BORDER_COLOR_INACTIVE = 0xff4f4f4f;
    private static final int BORDER_COLOR = 0xffa0a0a0;
    private static final int BACKGROUND_COLOR = 0xff000000;
    private String value;
    private int maxLength;
    private int frame;
    private boolean bordered;
    private boolean canLoseFocus;
    private boolean isEditable;
    private boolean shiftPressed;
    private int displayPos;
    private int cursorPos;
    private int highlightPos;
    private int textColor;
    private int textColorUneditable;
    @Nullable
    private String suggestion;
    @Nullable
    private Consumer<String> responder;
    private Predicate<String> filter;
    private BiFunction<String, Integer, String> formatter;
    @Nullable
    private Text hint;

    public EditBox(Entrance entrance, int x, int y, int width, int height, Text message) {
        this(entrance, x, y, width, height, null, message);
    }

    public EditBox(Entrance entrance, int x, int y, int width, int height, @Nullable EditBox editBox, Text message) {
        super(entrance, x, y, width, height, message);
        this.value = "";
        this.maxLength = Integer.MAX_VALUE;
        this.bordered = true;
        this.canLoseFocus = true;
        this.isEditable = true;
        this.textColor = DEFAULT_TEXT_COLOR;
        this.textColorUneditable = 7368816;
        this.filter = Objects::nonNull;
        this.formatter = (string, integer) -> string;
        if (editBox != null) {
            this.setValue(editBox.getValue());
        }

    }

    // FIXME: 22/10/23 move to platform
    public static String filterText(String string) {
        return filterText(string, false);
    }

    public static String filterText(String string, boolean bl) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] var3 = string.toCharArray();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            char c = var3[var5];
            if (isAllowedChatCharacter(c)) {
                stringBuilder.append(c);
            } else if (bl && c == '\n') {
                stringBuilder.append(c);
            }
        }

        return stringBuilder.toString();
    }

    public static boolean isAllowedChatCharacter(char c) {
        return c != 167 && c >= ' ' && c != 127;
    }

//	protected MutableComponent createNarrationMessage() {
//		Component component = this.getMessage();
//		return Component.translatable("gui.narrate.editBox", component, this.value);
//	}

    public static int offsetByCodepoints(String string, int i, int j) {
        int k = string.length();
        int l;
        if (j >= 0) {
            for (l = 0; i < k && l < j; ++l) {
                if (Character.isHighSurrogate(string.charAt(i++)) && i < k && Character.isLowSurrogate(string.charAt(i))) {
                    ++i;
                }
            }
        } else {
            for (l = j; i > 0 && l < 0; ++l) {
                --i;
                if (Character.isLowSurrogate(string.charAt(i)) && i > 0 && Character.isHighSurrogate(string.charAt(i - 1))) {
                    --i;
                }
            }
        }

        return i;
    }

    public void setResponder(Consumer<String> consumer) {
        this.responder = consumer;
    }

    public void setFormatter(BiFunction<String, Integer, String> biFunction) {
        this.formatter = biFunction;
    }

    public void tick() {
        ++this.frame;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String string) {
        if (this.filter.test(string)) {
            if (string.length() > this.maxLength) {
                this.value = string.substring(0, this.maxLength);
            } else {
                this.value = string;
            }

            this.moveCursorToEnd();
            this.setHighlightPos(this.cursorPos);
            this.onValueChange(string);
        }
    }

    public String getHighlighted() {
        int i = MathUtils.min(this.cursorPos, this.highlightPos);
        int j = MathUtils.max(this.cursorPos, this.highlightPos);
        return this.value.substring(i, j);
    }

    public void setFilter(Predicate<String> predicate) {
        this.filter = predicate;
    }

    private void onValueChange(String string) {
        if (this.responder != null) {
            this.responder.accept(string);
        }
    }

    private void deleteText(int i) {
        if (getEntrance().getClient().getWindow().isControlDown()) {
            this.deleteWords(i);
        } else {
            this.deleteChars(i);
        }
    }

    public void deleteWords(int i) {
        if (!this.value.isEmpty()) {
            if (this.highlightPos != this.cursorPos) {
                this.insertText("");
            } else {
                this.deleteChars(this.getWordPosition(i) - this.cursorPos);
            }
        }
    }

    public void deleteChars(int i) {
        if (!this.value.isEmpty()) {
            if (this.highlightPos != this.cursorPos) {
                this.insertText("");
            } else {
                int j = this.getCursorPos(i);
                int k = MathUtils.min(j, this.cursorPos);
                int l = MathUtils.max(j, this.cursorPos);
                if (k != l) {
                    String string = (new StringBuilder(this.value)).delete(k, l).toString();
                    if (this.filter.test(string)) {
                        this.value = string;
                        this.moveCursorTo(k);
                    }
                }
            }
        }
    }

    public int getWordPosition(int i) {
        return this.getWordPosition(i, this.getCursorPosition());
    }

    private int getWordPosition(int i, int j) {
        return this.getWordPosition(i, j, true);
    }

    private int getWordPosition(int i, int j, boolean bl) {
        int k = j;
        boolean bl2 = i < 0;
        int l = MathUtils.abs(i);

        for (int m = 0; m < l; ++m) {
            if (!bl2) {
                int n = this.value.length();
                k = this.value.indexOf(32, k);
                if (k == -1) {
                    k = n;
                } else {
                    while (bl && k < n && this.value.charAt(k) == ' ') {
                        ++k;
                    }
                }
            } else {
                while (bl && k > 0 && this.value.charAt(k - 1) == ' ') {
                    --k;
                }

                while (k > 0 && this.value.charAt(k - 1) != ' ') {
                    --k;
                }
            }
        }

        return k;
    }

    public void moveCursor(int i) {
        this.moveCursorTo(this.getCursorPos(i));
    }

    public void moveCursorTo(int i) {
        this.setCursorPosition(i);
        if (!this.shiftPressed) {
            this.setHighlightPos(this.cursorPos);
        }

        this.onValueChange(this.value);
    }

    public void moveCursorToStart() {
        this.moveCursorTo(0);
    }

    public void moveCursorToEnd() {
        this.moveCursorTo(this.value.length());
    }

    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        var client = getEntrance().getClient();
        if (!this.canConsumeInput()) {
            return false;
        } else {
            this.shiftPressed = client.getWindow().isShiftDown();
            if (client.getWindow().isSelectAll(keyCode)) {
                this.moveCursorToEnd();
                this.setHighlightPos(0);
                return true;
            } else {
                if (client.getWindow().isCopy(keyCode)) {
                    client.setClipboard(getHighlighted());
                    return true;
                } else {
                    if (client.getWindow().isPaste(keyCode)) {
                        if (this.isEditable) {
                            this.insertText(client.getClipboard());
                        }

                        return true;
                    } else {
                        if (client.getWindow().isCut(keyCode)) {
                            client.setClipboard(getHighlighted());
                            if (this.isEditable) {
                                this.insertText("");
                            }

                            return true;
                        } else {
                            return switch (keyCode) {
                                case GLFW.GLFW_KEY_BACKSPACE -> {
                                    if (this.isEditable) {
                                        this.shiftPressed = false;
                                        this.deleteText(BACKWARDS);
                                        this.shiftPressed = client.getWindow().isShiftDown();
                                    }
                                    yield true;
                                }
                                case GLFW.GLFW_KEY_DELETE -> {
                                    if (this.isEditable) {
                                        this.shiftPressed = false;
                                        this.deleteText(FORWARDS);
                                        this.shiftPressed = client.getWindow().isShiftDown();
                                    }
                                    yield true;
                                }
                                case GLFW.GLFW_KEY_RIGHT -> {
                                    if (client.getWindow().isControlDown()) {
                                        this.moveCursorTo(this.getWordPosition(FORWARDS));
                                    } else {
                                        this.moveCursor(FORWARDS);
                                    }
                                    yield true;
                                }
                                case GLFW.GLFW_KEY_LEFT -> {
                                    if (client.getWindow().isControlDown()) {
                                        this.moveCursorTo(this.getWordPosition(BACKWARDS));
                                    } else {
                                        this.moveCursor(BACKWARDS);
                                    }
                                    yield true;
                                }
                                case GLFW.GLFW_KEY_HOME -> {
                                    this.moveCursorToStart();
                                    yield true;
                                }
                                case GLFW.GLFW_KEY_END -> {
                                    this.moveCursorToEnd();
                                    yield true;
                                }
                                default -> false;
                            };
                        }
                    }
                }
            }
        }
    }

    public boolean canConsumeInput() {
        return this.isVisible() && this.isFocused() && this.isEditable() && this.isActive();
    }

    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isVisible() || !this.isActive()) {
            this.setFocus(false);
            return false;
        } else {
            boolean isMouseOver = isMouseOver(mouseX, mouseY);
            if (this.canLoseFocus) {
                this.setFocus(isMouseOver);
            }

            if (this.isFocused() && isMouseOver && button == 0) {
                var j = (int) MathUtils.floor(mouseX) - this.getX();
                if (this.bordered) {
                    j -= 4;
                }

                String string = getTypeface().subtractByWidth(this.value.substring(this.displayPos), this.getInnerWidth(), false);
                this.moveCursorTo(getTypeface().subtractByWidth(string, j, false).length() + this.displayPos);
                return true;
            } else {
                return false;
            }
        }
    }

    public void setFocus(boolean bl) {
        this.setFocused(bl);
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);

        int k;
        if (this.isBordered()) {
            k = this.isActive() ? this.isFocused() ? BORDER_COLOR_FOCUSED : BORDER_COLOR : BORDER_COLOR_INACTIVE;
            renderer.renderRect(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), k);
            renderer.renderRect(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1, BACKGROUND_COLOR);
        }

        k = this.isActive() && this.isEditable ? this.textColor : this.textColorUneditable;
        int l = this.cursorPos - this.displayPos;
        int m = this.highlightPos - this.displayPos;
        String string = getTypeface().subtractByWidth(this.value.substring(this.displayPos), this.getInnerWidth(), false);
        boolean bl = l >= 0 && l <= string.length();
        boolean bl2 = this.isFocused() && this.frame / 6 % 2 == 0 && bl;
        int n = this.bordered ? this.getX() + 4 : this.getX();
        int o = this.bordered ? this.getY() + (this.getHeight() - 8) / 2 : this.getY();
        int p = n;
        if (m > string.length()) {
            m = string.length();
        }

        if (!string.isEmpty()) {
            String string2 = bl ? string.substring(0, l) : string;
            p = renderer.renderTextFromStart(getTypeface(), this.formatter.apply(string2, this.displayPos), n, o, k, true);
        }

        boolean bl3 = this.cursorPos < this.value.length() || this.value.length() >= this.getMaxLength();
        int q = p;
        if (!bl) {
            q = l > 0 ? n + this.getWidth() : n;
        } else if (bl3) {
            q = p - 1;
            --p;
        }

        if (!string.isEmpty() && bl && l < string.length()) {
            renderer.renderTextFromStart(getTypeface(), this.formatter.apply(string.substring(l), this.cursorPos), p, o, k, true);
        }

        if (this.hint != null && string.isEmpty() && !this.isFocused()) {
            renderer.renderTextFromStart(getTypeface(), this.hint, p, o, k, true);
        }

        if (!bl3 && this.suggestion != null) {
            renderer.renderTextFromStart(getTypeface(), this.suggestion, q - 1, o, -8355712, true);
        }

        int var10002;
        int var10003;
        int var10004;
        if (bl2) {
            if (bl3) {
                var10002 = o - 1;
                var10003 = q + 1;
                var10004 = o + 1;
                renderer.renderRect(q, var10002, var10003, var10004 + 9, CURSOR_INSERT_COLOR);
            } else {
                renderer.renderTextFromStart(getTypeface(), CURSOR_APPEND_CHARACTER, q, o, k, true);
            }
        }

        if (m != l) {
            int r = n + getTypeface().measureWidth(string.substring(0, m));
            var10002 = o - 1;
            var10003 = r - 1;
            var10004 = o + 1;
            Objects.requireNonNull(getTypeface());
            this.renderHighlight(renderer, q, var10002, var10003, var10004 + 9);
        }

    }

    private void renderHighlight(Renderer renderer, int i, int j, int k, int l) {
        int m;
        if (i < k) {
            m = i;
            i = k;
            k = m;
        }

        if (j < l) {
            m = j;
            j = l;
            l = m;
        }

        if (k > this.getX() + this.getWidth()) {
            k = this.getX() + this.getWidth();
        }

        if (i > this.getX() + this.getWidth()) {
            i = this.getX() + this.getWidth();
        }

        renderer.renderRect(RenderLayers.GUI_TEXT_HIGHLIGHT, i, j, k, l, 0xFF0000FF);
    }

    private int getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength(int i) {
        this.maxLength = i;
        if (this.value.length() > i) {
            this.value = this.value.substring(0, i);
            this.onValueChange(this.value);
        }

    }

    public int getCursorPosition() {
        return this.cursorPos;
    }

    public void setCursorPosition(int i) {
        this.cursorPos = MathUtils.clamp(i, 0, this.value.length());
    }

    private boolean isBordered() {
        return this.bordered;
    }

    public void setBordered(boolean bl) {
        this.bordered = bl;
    }

    public void setTextColor(int i) {
        this.textColor = i;
    }

    public void setTextColorUneditable(int i) {
        this.textColorUneditable = i;
    }

    public boolean onFocusMove(boolean forwards) {
        return super.isVisible() && this.isEditable && super.onFocusMove(forwards);
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return super.isVisible() && mouseX >= (double) this.getX() && mouseX < (double) (this.getX() + this.getWidth()) && mouseY >= (double) this.getY() && mouseY < (double) (this.getY() + this.getHeight());
    }

    protected void onFocusedChanged(boolean bl) {
        if (bl) {
            this.frame = 0;
        }

    }

    private boolean isEditable() {
        return this.isEditable;
    }

    public void setEditable(boolean bl) {
        this.isEditable = bl;
    }

    public int getInnerWidth() {
        return this.isBordered() ? this.getWidth() - 8 : this.getWidth();
    }

    public void setHighlightPos(int i) {
        int j = this.value.length();
        this.highlightPos = MathUtils.clamp(i, 0, j);
        if (getTypeface() != null) {
            if (this.displayPos > j) {
                this.displayPos = j;
            }

            int k = this.getInnerWidth();
            String string = getTypeface().subtractByWidth(this.value.substring(this.displayPos), k, false);
            int l = string.length() + this.displayPos;
            if (this.highlightPos == this.displayPos) {
                this.displayPos -= getTypeface().subtractByWidth(this.value, k, true).length();
            }

            if (this.highlightPos > l) {
                this.displayPos += this.highlightPos - l;
            } else if (this.highlightPos <= this.displayPos) {
                this.displayPos -= this.displayPos - this.highlightPos;
            }

            this.displayPos = MathUtils.clamp(this.displayPos, 0, j);
        }

    }

    public void setCanLoseFocus(boolean bl) {
        this.canLoseFocus = bl;
    }

    public boolean isVisible() {
        return super.isVisible();
    }

    public void setVisible(boolean bl) {
        super.setVisible(bl);
    }

    public void setSuggestion(@Nullable String string) {
        this.suggestion = string;
    }

//	public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
//		narrationElementOutput.add(NarratedElementType.TITLE, (Component)this.createNarrationMessage());
//	}

    public int getScreenX(int i) {
        return i > this.value.length() ? this.getX() : this.getX() + getTypeface().measureWidth(this.value.substring(0, i));
    }

    public void setHint(Text text) {
        this.hint = text;
    }

    public void insertText(String string) {
        int i = MathUtils.min(this.cursorPos, this.highlightPos);
        int j = MathUtils.max(this.cursorPos, this.highlightPos);
        int k = this.maxLength - this.value.length() - (i - j);
        String string2 = filterText(string);
        int l = string2.length();
        if (k < l) {
            string2 = string2.substring(0, k);
            l = k;
        }

        String string3 = (new StringBuilder(this.value)).replace(i, j, string2).toString();
        if (this.filter.test(string3)) {
            this.value = string3;
            this.setCursorPosition(i + l);
            this.setHighlightPos(this.cursorPos);
            this.onValueChange(this.value);
        }
    }

    private int getCursorPos(int i) {
        return offsetByCodepoints(this.value, this.cursorPos, i);
    }

    public boolean onCharTyped(char character, int modifiers) {
        if (!this.canConsumeInput()) {
            return false;
        } else if (isAllowedChatCharacter(character)) {
            if (this.isEditable) {
                this.insertText(Character.toString(character));
            }

            return true;
        } else {
            return false;
        }
    }
}
