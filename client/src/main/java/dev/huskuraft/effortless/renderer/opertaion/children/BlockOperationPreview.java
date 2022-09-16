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
        var blockData = operation.getBlockData();
        if (blockData == null) return;

//        if (item instanceof BlockItem blockItem && itemStack.is(item)) {
//            blockData = blockItem.updateBlockStateFromTag(blockPosition, level, itemStack, blockData);
//        }
        var color = result.getColor();
        if (color == null) return;

        renderer.drawBlockModelByCamera(world, blockPosition, blockData, color);
    }

}
