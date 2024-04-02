package dev.huskuraft.effortless.renderer.opertaion.children;

import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.building.operation.block.BlockOperationResult;
import dev.huskuraft.effortless.renderer.opertaion.BlockRenderLayers;
import dev.huskuraft.effortless.renderer.opertaion.OperationsRenderer;

public class BlockOperationPreview implements OperationPreview {

    private final BlockOperationResult result;

    public BlockOperationPreview(OperationsRenderer operationsRenderer, BlockOperationResult result) {
        this.result = result;
    }

    @Override
    public void render(Renderer renderer, RendererParams rendererParams, float deltaTick) {
        if (rendererParams.isFull()) {
            return;
        }

        var operation = result.getOperation();
        var world = operation.getWorld();
        var player = operation.getPlayer();
        var blockPosition = operation.getInteraction().getBlockPosition();
        var blockState = operation.getBlockState();
        if (world == null || blockState == null || player == null) {
            return;
        }

//        if (item instanceof BlockItem blockItem && itemStack.is(item)) {
//            blockState = blockItem.updateBlockStateFromTag(blockPosition, level, itemStack, blockState);
//        }

        var color = result.getColor();
        if (color == null) {
            return;
        }
        var distance = player.getPosition().distance(blockPosition.toVector3d());
        if (distance > rendererParams.maxRenderDistance()) {
            return;
        }

        var scale = 129f / 128f;
        var camera = renderer.camera().position();

        renderer.pushPose();
        renderer.translate(blockPosition.toVector3d().sub(camera));
        renderer.translate((scale - 1) / -2f, (scale - 1) / -2f, (scale - 1) / -2f);
        renderer.scale(scale, scale, scale);
        renderer.renderBlockInWorld(BlockRenderLayers.block(color.getRGB()), world, blockPosition, blockState);
        renderer.popPose();

        rendererParams.accumulate();
    }

}
