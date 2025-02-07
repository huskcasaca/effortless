package dev.ftb.mods.ftbteams.net;

import dev.architectury.networking.NetworkManager;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.TeamMessage;
import dev.ftb.mods.ftbteams.client.gui.MyTeamScreen;
import dev.ftb.mods.ftbteams.data.ClientTeam;
import dev.ftb.mods.ftbteams.data.ClientTeamManagerImpl;
import dev.ftb.mods.ftbteams.data.TeamMessageImpl;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public record SyncMessageHistoryMessage(List<TeamMessage> messages) implements CustomPacketPayload {
    public static final Type<SyncMessageHistoryMessage> TYPE = new Type<>(FTBTeamsAPI.rl("sync_msg_history"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncMessageHistoryMessage> STREAM_CODEC = StreamCodec.composite(
            TeamMessageImpl.STREAM_CODEC.apply(ByteBufCodecs.list()), SyncMessageHistoryMessage::messages,
            SyncMessageHistoryMessage::new
    );

    public static SyncMessageHistoryMessage forTeam(Team team) {
        return new SyncMessageHistoryMessage(team.getMessageHistory());
    }

    public static void handle(SyncMessageHistoryMessage message, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientTeam team = ClientTeamManagerImpl.getInstance().selfTeam();
            if (team != null) {
                team.setMessageHistory(message.messages);
                MyTeamScreen.refreshIfOpen();
            }
        });
    }

    @Override
    public Type<SyncMessageHistoryMessage> type() {
        return TYPE;
    }
}
