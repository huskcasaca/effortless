package dev.huskuraft.effortless;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.events.CommonEventRegistry;
import dev.huskuraft.effortless.api.events.EventRegister;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;

@AutoService(Entrance.class)
public class Effortless implements Entrance {

    public static final String MOD_ID = "effortless";
    public static final int PROTOCOL_VERSION = 7;

    private final CommonEventRegistry commonEventRegistry = (CommonEventRegistry) EventRegister.getCommon();
    private final EffortlessNetworkChannel networkChannel = new EffortlessNetworkChannel(this);
    private final EffortlessStructureBuilder structureBuilder = new EffortlessStructureBuilder(this);
    private final EffortlessConfigStorage sessionConfigStorage = new EffortlessConfigStorage(this);
    private final EffortlessSessionManager sessionManager = new EffortlessSessionManager(this);
    private final EffortlessServerManager serverManager = new EffortlessServerManager(this);

    public static Effortless getInstance() {
        return (Effortless) Entrance.getInstance();
    }

    public static Text getSystemMessage(Text msg) {
        var id = ChatFormatting.GRAY + "[" + Text.translate("effortless.name") + "]" + ChatFormatting.RESET + " ";
        return Text.text(id + msg.getString());
    }

    public CommonEventRegistry getEventRegistry() {
        return commonEventRegistry;
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
