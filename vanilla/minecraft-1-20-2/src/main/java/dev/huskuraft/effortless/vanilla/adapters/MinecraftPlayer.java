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

public class MinecraftPlayer implements Player {

    protected final net.minecraft.world.entity.player.Player reference;

    MinecraftPlayer(net.minecraft.world.entity.player.Player reference) {
        this.reference = reference;
    }

    public static Player fromMinecraftPlayer(net.minecraft.world.entity.player.Player player) {
        if (player == null) {
            return null;
        }
        return new MinecraftPlayer(player);
    }

    public static net.minecraft.world.entity.player.Player toMinecraftPlayer(Player player) {
        if (player == null) {
            return null;
        }
        return player.reference();
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
        return new MinecraftServer(reference.getServer());
    }

    @Override
    public World getWorld() {
        return new MinecraftWorld(reference.level());
    }

    @Override
    public Text getDisplayName() {
        return MinecraftText.fromMinecraftText(reference.getDisplayName());
    }

    @Override
    public Vector3d getPosition() {
        return MinecraftPrimitives.fromMinecraftVector3d(reference.position());
    }

    @Override
    public Vector3d getEyePosition() {
        return MinecraftPrimitives.fromMinecraftVector3d(reference.getEyePosition());
    }

    @Override
    public Vector3d getEyeDirection() {
        return MinecraftPrimitives.fromMinecraftVector3d(reference.getLookAngle());
    }

    @Override
    public List<ItemStack> getItemStacks() {
        return reference.getInventory().items.stream().map(MinecraftItemStack::fromMinecraft).toList();
    }

    @Override
    public void setItemStack(int index, ItemStack itemStack) {
        reference.getInventory().setItem(index, MinecraftItemStack.toMinecraftItemStack(itemStack));
    }

    @Override
    public ItemStack getItemStack(InteractionHand hand) {
        return MinecraftItemStack.fromMinecraft(reference.getItemInHand(MinecraftPrimitives.toMinecraftInteractionHand(hand)));
    }

    @Override
    public void setItemStack(InteractionHand hand, ItemStack itemStack) {
        reference.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, MinecraftItemStack.toMinecraftItemStack(itemStack));
    }

    @Override
    public void sendMessage(Text message) {
        reference.sendSystemMessage(MinecraftText.toMinecraftText(message));
//        getRef().displayClientMessage(message, true);
    }

    @Override
    public void swing(InteractionHand hand) {
        reference.swing(MinecraftPrimitives.toMinecraftInteractionHand(hand));
    }

    @Override
    public boolean canInteractBlock(BlockPosition blockPosition) {
        return !reference.blockActionRestricted(reference.level(), MinecraftPrimitives.toMinecraftBlockPosition(blockPosition), switch (getGameType()) {
            case SURVIVAL -> GameType.SURVIVAL;
            case CREATIVE -> GameType.CREATIVE;
            case ADVENTURE -> GameType.ADVENTURE;
            case SPECTATOR -> GameType.SPECTATOR;
        });
    }

    @Override
    public boolean canAttackBlock(BlockPosition blockPosition) {
        return reference.getMainHandItem().getItem().canAttackBlock(reference.level().getBlockState(MinecraftPrimitives.toMinecraftBlockPosition(blockPosition)), reference.level(), MinecraftPrimitives.toMinecraftBlockPosition(blockPosition), reference);
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
        return (BlockInteraction) MinecraftPrimitives.fromMinecraftInteraction(reference.pick(maxDistance, deltaTick, includeFluids));
    }

    @Override
    public boolean tryPlaceBlock(BlockInteraction interaction) {
        var minecraftInteractionHand = MinecraftPrimitives.toMinecraftInteractionHand(interaction.getHand());
        var minecraftItemStack = reference.getItemInHand(minecraftInteractionHand);
        var minecraftBlockInteraction = MinecraftPrimitives.toMinecraftBlockInteraction(interaction);
        return ((BlockItem) minecraftItemStack.getItem()).place(new BlockPlaceContext(reference, minecraftInteractionHand, minecraftItemStack, minecraftBlockInteraction)).consumesAction();
    }

    @Override
    public boolean tryBreakBlock(BlockInteraction interaction) {
        var minecraftBlockPosition = MinecraftPrimitives.toMinecraftBlockPosition(interaction.getBlockPosition());
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
