package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.GameMode;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;

class MinecraftClientPlayer extends MinecraftPlayer {

    MinecraftClientPlayer(Player player) {
        super(player);
    }

    @Override
    public GameMode getGameType() {
        if (getRef() instanceof AbstractClientPlayer localPlayer) {
            return MinecraftClientAdapter.adapt(localPlayer.getPlayerInfo().getGameMode());
        }
        return null;
    }

    @Override
    public BlockInteraction raytrace(double maxDistance, float deltaTick, boolean includeFluids) {
        return (BlockInteraction) MinecraftClientAdapter.adapt(getRef().pick(maxDistance, deltaTick, includeFluids));
    }

}
