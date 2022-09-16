package dev.huskcasaca.effortless.buildmode;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.UUID;

public abstract class MultipleClickBuildable implements Buildable {
    //In singleplayer client and server variables are shared
    //Split everything that needs separate values and may not be called twice in one click
    protected Dictionary<UUID, Integer> rightClickClientTable = new Hashtable<>();
    protected Dictionary<UUID, Integer> rightClickServerTable = new Hashtable<>();
    protected Dictionary<UUID, BlockPos> firstPosTable = new Hashtable<>();
    protected Dictionary<UUID, Direction> sideHitTable = new Hashtable<>();
    protected Dictionary<UUID, Vec3> hitVecTable = new Hashtable<>();

    @Override
    public void initialize(Player player) {
        rightClickClientTable.put(player.getUUID(), 0);
        rightClickServerTable.put(player.getUUID(), 0);
        firstPosTable.put(player.getUUID(), BlockPos.ZERO);
        sideHitTable.put(player.getUUID(), Direction.UP);
        hitVecTable.put(player.getUUID(), Vec3.ZERO);
    }

    @Override
    public Direction getSideHit(Player player) {
        return sideHitTable.get(player.getUUID());
    }

    @Override
    public Vec3 getHitVec(Player player) {
        return hitVecTable.get(player.getUUID());
    }
}
