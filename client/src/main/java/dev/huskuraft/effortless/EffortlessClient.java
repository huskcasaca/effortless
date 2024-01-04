package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.core.ClientEntrance;
import dev.huskuraft.effortless.config.ClientConfigManager;

public abstract class EffortlessClient extends ClientEntrance {

    private final EffortlessClientEventsRegistry registry = new EffortlessClientEventsRegistry();

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
        return (EffortlessClient) instance;
    }

    public EffortlessClientChannel getChannel() {
        return channel;
    }

    public EffortlessClientEventsRegistry getEventRegistry() {
        return registry;
    }

    public EffortlessClientStructureBuilder getStructureBuilder() {
        return structureBuilder;
    }

    @Override
    public EffortlessClientManager getClientManager() {
        return clientManager;
    }

    public ClientConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public String getId() {
        return Effortless.MOD_ID;
    }

}
