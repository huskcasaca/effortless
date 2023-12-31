package dev.huskuraft.effortless.core;

import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.platform.Server;
import dev.huskuraft.effortless.text.Text;

import java.util.List;
import java.util.UUID;

public abstract class Player {

    public abstract UUID getId();

    public abstract Server getServer();

    public abstract World getWorld();

    public abstract Text getDisplayName();

    public abstract Vector3d getPosition();

    public abstract Vector3d getEyePosition();

    public abstract Vector3d getEyeDirection();

    public abstract List<ItemStack> getItemStacks();

    public abstract void setItemStack(int index, ItemStack itemStack);

    public abstract ItemStack getItemStack(InteractionHand hand);

    public abstract void setItemStack(InteractionHand hand, ItemStack itemStack);

    public abstract void sendMessage(Text messages);

    public abstract void swing(InteractionHand hand);

    public abstract boolean canInteractBlock(BlockPosition blockPosition);

    public abstract boolean canAttackBlock(BlockPosition blockPosition);

    public abstract GameMode getGameType();

    public abstract BlockInteraction raytrace(double maxDistance, float deltaTick, boolean includeFluids);

    public abstract boolean tryPlaceBlock(BlockInteraction interaction);

    public abstract boolean tryBreakBlock(BlockInteraction interaction);

}
