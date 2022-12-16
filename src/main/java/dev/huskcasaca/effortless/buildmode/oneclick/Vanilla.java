package dev.huskcasaca.effortless.buildmode.oneclick;

import dev.huskcasaca.effortless.buildmode.OneClickBuildable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class Vanilla extends OneClickBuildable {
    @Override
    public void initialize(Player player) {

    }

    @Override
    public List<BlockPos> onUse(Player player, BlockPos blockPos, Direction hitSide, Vec3 hitVec, boolean skipRaytrace) {
        List<BlockPos> list = new ArrayList<>();
        if (blockPos != null) list.add(blockPos);
        return list;
    }

    @Override
    public List<BlockPos> findCoordinates(Player player, BlockPos blockPos, boolean skipRaytrace) {
        List<BlockPos> list = new ArrayList<>();
        if (blockPos != null) list.add(blockPos);
        return list;
    }

    @Override
    public Direction getHitSide(Player player) {
        return null;
    }

    @Override
    public Vec3 getHitVec(Player player) {
        return null;
    }
}
