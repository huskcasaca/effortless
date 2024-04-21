package dev.huskuraft.effortless.renderer.opertaion.children;

import java.awt.*;
import java.util.List;

import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.building.operation.block.BlockBreakOperationResult;
import dev.huskuraft.effortless.building.operation.block.BlockOperationResult;
import dev.huskuraft.effortless.building.operation.block.BlockPlaceOperationResult;
import dev.huskuraft.effortless.renderer.opertaion.BlockRenderLayers;
import dev.huskuraft.effortless.renderer.opertaion.OperationsRenderer;

public class BlockOperationRenderer implements OperationRenderer {

    public static final Color BLOCK_PLACE_SUCCESS_COLOR = new Color(255, 255, 255);
    public static final Color BLOCK_PLACE_INSUFFICIENT_COLOR = new Color(235, 0, 0);
    public static final Color BLOCK_PLACE_FAIL_COLOR = new Color(128, 128, 128);

    public static final Color BLOCK_BREAK_SUCCESS_COLOR = new Color(235, 0, 0);
    public static final Color BLOCK_BREAK_FAIL_COLOR = new Color(128, 128, 128);

    private final BlockOperationResult result;

    public BlockOperationRenderer(OperationsRenderer operationsRenderer, BlockOperationResult result) {
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
            if (blockOperationResult.getOperation().getBlockState().isAir()) {
                return Color.GRAY;
            }
            return switch (blockOperationResult.result()) {
                case SUCCESS, CONSUME -> BLOCK_PLACE_SUCCESS_COLOR;
                case FAIL_ITEM_INSUFFICIENT -> BLOCK_PLACE_INSUFFICIENT_COLOR;
                default -> BLOCK_PLACE_FAIL_COLOR;
            };
        }
        if (blockOperationResult instanceof BlockBreakOperationResult) {
            return switch (blockOperationResult.result()) {
                case SUCCESS, CONSUME -> BLOCK_BREAK_SUCCESS_COLOR;
                default -> BLOCK_BREAK_FAIL_COLOR;
            };
        }
        return null;
    }

    public static List<Color> getAllColors() {
        return List.of(
                BLOCK_PLACE_SUCCESS_COLOR,
                BLOCK_PLACE_INSUFFICIENT_COLOR,
                BLOCK_PLACE_FAIL_COLOR,
                BLOCK_BREAK_SUCCESS_COLOR,
                BLOCK_BREAK_FAIL_COLOR
        );
    }

}
