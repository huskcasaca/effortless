package dev.huskuraft.effortless.renderer.opertaion.children;

import dev.huskuraft.effortless.building.operation.block.BlockOperationResult;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.renderer.opertaion.OperationsRenderer;

public class BlockOperationPreview implements OperationPreview {

    private final BlockOperationResult result;

    public BlockOperationPreview(OperationsRenderer operationsRenderer, BlockOperationResult result) {
        this.result = result;
    }

    @Override
    public void render(Renderer renderer, float deltaTick) {
        var operation = result.getOperation();
        var world = operation.getWorld();
        var blockPosition = operation.getInteraction().getBlockPosition();
        var blockState = operation.getBlockState();
        if (blockState == null) return;

//        if (item instanceof BlockItem blockItem && itemStack.is(item)) {
//            blockState = blockItem.updateBlockStateFromTag(blockPosition, level, itemStack, blockState);
//        }
        var color = result.getColor();
        if (color == null) return;

        var scale = 129 / 128f;
        var camera = renderer.camera().position();

        renderer.pushPose();
        renderer.translate(blockPosition.x() - camera.x(), blockPosition.y() - camera.y(), blockPosition.z() - camera.z());
        renderer.translate((scale - 1) / -2, (scale - 1) / -2, (scale - 1) / -2);
        renderer.scale(scale, scale, scale);
        renderer.renderBlockInWorld(renderer.blockRenderTextures().block(color.getRGB()), world, blockPosition, blockState);
        renderer.popPose();
    }

}
