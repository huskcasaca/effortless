package dev.huskuraft.effortless;

import dev.huskuraft.effortless.building.StructureBuilder;
import dev.huskuraft.effortless.config.ConfigManager;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.events.EventsRegistry;
import dev.huskuraft.effortless.networking.Channel;
import dev.huskuraft.effortless.networking.NetworkRegistry;
import dev.huskuraft.effortless.packets.AllPacketListener;

public abstract class Effortless extends Entrance {

    public static final String MOD_ID = "effortless";
    public static final int VERSION_NUMBER = 1;
    private static Effortless instance;

    private final EventsRegistry registry;
    private final EffortlessServerChannel channel;
    private final EffortlessServerStructureBuilder structureBuilder;

    protected Effortless() {
        instance = this;

        this.registry = new EventsRegistry();
        this.channel = new EffortlessServerChannel(this);
        this.structureBuilder = new EffortlessServerStructureBuilder(this);
    }

    public static Effortless getInstance() {
        return instance;
    }

    @Override
    public Channel<AllPacketListener> getChannel() {
        return channel;
    }

    @Override
    public EventsRegistry getEventRegistry() {
        return registry;
    }

    @Override
    public StructureBuilder getStructureBuilder() {
        return structureBuilder;
    }

    @Override
    public ConfigManager getConfigManager() {
        return null;
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

    protected void onRegisterNetwork(NetworkRegistry registry) {
        getEventRegistry().onRegisterNetwork().invoker().onRegisterNetwork(registry);
    }

}
