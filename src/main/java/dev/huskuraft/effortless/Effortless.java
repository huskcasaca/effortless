package dev.huskuraft.effortless;

import dev.huskuraft.universal.api.platform.PlatformLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auto.service.AutoService;

import dev.huskuraft.universal.api.events.impl.EventRegistry;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.ChatFormatting;
import dev.huskuraft.universal.api.text.Text;

@AutoService(Entrance.class)
public class Effortless implements Entrance {

    public static final String MOD_ID = "effortless";
    public static final String DEFAULT_CHANNEL = "default";
    public static final int PROTOCOL_VERSION = 13;
    public static final Logger LOGGER = LoggerFactory.getLogger(Effortless.class.getName());

    private final EventRegistry eventRegistry = PlatformLoader.getSingleton(EventRegistry.class);
    private final EffortlessNetworkChannel networkChannel = new EffortlessNetworkChannel(this);
    private final EffortlessStructureBuilder structureBuilder = new EffortlessStructureBuilder(this);
    private final EffortlessConfigStorage sessionConfigStorage = new EffortlessConfigStorage(this);
    private final EffortlessSessionManager sessionManager = new EffortlessSessionManager(this);
    private final EffortlessServerManager serverManager = new EffortlessServerManager(this);

    public static Effortless getInstance() {
        return (Effortless) Entrance.getInstance();
    }

    public static Text getSystemMessage(Text msg) {
        return Text.text("[").append(Text.translate("effortless.symbol")).append("] ").withStyle(ChatFormatting.GRAY).append(msg.withStyle(ChatFormatting.WHITE));
    }

    public static Text getMessage(Text msg) {
        return msg;
    }

    public EventRegistry getEventRegistry() {
        return eventRegistry;
    }

    public EffortlessNetworkChannel getChannel() {
        return networkChannel;
    }

    public EffortlessStructureBuilder getStructureBuilder() {
        return structureBuilder;
    }

    public EffortlessConfigStorage getSessionConfigStorage() {
        return sessionConfigStorage;
    }

    public EffortlessSessionManager getSessionManager() {
        return sessionManager;
    }

    public EffortlessServerManager getServerManager() {
        return serverManager;
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

}
