package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
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

    public static Vec3i toPlatformVector3i(Vector3i vector) {
        return new Vec3i(vector.x(), vector.y(), vector.z());
    }

    public static Vec3 toPlatformVector3d(Vector3d vector) {
        return new Vec3(vector.x(), vector.y(), vector.z());
    }

    public static BlockPosition toPlatformBlockPosition(BlockPos vector) {
        return new BlockPosition(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vector3d fromPlatformMinecraftVector3d(Vec3 vector) {
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
        return new EntityInteraction(fromPlatformMinecraftVector3d(entityHitResult.getLocation()), null);
    }

    public static BlockInteraction fromPlatformBlockInteraction(BlockHitResult blockHitResult) {
        if (blockHitResult == null) {
            return null;
        }
        return new BlockInteraction(fromPlatformMinecraftVector3d(blockHitResult.getLocation()), fromPlatformOrientation(blockHitResult.getDirection()), toPlatformBlockPosition(blockHitResult.getBlockPos()), blockHitResult.isInside());
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

    public static net.minecraft.world.InteractionHand toPlatformInteractionHand() {
        return net.minecraft.world.InteractionHand.MAIN_HAND;
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

    public static BlockState fromPlatformBlockState(net.minecraft.world.level.block.state.BlockState blockState) {
        return new MinecraftBlockState(blockState);
    }

    public static net.minecraft.world.level.block.state.BlockState toPlatformBlockState(BlockState blockState) {
        return blockState.reference();
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

    public static KeyBinding fromPlatformKeyBinding(KeyMapping keyBinding) {
        return new MinecraftKeyBinding(keyBinding);
    }

    public static KeyMapping toPlatformKeyBinding(KeyBinding keyBinding) {
        return keyBinding.reference();
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

//    public static Renderer fromPlatformRenderer(GuiGraphics renderer) {
//        return new MinecraftRenderer(renderer.pose());
//    }

    public static RenderLayer fromPlatformRenderLayer(RenderType renderLayer) {
        return () -> renderLayer;
    }

    public static RenderType toPlatformRenderLayer(RenderLayer renderLayer) {
        return renderLayer.reference();
    }

    public static Resource fromPlatformResource(ResourceLocation resource) {
        return new MinecraftResource(resource);
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

    public static Text fromPlatformText(Component text) {
        if (text == null) {
            return null;
        }
        return new MinecraftText(text);
    }

    public static Component toPlatformText(Text text) {
        if (text == null) {
            return null;
        }
        return text.reference();
    }

    public static Typeface fromPlatformTypeface(Font typeface) {
        return new MinecraftTypeface(typeface);
    }

    public static Font toPlatformTypeface(Typeface typeface) {
        return typeface.reference();
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
