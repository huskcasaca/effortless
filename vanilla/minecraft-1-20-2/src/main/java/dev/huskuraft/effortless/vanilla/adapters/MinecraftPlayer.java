package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.platform.Server;
import dev.huskuraft.effortless.text.Text;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;

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
        return MinecraftAdapter.adapt(getRef().level());
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
    public boolean canInteract(BlockPosition blockPosition) {
        getGameType();
        if (!getRef().getAbilities().mayBuild) {
            return false;
        }
        if (getRef().level().getBlockState(MinecraftAdapter.adapt(blockPosition)).getBlock() instanceof GameMasterBlock && !getRef().canUseGameMasterBlocks()) {
            return false;
        }
        return !getRef().blockActionRestricted(getRef().level(), MinecraftAdapter.adapt(blockPosition), MinecraftAdapter.adapt(getGameType()));
    }

    @Override
    public boolean canBreakBlock(BlockPosition blockPosition) {
        return getRef().isCreative() || getRef().getMainHandItem().getItem().canAttackBlock(getRef().level().getBlockState(MinecraftAdapter.adapt(blockPosition)), getRef().level(), MinecraftAdapter.adapt(blockPosition), getRef()) && getWorld().isBlockBreakable(blockPosition);
    }

    @Override
    public boolean canPlaceBlock(BlockPosition blockPosition) {
        return getWorld().isBlockPlaceable(blockPosition);
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

}
