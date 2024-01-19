package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.core.*;
import dev.huskuraft.effortless.api.math.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.nio.FloatBuffer;

public class MinecraftConvertor {

    public static Quaternionf fromPlatformQuaternion(org.joml.Quaternionf quaternion) {
        return new Quaternionf(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w());
    }

    public static org.joml.Quaternionf toPlatformQuaternion(Quaternionf quaternion) {
        return new org.joml.Quaternionf(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w());
    }

    public static Matrix3f fromPlatformMatrix3f(org.joml.Matrix3f matrix) {
        var buffer = FloatBuffer.allocate(9);
        matrix.get(buffer);
        return new Matrix3f(buffer);
    }

    public static org.joml.Matrix3f toPlatformMatrix3f(Matrix3f matrix) {
        var buffer = FloatBuffer.allocate(9);
        matrix.write(buffer);
        return new org.joml.Matrix3f(buffer);
    }

    public static Matrix4f fromPlatformMatrix4f(org.joml.Matrix4f matrix) {
        var buffer = FloatBuffer.allocate(16);
        matrix.get(buffer);
        return new Matrix4f(buffer);
    }

    public static org.joml.Matrix4f toPlatformMatrix4f(Matrix4f matrix) {
        var buffer = FloatBuffer.allocate(16);
        matrix.write(buffer);
        return new org.joml.Matrix4f(buffer);
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
        return new BlockInteraction(fromPlatformVector3d(blockHitResult.getLocation()), fromPlatformOrientation(blockHitResult.getDirection()), toPlatformBlockPosition(blockHitResult.getBlockPos()), blockHitResult.isInside());
    }

    public static BlockHitResult toPlatformBlockInteraction(BlockInteraction blockInteraction) {
        if (blockInteraction == null) {
            return null;
        }
        return new BlockHitResult(
                toPlatformVector3d(blockInteraction.getPosition()),
                toPlatformOrientation(blockInteraction.getDirection()),
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

    public static Orientation fromPlatformOrientation(Direction orientation) {
        return switch (orientation) {
            case DOWN -> Orientation.DOWN;
            case UP -> Orientation.UP;
            case NORTH -> Orientation.NORTH;
            case SOUTH -> Orientation.SOUTH;
            case WEST -> Orientation.WEST;
            case EAST -> Orientation.EAST;
        };
    }

    public static Direction toPlatformOrientation(Orientation orientation) {
        return switch (orientation) {
            case DOWN -> Direction.DOWN;
            case UP -> Direction.UP;
            case NORTH -> Direction.NORTH;
            case SOUTH -> Direction.SOUTH;
            case WEST -> Direction.WEST;
            case EAST -> Direction.EAST;
        };
    }

}
