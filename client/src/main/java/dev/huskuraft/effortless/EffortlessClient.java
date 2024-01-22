package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.platform.ClientEntrance;
import dev.huskuraft.effortless.api.platform.ClientContentFactory;
import dev.huskuraft.effortless.config.ClientConfigManager;

public abstract class EffortlessClient implements ClientEntrance {

    private final EffortlessClientEventsRegistry registry = new EffortlessClientEventsRegistry();

    private final EffortlessClientChannel channel;
    private final EffortlessClientConfigManager configManager;
    private final EffortlessClientStructureBuilder structureBuilder;
    private final EffortlessClientManager clientManager;

    protected EffortlessClient() {
        Instance.set(this);

        this.channel = new EffortlessClientChannel(this);
        this.configManager = new EffortlessClientConfigManager(this);
        this.structureBuilder = new EffortlessClientStructureBuilder(this);
        this.clientManager = new EffortlessClientManager(this);
    }

    public static EffortlessClient getInstance() {
        return (EffortlessClient) Instance.get();
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
    public ClientContentFactory getContentFactory() {
        return null;
    }

    @Override
    public String getId() {
        return Effortless.MOD_ID;
    }

}
