package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.core.BlockData;
import dev.huskuraft.effortless.core.BlockPosition;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.core.World;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class MinecraftWorld extends World {

    private final Level reference;

    MinecraftWorld(Level reference) {
        this.reference = reference;
    }

    public static World fromMinecraftWorld(Level level) {
        return new MinecraftWorld(level);
    }

    public static Level toMinecraftWorld(World world) {
        return ((MinecraftWorld) world).reference;
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return MinecraftPlayer.fromMinecraftPlayer(reference.getPlayerByUUID(uuid));
    }

    @Override
    public BlockData getBlockData(BlockPosition blockPosition) {
        return MinecraftBlockData.fromMinecraftBlockData(reference.getBlockState(MinecraftPlayer.toMinecraftBlockPosition(blockPosition)));
    }

    @Override
    public boolean isClient() {
        return reference.isClientSide();
    }

}
