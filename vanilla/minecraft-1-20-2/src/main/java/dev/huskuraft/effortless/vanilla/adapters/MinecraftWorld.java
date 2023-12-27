package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.BlockData;
import dev.huskuraft.effortless.core.BlockPosition;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.core.World;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class MinecraftWorld extends World {

    private final Level level;

    MinecraftWorld(Level level) {
        this.level = level;
    }

    public Level getRef() {
        return level;
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return new MinecraftPlayer(getRef().getPlayerByUUID(uuid));
    }

    @Override
    public BlockData getBlockData(BlockPosition blockPosition) {
        return new MinecraftBlockData(getRef().getBlockState(MinecraftBasicTypes.toMinecraftBlockPosition(blockPosition)));
    }

    @Override
    public boolean isClient() {
        return getRef().isClientSide();
    }

}
