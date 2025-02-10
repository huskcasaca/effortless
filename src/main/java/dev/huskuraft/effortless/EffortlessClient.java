package dev.huskuraft.effortless;

import com.google.auto.service.AutoService;

import dev.huskuraft.universal.api.events.impl.ClientEventRegistry;
import dev.huskuraft.universal.api.platform.ClientEntrance;
import dev.huskuraft.universal.api.platform.PlatformLoader;

@AutoService(ClientEntrance.class)
public class EffortlessClient implements ClientEntrance {

    private final ClientEventRegistry eventRegistry = PlatformLoader.getSingleton(ClientEventRegistry.class);
    private final EffortlessClientNetworkChannel channel = new EffortlessClientNetworkChannel(this);
    private final EffortlessClientStructureBuilder structureBuilder = new EffortlessClientStructureBuilder(this);
    private final EffortlessClientManager clientManager = new EffortlessClientManager(this);
    private final EffortlessClientTagConfigStorage tagConfigStorage = new EffortlessClientTagConfigStorage(this);
    private final EffortlessClientConfigStorage configStorage = new EffortlessClientConfigStorage(this);
    private final EffortlessClientSessionManager sessionManager = new EffortlessClientSessionManager(this);

    public static EffortlessClient getInstance() {
        return (EffortlessClient) ClientEntrance.getInstance();
    }

    public ClientEventRegistry getEventRegistry() {
        return eventRegistry;
    }

    public EffortlessClientNetworkChannel getChannel() {
        return channel;
    }

    public EffortlessClientStructureBuilder getStructureBuilder() {
        return structureBuilder;
    }

    @Override
    public EffortlessClientManager getClientManager() {
        return clientManager;
    }

    @Deprecated
    public EffortlessClientTagConfigStorage getTagConfigStorage() {
        return tagConfigStorage;
    }

    public EffortlessClientConfigStorage getConfigStorage() {
        return configStorage;
    }

    public EffortlessClientSessionManager getSessionManager() {
        return sessionManager;
    }

    @Override
    public String getId() {
        return Effortless.MOD_ID;
    }

}
