package dev.ftb.mods.ftbteams.data;

import dev.ftb.mods.ftbteams.FTBTeamsAPIImpl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

public record PlayerPermissions(boolean createParty, boolean invitePlayer, boolean addAlly) {
    public static final StreamCodec<FriendlyByteBuf, PlayerPermissions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, PlayerPermissions::createParty,
            ByteBufCodecs.BOOL, PlayerPermissions::invitePlayer,
            ByteBufCodecs.BOOL, PlayerPermissions::addAlly,
            PlayerPermissions::new
    );

    public static PlayerPermissions forPlayer(ServerPlayer player) {
        return new PlayerPermissions(
                FTBTUtils.canPlayerUseCommand(player, "ftbteams.party.create")
                        && !FTBTeamsAPIImpl.INSTANCE.isPartyCreationFromAPIOnly(),
                FTBTUtils.canPlayerUseCommand(player, "ftbteams.party.invite"),
                FTBTUtils.canPlayerUseCommand(player, "ftbteams.party.allies.add")
        );
    }
}
