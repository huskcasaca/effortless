package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.math.Quaternion;
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

public class MinecraftPrimitives {

    public static Quaternionf fromMinecraftQuaternion(Quaternion quaternion) {
        return new Quaternionf(quaternion.i(), quaternion.j(), quaternion.k(), quaternion.r());
    }

    public static Quaternion toMinecraftQuaternion(Quaternionf quaternion) {
        return new Quaternion(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w());
    }

    public static Matrix3f fromMinecraftMatrix3f(com.mojang.math.Matrix3f matrix) {
        var buffer = FloatBuffer.allocate(9);
        matrix.store(buffer);
        return new Matrix3f(buffer);
    }

    public static com.mojang.math.Matrix3f toMinecraftMatrix3f(Matrix3f matrix) {
        var buffer = FloatBuffer.allocate(9);
        matrix.write(buffer);
        var minecraftMatrix = new com.mojang.math.Matrix3f();
        minecraftMatrix.load(buffer);
        return minecraftMatrix;
    }

    public static Matrix4f fromMinecraftMatrix4f(com.mojang.math.Matrix4f matrix) {
        var buffer = FloatBuffer.allocate(16);
        matrix.store(buffer);
        return new Matrix4f(buffer);
    }

    public static com.mojang.math.Matrix4f toMinecraftMatrix4f(Matrix4f matrix) {
        var buffer = FloatBuffer.allocate(16);
        matrix.write(buffer);
        var minecraftMatrix = new com.mojang.math.Matrix4f();
        minecraftMatrix.load(buffer);
        return minecraftMatrix;
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
}
