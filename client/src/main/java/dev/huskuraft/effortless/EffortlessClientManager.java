package dev.huskuraft.effortless;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.events.api.EventResult;
import dev.huskuraft.effortless.events.input.KeyRegistry;
import dev.huskuraft.effortless.gui.Screen;
import dev.huskuraft.effortless.input.InputKey;
import dev.huskuraft.effortless.platform.Client;
import dev.huskuraft.effortless.platform.ClientManager;
import dev.huskuraft.effortless.platform.SubtitleManager;
import dev.huskuraft.effortless.platform.VanillaKeys;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.renderer.opertaion.OperationsRenderer;
import dev.huskuraft.effortless.renderer.outliner.OutlineRenderer;
import dev.huskuraft.effortless.renderer.pattern.PatternRenderer;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternRadialScreen;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternSettingsScreen;
import dev.huskuraft.effortless.screen.structure.EffortlessModeRadialScreen;
import dev.huskuraft.effortless.screen.transformer.EffortlessRandomizerSettingsScreen;

import java.util.Stack;

final class EffortlessClientManager extends ClientManager {

    private final Stack<Screen> screenStack = new Stack<>();

    private final ClientEntrance entrance;
    private final SubtitleManager subtitleManager;

    private final OperationsRenderer operationsRenderer;
    private final OutlineRenderer outlineRenderer;
    private final PatternRenderer patternRenderer;

    private Client client;

    private int interactionCooldown = 0;

    public EffortlessClientManager(Entrance entrance) {
        this.entrance = (ClientEntrance) entrance;
        this.subtitleManager = new SubtitleManager(entrance);

        this.operationsRenderer = new OperationsRenderer();
        this.outlineRenderer = new OutlineRenderer();
        this.patternRenderer = new PatternRenderer();

        getEntrance().getEventRegistry().getRegisterKeysEvent().register(this::onRegisterKeys);
        getEntrance().getEventRegistry().getKeyInputEvent().register(this::onKeyInput);
        getEntrance().getEventRegistry().getInteractionInputEvent().register(this::onInteractionInput);

        getEntrance().getEventRegistry().getClientStartEvent().register(this::onClientStart);
        getEntrance().getEventRegistry().getClientTickEvent().register(this::onClientTick);

        getEntrance().getEventRegistry().getRenderGuiEvent().register(this::onRenderGui);
        getEntrance().getEventRegistry().getRenderWorldEvent().register(this::onRenderEnd);
    }

    private ClientEntrance getEntrance() {
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

    @Override
    public OperationsRenderer getOperationsRenderer() {
        return operationsRenderer;
    }

    @Override
    public OutlineRenderer getOutlineRenderer() {
        return outlineRenderer;
    }

    @Override
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

    @Override
    public SubtitleManager getSubtitleManager() {
        return subtitleManager;
    }


    private void tickCooldown() {
        if (VanillaKeys.KEY_ATTACK.getBinding().isDown() || VanillaKeys.KEY_USE.getBinding().isDown() || VanillaKeys.KEY_PICK_ITEM.getBinding().isDown()) {
            return;
        }
        this.interactionCooldown = Math.max(0, this.interactionCooldown - 1);
    }

    private void setInteractionCooldown(int tick) {
        this.interactionCooldown = tick; // for single build speed
    }

    private boolean isInteractionCooldown() {
        return this.interactionCooldown == 0;
    }

    private void resetInteractionCooldown() {
        setInteractionCooldown(1);
    }

    private void openModeRadialScreen() {
        if (!(getRunningClient().getPanel() instanceof EffortlessModeRadialScreen)) {
            new EffortlessModeRadialScreen(getEntrance(), EffortlessKeys.BUILD_MODE_RADIAL).attach();
        }
    }

    private void openPatternRadialScreen() {
        if (!(getRunningClient().getPanel() instanceof EffortlessPatternRadialScreen)) {
            new EffortlessPatternRadialScreen(getEntrance(), EffortlessKeys.PATTERN_RADIAL).attach();
        }
    }

    private void openPatternSettingsScreen() {
        new EffortlessPatternSettingsScreen(
                getEntrance(),
                pattern -> {
                    getEntrance().getStructureBuilder().setPattern(getEntrance().getClient().getPlayer(), Pattern.DISABLED);
                    getEntrance().getConfigManager().editConfig(config -> {
                        config.getPatternConfig().setPatternSettings(pattern);
                    });
                },
                getEntrance().getConfigManager().getConfig().getPatternConfig().getPatternSettings()
        ).attach();
    }

    private void openRandomizerSettingsScreen() {
        new EffortlessRandomizerSettingsScreen(
                getEntrance(),
                settings -> {
                    getEntrance().getConfigManager().editConfig(config -> {
                        config.getTransformerConfig().setItemRandomizerSettings(settings);
                    });
                },
                getEntrance().getConfigManager().getConfig().getTransformerConfig().getRandomizerSettings()
        ).attach();
    }

    private void openSettingsScreen() {

    }

    public void onRegisterKeys(KeyRegistry keyRegistry) {
        for (var key : EffortlessKeys.values()) {
            keyRegistry.register(key);
        }
    }

    public void onKeyInput(InputKey key) {

        if (getRunningClient().getPlayer() == null)
            return;
        if (EffortlessKeys.BUILD_MODE_RADIAL.getBinding().isDown()) {
            openModeRadialScreen();
        }
        if (EffortlessKeys.PATTERN_RADIAL.getBinding().isDown()) {
            openPatternRadialScreen();
        }
        if (EffortlessKeys.BUILD_MODE_SETTINGS.getBinding().consumeClick()) {
//            openModeSettings(client);
        }
        if (EffortlessKeys.PATTERN_SETTINGS.getBinding().consumeClick()) {
            openPatternSettingsScreen();
        }
        if (EffortlessKeys.UNDO.getBinding().consumeClick()) {
            getEntrance().getStructureBuilder().undo(getRunningClient().getPlayer());
        }
        if (EffortlessKeys.REDO.getBinding().consumeClick()) {
            getEntrance().getStructureBuilder().redo(getRunningClient().getPlayer());
        }
        if (EffortlessKeys.SETTINGS.getBinding().consumeClick()) {
            openSettingsScreen();
        }
        if (EffortlessKeys.TOGGLE_REPLACE.getBinding().consumeClick()) {
            getEntrance().getStructureBuilder().setBuildFeature(getRunningClient().getPlayer(), getEntrance().getStructureBuilder().getContext(getRunningClient().getPlayer()).replaceMode().next());
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
            case ATTACK -> {
                var result = getEntrance().getStructureBuilder().onPlayerBreak(getRunningClient().getPlayer());
                yield EventResult.interrupt(result.isSuccess());
            }
            case USE_ITEM -> {
                var result = getEntrance().getStructureBuilder().onPlayerPlace(getRunningClient().getPlayer());
                yield EventResult.interrupt(result.isSuccess());
            }
            case UNKNOWN -> {
                yield EventResult.pass();
            }
        };

    }

    public synchronized void onClientStart(Client client) {
        setRunningClient(client);
    }

    public synchronized void onClientStopping(Client client) {
        setRunningClient(null);
    }

    public void onClientTick(Client client, TickPhase phase) {
        switch (phase) {
            case START -> {
                tickCooldown();

                subtitleManager.tick();

                operationsRenderer.tick();
                outlineRenderer.tick();
                patternRenderer.tick();
            }
            case END -> {
            }
        }
    }

    public void onRenderGui(Renderer renderer, float deltaTick) {
        getSubtitleManager().renderGuiOverlay(renderer, deltaTick);
    }

    public void onRenderEnd(Renderer renderer, float deltaTick) {
        patternRenderer.render(renderer, deltaTick);
        outlineRenderer.render(renderer, deltaTick);
        operationsRenderer.render(renderer, deltaTick);


    }
}
