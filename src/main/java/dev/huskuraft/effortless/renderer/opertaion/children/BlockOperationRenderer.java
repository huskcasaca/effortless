package dev.huskuraft.effortless.renderer.opertaion.children;

import java.awt.*;
import java.util.List;

import dev.huskuraft.universal.api.renderer.Renderer;
import dev.huskuraft.effortless.building.operation.block.BlockOperationResult;
import dev.huskuraft.effortless.renderer.opertaion.BlockRenderLayers;
import dev.huskuraft.effortless.renderer.opertaion.OperationsRenderer;

public class BlockOperationRenderer implements OperationRenderer {

    public static final Color BLOCK_PLACE_SUCCESS_COLOR = new Color(255, 255, 255);
    public static final Color BLOCK_PLACE_INSUFFICIENT_COLOR = new Color(235, 0, 0);
    public static final Color BLOCK_PLACE_FAIL_COLOR = new Color(128, 128, 128);

    public static final Color BLOCK_BREAK_SUCCESS_COLOR = new Color(235, 0, 0);
    public static final Color BLOCK_BREAK_FAIL_COLOR = BLOCK_PLACE_FAIL_COLOR;

    public static final Color BLOCK_INTERACT_SUCCESS_COLOR = Color.YELLOW;
    public static final Color BLOCK_INTERACT_FAIL_COLOR = BLOCK_PLACE_FAIL_COLOR;

    public static final Color BLOCK_COPY_SUCCESS_COLOR = new Color(0, 235, 0);
    public static final Color BLOCK_COPY_FAIL_COLOR = BLOCK_PLACE_FAIL_COLOR;

    public static final Color BLOCK_HIDEEN_COLOR = null;

    private final BlockOperationResult result;

    public BlockOperationRenderer(OperationsRenderer operationsRenderer, BlockOperationResult result) {
        this.result = result;
    }

    public static Color getColorByOpResult(BlockOperationResult blockOperationResult) {
        switch (blockOperationResult.getOperation().getType()) {
            case UPDATE -> {
                if (blockOperationResult.getBlockStateToBreak() == null || blockOperationResult.getBlockStateToPlace() == null) {
                    return BLOCK_HIDEEN_COLOR;
                }
                if (!blockOperationResult.getBlockStateToPlace().isAir()) {
                    return switch (blockOperationResult.result()) {
                        case SUCCESS, SUCCESS_PARTIAL, CONSUME -> BLOCK_PLACE_SUCCESS_COLOR;
                        case FAIL_PLACE_ITEM_INSUFFICIENT, FAIL_PLACE_ITEM_NOT_BLOCK -> BLOCK_PLACE_INSUFFICIENT_COLOR;
                        case FAIL_WORLD_HEIGHT, FAIL_WORLD_BORDER, FAIL_WORLD_INCORRECT_DIM -> BLOCK_HIDEEN_COLOR;
                        default -> BLOCK_PLACE_FAIL_COLOR;
                    };
                }
                return switch (blockOperationResult.result()) {
                    case SUCCESS, SUCCESS_PARTIAL, CONSUME -> BLOCK_BREAK_SUCCESS_COLOR;
                    case FAIL_WORLD_HEIGHT, FAIL_WORLD_BORDER, FAIL_WORLD_INCORRECT_DIM -> BLOCK_HIDEEN_COLOR;
                    default -> BLOCK_BREAK_FAIL_COLOR;
                };

            }
            case INTERACT -> {
                return switch (blockOperationResult.result()) {
                    case SUCCESS, SUCCESS_PARTIAL, CONSUME -> BLOCK_INTERACT_SUCCESS_COLOR;
                    case FAIL_WORLD_HEIGHT, FAIL_WORLD_BORDER, FAIL_WORLD_INCORRECT_DIM -> BLOCK_HIDEEN_COLOR;
                    default -> BLOCK_INTERACT_FAIL_COLOR;
                };
            }
            case COPY -> {
                return switch (blockOperationResult.result()) {
                    case SUCCESS, SUCCESS_PARTIAL, CONSUME -> BLOCK_COPY_SUCCESS_COLOR;
                    case FAIL_WORLD_HEIGHT, FAIL_WORLD_BORDER, FAIL_WORLD_INCORRECT_DIM -> BLOCK_HIDEEN_COLOR;
                    default -> BLOCK_COPY_FAIL_COLOR;
                };
            }
        }
        return BLOCK_HIDEEN_COLOR;
    }

    public static List<Color> getAllColors() {
        return List.of(
                BLOCK_PLACE_SUCCESS_COLOR,
                BLOCK_PLACE_INSUFFICIENT_COLOR,
                BLOCK_PLACE_FAIL_COLOR,
                BLOCK_BREAK_SUCCESS_COLOR,
                BLOCK_BREAK_FAIL_COLOR,
                BLOCK_INTERACT_SUCCESS_COLOR,
                BLOCK_INTERACT_FAIL_COLOR,
                BLOCK_COPY_SUCCESS_COLOR,
                BLOCK_COPY_FAIL_COLOR
        );
    }

    public BlockOperationResult getResult() {
        return result;
    }

    @Override
    public void render(Renderer renderer, RenderContext renderContext, float deltaTick) {
        if (!renderContext.showBlockPreview()) {
            return;
        }

        var operation = getResult().getOperation();

        if (renderContext.maxRenderVolume() < operation.getContext().getVolume()) {
            return;
        }

        var world = operation.getWorld();
        var player = operation.getPlayer();
        var blockPosition = operation.getBlockPosition();
        var blockState = getResult().getBlockStateForRenderer();
        var blockEntity = getResult().getBlockEntityForRenderer();
        if (world == null || player == null) {
            return;
        }

        var scale = 129f / 128f;
        var camera = renderer.getCamera().position();

        var distance = player.getPosition().distance(blockPosition.toVector3d());
        if (distance > renderContext.maxRenderDistance()) {
            return;
        }

        var color = getColorByOpResult(getResult());

        if (color == null) {
            return;
        }

        renderer.pushPose();
        renderer.translate(blockPosition.toVector3d().sub(camera));
        if (blockEntity != null) {
            renderer.renderBlockEntity(BlockRenderLayers.block(color.getRGB()), world, blockPosition, blockEntity);
        }

        if (blockState != null) {
            renderer.translate((scale - 1) / -2f, (scale - 1) / -2f, (scale - 1) / -2f);
            renderer.scale(scale, scale, scale);
            renderer.renderBlockState(BlockRenderLayers.block(color.getRGB()), world, blockPosition, blockState);
        }

        renderer.popPose();

    }

}
