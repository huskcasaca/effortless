package dev.huskuraft.effortless.vanilla.core;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.GameMode;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.PlayerInfo;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.platform.Client;
import dev.huskuraft.effortless.api.platform.Server;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.vanilla.platform.MinecraftClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

public class MinecraftPlayer implements Player {

    protected final net.minecraft.world.entity.player.Player reference;

    MinecraftPlayer(net.minecraft.world.entity.player.Player reference) {
        this.reference = reference;
    }

    public static Player ofNullable(net.minecraft.world.entity.player.Player reference) {
        return reference == null ? null : new MinecraftPlayer(reference);
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
    public boolean isDeadOrDying() {
        return reference.isDeadOrDying();
    }

    @Override
    public Client getClient() {
        return new MinecraftClient(Minecraft.getInstance());
    }

    @Override
    public Server getServer() {
        return new MinecraftServer(reference.getServer());
    }

    @Override
    public World getWorld() {
        return MinecraftWorld.ofNullable(reference.getLevel());
    }

    @Override
    public Text getDisplayName() {
        return new MinecraftText(reference.getDisplayName());
    }

    @Override
    public Vector3d getPosition() {
        return MinecraftConvertor.fromPlatformVector3d(reference.position());
    }

    @Override
    public Vector3d getEyePosition() {
        return MinecraftConvertor.fromPlatformVector3d(reference.getEyePosition());
    }

    @Override
    public Vector3d getEyeDirection() {
        return MinecraftConvertor.fromPlatformVector3d(reference.getLookAngle());
    }

    @Override
    public List<ItemStack> getItemStacks() {
        return reference.getInventory().items.stream().map(itemStack -> new MinecraftItemStack(itemStack)).collect(Collectors.toList());
    }

    @Override
    public void setItemStack(int index, ItemStack itemStack) {
        reference.getInventory().setItem(index, itemStack.reference());
    }

    @Override
    public ItemStack getItemStack(InteractionHand hand) {
        return new MinecraftItemStack(reference.getItemInHand(MinecraftConvertor.toPlatformInteractionHand(hand)));
    }

    @Override
    public void setItemStack(InteractionHand hand, ItemStack itemStack) {
        reference.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, itemStack.reference());
    }

    @Override
    public void sendMessage(Text message) {
        reference.sendSystemMessage(message.reference());
//        getRef().displayClientMessage(message, true);
    }

    @Override
    public void swing(InteractionHand hand) {
        reference.swing(MinecraftConvertor.toPlatformInteractionHand(hand));
    }

    @Override
    public boolean canInteractBlock(BlockPosition blockPosition) {
        return !reference.blockActionRestricted(getWorld().reference(), MinecraftConvertor.toPlatformBlockPosition(blockPosition), switch (getGameType()) {
            case SURVIVAL -> GameType.SURVIVAL;
            case CREATIVE -> GameType.CREATIVE;
            case ADVENTURE -> GameType.ADVENTURE;
            case SPECTATOR -> GameType.SPECTATOR;
        });
    }

    @Override
    public boolean canAttackBlock(BlockPosition blockPosition) {
        return reference.getMainHandItem().getItem().canAttackBlock(((Level) getWorld().reference()).getBlockState(MinecraftConvertor.toPlatformBlockPosition(blockPosition)), getWorld().reference(), MinecraftConvertor.toPlatformBlockPosition(blockPosition), reference);
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
