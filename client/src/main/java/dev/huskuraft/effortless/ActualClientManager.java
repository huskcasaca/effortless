package dev.huskuraft.effortless;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.core.ClientEntrance;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.TickPhase;
import dev.huskuraft.effortless.gui.Screen;
import dev.huskuraft.effortless.input.KeyRegistry;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternRadialScreen;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternSettingsScreen;
import dev.huskuraft.effortless.screen.randomizer.EffortlessRandomizerSettingsScreen;
import dev.huskuraft.effortless.screen.structure.EffortlessModeRadialScreen;
import dev.huskuraft.effortless.platform.Client;
import dev.huskuraft.effortless.platform.ClientManager;
import dev.huskuraft.effortless.platform.SubtitleManager;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.renderer.opertaion.OperationsRenderer;
import dev.huskuraft.effortless.renderer.outliner.OutlineRenderer;
import dev.huskuraft.effortless.renderer.transformer.TransformerRenderer;

import java.util.Stack;

final class ActualClientManager extends ClientManager {

    private final Stack<Screen> screenStack = new Stack<>();

    private final ClientEntrance entrance;
    private final SubtitleManager subtitleManager;

    private final OperationsRenderer operationsRenderer;
    private final OutlineRenderer outlineRenderer;
    private final TransformerRenderer transformerRenderer;

    private Client client;

    public ActualClientManager(Entrance entrance) {
        this.entrance = (ClientEntrance) entrance;
        this.subtitleManager = new SubtitleManager(entrance);

        this.operationsRenderer = new OperationsRenderer();
        this.outlineRenderer = new OutlineRenderer();
        this.transformerRenderer = new TransformerRenderer();

        getEntrance().getEventRegistry().onRegisterKeys().register(this::onRegisterKeys);
        getEntrance().getEventRegistry().onKeyPress().register(this::onKeyPress);

        getEntrance().getEventRegistry().onClientStart().register(this::onClientStart);
        getEntrance().getEventRegistry().onClientTick().register(this::onClientTick);

        getEntrance().getEventRegistry().onRenderGui().register(this::onRenderGui);
        getEntrance().getEventRegistry().onRenderWorld().register(this::onRenderEnd);
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
    public TransformerRenderer getTransformerRenderer() {
        return transformerRenderer;
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

    private void openModeRadialScreen() {
        if (!(getRunningClient().getPanel() instanceof EffortlessModeRadialScreen)) {
            new EffortlessModeRadialScreen(getEntrance(), ActualKeys.BUILD_MODE_RADIAL).attach();
        }
    }

    private void openPatternRadialScreen() {
        if (!(getRunningClient().getPanel() instanceof EffortlessPatternRadialScreen)) {
            new EffortlessPatternRadialScreen(getEntrance(), ActualKeys.PATTERN_RADIAL).attach();
        }
    }

    private void openPatternSettingsScreen() {
        new EffortlessPatternSettingsScreen(
                getEntrance(),
                pattern -> {
                    getEntrance().getStructureBuilder().setPattern(getRunningClient().getPlayer(), Pattern.DISABLED);
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
        for (var key : ActualKeys.values()) {
            keyRegistry.register(key);
        }
    }

    public void onKeyPress(int key, int scanCode, int action, int modifiers) {

        if (getRunningClient().getPlayer() == null)
            return;
        if (ActualKeys.BUILD_MODE_RADIAL.isDown()) {
            openModeRadialScreen();
        }
        if (ActualKeys.PATTERN_RADIAL.isDown()) {
            openPatternRadialScreen();
        }
        if (ActualKeys.BUILD_MODE_SETTINGS.consumeClick()) {
//            openModeSettings(client);
        }
        if (ActualKeys.PATTERN_SETTINGS.consumeClick()) {
            openPatternSettingsScreen();
        }
        if (ActualKeys.UNDO.consumeClick()) {

        }
        if (ActualKeys.REDO.consumeClick()) {

        }
        if (ActualKeys.SETTINGS.consumeClick()) {
            openSettingsScreen();
        }
        if (ActualKeys.TOGGLE_REPLACE.consumeClick()) {
//            cycleReplaceMode(player);
        }
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
                subtitleManager.tick();

                operationsRenderer.tick();
                outlineRenderer.tick();
                transformerRenderer.tick();
            }
            case END -> {
            }
        }
    }

    public void onRenderGui(Renderer renderer, float deltaTick) {
        getSubtitleManager().renderGuiOverlay(renderer, deltaTick);
    }

    public void onRenderEnd(Renderer renderer, float deltaTick) {

        outlineRenderer.render(renderer, deltaTick);
        operationsRenderer.render(renderer, deltaTick);
        transformerRenderer.render(renderer, deltaTick);
        renderer.draw();

    }
}
