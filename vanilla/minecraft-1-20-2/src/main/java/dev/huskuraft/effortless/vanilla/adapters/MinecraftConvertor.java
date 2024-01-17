package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.huskuraft.effortless.api.core.*;
import dev.huskuraft.effortless.api.gui.Typeface;
import dev.huskuraft.effortless.api.input.KeyBinding;
import dev.huskuraft.effortless.api.math.*;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.platform.Client;
import dev.huskuraft.effortless.api.platform.Server;
import dev.huskuraft.effortless.api.renderer.*;
import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.api.text.Text;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
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

    public static BlockPos toPlatformBlockPosition(BlockPosition value) {
        return new BlockPos(value.x(), value.y(), value.z());
    }

    public static Vec3i toPlatformVector3i(Vector3i value) {
        return new Vec3i(value.x(), value.y(), value.z());
    }

    public static Vec3 toPlatformVector3d(Vector3d value) {
        return new Vec3(value.x(), value.y(), value.z());
    }

    public static BlockPosition toPlatformBlockPosition(BlockPos value) {
        return new BlockPosition(value.getX(), value.getY(), value.getZ());
    }

    public static Vector3d fromPlatformMinecraftVector3d(Vec3 value) {
        return new Vector3d(value.x(), value.y(), value.z());
    }

    public static Vector3i fromPlatformVector3i(Vec3i value) {
        return new Vector3i(value.getX(), value.getY(), value.getZ());
    }

    public static Interaction fromPlatformInteraction(HitResult value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BlockHitResult blockHitResult) {
            return fromPlatformBlockInteraction(blockHitResult);
        }
        if (value instanceof EntityHitResult entityHitResult) {
            return fromPlatformEntityInteraction(entityHitResult);
        }
        throw new IllegalArgumentException("Unknown Interaction: " + value);
    }

    public static EntityInteraction fromPlatformEntityInteraction(EntityHitResult value) {
        if (value == null) {
            return null;
        }
        return new EntityInteraction(fromPlatformMinecraftVector3d(value.getLocation()), null);
    }

    public static BlockInteraction fromPlatformBlockInteraction(BlockHitResult value) {
        if (value == null) {
            return null;
        }
        return new BlockInteraction(fromPlatformMinecraftVector3d(value.getLocation()), fromPlatformOrientation(value.getDirection()), toPlatformBlockPosition(value.getBlockPos()), value.isInside());
    }

    public static BlockHitResult toPlatformBlockInteraction(BlockInteraction value) {
        if (value == null) {
            return null;
        }
        return new BlockHitResult(
                toPlatformVector3d(value.getPosition()),
                toPlatformOrientation(value.getDirection()),
                toPlatformBlockPosition(value.getBlockPosition()),
                value.isInside());
    }

    public static InteractionHand fromPlatformInteractionHand(net.minecraft.world.InteractionHand value) {
        return switch (value) {
            case MAIN_HAND -> InteractionHand.MAIN;
            case OFF_HAND -> InteractionHand.OFF;
        };
    }

    public static net.minecraft.world.InteractionHand toPlatformInteractionHand() {
        return net.minecraft.world.InteractionHand.MAIN_HAND;
    }

    public static net.minecraft.world.InteractionHand toPlatformInteractionHand(InteractionHand value) {
        return switch (value) {
            case MAIN -> net.minecraft.world.InteractionHand.MAIN_HAND;
            case OFF -> net.minecraft.world.InteractionHand.OFF_HAND;
        };
    }

    public static Orientation fromPlatformOrientation(Direction value) {
        return switch (value) {
            case DOWN -> Orientation.DOWN;
            case UP -> Orientation.UP;
            case NORTH -> Orientation.NORTH;
            case SOUTH -> Orientation.SOUTH;
            case WEST -> Orientation.WEST;
            case EAST -> Orientation.EAST;
        };
    }

    public static Direction toPlatformOrientation(Orientation value) {
        return switch (value) {
            case DOWN -> Direction.DOWN;
            case UP -> Direction.UP;
            case NORTH -> Direction.NORTH;
            case SOUTH -> Direction.SOUTH;
            case WEST -> Direction.WEST;
            case EAST -> Direction.EAST;
        };
    }

    public static BlockState fromPlatformBlockState(net.minecraft.world.level.block.state.BlockState value) {
        return new MinecraftBlockState(value);
    }

    public static net.minecraft.world.level.block.state.BlockState toPlatformBlockState(BlockState value) {
        return value.reference();
    }

    public static Buffer fromPlatformBuffer(FriendlyByteBuf buffer) {
        return new MinecraftBuffer(buffer);
    }

    public static FriendlyByteBuf toPlatformBuffer(Buffer buffer) {
        return buffer.reference();
    }

    public static BufferSource fromPlatformBufferSource(MultiBufferSource.BufferSource bufferSource) {
        return new MinecraftBufferSource(bufferSource);
    }

    public static Camera fromPlatformCamera(net.minecraft.client.Camera camera) {
        return new MinecraftCamera(camera);
    }

    public static Client fromPlatformClient(Minecraft minecraft) {
        return new MinecraftClient(minecraft);
    }

    public static Item fromPlatformItem(net.minecraft.world.item.Item item) {
        return new MinecraftItem(item);
    }

    public static net.minecraft.world.item.Item toPlatformItem(Item item) {
        return item.reference();
    }

    public static ItemStack fromPlatformItemStack(net.minecraft.world.item.ItemStack itemStack) {
        return new MinecraftItemStack(itemStack);
    }

    public static ItemStack fromPlatformItemStack(net.minecraft.world.item.Item item, int count) {
        return new MinecraftItemStack(item, count);
    }

    public static ItemStack fromPlatformItemStack(net.minecraft.world.item.Item item, CompoundTag tag, int count) {
        return new MinecraftItemStack(item, tag, count);
    }

    public static net.minecraft.world.item.ItemStack toPlatformItemStack(ItemStack itemStack) {
        return itemStack.reference();
    }

    public static KeyBinding fromPlatformKeyBinding(KeyMapping value) {
        return new MinecraftKeyBinding(value);
    }

    public static KeyMapping toPlatformKeyBinding(KeyBinding value) {
        return value.reference();
    }

    public static MatrixStack fromPlatformMatrixStack(PoseStack matrixStack) {
        return new MinecraftMatrixStack(matrixStack);
    }

    public static PoseStack toPlatformMatrixStack(MatrixStack matrixStack) {
        return matrixStack.reference();
    }

    public static Player fromPlatformPlayer(net.minecraft.world.entity.player.Player player) {
        if (player == null) {
            return null;
        }
        return new MinecraftPlayer(player);
    }

    public static net.minecraft.world.entity.player.Player toPlatformPlayer(Player player) {
        if (player == null) {
            return null;
        }
        return player.reference();
    }

    public static Renderer fromPlatformRenderer(PoseStack matrixStack) {
        return new MinecraftRenderer(matrixStack);
    }

    public static Renderer fromPlatformRenderer(GuiGraphics renderer) {
        return new MinecraftRenderer(renderer.pose());
    }

    public static RenderLayer fromPlatformRenderLayer(RenderType renderLayer) {
        return () -> renderLayer;
    }

    public static RenderType toPlatformRenderLayer(RenderLayer renderLayer) {
        return renderLayer.reference();
    }

    public static Resource fromPlatformResource(ResourceLocation reference) {
        return new MinecraftResource(reference);
    }

    public static ResourceLocation toPlatformResource(Resource resource) {
        return resource.reference();
    }

    public static Server fromPlatformServer(net.minecraft.server.MinecraftServer server) {
        return new MinecraftServer(server);
    }

    public static TagRecord fromPlatformTagRecord(CompoundTag tag) {
        return new MinecraftTagRecord(tag);
    }

    public static CompoundTag toPlatformTagRecord(TagRecord tag) {
        return ((MinecraftTagRecord) tag).referenceValue();
    }

    public static TagElement fromPlatformTagElement(Tag tag) {
        if (tag == null) {
            return null;
        }
        return new MinecraftTagElement(tag);
    }

    public static Tag toPlatformTagElement(TagElement tag) {
        if (tag == null) {
            return null;
        }
        return ((MinecraftTagElement) tag).referenceValue();
    }

    public static Text fromPlatformText(Component value) {
        if (value == null) {
            return null;
        }
        return new MinecraftText(value);
    }

    public static Component toPlatformText(Text value) {
        if (value == null) {
            return null;
        }
        return value.reference();
    }

    public static Typeface fromPlatformTypeface(Font value) {
        return new MinecraftTypeface(value);
    }

    public static Font toPlatformTypeface(Typeface value) {
        return value.reference();
    }

    public static VertexBuffer fromPlatformVertexBuffer(VertexConsumer vertexBuffer) {
        return new MinecraftVertexBuffer(vertexBuffer);
    }

    public static Window fromPlatformWindow(com.mojang.blaze3d.platform.Window window) {
        return new MinecraftWindow(window);
    }

    public static World fromPlatformWorld(Level world) {
        return new MinecraftWorld(world);
    }

    public static Level toPlatformWorld(World world) {
        return world.reference();
    }
}
