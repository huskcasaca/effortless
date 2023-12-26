package dev.huskuraft.effortless;

import dev.huskuraft.effortless.building.StructureBuilder;
import dev.huskuraft.effortless.config.ClientConfigManager;
import dev.huskuraft.effortless.core.ClientEntrance;
import dev.huskuraft.effortless.core.InteractionHand;
import dev.huskuraft.effortless.core.InteractionType;
import dev.huskuraft.effortless.core.TickPhase;
import dev.huskuraft.effortless.events.ClientEventsRegistry;
import dev.huskuraft.effortless.events.api.EventResult;
import dev.huskuraft.effortless.events.input.KeyRegistry;
import dev.huskuraft.effortless.input.InputKey;
import dev.huskuraft.effortless.networking.Channel;
import dev.huskuraft.effortless.networking.NetworkRegistry;
import dev.huskuraft.effortless.packets.AllPacketListener;
import dev.huskuraft.effortless.platform.Client;
import dev.huskuraft.effortless.platform.ClientManager;
import dev.huskuraft.effortless.renderer.Renderer;

public abstract class EffortlessClient extends ClientEntrance {

    private static EffortlessClient instance;
    private final ClientEventsRegistry registry = new ClientEventsRegistry();

    private final Channel<AllPacketListener> channel = new ActualClientChannel(this, Effortless.CHANNEL_ID);
    private final ClientConfigManager configManager = new ActualClientConfigManager(this);
    private final StructureBuilder structureBuilder = new ActualClientStructureBuilder(this);
    private final ClientManager clientManager = new ActualClientManager(this);

    protected EffortlessClient() {
        instance = this;
    }

    public static EffortlessClient getInstance() {
        return instance;
    }

    @Override
    public Channel<AllPacketListener> getChannel() {
        return channel;
    }

    @Override
    public ClientEventsRegistry getEventRegistry() {
        return registry;
    }

    @Override
    public StructureBuilder getStructureBuilder() {
        return structureBuilder;
    }

    @Override
    public ClientManager getClientManager() {
        return clientManager;
    }

    @Override
    public ClientConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public String getId() {
        return Effortless.MOD_ID;
    }

    protected void onRegisterNetwork(NetworkRegistry registry) {
        getEventRegistry().onRegisterNetwork().invoker().onRegisterNetwork(registry);
    }

    protected void onRegisterKeys(KeyRegistry keyRegistry) {
        getEventRegistry().onRegisterKeys().invoker().onRegisterKeys(keyRegistry);
    }

    protected void onClientStart(Client client) {
        getEventRegistry().onClientStart().invoker().onClientStart(client);
    }

    protected void onClientTick(Client client, TickPhase phase) {
        getEventRegistry().onClientTick().invoker().onClientTick(client, phase);
    }

    protected void onRenderGui(Renderer renderer, float deltaTick) {
        getEventRegistry().onRenderGui().invoker().onRenderGui(renderer, deltaTick);
    }

    protected void onRenderWorld(Renderer renderer, float deltaTick) {
        getEventRegistry().onRenderWorld().invoker().onRenderWorld(renderer, deltaTick);
    }

    protected void onKeyInput(InputKey key) {
        getEventRegistry().onKeyInput().invoker().onKeyInput(key);
    }

    protected EventResult onInteractionInput(InteractionType type, InteractionHand hand) {
        return getEventRegistry().onInteractionInput().invoker().onInteractionInput(type, hand);
    }

}
