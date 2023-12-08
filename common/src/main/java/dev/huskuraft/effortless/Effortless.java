package dev.huskuraft.effortless;

import dev.huskuraft.effortless.building.StructureBuilder;
import dev.huskuraft.effortless.config.ConfigManager;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.events.EventsRegistry;
import dev.huskuraft.effortless.networking.Channel;
import dev.huskuraft.effortless.networking.NetworkRegistry;
import dev.huskuraft.effortless.packets.AllPacketListener;

public abstract class Effortless extends Entrance {

    public static final String MOD_ID = "effortless";
    public static final Resource CHANNEL_ID = Resource.of("default_channel");
    public static final int COMPATIBILITY_VERSION = 1;
    protected static Effortless instance;
    private final EventsRegistry registry = new EventsRegistry();
    private final Channel<AllPacketListener> channel = new ActualServerChannel(this, Effortless.CHANNEL_ID);
    private final StructureBuilder structureBuilder = new ActualServerStructureBuilder(this);

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
