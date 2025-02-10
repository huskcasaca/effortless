package dev.huskuraft.effortless;

import java.util.Stack;

import dev.huskuraft.universal.api.core.Direction;
import dev.huskuraft.universal.api.core.Interaction;
import dev.huskuraft.universal.api.core.InteractionHand;
import dev.huskuraft.universal.api.core.InteractionType;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.events.EventResult;
import dev.huskuraft.universal.api.events.input.KeyRegistry;
import dev.huskuraft.universal.api.events.lifecycle.ClientTick;
import dev.huskuraft.universal.api.events.render.RegisterShader;
import dev.huskuraft.universal.api.gui.Screen;
import dev.huskuraft.universal.api.input.InputKey;
import dev.huskuraft.universal.api.input.Keys;
import dev.huskuraft.universal.api.input.OptionKeys;
import dev.huskuraft.universal.api.platform.Client;
import dev.huskuraft.universal.api.platform.ClientManager;
import dev.huskuraft.universal.api.platform.Platform;
import dev.huskuraft.universal.api.renderer.Renderer;
import dev.huskuraft.universal.api.renderer.Shaders;
import dev.huskuraft.universal.api.text.ChatFormatting;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.clipboard.SnapshotTransform;
import dev.huskuraft.effortless.renderer.BlockShaders;
import dev.huskuraft.effortless.renderer.opertaion.OperationsRenderer;
import dev.huskuraft.effortless.renderer.outliner.OutlineRenderer;
import dev.huskuraft.effortless.renderer.pattern.PatternRenderer;
import dev.huskuraft.effortless.renderer.tooltip.TooltipRenderer;
import dev.huskuraft.effortless.screen.clipboard.EffortlessClipboardScreen;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternScreen;
import dev.huskuraft.effortless.screen.settings.EffortlessSettingsScreen;
import dev.huskuraft.effortless.screen.structure.EffortlessStructureScreen;
import dev.huskuraft.effortless.screen.test.EffortlessTestScreen;

public final class EffortlessClientManager implements ClientManager {

    private final Stack<Screen> screenStack = new Stack<>();

    private final EffortlessClient entrance;
    private final TooltipRenderer tooltipRenderer;

    private final OperationsRenderer operationsRenderer;
    private final OutlineRenderer outlineRenderer;
    private final PatternRenderer patternRenderer;

    private Client client;

    private int interactionCooldown = 0;

    public EffortlessClientManager(EffortlessClient entrance) {
        this.entrance = entrance;
        this.tooltipRenderer = new TooltipRenderer(entrance);

        this.operationsRenderer = new OperationsRenderer(entrance);
        this.outlineRenderer = new OutlineRenderer();
        this.patternRenderer = new PatternRenderer(entrance);

        getEntrance().getEventRegistry().getRegisterKeysEvent().register(this::onRegisterKeys);
        getEntrance().getEventRegistry().getKeyInputEvent().register(this::onKeyInput);
        getEntrance().getEventRegistry().getInteractionInputEvent().register(this::onInteractionInput);

        getEntrance().getEventRegistry().getClientStartEvent().register(this::onClientStart);
        getEntrance().getEventRegistry().getClientTickEvent().register(this::onClientTick);

        getEntrance().getEventRegistry().getRenderGuiEvent().register(this::onRenderGui);
        getEntrance().getEventRegistry().getRenderWorldEvent().register(this::onRenderEnd);

        getEntrance().getEventRegistry().getRegisterShaderEvent().register(this::onRegisterShader);
    }

    private EffortlessClient getEntrance() {
        return entrance;
    }

    private Player getPlayer() {
        return getEntrance().getClient().getPlayer();
    }

    @Override
    public Client getRunningClient() {
        return client;
    }

    @Override
    public void setRunningClient(Client client) {
        this.client = client;
    }

    public OperationsRenderer getOperationsRenderer() {
        return operationsRenderer;
    }

    public OutlineRenderer getOutlineRenderer() {
        return outlineRenderer;
    }

    public PatternRenderer getPatternRenderer() {
        return patternRenderer;
    }

    @Override
    public void pushScreen(Screen screen) {
        if (screen == null) {
            screenStack.clear();
        } else {
            screenStack.push(getRunningClient().getPanel());
        }
        getRunningClient().setPanel(screen);
    }

    @Override
    public void popScreen(Screen screen) {
        if (getRunningClient().getPanel() != screen) {
            return;
        }
        if (screenStack.isEmpty()) {
            getRunningClient().setPanel(null);
        } else {
            getRunningClient().setPanel(screenStack.pop());
        }
    }

    public TooltipRenderer getTooltipRenderer() {
        return tooltipRenderer;
    }

    private void tickCooldown() {
        if (OptionKeys.KEY_ATTACK.getKeyBinding().isDown() || OptionKeys.KEY_USE.getKeyBinding().isDown() || OptionKeys.KEY_PICK_ITEM.getKeyBinding().isDown()) {
            return;
        }
        this.interactionCooldown = Math.max(0, this.interactionCooldown - 1);
    }

    private boolean isInteractionCooldown() {
        return this.interactionCooldown == 0;
    }

    private void setInteractionCooldown(int tick) {
        this.interactionCooldown = tick; // for single build speed
    }

    private void resetInteractionCooldown() {
        setInteractionCooldown(1);
    }


    public void onRegisterKeys(KeyRegistry keyRegistry) {
        for (var key : EffortlessKeys.values()) {
            keyRegistry.register(key);
        }
    }

    public void onKeyInput(InputKey key) {

        if (getRunningClient() == null) {
            return;
        }

        if (getRunningClient().getPlayer() == null) {
            return;
        }

        if (Keys.KEY_ESCAPE.isDown()) {
            getEntrance().getStructureBuilder().resetInteractions(getRunningClient().getPlayer());
        }

        if (EffortlessKeys.BUILD_MODE_RADIAL.getKeyBinding().isDown()) {
            if (!(getRunningClient().getPanel() instanceof EffortlessStructureScreen)) {
                new EffortlessStructureScreen(getEntrance(), EffortlessKeys.BUILD_MODE_RADIAL.getKeyBinding()).attach();
            }
        }
        if (EffortlessKeys.UNDO.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            getEntrance().getStructureBuilder().undo(getRunningClient().getPlayer());
        }
        if (EffortlessKeys.REDO.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            getEntrance().getStructureBuilder().redo(getRunningClient().getPlayer());
        }
        if (EffortlessKeys.SETTINGS.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            new EffortlessSettingsScreen(getEntrance()).attach();
        }
        if (EffortlessKeys.TOGGLE_CLIPBOARD.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            getEntrance().getStructureBuilder().setClipboard(getRunningClient().getPlayer(), getEntrance().getStructureBuilder().getContext(getRunningClient().getPlayer()).clipboard().toggled());
            getEntrance().getClient().getPlayer().sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.toggle_clipboard", getEntrance().getStructureBuilder().getContext(getRunningClient().getPlayer()).clipboard().getNameText().withStyle(ChatFormatting.GOLD))));
        }
        if (EffortlessKeys.TOGGLE_PATTERN.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            getEntrance().getStructureBuilder().setPattern(getRunningClient().getPlayer(), getEntrance().getStructureBuilder().getContext(getRunningClient().getPlayer()).pattern().toggled());
            getEntrance().getClient().getPlayer().sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.toggle_pattern", getEntrance().getStructureBuilder().getContext(getRunningClient().getPlayer()).pattern().getNameText().withStyle(ChatFormatting.GOLD))));
        }
        if (EffortlessKeys.TOGGLE_REPLACE.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            getEntrance().getStructureBuilder().setReplace(getRunningClient().getPlayer(), getEntrance().getStructureBuilder().getContext(getRunningClient().getPlayer()).replace().next());
            getEntrance().getClient().getPlayer().sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.toggle_replace", getEntrance().getStructureBuilder().getContext(getRunningClient().getPlayer()).replace().getNameText().withStyle(ChatFormatting.GOLD))));
        }

        if (EffortlessKeys.ROTATE_X.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.ROTATE_X);
        }

        if (EffortlessKeys.ROTATE_Y.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.ROTATE_Y);
        }

        if (EffortlessKeys.ROTATE_Z.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.ROTATE_Z);
        }

        if (EffortlessKeys.MOVE_BACKWARD.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            switch (Direction.fromYRot(getPlayer().getYRot())) {
                case NORTH ->
                        getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.INCREASE_Z);
                case SOUTH ->
                        getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.DECREASE_Z);
                case WEST ->
                        getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.INCREASE_X);
                case EAST ->
                        getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.DECREASE_X);
            }
        }

        if (EffortlessKeys.MOVE_FORWARD.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            switch (Direction.fromYRot(getPlayer().getYRot())) {
                case NORTH ->
                        getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.DECREASE_Z);
                case SOUTH ->
                        getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.INCREASE_Z);
                case WEST ->
                        getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.DECREASE_X);
                case EAST ->
                        getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.INCREASE_X);
            }
        }

        if (EffortlessKeys.MOVE_LEFT.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            switch (Direction.fromYRot(getPlayer().getYRot())) {
                case NORTH ->
                        getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.DECREASE_X);
                case SOUTH ->
                        getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.INCREASE_X);
                case WEST ->
                        getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.INCREASE_Z);
                case EAST ->
                        getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.DECREASE_Z);
            }
        }

        if (EffortlessKeys.MOVE_RIGHT.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            switch (Direction.fromYRot(getPlayer().getYRot())) {
                case NORTH ->
                        getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.INCREASE_X);
                case SOUTH ->
                        getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.DECREASE_X);
                case WEST ->
                        getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.DECREASE_Z);
                case EAST ->
                        getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.INCREASE_Z);
            }
        }

        if (EffortlessKeys.MOVE_UP.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.INCREASE_Y);
        }

        if (EffortlessKeys.MOVE_DOWN.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.DECREASE_Y);
        }

        if (EffortlessKeys.MIRROR_X.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.MIRROR_X);
        }

        if (EffortlessKeys.MIRROR_Y.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.MIRROR_Y);
        }

        if (EffortlessKeys.MIRROR_Z.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            getEntrance().getStructureBuilder().updateClipboard(getPlayer(), SnapshotTransform.MIRROR_Z);
        }

        if (EffortlessKeys.EDIT_CLIPBOARD.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            new EffortlessClipboardScreen(getEntrance()).attach();
        }

        if (EffortlessKeys.EDIT_PATTERN.getKeyBinding().consumeClick()) {
            getEntrance().getClient().getSoundManager().playButtonClickSound();
            new EffortlessPatternScreen(getEntrance()).attach();
        }

//        if (EffortlessKeys.EDIT_REPLACE.getKeyBinding().consumeClick()) {
//            getEntrance().getClient().getSoundManager().playButtonClickSound();
//        }

        if (Platform.getInstance().isDevelopment()) {
            if (Keys.KEY_LEFT_CONTROL.isDown() && Keys.KEY_ENTER.isDown()) {
                getEntrance().getClient().getSoundManager().playButtonClickSound();
                new EffortlessTestScreen(getEntrance()).attach();
            }
        }
    }

    public EventResult onInteractionInput(InteractionType type, InteractionHand hand) {

        if (getEntrance().getStructureBuilder().getContext(getRunningClient().getPlayer()).isDisabled()) {
            return EventResult.pass();
        }

        if (!isInteractionCooldown()) {
            return EventResult.interruptFalse();
        } else {
            resetInteractionCooldown();
        }

        var interaction = getEntrance().getClient().getLastInteraction();
        if (interaction != null && interaction.getTarget() == Interaction.Target.ENTITY) {
            return EventResult.interruptFalse();
        }

        return switch (type) {
            case ATTACK, USE_ITEM -> {
                yield getEntrance().getStructureBuilder().onPlayerInteract(getRunningClient().getPlayer(), type, hand);
            }
            case UNKNOWN -> EventResult.pass();
        };

    }

    public synchronized void onClientStart(Client client) {
        setRunningClient(client);
    }

    public void onClientTick(Client client, ClientTick.Phase phase) {
        switch (phase) {
            case START -> {
                tickCooldown();

                tooltipRenderer.tick();

                operationsRenderer.tick();
                outlineRenderer.tick();
                patternRenderer.tick();
            }
            case END -> {
            }
        }
    }

    public void onRenderGui(Renderer renderer, float deltaTick) {
        if (getRunningClient().getPanel() != null && !(getRunningClient().getPanel() instanceof EffortlessStructureScreen)) {
            return;
        }

        getTooltipRenderer().renderGuiOverlay(renderer, deltaTick);
    }

    public void onRenderEnd(Renderer renderer, float deltaTick) {
        patternRenderer.render(renderer, deltaTick);
        outlineRenderer.render(renderer, deltaTick);
        operationsRenderer.render(renderer, deltaTick);
    }

    public void onRegisterShader(RegisterShader.ShadersSink sink) {
        BlockShaders.TINTED_OUTLINE.register(sink);
        for (var value : Shaders.values()) {
            value.register(sink);
        }
    }
}
