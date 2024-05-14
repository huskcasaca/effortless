package dev.huskuraft.effortless;

import java.util.Stack;

import dev.huskuraft.effortless.api.core.Interaction;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.InteractionType;
import dev.huskuraft.effortless.api.events.EventResult;
import dev.huskuraft.effortless.api.events.input.KeyRegistry;
import dev.huskuraft.effortless.api.events.lifecycle.ClientTick;
import dev.huskuraft.effortless.api.events.render.RegisterShader;
import dev.huskuraft.effortless.api.gui.Screen;
import dev.huskuraft.effortless.api.input.InputKey;
import dev.huskuraft.effortless.api.input.Keys;
import dev.huskuraft.effortless.api.input.OptionKeys;
import dev.huskuraft.effortless.api.platform.Client;
import dev.huskuraft.effortless.api.platform.ClientManager;
import dev.huskuraft.effortless.api.platform.Platform;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.renderer.Shaders;
import dev.huskuraft.effortless.renderer.BlockShaders;
import dev.huskuraft.effortless.renderer.opertaion.OperationsRenderer;
import dev.huskuraft.effortless.renderer.outliner.OutlineRenderer;
import dev.huskuraft.effortless.renderer.pattern.PatternRenderer;
import dev.huskuraft.effortless.renderer.tooltip.TooltipRenderer;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternScreen;
import dev.huskuraft.effortless.screen.settings.EffortlessSettingsScreen;
import dev.huskuraft.effortless.screen.structure.EffortlessBuildModeWheelScreen;
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
    public void pushPanel(Screen screen) {
        if (screen == null) {
            screenStack.clear();
        } else {
            screenStack.push(getRunningClient().getPanel());
        }
        getRunningClient().setPanel(screen);
    }

    @Override
    public void popPanel(Screen screen) {
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
        if (OptionKeys.KEY_ATTACK.getBinding().isDown() || OptionKeys.KEY_USE.getBinding().isDown() || OptionKeys.KEY_PICK_ITEM.getBinding().isDown()) {
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

        if (Keys.KEY_ESCAPE.getBinding().isKeyDown()) {
            getEntrance().getStructureBuilder().resetContextInteractions(getRunningClient().getPlayer());
        }

        if (EffortlessKeys.BUILD_MODE_RADIAL.getBinding().isDown()) {
            if (!(getRunningClient().getPanel() instanceof EffortlessBuildModeWheelScreen)) {
                new EffortlessBuildModeWheelScreen(getEntrance(), EffortlessKeys.BUILD_MODE_RADIAL).attach();
            }
        }
        if (EffortlessKeys.BUILD_MODE_SETTINGS.getBinding().consumeClick()) {

        }
        if (EffortlessKeys.PATTERN_SETTINGS.getBinding().consumeClick()) {
            new EffortlessPatternScreen(getEntrance()).attach();
        }
        if (EffortlessKeys.UNDO.getBinding().consumeClick()) {
            getEntrance().getStructureBuilder().undo(getRunningClient().getPlayer());
        }
        if (EffortlessKeys.REDO.getBinding().consumeClick()) {
            getEntrance().getStructureBuilder().redo(getRunningClient().getPlayer());
        }
        if (EffortlessKeys.SETTINGS.getBinding().consumeClick()) {
            new EffortlessSettingsScreen(getEntrance()).attach();
        }
        if (EffortlessKeys.TOGGLE_REPLACE.getBinding().consumeClick()) {
            getEntrance().getStructureBuilder().setBuildFeature(getRunningClient().getPlayer(), getEntrance().getStructureBuilder().getContext(getRunningClient().getPlayer()).replaceMode().next());
        }

        if (Platform.getInstance().isDevelopment()) {
            if (Keys.KEY_LEFT_CONTROL.getBinding().isKeyDown() && Keys.KEY_ENTER.getBinding().isKeyDown()) {
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
        if (getRunningClient().getPanel() != null && !(getRunningClient().getPanel() instanceof EffortlessBuildModeWheelScreen)) {
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
