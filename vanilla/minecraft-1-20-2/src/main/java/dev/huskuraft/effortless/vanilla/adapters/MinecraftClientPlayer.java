package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.GameMode;
import dev.huskuraft.effortless.core.Player;
import net.minecraft.client.player.AbstractClientPlayer;

public class MinecraftClientPlayer extends MinecraftPlayer {

    MinecraftClientPlayer(net.minecraft.world.entity.player.Player player) {
        super(player);
    }

    public static Player fromMinecraftPlayer(net.minecraft.world.entity.player.Player player) {
        return new MinecraftClientPlayer(player);
    }

    public static net.minecraft.world.entity.player.Player toMinecraftPlayer(Player player) {
        return ((MinecraftClientPlayer) player).reference;
    }

    @Override
    public GameMode getGameType() {
        if (reference instanceof AbstractClientPlayer localPlayer) {
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
        return (BlockInteraction) MinecraftPlayer.fromMinecraftInteraction(reference.pick(maxDistance, deltaTick, includeFluids));
    }

}
