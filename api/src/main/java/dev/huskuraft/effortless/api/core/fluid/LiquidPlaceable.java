package dev.huskuraft.effortless.api.core.fluid;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.FluidState;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;

public interface LiquidPlaceable {

    boolean canPlaceLiquid(World world/*BlockGetter*/, Player player, BlockPosition blockPosition, BlockState blockState, Fluid fluid);

    boolean placeLiquid(World world, BlockPosition blockPosition, BlockState blockState, FluidState fluidState);

}
