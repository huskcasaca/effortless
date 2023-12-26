package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.platform.Server;
import dev.huskuraft.effortless.text.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;

import java.util.UUID;
import java.util.stream.Collectors;

class MinecraftPlayer extends Player {

    private final net.minecraft.world.entity.player.Player player;

    MinecraftPlayer(net.minecraft.world.entity.player.Player player) {
        this.player = player;
    }

    public net.minecraft.world.entity.player.Player getRef() {
        return player;
    }

    @Override
    public UUID getId() {
        return getRef().getUUID();
    }

    @Override
    public Server getServer() {
        return MinecraftAdapter.adapt(getRef().getServer());
    }

    @Override
    public World getWorld() {
        return new MinecraftWorld(getRef().level());
    }

    @Override
    public Storage getStorage() {
        if (getRef().isCreative()) {
            return Storage.fullStorage();
        }
        return Storage.create(getRef().getInventory().items.stream().map(MinecraftAdapter::adapt).collect(Collectors.toList()));
    }

    @Override
    public Text getDisplayName() {
        return MinecraftAdapter.adapt(getRef().getDisplayName());
    }

    @Override
    public Vector3d getPosition() {
        return MinecraftAdapter.adapt(getRef().position());
    }

    @Override
    public Vector3d getEyePosition() {
        return MinecraftAdapter.adapt(getRef().getEyePosition());
    }

    @Override
    public Vector3d getEyeDirection() {
        return MinecraftAdapter.adapt(getRef().getLookAngle());
    }

    @Override
    public ItemStack getItemStack(InteractionHand hand) {
        return MinecraftAdapter.adapt(getRef().getItemInHand(MinecraftAdapter.adapt(hand)));
    }

    @Override
    public void setItemStack(InteractionHand hand, ItemStack itemStack) {
        getRef().setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, MinecraftAdapter.adapt(itemStack));
    }

    @Override
    public void sendMessage(Text message) {
        getRef().sendSystemMessage(MinecraftAdapter.adapt(message));
//        getRef().displayClientMessage(message, true);
    }

    @Override
    public void swing(InteractionHand hand) {
        getRef().swing(MinecraftAdapter.adapt(hand));
    }

    @Override
    public boolean canInteractBlock(BlockInteraction interaction) {
        return !getRef().blockActionRestricted(getRef().level(), MinecraftAdapter.adapt(interaction.getBlockPosition()), MinecraftAdapter.adapt(getGameType()));
    }

    @Override
    public boolean canAttackBlock(BlockPosition blockPosition) {
        return getRef().getMainHandItem().getItem().canAttackBlock(getRef().level().getBlockState(MinecraftAdapter.adapt(blockPosition)), getRef().level(), MinecraftAdapter.adapt(blockPosition), getRef());
    }

    @Override
    public GameMode getGameType() {
        if (getRef() instanceof ServerPlayer serverPlayer) {
            return MinecraftAdapter.adapt(serverPlayer.gameMode.getGameModeForPlayer());
        }
        return null;
    }

    @Override
    public BlockInteraction raytrace(double maxDistance, float deltaTick, boolean includeFluids) {
        return (BlockInteraction) MinecraftAdapter.adapt(getRef().pick(maxDistance, deltaTick, includeFluids));
    }

    @Override
    public boolean tryPlaceBlock(BlockInteraction interaction, BlockData blockData, ItemStack itemStack) {
        return ((BlockItem) MinecraftAdapter.adapt(blockData).getBlock().asItem()).place(new BlockPlaceContext(getRef(), MinecraftAdapter.adapt(interaction.getHand()), MinecraftAdapter.adapt(itemStack), MinecraftAdapter.adapt(interaction))).consumesAction();
    }

    @Override
    public boolean tryBreakBlock(BlockInteraction interaction) {
        var blockPosRef = MinecraftAdapter.adapt(interaction.getBlockPosition());
        if (getRef() instanceof ServerPlayer serverPlayer) {
            return serverPlayer.gameMode.destroyBlock(blockPosRef);
        }
        return Minecraft.getInstance().gameMode.destroyBlock(blockPosRef);
    }

}
