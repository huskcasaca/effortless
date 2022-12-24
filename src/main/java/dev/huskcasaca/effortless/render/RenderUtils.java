package dev.huskcasaca.effortless.render;

import com.mojang.blaze3d.vertex.*;
import dev.huskcasaca.effortless.Effortless;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

@Environment(EnvType.CLIENT)
public class RenderUtils {

    private static RandomSource RAND = RandomSource.create();

    protected static VertexConsumer beginLines(MultiBufferSource.BufferSource renderTypeBuffer) {
        return renderTypeBuffer.getBuffer(BuildRenderType.lines());
    }

    protected static void endLines(MultiBufferSource.BufferSource renderTypeBuffer) {
        renderTypeBuffer.endBatch();
    }

    protected static VertexConsumer beginPlanes(MultiBufferSource multiBufferSource) {
        return multiBufferSource.getBuffer(BuildRenderType.planes());
    }

    protected static void endPlanes(MultiBufferSource.BufferSource renderTypeBuffer) {
        renderTypeBuffer.endBatch();
    }

    protected static void renderBlockPreview(PoseStack poseStack, MultiBufferSource.BufferSource renderTypeBuffer, BlockRenderDispatcher dispatcher,
                                             BlockPos blockPos, BlockState blockState, float dissolve, BlockPos firstPos, BlockPos secondPos, boolean red) {
        if (blockState == null) return;

        poseStack.pushPose();
        var camera = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        poseStack.translate(blockPos.getX() - camera.x, blockPos.getY() - camera.y, blockPos.getZ() - camera.z);
//        poseStack.rotate(Vector3f.YP.rotationDegrees(-90f));
        poseStack.translate(-1 / 256f, -1 / 256f, -1 / 256f);
        poseStack.scale(129 / 128f, 129 / 128f, 129 / 128f);

        //Begin block preview rendering
        var blockPreviewRenderType = BuildRenderType.getBlockPreviewRenderType(dissolve, blockPos, firstPos, secondPos, red);
        var buffer = renderTypeBuffer.getBuffer(blockPreviewRenderType);
//        ItemBlockRenderTypes.getChunkRenderType(blockState);

        try {
            var level = Minecraft.getInstance().level;
            var model = dispatcher.getBlockModel(blockState);
            var seed = blockState.getSeed(firstPos);
            // TODO: 8/9/22
            dispatcher.getModelRenderer().tesselateBlock(level, model, blockState, blockPos, poseStack, buffer, false, RAND, seed, OverlayTexture.NO_OVERLAY);
        } catch (NullPointerException e) {
//            e.printStackTrace();
            Effortless.logger.warn("RenderUtils::renderBlockPreview cannot render " + blockState.getBlock().toString());
            var model = dispatcher.getBlockModel(blockState);
            dispatcher.getModelRenderer().renderModel(poseStack.last(), buffer, blockState, model, 1f, 1f, 1f, 2, OverlayTexture.NO_OVERLAY/*, ModelData.EMPTY, blockPreviewRenderType*/);
        }

        renderTypeBuffer.endBatch();
        poseStack.popPose();
    }

    protected static void renderBlockOutline(PoseStack poseStack, VertexConsumer buffer, BlockPos pos, boolean red) {
        if (red) {
            renderBlockOutline(poseStack, buffer, pos, new Vec3(1f, 0f, 0f));
        } else {
            renderBlockOutline(poseStack, buffer, pos, new Vec3(1f, 1f, 1f));
        }
    }

    protected static void renderBlockOutline(PoseStack poseStack, VertexConsumer buffer, BlockPos pos, Vec3 color) {
        renderBlockOutline(poseStack, buffer, pos, pos, color);
    }

    //Renders outline. Pos1 has to be minimal x,y,z and pos2 maximal x,y,z
    protected static void renderBlockOutline(PoseStack poseStack, VertexConsumer buffer, BlockPos pos1, BlockPos pos2, Vec3 color) {
        AABB aabb = new AABB(pos1, pos2.offset(1, 1, 1)).inflate(0.0020000000949949026);

        LevelRenderer.renderLineBox(poseStack, buffer, aabb, (float) color.x, (float) color.y, (float) color.z, 0.4f);
    }

    protected static void renderBlockOutline(PoseStack poseStack, VertexConsumer buffer, BlockPos pos, VoxelShape voxelShape, boolean red) {
        if (red) {
            renderBlockOutline(poseStack, buffer, pos, voxelShape, new Vec3(1f, 0f, 0f));
        } else {
            renderBlockOutline(poseStack, buffer, pos, voxelShape, new Vec3(1f, 1f, 1f));
        }
    }

    //Renders outline with given bounding box
    protected static void renderBlockOutline(PoseStack poseStack, VertexConsumer buffer, BlockPos pos, VoxelShape voxelShape, Vec3 color) {
        var camera = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        LevelRenderer.renderVoxelShape(poseStack, buffer, voxelShape, pos.getX() - camera.x(), pos.getY() - camera.y(), pos.getZ() - camera.z(), (float) color.x, (float) color.y, (float) color.z, 0.4f);
    }

    //TODO
    //Sends breaking progress for all coordinates to renderglobal, so all blocks get visually broken
//    @Override
//    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
//        Minecraft mc = Minecraft.getInstance();
//
//        var modifierSettings = ModifierSettingsManager.getModifierSettings(mc.player);
//        if (!BuildModifiers.isEnabled(modifierSettings, pos)) return;
//
//        List<BlockPos> coordinates = BuildModifiers.findCoordinates(mc.player, pos);
//        for (int i = 1; i < coordinates.size(); i++) {
//            var coordinate = coordinates.get(i);
//            if (SurvivalHelper.canBreak(mc.world, mc.player, coordinate)) {
//                //Send i as entity id because only one block can be broken per id
//                //Unless i happens to be the player id, then take something else
//                int fakeId = mc.player.getEntityId() != i ? i : coordinates.size();
//                mc.renderGlobal.sendBlockBreakProgress(fakeId, coordinate, progress);
//            }
//        }
//    }
}
