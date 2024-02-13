package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.platform.Entrance;

public abstract class Effortless implements Entrance {

    public static final String MOD_ID = "effortless";
    public static final int VERSION_NUMBER = 2;

    private final EffortlessEventRegistry registry;
    private final EffortlessServerChannel channel;
    private final EffortlessServerStructureBuilder structureBuilder;

    protected Effortless() {
        Instance.set(this);

        this.registry = new EffortlessEventRegistry();
        this.channel = new EffortlessServerChannel(this);
        this.structureBuilder = new EffortlessServerStructureBuilder(this);
    }

    public EffortlessServerChannel getChannel() {
        return channel;
    }

    public EffortlessEventRegistry getEventRegistry() {
        return registry;
    }

    public EffortlessServerStructureBuilder getStructureBuilder() {
        return structureBuilder;
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

}
