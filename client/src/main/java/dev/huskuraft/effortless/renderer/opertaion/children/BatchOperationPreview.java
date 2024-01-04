package dev.huskuraft.effortless.renderer.opertaion.children;

import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.building.operation.batch.BatchOperationResult;
import dev.huskuraft.effortless.renderer.opertaion.OperationsRenderer;

import java.util.List;

public final class BatchOperationPreview implements OperationPreview {

    private final List<OperationPreview> previews;

    public BatchOperationPreview(OperationsRenderer operationsRenderer, BatchOperationResult result) {
        this.previews = result.getResult().stream().map(operationsRenderer::createRenderer).toList();
    }

    @Override
    public void render(Renderer renderer, float deltaTick) {
        for (var preview : previews) {
            preview.render(renderer, deltaTick);
        }
    }

}
