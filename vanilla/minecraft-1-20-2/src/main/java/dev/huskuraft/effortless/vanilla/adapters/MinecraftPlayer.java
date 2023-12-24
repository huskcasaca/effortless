package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.platform.Server;
import dev.huskuraft.effortless.text.Text;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
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

    @Override
    public boolean tryPlaceBlockNoCheck(BlockInteraction interaction, BlockData blockData, ItemStack itemStack) {

        var playerRef = getRef();
        var levelRef = MinecraftAdapter.adapt(getWorld());
        var itemStackRef = MinecraftAdapter.adapt(itemStack);
        var blockStateRef = MinecraftAdapter.adapt(blockData);
        var blockItemRef = (BlockItem) blockStateRef.getBlock().asItem();
        var blockHitResultRef = MinecraftAdapter.adapt(interaction);
        var handRef = MinecraftAdapter.adapt(interaction.getHand());
        var blockPosRef = blockHitResultRef.getBlockPos();

        var innerContext = new BlockPlaceContext(playerRef, handRef, itemStackRef, blockHitResultRef);
        var isRemoved = levelRef.setBlock(innerContext.getClickedPos(), blockStateRef, 11);
        if (!isRemoved) {
            return false;
        }
        var blockStateRef2 = levelRef.getBlockState(blockPosRef);
        if (blockStateRef2.is(blockStateRef.getBlock())) {
//            blockStateRef2 = blockItemRef.updateBlockStateFromTag(blockPosRef, levelRef, itemStack, blockStateRef2);
//            blockItemRef.updateCustomBlockEntityTag(blockPosRef, levelRef, player, itemStack, blockStateRef2);
            blockStateRef2.getBlock().setPlacedBy(levelRef, blockPosRef, blockStateRef2, playerRef, itemStackRef);
            if (playerRef instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockPosRef, itemStackRef);
            }
        }
        if (!playerRef.getAbilities().instabuild) {
            playerRef.getMainHandItem().shrink(1);
        }

        if (playerRef instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPosRef, itemStackRef);
        }
        playerRef.awardStat(Stats.ITEM_USED.get(blockItemRef));
        return true;

    }

    @Override
    public boolean tryBreakBlockNoCheck(BlockInteraction interaction) {
        var playerRef = getRef();
        var levelRef = MinecraftAdapter.adapt(getWorld());
        var blockPosRef = MinecraftAdapter.adapt(interaction.getBlockPosition());
        var blockStateRef = levelRef.getBlockState(blockPosRef);
        var blockRef = blockStateRef.getBlock();
        var blockEntityRef = levelRef.getBlockEntity(blockPosRef);
        var fluidStateRef = levelRef.getFluidState(blockPosRef);

        blockRef.playerWillDestroy(levelRef, blockPosRef, blockStateRef, playerRef);
        var isRemoved = levelRef.setBlock(blockPosRef, fluidStateRef.createLegacyBlock(), 11);
        if (isRemoved) {
            blockRef.destroy(levelRef, blockPosRef, blockStateRef);
        }
        // server
        if (playerRef.isCreative()) {
            return true;
        }
        var itemStackRef = playerRef.getMainHandItem();
        var itemStackRef2 = itemStackRef.copy();
        var isCorrectTool = playerRef.hasCorrectToolForDrops(blockStateRef);
        itemStackRef.mineBlock(levelRef, blockStateRef, blockPosRef, playerRef);
        if (isRemoved && isCorrectTool) {
            blockRef.playerDestroy(levelRef, playerRef, blockPosRef, blockStateRef, blockEntityRef, itemStackRef2);
            return true;
        }
        return false;
    }
}
