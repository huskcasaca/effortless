package dev.huskuraft.effortless.api.core.fluid;

import java.util.Optional;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.sound.Sound;

public interface BucketCollectable {

    ItemStack pickupBlock(World world, Player player, BlockPosition blockPosition, BlockState blockState);

    Optional<Sound> getPickupSound();

}
