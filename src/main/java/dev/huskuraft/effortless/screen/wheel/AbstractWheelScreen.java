package dev.huskuraft.effortless.screen.wheel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.universal.api.core.AxisDirection;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.core.ResourceLocation;
import dev.huskuraft.universal.api.gui.AbstractScreen;
import dev.huskuraft.universal.api.gui.tooltip.TooltipHelper;
import dev.huskuraft.universal.api.input.KeyBinding;
import dev.huskuraft.universal.api.math.MathUtils;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.renderer.RenderLayers;
import dev.huskuraft.universal.api.renderer.Renderer;
import dev.huskuraft.universal.api.text.ChatFormatting;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.universal.api.utils.ColorUtils;
import dev.huskuraft.effortless.building.Option;

public abstract class AbstractWheelScreen<S, B> extends AbstractScreen {

    public static final int ANIMATION_OFFSET_Y = 12;
    public static final int ANIMATION_TICKS = 4;
    private static final float FADE_SPEED = 0.5f;
    private static final int WATERMARK_TEXT_COLOR = 0x8d7f7f7f;
    private static final int DEFAULT_RADIAL_SLOTS = 12;
    private static final ColorState RADIAL_SLOT_COLOR_STATE = new ColorState(new Color(0f, 0f, 0f, 0.42f), new Color(0f, 0f, 0f, 0.42f), new Color(0.24f, 0.24f, 0.24f, 0.50f), new Color(0.36f, 0.36f, 0.36f, 0.64f), new Color(0.42f, 0.42f, 0.42f, 0.64f));
    private static final ColorState RADIAL_BUTTON_COLOR_STATE = RADIAL_SLOT_COLOR_STATE;
    private static final int WHITE_TEXT_COLOR = 0xFFFFFF;
    private static final double RING_INNER_EDGE = 32;
    private static final double RING_OUTER_EDGE = 67;
    private static final double CATEGORY_LINE_OUTER_EDGE = 36;
    private static final double TEXT_DISTANCE = 84;
    private static final double SECTION_OFFSET_X = 112;
    private static final double SECTION_OFFSET_Y = 0;
    private static final int BUTTON_WIDTH = 22;
    private static final int BUTTON_HEIGHT = 22;
    private static final double BUTTON_OFFSET_X = 26;
    private static final double BUTTON_OFFSET_Y = 26;
    private static final double TITLE_HEIGHT = 10;
    private static final int MIN_RADIAL_SIZE = 8;
    private static final float MOUSE_SCROLL_THRESHOLD = 2;
    private BiConsumer<Slot<S>, Boolean> radialSelectResponder;
    private BiConsumer<Button<B>, Boolean> radialOptionSelectResponder;
    private List<? extends Slot<S>> radialSlots = List.of();
    private List<? extends ButtonSet<B>> leftButtons = List.of();
    private List<? extends ButtonSet<B>> rightButtons = List.of();
    private Slot<S> hoveredSlot;
    private Button<B> hoveredButton;
    private Collection<? extends Slot<S>> selectedSlot = new HashSet<>();
    private float lastScrollOffset = 0;
    // TODO: 20/2/23 rename
    private float visibility = 1;
    private float animationTicks = 0;
    private float animationScaleTicks = 0;
    private boolean detached = false;

    public AbstractWheelScreen(Entrance entrance, Text text) {
        super(entrance, text);
    }

    protected static <T> Slot<T> slot(Object id, Text name, ResourceLocation icon, Color tintColor, T content) {
        return new Slot<>() {

            @Override
            public Object getId() {
                return id;
            }

            @Override
            public Text getDisplayName() {
                return name;
            }

            @Override
            public Text getDisplayCategory() {
                return null;
            }

            @Override
            public ResourceLocation getIcon() {
                return icon;
            }

            @Override
            public Color getTintColor() {
                return tintColor;
            }

            @Override
            public T getContent() {
                return content;
            }

        };
    }

    protected static <T> Button<T> button(Object id, Text name, Text category, Text summary, List<Text> description, ResourceLocation icon, T content, boolean activated) {
        return new Button<>() {

            @Override
            public Object getId() {
                return id;
            }

            @Override
            public Text getName() {
                return name;
            }

            @Override
            public Text getCategory() {
                return category;
            }

            @Override
            public Text getSummary() {
                return summary;
            }

            @Override
            public List<Text> getDescriptions() {
                return description;
            }

            @Override
            public ResourceLocation getIcon() {
                return icon;
            }

            @Override
            public Color getTintColor() {
                return null;
            }

            @Override
            public T getContent() {
                return content;
            }

            @Override
            public boolean isActivated() {
                return activated;
            }
        };
    }

    @SafeVarargs
    protected static <T> ButtonSet<T> buttonSet(Button<T>... entries) {
        return buttonSet(List.of(entries));
    }

    protected static <T> ButtonSet<T> buttonSet(List<? extends Button<T>> entries) {
        return new ButtonSet<>() {
            @Override
            public Text getDisplayName() {
                return null;
            }

            @Override
            public List<? extends Button<T>> getButtons() {
                return entries;
            }
        };
    }

    public static <T extends Option> Button<T> button(T option) {
        return button(option, false);
    }

    public static <T extends Option> Button<T> lazyButton(Supplier<Button<T>> supplier) {
        return new Button<>() {

            @Override
            public Object getId() {
                return supplier.get().getId();
            }

            @Override
            public Text getName() {
                return supplier.get().getName();
            }

            @Override
            public Text getCategory() {
                return supplier.get().getCategory();
            }

            @Override
            public Text getSummary() {
                return supplier.get().getSummary();
            }

            @Override
            public List<Text> getDescriptions() {
                return supplier.get().getDescriptions();
            }

            @Override
            public ResourceLocation getIcon() {
                return supplier.get().getIcon();
            }

            @Override
            public Color getTintColor() {
                return supplier.get().getTintColor();
            }

            @Override
            public T getContent() {
                return supplier.get().getContent();
            }

            @Override
            public boolean isActivated() {
                return supplier.get().isActivated();
            }
        };
    }

    public static <T extends Option> Button<T> button(T option, boolean activated) {
        return button(option, option.getNameText(), option.getCategoryText(), option.getTooltipText(), List.of(), option.getIcon(), option, activated);
    }

    public static <T extends Option> Button<T> button(T option, boolean activated, Text name, List<Text> description) {
        return button(option, name, option.getCategoryText(), option.getTooltipText(), description, option.getIcon(), option, activated);
    }

    protected abstract KeyBinding getAssignedKeyBinds();
    @Override
    public void init(int width, int height) {
        super.init(width, height);
        if (detached) {
            super.detach();
        }
    }

    @Override
    public boolean isPauseGame() {
        return false;
    }

    @Override
    public void onAnimateTick(float partialTick) {
        this.detached = !getAssignedKeyBinds().isKeyDown();
        this.animationTicks = Math.min(Math.max(animationTicks + (detached ? -1 : 1) * partialTick, 0), MAX_ANIMATION_TICKS);
        this.animationScaleTicks = Math.min(Math.max(animationScaleTicks + (detached ? -1 : 1) * partialTick, 0), MAX_ANIMATION_TICKS);
        if (detached && animationTicks == 0) {
            detach();
        }

    }

    @Override
    public void onCreate() {
    }

    private static int pack(int r, int g, int b, int alpha) {
        return alpha << 24 | r << 16 | g << 8 | b;
    }


    @Override
    public void renderWidgetBackground(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        if (isTransparentBackground() && getEntrance().getClient().isLoaded()) {
            renderer.renderGradientRect(0, 0, super.getWidth(), super.getHeight(), ColorUtils.setAlpha(0x101010, (int) (0xC0 * getAnimationFactor())), ColorUtils.setAlpha(0x101010, (int) (0xD0 * getAnimationFactor())));
        } else {
            renderer.setRsShaderColor(0.25F, 0.25F, 0.25F, getAnimationFactor());
            renderer.renderPanelBackgroundTexture(0, 0, 0F, 0F, getWidth(), getHeight());
            renderer.setRsShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {

        renderer.pushPose();
        renderer.translate(0, 0 * getAnimationScaleFactor() * ANIMATION_OFFSET_Y, 0);
        renderer.translate(getX() + getWidth() / 2f, getY() + getHeight() / 2f, 0);
        renderer.scale(MathUtils.lerp(getAnimationScaleFactor(), 0.92, 1));
        renderer.translate(-getX() - getWidth() / 2f, -getY() - getHeight() / 2f, 0);
        renderer.setRsShaderColor(1.0F, 1.0F, 1.0F, getAnimationScaleFactor());
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);
        hoveredSlot = null;
        hoveredButton = null;
        renderRadialButtonSets(renderer, mouseX, mouseY, leftButtons, AxisDirection.NEGATIVE);
        renderRadialButtonSets(renderer, mouseX, mouseY, rightButtons, AxisDirection.POSITIVE);
        renderRadialSlots(renderer, mouseX, mouseY, radialSlots);
        renderer.popPose();
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        var result = false;
        if (isActive() && isVisible()) {
            if (radialSelectResponder != null && hoveredSlot != null) {
                radialSelectResponder.accept(hoveredSlot, button == 0);
                playRadialMenuSound();
                result = true;
            }

            if (radialOptionSelectResponder != null && hoveredButton != null) {
                radialOptionSelectResponder.accept(hoveredButton, button == 0);
                playRadialMenuSound();
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean onMouseScrolled(double mouseX, double mouseY, double amountX, double amountY) {
        var sign = lastScrollOffset * amountY;
        if (sign < 0) {
            lastScrollOffset = 0;
        }
        lastScrollOffset += amountY;
        if (lastScrollOffset > MOUSE_SCROLL_THRESHOLD) {
            playRadialMenuSound();
            getEntrance().getStructureBuilder().setStructure(getPlayer(), getEntrance().getConfigStorage().getStructure(getEntrance().getStructureBuilder().getContext(getPlayer()).buildMode().previous()));
            lastScrollOffset = 0;
        } else if (lastScrollOffset < -MOUSE_SCROLL_THRESHOLD) {
            playRadialMenuSound();
            getEntrance().getStructureBuilder().setStructure(getPlayer(), getEntrance().getConfigStorage().getStructure(getEntrance().getStructureBuilder().getContext(getPlayer()).buildMode().next()));
            lastScrollOffset = 0;
        }
        return true;
    }

    public final void setRadialSelectResponder(BiConsumer<Slot<S>, Boolean> consumer) {
        this.radialSelectResponder = consumer;
    }

    public final void setRadialOptionSelectResponder(BiConsumer<Button<B>, Boolean> consumer) {
        this.radialOptionSelectResponder = consumer;
    }

    public final void setRadialSlots(List<Slot<S>> slots) {
        this.radialSlots = slots;
    }

    @SafeVarargs
    public final void setLeftButtons(ButtonSet<B>... options) {
        setLeftButtons(List.of(options));
    }

    public final void setLeftButtons(List<? extends ButtonSet<B>> options) {
        this.leftButtons = options;
    }

    @SafeVarargs
    public final void setRightButtons(ButtonSet<B>... options) {
        setRightButtons(List.of(options));
    }

    public final void setRightButtons(List<? extends ButtonSet<B>> options) {
        this.rightButtons = options;
    }

    @SafeVarargs
    public final void setSelectedSlots(Slot<S>... slots) {
        setSelectedSlots(Set.of(slots));
    }

    public final void setSelectedSlots(Collection<? extends Slot<S>> slots) {
        this.selectedSlot = slots;
    }

    public void setScaleAnimation(float partialTicks) {
        this.animationScaleTicks = partialTicks;
    }

    private void renderRadialSlots(Renderer renderer, int mouseX, int mouseY, List<? extends Slot<S>> slots) {

        double middleX = getWidth() / 2.0;
        double middleY = getHeight() / 2.0;

        var mouseCenterX = mouseX - middleX;
        var mouseCenterY = mouseY - middleY;

        var mouseRad = (MathUtils.atan2(mouseCenterY, mouseCenterX) + 2 * MathUtils.PI) % (2 * MathUtils.PI);

        var ringInnerEdge = RING_INNER_EDGE * 0.72f + RING_INNER_EDGE * visibility * 0.28f;
        var ringOuterEdge = RING_OUTER_EDGE * 0.72f + RING_OUTER_EDGE * visibility * 0.28f;
        var categoryOuterEdge = CATEGORY_LINE_OUTER_EDGE * 0.72f + CATEGORY_LINE_OUTER_EDGE * visibility * 0.28f;

        var innerGap = MathUtils.PI * 0.007; // gap between buttons in radians at inner edge
        var outerGap = innerGap * ringInnerEdge / ringOuterEdge; // gap between buttons in radians at outer edge
        var rad = 2.0 * MathUtils.PI / MathUtils.max(MIN_RADIAL_SIZE, slots.size());

        for (int i = 0; i < slots.size(); i++) {

            var slot = slots.get(i);
            var lRad = (i - 0.5) * rad - MathUtils.PI / 2.0;
            var rRad = (i + 0.5) * rad - MathUtils.PI / 2.0;

            var x1 = MathUtils.cos(lRad);
            var x2 = MathUtils.cos(rRad);
            var y1 = MathUtils.sin(lRad);
            var y2 = MathUtils.sin(rRad);

            var x1m1 = MathUtils.cos(lRad + innerGap) * ringInnerEdge;
            var x2m1 = MathUtils.cos(rRad - innerGap) * ringInnerEdge;
            var y1m1 = MathUtils.sin(lRad + innerGap) * ringInnerEdge;
            var y2m1 = MathUtils.sin(rRad - innerGap) * ringInnerEdge;

            var x1m2 = MathUtils.cos(lRad + outerGap) * ringOuterEdge;
            var x2m2 = MathUtils.cos(rRad - outerGap) * ringOuterEdge;
            var y1m2 = MathUtils.sin(lRad + outerGap) * ringOuterEdge;
            var y2m2 = MathUtils.sin(rRad - outerGap) * ringOuterEdge;

            var isActivated = selectedSlot.stream().anyMatch(obj -> Objects.equals(obj.getId(), slot.getId()));
            var isMouseInQuad = inTriangle(x1m1, y1m1, x2m2, y2m2, x2m1, y2m1, mouseCenterX, mouseCenterY) || inTriangle(x1m1, y1m1, x1m2, y1m2, x2m2, y2m2, mouseCenterX, mouseCenterY);
            var isHovered = (lRad <= mouseRad && mouseRad <= rRad || lRad <= mouseRad - 2 * MathUtils.PI && mouseRad - 2 * MathUtils.PI <= rRad) && isMouseInQuad;

            var color = RADIAL_SLOT_COLOR_STATE.defaultColor();
            if (isActivated) color = RADIAL_SLOT_COLOR_STATE.activedColor();
            if (isHovered) color = RADIAL_SLOT_COLOR_STATE.hoveredColor();
            if (isActivated && isHovered) color = RADIAL_SLOT_COLOR_STATE.activedHoveredColor();

            if (isHovered) {
                hoveredSlot = slot;
                var x = (x1 + x2) * 0.5;
                var y = (y1 + y2) * 0.5;

                int textX = (int) (x * TEXT_DISTANCE);
                int textY = (int) (y * TEXT_DISTANCE) - getTypeface().getLineHeight() / 2;

                var text = slot.getDisplayName();

                if (x <= -0.2) {
                    textX -= getTypeface().measureWidth(text);
                } else if (-0.2 <= x && x <= 0.2) {
                    textX -= getTypeface().measureWidth(text) / 2;
                }
                // FIXME: 27/9/23
                renderer.renderTextFromStart(getTypeface(), text, (int) middleX + textX, (int) middleY + textY, WHITE_TEXT_COLOR, true);
            }

            // background tint
            renderer.renderQuad((int) (middleX + x1m1), (int) (middleY + y1m1), (int) (middleX + x2m1), (int) (middleY + y2m1), (int) (middleX + x2m2), (int) (middleY + y2m2), (int) (middleX + x1m2), (int) (middleY + y1m2), 100, color.getRGB());

            // category line
            color = slot.getTintColor();

            var x1m3 = MathUtils.cos(lRad + innerGap) * categoryOuterEdge;
            var x2m3 = MathUtils.cos(rRad - innerGap) * categoryOuterEdge;
            var y1m3 = MathUtils.sin(lRad + innerGap) * categoryOuterEdge;
            var y2m3 = MathUtils.sin(rRad - innerGap) * categoryOuterEdge;

            renderer.renderQuad((int) (middleX + x1m1), (int) (middleY + y1m1), (int) (middleX + x2m1), (int) (middleY + y2m1), (int) (middleX + x2m3), (int) (middleY + y2m3), (int) (middleX + x1m3), (int) (middleY + y1m3), 200, color.getRGB());

            // icon
            if (slot.getIcon() != null) {
                var iconX = (x1 + x2) * 0.5 * (ringOuterEdge * 0.55 + 0.45 * ringInnerEdge);
                var iconY = (y1 + y2) * 0.5 * (ringOuterEdge * 0.55 + 0.45 * ringInnerEdge);

                renderer.pushPose();
                renderer.translate(0, 0, 300);
                renderer.renderTexture(slot.getIcon(), (int) MathUtils.round(middleX + iconX - 8), (int) MathUtils.round(middleY + iconY - 8), 16, 16, 0f, 0f, 18, 18, 18, 18);
                renderer.popPose();
            }

        }

    }

    private void renderRadialButtonSets(Renderer renderer, int mouseX, int mouseY, List<? extends ButtonSet<B>> buttonSets, AxisDirection direction) {

        double middleX = getWidth() / 2.0;
        double middleY = getHeight() / 2.0;

        var mouseCenterX = mouseX - middleX;
        var mouseCenterY = mouseY - middleY;

        for (var row = 0; row < buttonSets.size(); row++) {
            var buttonSet = buttonSets.get(row);
            var buttons = buttonSet.getButtons();

            for (int col = 0; col < buttons.size(); col++) {
                var button = buttons.get(col);

                var x = (SECTION_OFFSET_X + BUTTON_OFFSET_X * col) * direction.getStep();
                var y = (SECTION_OFFSET_Y + BUTTON_OFFSET_Y * (row - (buttonSets.size() - 1) / 2f)) * 1;

                var x1 = x - BUTTON_WIDTH / 2d;
                var y1 = y - BUTTON_HEIGHT / 2d;

                var x2 = x + BUTTON_WIDTH / 2d;
                var y2 = y + BUTTON_HEIGHT / 2d;

                var isActivated = button.isActivated();
                var isHovered = x1 <= mouseCenterX && x2 >= mouseCenterX && y1 <= mouseCenterY && y2 >= mouseCenterY;

                var color = RADIAL_BUTTON_COLOR_STATE.defaultColor();
                if (isActivated) color = RADIAL_BUTTON_COLOR_STATE.activedColor();
                if (isHovered) color = RADIAL_BUTTON_COLOR_STATE.hoveredColor();
                if (isActivated && isHovered) color = RADIAL_BUTTON_COLOR_STATE.activedHoveredColor();

                if (isHovered) {
                    hoveredButton = button;
                }

                renderer.renderRect(RenderLayers.GUI, (int) (middleX + x1), (int) (middleY + y1), (int) (middleX + x2), (int) (middleY + y2), color.getRGB(), 0);
                // icon
                if (button.getIcon() != null) {
                    renderer.pushPose();
                    var iconX = x;
                    var iconY = y;
                    renderer.translate((int) MathUtils.round(middleX + iconX - 8), (int) MathUtils.round(middleY + iconY - 8), 0);
                    renderer.renderTexture(button.getIcon(), 0, 0, 16, 16, 0f, 0f, 18, 18, 18, 18);
                    renderer.popPose();
                }

            }
        }
    }

    @Override
    public void renderWidgetOverlay(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidgetOverlay(renderer, mouseX, mouseY, deltaTick);

        if (hoveredButton != null) {
            var tooltip = new ArrayList<Text>();
            tooltip.add(hoveredButton.getCategory().withStyle(ChatFormatting.WHITE));
            tooltip.add(hoveredButton.getName().withStyle(ChatFormatting.GOLD));

            if (!hoveredButton.getDescriptions().isEmpty()) {
                tooltip.addAll(hoveredButton.getDescriptions());
            }
            if (!hoveredButton.getSummary().getString().isEmpty()) {
//                tooltip.add(Text.text("Click [Left Button] for Switch").withStyle(ChatFormatting.DARK_GRAY));
//                tooltip.add(Text.text("Click [Right Button] for More Configs").withStyle(ChatFormatting.DARK_GRAY));
                tooltip.add(Text.empty());
                tooltip.add(TooltipHelper.holdShiftForSummary());
                if (TooltipHelper.isSummaryButtonDown()) {
                    tooltip.add(Text.empty());
//                    tooltip.add(hoveredButton.getTooltip());
                    tooltip.addAll(TooltipHelper.wrapLines(getTypeface(), hoveredButton.getSummary().withStyle(ChatFormatting.GRAY)));
                }
            }
            renderer.renderTooltip(getTypeface(), tooltip, mouseX, mouseY);
        }
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    private Player getPlayer() {
        return getEntrance().getClient().getPlayer();
    }

    public static final int MAX_ANIMATION_TICKS = 4;

    private float getAnimationFactor() {
        var fac = 1f - Math.min(animationTicks, MAX_ANIMATION_TICKS) / MAX_ANIMATION_TICKS;
        if (detached) {
            return 1 - MathUtils.lerp((1 - fac) * (1 - fac), 1f, 0f);
        }
        return 1 - MathUtils.lerp(fac * fac, 0f, 1f);
    }

    private float getAnimationScaleFactor() {
        var fac = 1f - Math.min(animationScaleTicks, MAX_ANIMATION_TICKS) / MAX_ANIMATION_TICKS;
        if (detached) {
            return 1 - MathUtils.lerp((1 - fac) * (1 - fac), 1f, 0f);
        }
        return 1 - MathUtils.lerp(fac * fac, 0f, 1f);
    }

    private boolean inTriangle(double x1, double y1, double x2, double y2, double x3, double y3, double x, double y) {
        var ab = (x1 - x) * (y2 - y) - (x2 - x) * (y1 - y);
        var bc = (x2 - x) * (y3 - y) - (x3 - x) * (y2 - y);
        var ca = (x3 - x) * (y1 - y) - (x1 - x) * (y3 - y);
        return MathUtils.sign(ab) == MathUtils.sign(bc) && MathUtils.sign(bc) == MathUtils.sign(ca);
    }

    private void playRadialMenuSound() {
        getEntrance().getClient().getSoundManager().playButtonClickSound();
    }

    public interface Slot<T> {

        Object getId();

        Text getDisplayName();

        Text getDisplayCategory();

        ResourceLocation getIcon();

        Color getTintColor();

        T getContent();

    }

    public interface Button<T> {

        Object getId();

        Text getName();

        Text getCategory();

        Text getSummary();

        List<Text> getDescriptions();

        ResourceLocation getIcon();

        Color getTintColor();

        T getContent();

        boolean isActivated();

    }

    public interface ButtonSet<T> {

        Text getDisplayName();

        List<? extends Button<T>> getButtons();

    }

    record ColorState(Color disabledColor, Color defaultColor, Color hoveredColor, Color activedColor, Color activedHoveredColor) {
    }

}

