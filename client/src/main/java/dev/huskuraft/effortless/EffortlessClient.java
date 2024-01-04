package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.core.ClientEntrance;
import dev.huskuraft.effortless.api.platform.ClientManager;
import dev.huskuraft.effortless.building.StructureBuilder;
import dev.huskuraft.effortless.client.ClientEventsRegistry;
import dev.huskuraft.effortless.config.ClientConfigManager;
import dev.huskuraft.effortless.networking.Channel;
import dev.huskuraft.effortless.packets.AllPacketListener;

public abstract class EffortlessClient extends ClientEntrance {

    private static EffortlessClient instance;
    private final ClientEventsRegistry registry = new ClientEventsRegistry();

    private final EffortlessClientChannel channel;
    private final EffortlessClientConfigManager configManager;
    private final EffortlessClientStructureBuilder structureBuilder;
    private final EffortlessClientManager clientManager;

    protected EffortlessClient() {
        instance = this;

        this.channel = new EffortlessClientChannel(this);
        this.configManager = new EffortlessClientConfigManager(this);
        this.structureBuilder = new EffortlessClientStructureBuilder(this);
        this.clientManager = new EffortlessClientManager(this);
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

}
