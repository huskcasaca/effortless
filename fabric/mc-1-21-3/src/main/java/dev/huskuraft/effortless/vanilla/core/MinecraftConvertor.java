package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class MinecraftConvertor {

    public static Quaternionf fromPlatformQuaternion(org.joml.Quaternionf quaternion) {
        return new Quaternionf(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w());
    }

    public static org.joml.Quaternionf toPlatformQuaternion(Quaternionf quaternion) {
        return new org.joml.Quaternionf(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w());
    }

    public static Matrix3f fromPlatformMatrix3f(org.joml.Matrix3f matrix) {
        return new Matrix3f(
                matrix.m00(), matrix.m01(), matrix.m02(),
                matrix.m10(), matrix.m11(), matrix.m12(),
                matrix.m20(), matrix.m21(), matrix.m22()
        );
    }

    public static org.joml.Matrix3f toPlatformMatrix3f(Matrix3f matrix) {
        return new org.joml.Matrix3f(
                matrix.m00(), matrix.m01(), matrix.m02(),
                matrix.m10(), matrix.m11(), matrix.m12(),
                matrix.m20(), matrix.m21(), matrix.m22()
        );
    }

    public static Matrix4f fromPlatformMatrix4f(org.joml.Matrix4f matrix) {
        return new Matrix4f(
                matrix.m00(), matrix.m01(), matrix.m02(), matrix.m03(),
                matrix.m10(), matrix.m11(), matrix.m12(), matrix.m13(),
                matrix.m20(), matrix.m21(), matrix.m22(), matrix.m23(),
                matrix.m30(), matrix.m31(), matrix.m32(), matrix.m33()
        );
    }

    public static org.joml.Matrix4f toPlatformMatrix4f(Matrix4f matrix) {
        return new org.joml.Matrix4f(
                matrix.m00(), matrix.m01(), matrix.m02(), matrix.m03(),
                matrix.m10(), matrix.m11(), matrix.m12(), matrix.m13(),
                matrix.m20(), matrix.m21(), matrix.m22(), matrix.m23(),
                matrix.m30(), matrix.m31(), matrix.m32(), matrix.m33()
        );
    }

    public static BlockPos toPlatformBlockPosition(BlockPosition blockPosition) {
        return new BlockPos(blockPosition.x(), blockPosition.y(), blockPosition.z());
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
        if (interactionResult instanceof net.minecraft.world.InteractionResult.Success) return InteractionResult.SUCCESS;
        if (interactionResult.consumesAction()) return InteractionResult.CONSUME;
        if (interactionResult instanceof net.minecraft.world.InteractionResult.Pass) return InteractionResult.PASS;
        if (interactionResult instanceof net.minecraft.world.InteractionResult.Fail) return InteractionResult.FAIL;
        if (interactionResult instanceof net.minecraft.world.InteractionResult.TryEmptyHandInteraction) return InteractionResult.PASS;
        return InteractionResult.PASS;
        ////
        ////
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
