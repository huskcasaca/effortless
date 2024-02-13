package dev.huskuraft.effortless.renderer.opertaion;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

import dev.huskuraft.effortless.api.renderer.RenderFadeEntry;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.operation.batch.BatchOperationResult;
import dev.huskuraft.effortless.building.operation.block.BlockBreakOnClientOperationResult;
import dev.huskuraft.effortless.building.operation.block.BlockBreakOperationResult;
import dev.huskuraft.effortless.building.operation.block.BlockPlaceOnClientOperationResult;
import dev.huskuraft.effortless.building.operation.block.BlockPlaceOperationResult;
import dev.huskuraft.effortless.renderer.opertaion.children.BatchOperationPreview;
import dev.huskuraft.effortless.renderer.opertaion.children.BlockOperationPreview;
import dev.huskuraft.effortless.renderer.opertaion.children.OperationPreview;

public class OperationsRenderer {

    private final Map<Object, RenderFadeEntry<? extends OperationPreview>> results = Collections.synchronizedMap(new LinkedHashMap<>());
    private final Map<Class<?>, BiFunction<OperationsRenderer, OperationResult, ? extends OperationPreview>> resultRendererMap = Collections.synchronizedMap(new HashMap<>());

    public OperationsRenderer() {
        registerRenderers();
    }

    private <R extends OperationResult, O extends OperationPreview> void registerRenderer(Class<R> result, BiFunction<OperationsRenderer, R, O> renderer) {
        resultRendererMap.put(result, (BiFunction<OperationsRenderer, OperationResult, O>) renderer);
    }

    @SuppressWarnings({"unchecked"})
    public <R extends OperationResult> OperationPreview createRenderer(R result) {
        try {
            return resultRendererMap.get(result.getClass()).apply(this, result);
        } catch (Exception e) {
            throw e;
        }
    }

    private void registerRenderers() {
        registerRenderer(BlockPlaceOperationResult.class, BlockOperationPreview::new);
        registerRenderer(BlockBreakOperationResult.class, BlockOperationPreview::new);
        registerRenderer(BlockPlaceOnClientOperationResult.class, BlockOperationPreview::new);
        registerRenderer(BlockBreakOnClientOperationResult.class, BlockOperationPreview::new);
        registerRenderer(BatchOperationResult.class, BatchOperationPreview::new);
    }

    public <R extends OperationResult> void showResult(Object id, R result) {
        results.put(id, new RenderFadeEntry<>(createRenderer(result)));
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
        results.forEach((k, v) -> {
            v.getValue().render(renderer, deltaTick);
        });
    }

}
