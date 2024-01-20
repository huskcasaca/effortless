package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.math.Vector3i;
import dev.huskuraft.effortless.platform.Server;
import dev.huskuraft.effortless.text.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;
import java.util.stream.Collectors;

public class MinecraftPlayer extends Player {

    protected final net.minecraft.world.entity.player.Player reference;

    MinecraftPlayer(net.minecraft.world.entity.player.Player reference) {
        this.reference = reference;
    }

    public static Player fromMinecraftPlayer(net.minecraft.world.entity.player.Player player) {
        return new MinecraftPlayer(player);
    }

    public static net.minecraft.world.entity.player.Player toMinecraftPlayer(Player player) {
        return ((MinecraftPlayer) player).reference;
    }

    public static BlockPos toMinecraftBlockPosition(BlockPosition value) {
        return new BlockPos(value.x(), value.y(), value.z());
    }

    public static BlockPos toMinecraftBlockPosition(Vector3i value) {
        return new BlockPos(value.x(), value.y(), value.z());
    }

    public static Vec3i toMinecraftVector3i(Vector3i value) {
        return new Vec3i(value.x(), value.y(), value.z());
    }

    public static Vec3 toMinecraftVector3d(Vector3d value) {
        return new Vec3(value.x(), value.y(), value.z());
    }

    public static BlockPosition toBlockPosition(BlockPos value) {
        return new BlockPosition(value.getX(), value.getY(), value.getZ());
    }

    public static Vector3d fromMinecraftVector3d(Vec3 value) {
        return new Vector3d(value.x(), value.y(), value.z());
    }

    public static Vector3i fromMinecraftVector3i(Vec3i value) {
        return new Vector3i(value.getX(), value.getY(), value.getZ());
    }

    public static Interaction fromMinecraftInteraction(HitResult value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BlockHitResult blockHitResult) {
            return fromMinecraftBlockInteraction(blockHitResult);
        }
        if (value instanceof EntityHitResult entityHitResult) {
            return fromMinecraftEntityInteraction(entityHitResult);
        }
        throw new IllegalArgumentException("Unknown Interaction: " + value);
    }

    public static EntityInteraction fromMinecraftEntityInteraction(EntityHitResult value) {
        if (value == null) {
            return null;
        }
        return new EntityInteraction(fromMinecraftVector3d(value.getLocation()), null);
    }

    public static BlockInteraction fromMinecraftBlockInteraction(BlockHitResult value) {
        if (value == null) {
            return null;
        }
        return new BlockInteraction(fromMinecraftVector3d(value.getLocation()), fromMinecraftOrientation(value.getDirection()), toBlockPosition(value.getBlockPos()), value.isInside());
    }

    public static BlockHitResult toMinecraftBlockInteraction(BlockInteraction value) {
        if (value == null) {
            return null;
        }
        return new BlockHitResult(
                toMinecraftVector3d(value.getPosition()),
                toMinecraftOrientation(value.getDirection()),
                toMinecraftBlockPosition(value.getBlockPosition()),
                value.isInside());
    }

    public static InteractionHand fromMinecraftInteractionHand(net.minecraft.world.InteractionHand value) {
        return switch (value) {
            case MAIN_HAND -> InteractionHand.MAIN;
            case OFF_HAND -> InteractionHand.OFF;
        };
    }

    public static net.minecraft.world.InteractionHand toMinecraftInteractionHand() {
        return net.minecraft.world.InteractionHand.MAIN_HAND;
    }

    public static net.minecraft.world.InteractionHand toMinecraftInteractionHand(InteractionHand value) {
        return switch (value) {
            case MAIN -> net.minecraft.world.InteractionHand.MAIN_HAND;
            case OFF -> net.minecraft.world.InteractionHand.OFF_HAND;
        };
    }

    public static Orientation fromMinecraftOrientation(Direction value) {
        return switch (value) {
            case DOWN -> Orientation.DOWN;
            case UP -> Orientation.UP;
            case NORTH -> Orientation.NORTH;
            case SOUTH -> Orientation.SOUTH;
            case WEST -> Orientation.WEST;
            case EAST -> Orientation.EAST;
        };
    }

    public static Direction toMinecraftOrientation(Orientation value) {
        return switch (value) {
            case DOWN -> Direction.DOWN;
            case UP -> Direction.UP;
            case NORTH -> Direction.NORTH;
            case SOUTH -> Direction.SOUTH;
            case WEST -> Direction.WEST;
            case EAST -> Direction.EAST;
        };
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
    public Storage getStorage() {
        if (reference.isCreative()) {
            return Storage.fullStorage();
        }
        return Storage.create(reference.getInventory().items.stream().map(MinecraftItemStack::fromMinecraft).collect(Collectors.toList()));
    }

    @Override
    public Text getDisplayName() {
        return MinecraftText.fromMinecraftText(reference.getDisplayName());
    }

    @Override
    public Vector3d getPosition() {
        return fromMinecraftVector3d(reference.position());
    }

    @Override
    public Vector3d getEyePosition() {
        return fromMinecraftVector3d(reference.getEyePosition());
    }

    @Override
    public Vector3d getEyeDirection() {
        return fromMinecraftVector3d(reference.getLookAngle());
    }

    @Override
    public ItemStack getItemStack(InteractionHand hand) {
        return MinecraftItemStack.fromMinecraft(reference.getItemInHand(toMinecraftInteractionHand(hand)));
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
        reference.swing(toMinecraftInteractionHand(hand));
    }

    @Override
    public boolean canInteractBlock(BlockPosition blockPosition) {
        return !reference.blockActionRestricted(reference.level(), toMinecraftBlockPosition(blockPosition), switch (getGameType()) {
            case SURVIVAL -> GameType.SURVIVAL;
            case CREATIVE -> GameType.CREATIVE;
            case ADVENTURE -> GameType.ADVENTURE;
            case SPECTATOR -> GameType.SPECTATOR;
        });
    }

    @Override
    public boolean canAttackBlock(BlockPosition blockPosition) {
        return reference.getMainHandItem().getItem().canAttackBlock(reference.level().getBlockState(toMinecraftBlockPosition(blockPosition)), reference.level(), toMinecraftBlockPosition(blockPosition), reference);
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
        return null;
    }

    @Override
    public BlockInteraction raytrace(double maxDistance, float deltaTick, boolean includeFluids) {
        return (BlockInteraction) fromMinecraftInteraction(reference.pick(maxDistance, deltaTick, includeFluids));
    }

    @Override
    public boolean tryPlaceBlock(BlockInteraction interaction, BlockData blockData, ItemStack itemStack) {
        return ((BlockItem) MinecraftBlockData.toMinecraftBlockData(blockData).getBlock().asItem()).place(new BlockPlaceContext(reference, toMinecraftInteractionHand(interaction.getHand()), MinecraftItemStack.toMinecraftItemStack(itemStack), toMinecraftBlockInteraction(interaction))).consumesAction();
    }

    @Override
    public boolean tryBreakBlock(BlockInteraction interaction) {
        if (reference instanceof ServerPlayer serverPlayer) {
            return serverPlayer.gameMode.destroyBlock(toMinecraftBlockPosition(interaction.getBlockPosition()));
        }
        return Minecraft.getInstance().gameMode.destroyBlock(toMinecraftBlockPosition(interaction.getBlockPosition()));
    }

}
