package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.core.*;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.platform.Server;
import dev.huskuraft.effortless.api.text.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.GameType;

import java.util.List;
import java.util.UUID;

class MinecraftPlayer implements Player {

    protected final net.minecraft.world.entity.player.Player reference;

    MinecraftPlayer(net.minecraft.world.entity.player.Player reference) {
        this.reference = reference;
    }

    @Override
    public net.minecraft.world.entity.player.Player referenceValue() {
        return reference;
    }

    @Override
    public UUID getId() {
        return reference.getUUID();
    }

    @Override
    public Server getServer() {
        return MinecraftConvertor.fromPlatformServer(reference.getServer());
    }

    @Override
    public World getWorld() {
        return MinecraftConvertor.fromPlatformWorld(reference.level());
    }

    @Override
    public Text getDisplayName() {
        return MinecraftConvertor.fromPlatformText(reference.getDisplayName());
    }

    @Override
    public Vector3d getPosition() {
        return MinecraftConvertor.fromPlatformMinecraftVector3d(reference.position());
    }

    @Override
    public Vector3d getEyePosition() {
        return MinecraftConvertor.fromPlatformMinecraftVector3d(reference.getEyePosition());
    }

    @Override
    public Vector3d getEyeDirection() {
        return MinecraftConvertor.fromPlatformMinecraftVector3d(reference.getLookAngle());
    }

    @Override
    public List<ItemStack> getItemStacks() {
        return reference.getInventory().items.stream().map(MinecraftConvertor::fromPlatformItemStack).toList();
    }

    @Override
    public void setItemStack(int index, ItemStack itemStack) {
        reference.getInventory().setItem(index, MinecraftConvertor.toPlatformItemStack(itemStack));
    }

    @Override
    public ItemStack getItemStack(InteractionHand hand) {
        return MinecraftConvertor.fromPlatformItemStack(reference.getItemInHand(MinecraftConvertor.toPlatformInteractionHand(hand)));
    }

    @Override
    public void setItemStack(InteractionHand hand, ItemStack itemStack) {
        reference.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, MinecraftConvertor.toPlatformItemStack(itemStack));
    }

    @Override
    public void sendMessage(Text message) {
        reference.sendSystemMessage(MinecraftConvertor.toPlatformText(message));
//        getRef().displayClientMessage(message, true);
    }

    @Override
    public void swing(InteractionHand hand) {
        reference.swing(MinecraftConvertor.toPlatformInteractionHand(hand));
    }

    @Override
    public boolean canInteractBlock(BlockPosition blockPosition) {
        return !reference.blockActionRestricted(reference.level(), MinecraftConvertor.toPlatformBlockPosition(blockPosition), switch (getGameType()) {
            case SURVIVAL -> GameType.SURVIVAL;
            case CREATIVE -> GameType.CREATIVE;
            case ADVENTURE -> GameType.ADVENTURE;
            case SPECTATOR -> GameType.SPECTATOR;
        });
    }

    @Override
    public boolean canAttackBlock(BlockPosition blockPosition) {
        return reference.getMainHandItem().getItem().canAttackBlock(reference.level().getBlockState(MinecraftConvertor.toPlatformBlockPosition(blockPosition)), reference.level(), MinecraftConvertor.toPlatformBlockPosition(blockPosition), reference);
    }

    @Override
    public GameMode getGameType() {
        if (reference instanceof ServerPlayer serverPlayer) {
            return switch (serverPlayer.gameMode.getGameModeForPlayer()) {
                case SURVIVAL -> GameMode.SURVIVAL;
                case CREATIVE -> GameMode.CREATIVE;
                case ADVENTURE -> GameMode.ADVENTURE;
                case SPECTATOR -> GameMode.SPECTATOR;
            };
        }
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
        return (BlockInteraction) MinecraftConvertor.fromPlatformInteraction(reference.pick(maxDistance, deltaTick, includeFluids));
    }

    @Override
    public boolean tryPlaceBlock(BlockInteraction interaction) {
        var minecraftInteractionHand = MinecraftConvertor.toPlatformInteractionHand(interaction.getHand());
        var minecraftItemStack = reference.getItemInHand(minecraftInteractionHand);
        var minecraftBlockInteraction = MinecraftConvertor.toPlatformBlockInteraction(interaction);
        return ((BlockItem) minecraftItemStack.getItem()).place(new BlockPlaceContext(reference, minecraftInteractionHand, minecraftItemStack, minecraftBlockInteraction)).consumesAction();
    }

    @Override
    public boolean tryBreakBlock(BlockInteraction interaction) {
        var minecraftBlockPosition = MinecraftConvertor.toPlatformBlockPosition(interaction.getBlockPosition());
        if (reference instanceof ServerPlayer serverPlayer) {
            return serverPlayer.gameMode.destroyBlock(minecraftBlockPosition);
        }
        if (reference instanceof LocalPlayer localPlayer) {
            return Minecraft.getInstance().gameMode != null && Minecraft.getInstance().gameMode.destroyBlock(minecraftBlockPosition);
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftPlayer player && reference.equals(player.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

}
