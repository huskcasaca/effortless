package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.math.Vector3i;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.platform.Server;
import dev.huskuraft.effortless.tag.TagElement;
import dev.huskuraft.effortless.tag.TagRecord;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MinecraftAdapter {

    public static World adapt(Level world) {
        if (world == null) {
            return null;
        }
        return new MinecraftWorld(world);
    }

    public static Level adapt(World world) {
        if (world == null) {
            return null;
        }
        return ((MinecraftWorld) world).getRef();
    }

    public static Player adapt(net.minecraft.world.entity.player.Player player) {
        if (player == null) {
            return null;
        }
        return new MinecraftPlayer(player);
    }

    public static net.minecraft.world.entity.player.Player adapt(Player player) {
        if (player == null) {
            return null;
        }
        return ((MinecraftPlayer) player).getRef();
    }

    public static Server adapt(net.minecraft.server.MinecraftServer server) {
        if (server == null) {
            return null;
        }
        return new MinecraftServer(server);
    }

    public static net.minecraft.server.MinecraftServer adapt(Server server) {
        if (server == null) {
            return null;
        }
        return ((MinecraftServer) server).getRef();
    }

    public static ItemStack adapt(net.minecraft.world.item.ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        return new MinecraftItemStack(itemStack);
    }

    public static net.minecraft.world.item.ItemStack adapt(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        return ((MinecraftItemStack) itemStack).getRef();
    }

    public static Item adapt(net.minecraft.world.item.Item item) {
        if (item == null) {
            return null;
        }
        return new MinecraftItem(item);
    }

    public static net.minecraft.world.item.Item adapt(Item item) {
        if (item == null) {
            return null;
        }
        return ((MinecraftItem) item).getRef();
    }

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

    public static BlockData adapt(BlockState blockState) {
        if (blockState == null) {
            return null;
        }
        return new MinecraftBlockData(blockState);
    }

    public static BlockState adapt(BlockData blockData) {
        if (blockData == null) {
            return null;
        }
        return ((MinecraftBlockData) blockData).getRef();
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


    public static Buffer adapt(FriendlyByteBuf buffer) {
        if (buffer == null) {
            return null;
        }
        return new MinecraftBuffer(buffer);
    }

    public static FriendlyByteBuf adapt(Buffer buffer) {
        if (buffer == null) {
            return null;
        }
        return ((MinecraftBuffer) buffer).getRef();
    }

    public static Text adapt(Component component) {
        if (component == null) {
            return null;
        }
        return new MinecraftText(component);
    }

//    public static Component extract(Object textLike) {
//        if (textLike instanceof Text text) {
//            var component = (Component) null;
//            if (textLike instanceof EmptyText) {
//                component = Component.empty();
//            } else if (textLike instanceof PlainText plainText) {
//                component = Component.literal(plainText.getText());
//            } else if (textLike instanceof TranslatableText translatableText) {
//                if (translatableText.getArgs().length == 0) {
//                    component = Component.translatable(translatableText.getKey());
//                } else {
//                    component = Component.translatable(translatableText.getKey(), Arrays.stream(translatableText.getArgs()).map(MinecraftAdapter::extract).toArray());
//                }
//            } else if (textLike instanceof MinecraftText minecraftText) {
//                component = minecraftText.getRef();
//            }
//            if (component instanceof MutableComponent mutableComponent) {
//                for (var style : text.getStyles()) {
//                    mutableComponent = mutableComponent.withStyle(adapt(style));
//                }
//                for (var sibling : text.getSiblings()) {
//                    mutableComponent = mutableComponent.append(extract(sibling));
//                }
//                return mutableComponent;
//            }
//        }
//        return Component.literal(textLike.toString());
//    }

    public static Component adapt(Text text) {
        if (text == null) {
            return null;
        }
        return ((MinecraftText) text).getRef();
    }

    public static TextStyle adapt(ChatFormatting chatFormatting) {
        if (chatFormatting == null) {
            return null;
        }
        return switch (chatFormatting) {
            case BLACK -> TextStyle.BLACK;
            case DARK_BLUE -> TextStyle.DARK_BLUE;
            case DARK_GREEN -> TextStyle.DARK_GREEN;
            case DARK_AQUA -> TextStyle.DARK_AQUA;
            case DARK_RED -> TextStyle.DARK_RED;
            case DARK_PURPLE -> TextStyle.DARK_PURPLE;
            case GOLD -> TextStyle.GOLD;
            case GRAY -> TextStyle.GRAY;
            case DARK_GRAY -> TextStyle.DARK_GRAY;
            case BLUE -> TextStyle.BLUE;
            case GREEN -> TextStyle.GREEN;
            case AQUA -> TextStyle.AQUA;
            case RED -> TextStyle.RED;
            case LIGHT_PURPLE -> TextStyle.LIGHT_PURPLE;
            case YELLOW -> TextStyle.YELLOW;
            case WHITE -> TextStyle.WHITE;
            case OBFUSCATED -> TextStyle.OBFUSCATED;
            case BOLD -> TextStyle.BOLD;
            case STRIKETHROUGH -> TextStyle.STRIKETHROUGH;
            case UNDERLINE -> TextStyle.UNDERLINE;
            case ITALIC -> TextStyle.ITALIC;
            case RESET -> TextStyle.RESET;
        };
    }

    public static ChatFormatting adapt(TextStyle textStyle) {
        if (textStyle == null) {
            return null;
        }
        return switch (textStyle) {
            case BLACK -> ChatFormatting.BLACK;
            case DARK_BLUE -> ChatFormatting.DARK_BLUE;
            case DARK_GREEN -> ChatFormatting.DARK_GREEN;
            case DARK_AQUA -> ChatFormatting.DARK_AQUA;
            case DARK_RED -> ChatFormatting.DARK_RED;
            case DARK_PURPLE -> ChatFormatting.DARK_PURPLE;
            case GOLD -> ChatFormatting.GOLD;
            case GRAY -> ChatFormatting.GRAY;
            case DARK_GRAY -> ChatFormatting.DARK_GRAY;
            case BLUE -> ChatFormatting.BLUE;
            case GREEN -> ChatFormatting.GREEN;
            case AQUA -> ChatFormatting.AQUA;
            case RED -> ChatFormatting.RED;
            case LIGHT_PURPLE -> ChatFormatting.LIGHT_PURPLE;
            case YELLOW -> ChatFormatting.YELLOW;
            case WHITE -> ChatFormatting.WHITE;
            case OBFUSCATED -> ChatFormatting.OBFUSCATED;
            case BOLD -> ChatFormatting.BOLD;
            case STRIKETHROUGH -> ChatFormatting.STRIKETHROUGH;
            case UNDERLINE -> ChatFormatting.UNDERLINE;
            case ITALIC -> ChatFormatting.ITALIC;
            case RESET -> ChatFormatting.RESET;
        };
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

    public static PerformResult adapt(net.minecraft.world.InteractionResult interactionResult) {
        if (interactionResult == null) {
            return null;
        }
        return switch (interactionResult) {
            case SUCCESS -> PerformResult.SUCCESS;
            case CONSUME -> PerformResult.CONSUME;
            case CONSUME_PARTIAL -> PerformResult.CONSUME_PARTIAL;
            case PASS -> PerformResult.PASS;
            case FAIL -> PerformResult.FAIL;
        };
    }

    public static net.minecraft.world.InteractionResult adapt(PerformResult performResult) {
        if (performResult == null) {
            return null;
        }
        return switch (performResult) {
            case SUCCESS -> net.minecraft.world.InteractionResult.SUCCESS;
            case CONSUME -> net.minecraft.world.InteractionResult.CONSUME;
            case CONSUME_PARTIAL -> net.minecraft.world.InteractionResult.CONSUME_PARTIAL;
            case PASS -> net.minecraft.world.InteractionResult.PASS;
            case FAIL -> net.minecraft.world.InteractionResult.FAIL;
        };
    }

}
