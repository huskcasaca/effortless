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
import net.minecraft.world.level.GameType;

import java.util.UUID;
import java.util.stream.Collectors;

public class MinecraftPlayer extends Player {

    private final net.minecraft.world.entity.player.Player player;

    public MinecraftPlayer(net.minecraft.world.entity.player.Player player) {
        this.player = player;
    }

    public static net.minecraft.world.entity.player.Player toMinecraftPlayer(Player player) {
        return ((MinecraftPlayer) player).getRef();
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
        return new MinecraftServer(getRef().getServer());
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
        return Storage.create(getRef().getInventory().items.stream().map(MinecraftItemStack::new).collect(Collectors.toList()));
    }

    @Override
    public Text getDisplayName() {
        return MinecraftText.fromMinecraftText(getRef().getDisplayName());
    }

    @Override
    public Vector3d getPosition() {
        return MinecraftBasicTypes.fromMinecraftVector3d(getRef().position());
    }

    @Override
    public Vector3d getEyePosition() {
        return MinecraftBasicTypes.fromMinecraftVector3d(getRef().getEyePosition());
    }

    @Override
    public Vector3d getEyeDirection() {
        return MinecraftBasicTypes.fromMinecraftVector3d(getRef().getLookAngle());
    }

    @Override
    public ItemStack getItemStack(InteractionHand hand) {
        return new MinecraftItemStack(getRef().getItemInHand(MinecraftBasicTypes.toMinecraftInteractionHand(hand)));
    }

    @Override
    public void setItemStack(InteractionHand hand, ItemStack itemStack) {
        getRef().setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, MinecraftItemStack.toMinecraftItemStack(itemStack));
    }

    @Override
    public void sendMessage(Text message) {
        getRef().sendSystemMessage(MinecraftText.toMinecraftText(message));
//        getRef().displayClientMessage(message, true);
    }

    @Override
    public void swing(InteractionHand hand) {
        getRef().swing(MinecraftBasicTypes.toMinecraftInteractionHand(hand));
    }

    @Override
    public boolean canInteractBlock(BlockPosition blockPosition) {
        return !getRef().blockActionRestricted(getRef().level(), MinecraftBasicTypes.toMinecraftBlockPosition(blockPosition), switch (getGameType()) {
            case SURVIVAL -> GameType.SURVIVAL;
            case CREATIVE -> GameType.CREATIVE;
            case ADVENTURE -> GameType.ADVENTURE;
            case SPECTATOR -> GameType.SPECTATOR;
        });
    }

    @Override
    public boolean canAttackBlock(BlockPosition blockPosition) {
        return getRef().getMainHandItem().getItem().canAttackBlock(getRef().level().getBlockState(MinecraftBasicTypes.toMinecraftBlockPosition(blockPosition)), getRef().level(), MinecraftBasicTypes.toMinecraftBlockPosition(blockPosition), getRef());
    }

    @Override
    public GameMode getGameType() {
        if (getRef() instanceof ServerPlayer serverPlayer) {
            return switch (serverPlayer.gameMode.getGameModeForPlayer()) {
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
        return (BlockInteraction) MinecraftBasicTypes.fromMinecraftInteraction(getRef().pick(maxDistance, deltaTick, includeFluids));
    }

    @Override
    public boolean tryPlaceBlock(BlockInteraction interaction, BlockData blockData, ItemStack itemStack) {
        return ((BlockItem) ((MinecraftBlockData) blockData).getRef().getBlock().asItem()).place(new BlockPlaceContext(getRef(), MinecraftBasicTypes.toMinecraftInteractionHand(interaction.getHand()), MinecraftItemStack.toMinecraftItemStack(itemStack), MinecraftBasicTypes.toMinecraftBlockInteraction(interaction))).consumesAction();
    }

    @Override
    public boolean tryBreakBlock(BlockInteraction interaction) {
        if (getRef() instanceof ServerPlayer serverPlayer) {
            return serverPlayer.gameMode.destroyBlock(MinecraftBasicTypes.toMinecraftBlockPosition(interaction.getBlockPosition()));
        }
        return Minecraft.getInstance().gameMode.destroyBlock(MinecraftBasicTypes.toMinecraftBlockPosition(interaction.getBlockPosition()));
    }

}
