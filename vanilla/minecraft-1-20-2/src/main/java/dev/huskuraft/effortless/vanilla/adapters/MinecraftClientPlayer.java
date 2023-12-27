package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.GameMode;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;

public class MinecraftClientPlayer extends MinecraftPlayer {

    public MinecraftClientPlayer(Player player) {
        super(player);
    }

    @Override
    public GameMode getGameType() {
        if (getRef() instanceof AbstractClientPlayer localPlayer) {
            return switch (localPlayer.getPlayerInfo().getGameMode()) {
                case SURVIVAL -> GameMode.SURVIVAL;
                case CREATIVE -> GameMode.CREATIVE;
                case ADVENTURE -> GameMode.ADVENTURE;
                case SPECTATOR -> GameMode.SPECTATOR;
            };
        }
        return null;
    }

    @Override
    public BlockInteraction raytrace(double maxDistance, float deltaTick, boolean includeFluids) {
        return (BlockInteraction) MinecraftBasicTypes.fromMinecraftInteraction(getRef().pick(maxDistance, deltaTick, includeFluids));
    }

}
