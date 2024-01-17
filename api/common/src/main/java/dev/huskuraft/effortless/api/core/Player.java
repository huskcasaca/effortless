package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.platform.Server;
import dev.huskuraft.effortless.api.text.Text;

import java.util.List;
import java.util.UUID;

public interface Player extends PlatformReference {

    UUID getId();

    Server getServer();

    World getWorld();

    Text getDisplayName();

    Vector3d getPosition();

    Vector3d getEyePosition();

    Vector3d getEyeDirection();

    List<ItemStack> getItemStacks();

    void setItemStack(int index, ItemStack itemStack);

    ItemStack getItemStack(InteractionHand hand);

    void setItemStack(InteractionHand hand, ItemStack itemStack);

    void sendMessage(Text messages);

    void swing(InteractionHand hand);

    boolean canInteractBlock(BlockPosition blockPosition);

    boolean canAttackBlock(BlockPosition blockPosition);

    GameMode getGameType();

    BlockInteraction raytrace(double maxDistance, float deltaTick, boolean includeFluids);

    boolean tryPlaceBlock(BlockInteraction interaction);

    boolean tryBreakBlock(BlockInteraction interaction);

}
