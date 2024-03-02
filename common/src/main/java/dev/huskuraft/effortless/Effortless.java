package dev.huskuraft.effortless;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.events.EventRegistry;
import dev.huskuraft.effortless.api.networking.NetworkChannel;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.platform.PlatformLoader;

@AutoService(Entrance.class)
public class Effortless implements Entrance {

    public static final String MOD_ID = "effortless";
    public static final int PROTOCOL_VERSION = 3;

    private final EventRegistry eventRegistry = PlatformLoader.getSingleton();
    private final EffortlessNetworkChannel networkChannel = new EffortlessNetworkChannel(this);
    private final EffortlessStructureBuilder structureBuilder = new EffortlessStructureBuilder(this);
    private final EffortlessSessionConfigStorage sessionConfigStorage = new EffortlessSessionConfigStorage(this);

    public Effortless() {

    }

    public static Effortless getInstance() {
        return (Effortless) Entrance.getInstance();
    }

    public EventRegistry getEventRegistry() {
        return eventRegistry;
    }

    public NetworkChannel getChannel() {
        return networkChannel;
    }

    public EffortlessStructureBuilder getStructureBuilder() {
        return structureBuilder;
    }

    public EffortlessSessionConfigStorage getSessionConfigStorage() {
        return sessionConfigStorage;
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

}
