package dev.huskuraft.effortless.renderer.opertaion.children;

import java.util.List;

import dev.huskuraft.universal.api.renderer.Renderer;
import dev.huskuraft.effortless.building.operation.batch.BatchOperationResult;
import dev.huskuraft.effortless.renderer.opertaion.OperationsRenderer;

public final class BatchOperationRenderer implements OperationRenderer {

    private final List<OperationRenderer> previews;

    public BatchOperationRenderer(OperationsRenderer operationsRenderer, BatchOperationResult result) {
        this.previews = result.getResults().stream().map(operationsRenderer::createRenderer).toList();
    }

    @Override
    public void render(Renderer renderer, RenderContext renderContext, float deltaTick) {
        for (var preview : previews) {
            preview.render(renderer, renderContext, deltaTick);
        }
    }

}
