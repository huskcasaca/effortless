package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.platform.ClientEntrance;

public abstract class EffortlessClient implements ClientEntrance {

    private final EffortlessClientEventRegistry eventRegistry = new EffortlessClientEventRegistry();
    private final EffortlessClientChannel channel;
    private final EffortlessClientStructureBuilder structureBuilder;
    private final EffortlessClientManager clientManager;
    private final EffortlessClientConfigManager configManager;

    protected EffortlessClient() {
        Instance.set(this);

        this.channel = new EffortlessClientChannel(this);
        this.structureBuilder = new EffortlessClientStructureBuilder(this);
        this.clientManager = new EffortlessClientManager(this);
        this.configManager = new EffortlessClientConfigManager(this);
    }

    public static EffortlessClient getInstance() {
        return (EffortlessClient) Instance.get();
    }

    public EffortlessClientChannel getChannel() {
        return channel;
    }

    public EffortlessClientEventRegistry getEventRegistry() {
        return eventRegistry;
    }

    public EffortlessClientStructureBuilder getStructureBuilder() {
        return structureBuilder;
    }

    @Override
    public EffortlessClientManager getClientManager() {
        return clientManager;
    }

    public EffortlessClientConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public String getId() {
        return Effortless.MOD_ID;
    }

}
