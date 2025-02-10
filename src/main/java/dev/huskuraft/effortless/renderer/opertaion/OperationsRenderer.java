package dev.huskuraft.effortless.renderer.opertaion;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.universal.api.renderer.RenderFadeEntry;
import dev.huskuraft.universal.api.renderer.Renderer;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.operation.batch.BatchOperationResult;
import dev.huskuraft.effortless.building.operation.block.BlockInteractOperationResult;
import dev.huskuraft.effortless.building.operation.block.BlockStateCopyOperationResult;
import dev.huskuraft.effortless.building.operation.block.BlockStateUpdateOperationResult;
import dev.huskuraft.effortless.renderer.opertaion.children.BatchOperationRenderer;
import dev.huskuraft.effortless.renderer.opertaion.children.BlockOperationRenderer;
import dev.huskuraft.effortless.renderer.opertaion.children.OperationRenderer;

public class OperationsRenderer {

    private final Map<Object, RenderFadeEntry<? extends OperationRenderer>> results = Collections.synchronizedMap(new LinkedHashMap<>());
    private final Map<Class<?>, BiFunction<OperationsRenderer, OperationResult, ? extends OperationRenderer>> resultRendererMap = Collections.synchronizedMap(new HashMap<>());

    private final EffortlessClient entrance;

    public OperationsRenderer(EffortlessClient entrance) {
        this.entrance = entrance;
        registerRenderers();
    }

    public EffortlessClient getEntrance() {
        return entrance;
    }

    private <R extends OperationResult, O extends OperationRenderer> void registerRenderer(Class<R> result, BiFunction<OperationsRenderer, R, O> renderer) {
        resultRendererMap.put(result, (BiFunction<OperationsRenderer, OperationResult, O>) renderer);
    }

    @SuppressWarnings({"unchecked"})
    public <R extends OperationResult> OperationRenderer createRenderer(R result) {
        try {
            return resultRendererMap.get(result.getClass()).apply(this, result);
        } catch (Exception e) {
            Effortless.LOGGER.error("No renderer found for result: " + result.getClass().getSimpleName(), e);
            throw e;
        }
    }

    private void registerRenderers() {
        registerRenderer(BlockInteractOperationResult.class, BlockOperationRenderer::new);
        registerRenderer(BlockStateCopyOperationResult.class, BlockOperationRenderer::new);
        registerRenderer(BlockStateUpdateOperationResult.class, BlockOperationRenderer::new);

        registerRenderer(BatchOperationResult.class, BatchOperationRenderer::new);
    }

    public <R extends OperationResult> void showResult(Object id, R result) {
        results.put(id, new RenderFadeEntry<>(createRenderer(result)));
    }

    public void remove(Object id) {
        results.remove(id);
    }

    public void tick() {
        var iterator = results.values().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            entry.tick();
            if (!entry.isAlive()) {
                iterator.remove();
            }
        }
    }

    public void render(Renderer renderer, float deltaTick) {
        var renderParams = new OperationRenderer.RenderContext.Default(
                getEntrance().getConfigStorage().get().renderConfig().showBlockPreview(),
                getEntrance().getConfigStorage().get().renderConfig().maxRenderVolume(),
                getEntrance().getClientManager().getRunningClient().getOptions().renderDistance() * 16 + 16);
        results.forEach((k, v) -> {
            v.getValue().render(renderer, renderParams, deltaTick);
        });
    }

}
