package dev.huskuraft.effortless.vanilla.core;

import java.nio.FloatBuffer;

import com.mojang.math.Quaternion;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.ChunkPosition;
import dev.huskuraft.effortless.api.core.Direction;
import dev.huskuraft.effortless.api.core.EntityInteraction;
import dev.huskuraft.effortless.api.core.Interaction;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.InteractionResult;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.Matrix3f;
import dev.huskuraft.effortless.api.math.Matrix4f;
import dev.huskuraft.effortless.api.math.Quaternionf;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.math.Vector3i;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class MinecraftConvertor {

    public static Quaternionf fromPlatformQuaternion(Quaternion quaternion) {
        return new Quaternionf(quaternion.i(), quaternion.j(), quaternion.k(), quaternion.r());
    }

    public static Quaternion toPlatformQuaternion(Quaternionf quaternion) {
        return new Quaternion(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w());
    }

    public static Matrix3f fromPlatformMatrix3f(com.mojang.math.Matrix3f matrix) {
        var buffer = FloatBuffer.allocate(9);
        matrix.store(buffer);
        return new Matrix3f(buffer);
    }

    public static com.mojang.math.Matrix3f toPlatformMatrix3f(Matrix3f matrix) {
        var buffer = FloatBuffer.allocate(9);
        matrix.write(buffer);
        var minecraftMatrix = new com.mojang.math.Matrix3f();
        minecraftMatrix.load(buffer);
        return minecraftMatrix;
    }

    public static Matrix4f fromPlatformMatrix4f(com.mojang.math.Matrix4f matrix) {
        var buffer = FloatBuffer.allocate(16);
        matrix.store(buffer);
        return new Matrix4f(buffer);
    }

    public static com.mojang.math.Matrix4f toPlatformMatrix4f(Matrix4f matrix) {
        var buffer = FloatBuffer.allocate(16);
        matrix.write(buffer);
        var minecraftMatrix = new com.mojang.math.Matrix4f();
        minecraftMatrix.load(buffer);
        return minecraftMatrix;
    }

    public static BlockPos toPlatformBlockPosition(BlockPosition blockPosition) {
        return new BlockPos(blockPosition.x(), blockPosition.y(), blockPosition.z());
    }

    public static ChunkPos toPlatformChunkPosition(ChunkPosition chunkPosition) {
        return new ChunkPos(chunkPosition.x(), chunkPosition.z());
    }

    public static Vec3i toPlatformVector3i(Vector3i vector) {
        return new Vec3i(vector.x(), vector.y(), vector.z());
    }

    public static Vec3 toPlatformVector3d(Vector3d vector) {
        return new Vec3(vector.x(), vector.y(), vector.z());
    }

    public static BlockPosition toPlatformBlockPosition(BlockPos vector) {
        return new BlockPosition(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vector3d fromPlatformVector3d(Vec3 vector) {
        return new Vector3d(vector.x(), vector.y(), vector.z());
    }

    public static Vector3i fromPlatformVector3i(Vec3i vector) {
        return new Vector3i(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Interaction fromPlatformInteraction(HitResult hitResult) {
        if (hitResult == null) {
            return null;
        }
        if (hitResult instanceof BlockHitResult blockHitResult) {
            return fromPlatformBlockInteraction(blockHitResult);
        }
        if (hitResult instanceof EntityHitResult entityHitResult) {
            return fromPlatformEntityInteraction(entityHitResult);
        }
        throw new IllegalArgumentException("Unknown Interaction: " + hitResult);
    }

    public static EntityInteraction fromPlatformEntityInteraction(EntityHitResult entityHitResult) {
        if (entityHitResult == null) {
            return null;
        }
        return new EntityInteraction(fromPlatformVector3d(entityHitResult.getLocation()), null);
    }

    public static BlockInteraction fromPlatformBlockInteraction(BlockHitResult blockHitResult) {
        if (blockHitResult == null) {
            return null;
        }
        return new BlockInteraction(fromPlatformVector3d(blockHitResult.getLocation()), fromPlatformDirection(blockHitResult.getDirection()), toPlatformBlockPosition(blockHitResult.getBlockPos()), blockHitResult.isInside(), blockHitResult.getType() == BlockHitResult.Type.MISS);
    }

    public static BlockHitResult toPlatformBlockInteraction(BlockInteraction blockInteraction) {
        if (blockInteraction == null) {
            return null;
        }
        return new BlockHitResult(
                toPlatformVector3d(blockInteraction.getPosition()),
                toPlatformDirection(blockInteraction.getDirection()),
                toPlatformBlockPosition(blockInteraction.getBlockPosition()),
                blockInteraction.isInside());
    }

    public static InteractionHand fromPlatformInteractionHand(net.minecraft.world.InteractionHand interactionHand) {
        return switch (interactionHand) {
            case MAIN_HAND -> InteractionHand.MAIN;
            case OFF_HAND -> InteractionHand.OFF;
        };
    }

    public static net.minecraft.world.InteractionHand toPlatformInteractionHand(InteractionHand interactionHand) {
        return switch (interactionHand) {
            case MAIN -> net.minecraft.world.InteractionHand.MAIN_HAND;
            case OFF -> net.minecraft.world.InteractionHand.OFF_HAND;
        };
    }

    public static Direction fromPlatformDirection(net.minecraft.core.Direction direction) {
        return switch (direction) {
            case DOWN -> Direction.DOWN;
            case UP -> Direction.UP;
            case NORTH -> Direction.NORTH;
            case SOUTH -> Direction.SOUTH;
            case WEST -> Direction.WEST;
            case EAST -> Direction.EAST;
        };
    }

    public static net.minecraft.core.Direction toPlatformDirection(Direction direction) {
        return switch (direction) {
            case DOWN -> net.minecraft.core.Direction.DOWN;
            case UP -> net.minecraft.core.Direction.UP;
            case NORTH -> net.minecraft.core.Direction.NORTH;
            case SOUTH -> net.minecraft.core.Direction.SOUTH;
            case WEST -> net.minecraft.core.Direction.WEST;
            case EAST -> net.minecraft.core.Direction.EAST;
        };
    }


    public static InteractionResult fromPlatformInteractionResult(net.minecraft.world.InteractionResult interactionResult) {
        return switch (interactionResult) {
            case SUCCESS -> InteractionResult.SUCCESS;
//            case SUCCESS_NO_ITEM_USED -> InteractionResult.SUCCESS_NO_ITEM_USED;
            case CONSUME -> InteractionResult.CONSUME;
            case CONSUME_PARTIAL -> InteractionResult.CONSUME_PARTIAL;
            case PASS -> InteractionResult.PASS;
            case FAIL -> InteractionResult.FAIL;
        };
    }

    public static BlockPlaceContext toPlatformBlockPlaceContext(Player player, BlockInteraction blockInteraction) {
        return new BlockPlaceContext(
                player.reference(),
                toPlatformInteractionHand(blockInteraction.getHand()),
                player.getItemStack(blockInteraction.getHand()).reference(),
                toPlatformBlockInteraction(blockInteraction)
        );
    }

}
