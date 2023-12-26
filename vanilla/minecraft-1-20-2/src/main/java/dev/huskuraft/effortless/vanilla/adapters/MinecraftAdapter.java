package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.math.Vector3i;
import dev.huskuraft.effortless.tag.TagElement;
import dev.huskuraft.effortless.tag.TagRecord;
import dev.huskuraft.effortless.text.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MinecraftAdapter {

    public static Vector3d adapt(Vec3 vec3) {
        if (vec3 == null) {
            return null;
        }
        return new Vector3d(vec3.x(), vec3.y(), vec3.z());
    }

    public static Vec3 adapt(Vector3d vector) {
        if (vector == null) {
            return null;
        }
        return new Vec3(vector.x(), vector.y(), vector.z());
    }

    public static Vector3i adapt(Vec3i vec3i) {
        if (vec3i == null) {
            return null;
        }
        return new Vector3i(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    public static Vec3i adapt(Vector3i vector) {
        if (vector == null) {
            return null;
        }
        return new Vec3i(vector.x(), vector.y(), vector.z());
    }

    public static BlockPosition adapt(BlockPos blockPos) {
        if (blockPos == null) {
            return null;
        }
        return new BlockPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    @Deprecated // remove blockpos
    public static BlockPos adapt(BlockPosition blockPosition) {
        if (blockPosition == null) {
            return null;
        }
        return new BlockPos(blockPosition.x(), blockPosition.y(), blockPosition.z());
    }

    public static Interaction adapt(HitResult hitResult) {
        if (hitResult == null) {
            return null;
        }
        if (hitResult instanceof BlockHitResult blockHitResult) {
            return adapt(blockHitResult);
        }
        if (hitResult instanceof EntityHitResult entityHitResult) {
            return adapt(entityHitResult);
        }
        throw new IllegalArgumentException("");
    }

    public static EntityInteraction adapt(EntityHitResult entityHitResult) {
        if (entityHitResult == null) {
            return null;
        }
        // FIXME: 15/10/23 miss
        return new EntityInteraction(MinecraftAdapter.adapt(entityHitResult.getLocation()), null);
    }

    public static BlockInteraction adapt(BlockHitResult blockHitResult) {
        if (blockHitResult == null) {
            return null;
        }
        // FIXME: 15/10/23 miss
        return new BlockInteraction(MinecraftAdapter.adapt(blockHitResult.getLocation()), MinecraftAdapter.adapt(blockHitResult.getDirection()), MinecraftAdapter.adapt(blockHitResult.getBlockPos()), blockHitResult.isInside());
    }

    public static BlockHitResult adapt(BlockInteraction blockHitResult) {
        if (blockHitResult == null) {
            return null;
        }
        // FIXME: 15/10/23 miss
        return new BlockHitResult(MinecraftAdapter.adapt(blockHitResult.getPosition()), MinecraftAdapter.adapt(blockHitResult.getDirection()), MinecraftAdapter.adapt(blockHitResult.getBlockPosition()), blockHitResult.isInside());
    }

    public static TagElement adapt(Tag tag) {
        if (tag == null) {
            return null;
        }
        return new MinecraftTagElement(tag);
    }

    public static Tag adapt(TagElement tag) {
        if (tag == null) {
            return null;
        }
        return ((MinecraftTagElement) tag).getRef();
    }

    public static TagRecord adapt(CompoundTag tag) {
        if (tag == null) {
            return null;
        }
        return new MinecraftTagElement(tag).asRecord();
    }

    public static CompoundTag adapt(TagRecord tag) {
        if (tag == null) {
            return null;
        }
        return ((MinecraftTagRecord) tag).getRef();
    }


    public static Orientation adapt(Direction direction) {
        if (direction == null) {
            return null;
        }
        return switch (direction) {
            case DOWN -> Orientation.DOWN;
            case UP -> Orientation.UP;
            case NORTH -> Orientation.NORTH;
            case SOUTH -> Orientation.SOUTH;
            case WEST -> Orientation.WEST;
            case EAST -> Orientation.EAST;
        };
    }

    public static Direction adapt(Orientation orientation) {
        if (orientation == null) {
            return null;
        }
        return switch (orientation) {
            case DOWN -> Direction.DOWN;
            case UP -> Direction.UP;
            case NORTH -> Direction.NORTH;
            case SOUTH -> Direction.SOUTH;
            case WEST -> Direction.WEST;
            case EAST -> Direction.EAST;
        };
    }


    public static Axis adapt(Direction.Axis axis) {
        if (axis == null) {
            return null;
        }
        return switch (axis) {
            case X -> Axis.X;
            case Y -> Axis.Y;
            case Z -> Axis.Z;
        };
    }

    public static Direction.Axis adapt(Axis axis) {
        if (axis == null) {
            return null;
        }
        return switch (axis) {
            case X -> Direction.Axis.X;
            case Y -> Direction.Axis.Y;
            case Z -> Direction.Axis.Z;
        };
    }

    public static Revolve adapt(Rotation rotation) {
        if (rotation == null) {
            return null;
        }
        return switch (rotation) {
            case NONE -> Revolve.NONE;
            case CLOCKWISE_90 -> Revolve.CLOCKWISE_90;
            case CLOCKWISE_180 -> Revolve.CLOCKWISE_180;
            case COUNTERCLOCKWISE_90 -> Revolve.COUNTERCLOCKWISE_90;
        };
    }

    public static Rotation adapt(Revolve revolve) {
        if (revolve == null) {
            return null;
        }
        return switch (revolve) {
            case NONE -> Rotation.NONE;
            case CLOCKWISE_90 -> Rotation.CLOCKWISE_90;
            case CLOCKWISE_180 -> Rotation.CLOCKWISE_180;
            case COUNTERCLOCKWISE_90 -> Rotation.COUNTERCLOCKWISE_90;
        };
    }

    public static GameMode adapt(GameType gameType) {
        if (gameType == null) {
            return null;
        }
        return switch (gameType) {
            case SURVIVAL -> GameMode.SURVIVAL;
            case CREATIVE -> GameMode.CREATIVE;
            case ADVENTURE -> GameMode.ADVENTURE;
            case SPECTATOR -> GameMode.SPECTATOR;
        };
    }

    public static GameType adapt(GameMode gameMode) {
        if (gameMode == null) {
            return null;
        }
        return switch (gameMode) {
            case SURVIVAL -> GameType.SURVIVAL;
            case CREATIVE -> GameType.CREATIVE;
            case ADVENTURE -> GameType.ADVENTURE;
            case SPECTATOR -> GameType.SPECTATOR;
        };
    }

    public static Text adapt(Component component) {
        if (component == null) {
            return null;
        }
        return new MinecraftText(component);
    }

    public static Component adapt(Text text) {
        if (text == null) {
            return null;
        }
        return ((MinecraftText) text).getRef();
    }

    public static InteractionHand adapt(net.minecraft.world.InteractionHand interactionHand) {
        if (interactionHand == null) {
            return null;
        }
        return switch (interactionHand) {
            case MAIN_HAND -> InteractionHand.MAIN;
            case OFF_HAND -> InteractionHand.OFF;
        };
    }

    public static net.minecraft.world.InteractionHand adapt(InteractionHand interactionHand) {
        if (interactionHand == null) {
            return null;
        }
        return switch (interactionHand) {
            case MAIN -> net.minecraft.world.InteractionHand.MAIN_HAND;
            case OFF -> net.minecraft.world.InteractionHand.OFF_HAND;
        };
    }

    public static Resource adapt(ResourceLocation resourceLocation) {
        if (resourceLocation == null) {
            return null;
        }
        return new MinecraftResource(resourceLocation);
    }

    public static ResourceLocation adapt(Resource resource) {
        if (resource == null) {
            return null;
        }
        if (resource instanceof MinecraftResource minecraftResource) {
            return minecraftResource.getRef();
        }
        return new ResourceLocation(resource.getNamespace(), resource.getPath());
    }

}
