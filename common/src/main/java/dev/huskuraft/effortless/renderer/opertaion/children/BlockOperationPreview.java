package dev.huskuraft.effortless.renderer.opertaion.children;

import java.awt.*;
import java.util.List;

import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.building.operation.block.BlockBreakOperationResult;
import dev.huskuraft.effortless.building.operation.block.BlockOperationResult;
import dev.huskuraft.effortless.building.operation.block.BlockPlaceOperationResult;
import dev.huskuraft.effortless.renderer.opertaion.BlockRenderLayers;
import dev.huskuraft.effortless.renderer.opertaion.OperationsRenderer;

public class BlockOperationPreview implements OperationPreview {

    public static final Color BLOCK_BREAK_OP_COLOR = new Color(1f, 0, 0, 0.5f);
    public static final Color BLOCK_PLACE_SUCC_OP_COLOR = new Color(235, 235, 235);
    public static final Color BLOCK_PLACE_FAIL_OP_COLOR = new Color(255, 0, 0);

    private final BlockOperationResult result;

    public BlockOperationPreview(OperationsRenderer operationsRenderer, BlockOperationResult result) {
        this.result = result;
    }

    @Override
    public void render(Renderer renderer, RendererParams rendererParams, float deltaTick) {
        if (!rendererParams.shouldRenderBlocks()) {
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

        var color = getColorByOpResult(result);

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

    public static Color getColorByOpResult(BlockOperationResult blockOperationResult) {
        if (blockOperationResult instanceof BlockPlaceOperationResult) {
            return switch (blockOperationResult.result()) {
                case SUCCESS, CONSUME -> Color.WHITE;
                case FAIL_ITEM_INSUFFICIENT -> Color.RED;
                default -> Color.GRAY;
            };
        }
        if (blockOperationResult instanceof BlockBreakOperationResult) {
            return switch (blockOperationResult.result()) {
                case SUCCESS, CONSUME -> BLOCK_BREAK_OP_COLOR;
                default -> Color.GRAY;
            };
        }
        return null;
    }

    public static List<Color> getAllColors() {
        return List.of(
                Color.WHITE,
                Color.RED,
                Color.GRAY,
                BLOCK_BREAK_OP_COLOR,
                BLOCK_PLACE_SUCC_OP_COLOR,
                BLOCK_PLACE_FAIL_OP_COLOR
        );
    }

}
