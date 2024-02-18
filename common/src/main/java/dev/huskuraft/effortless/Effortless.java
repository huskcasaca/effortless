package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.platform.Entrance;

public abstract class Effortless implements Entrance {

    public static final String MOD_ID = "effortless";
    public static final int PROTOCOL_VERSION = 3;

    private final EffortlessEventRegistry eventRegistry;
    private final EffortlessChannel channel;
    private final EffortlessStructureBuilder structureBuilder;

    protected Effortless() {
        Instance.set(this);

        this.eventRegistry = new EffortlessEventRegistry();
        this.channel = new EffortlessChannel(this);
        this.structureBuilder = new EffortlessStructureBuilder(this);
    }

    public EffortlessEventRegistry getEventRegistry() {
        return eventRegistry;
    }

    public EffortlessChannel getChannel() {
        return channel;
    }

    public EffortlessStructureBuilder getStructureBuilder() {
        return structureBuilder;
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

}
