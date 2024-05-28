package dev.huskuraft.effortless.building.operation;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.Context;

public record OperationTooltip(
        Type type,
        Context context,
        Map<BlockSummary, Map<BlockState, Integer>> blockSummary,
        Map<EntitySummary, Map<BlockState, Integer>> entitySummary
) {

    public static OperationTooltip reduce(
            Type type,
            Context context,
            Map<BlockSummary, List<BlockState>> blockSummary,
            Map<EntitySummary, List<BlockState>> entitySummary
    ) {
        var flattenBlockSummary = blockSummary.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().collect(Collectors.toMap(Function.identity(), e1 -> 1, Integer::sum, LinkedHashMap::new))));
        var flattenEntitySummary = entitySummary.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().collect(Collectors.toMap(Function.identity(), e1 -> 1, Integer::sum, LinkedHashMap::new))));
        return new OperationTooltip(type, context, (Map) flattenBlockSummary, (Map) flattenEntitySummary);
    }

    public static OperationTooltip build(Context context, BlockSummary blockSummary, List<BlockState> blockStates) {
        return reduce(Type.BUILD, context, Map.of(blockSummary, blockStates), Map.of());
    }

    public static OperationTooltip build(Context context) {
        return new OperationTooltip(Type.BUILD, context, Map.of(), Map.of());
    }
    public static OperationTooltip build(
            Context context,
            Map<BlockSummary, List<BlockState>> blockSummary,
            Map<EntitySummary, List<BlockState>> entitySummary
    ) {
        return reduce(Type.BUILD, context, blockSummary, entitySummary);
    }

    public static OperationTooltip empty(Type type) {
        return new OperationTooltip(type, Context.defaultSet(), Map.of(), Map.of());
    }

    public OperationTooltip withType(Type type) {
        return new OperationTooltip(type, context, blockSummary, entitySummary);
    }

    public Map<BlockSummary, List<ItemStack>> itemStackSummary() {
        return Arrays.stream(BlockSummary.values()).map(b -> Map.entry(b, blockSummary.getOrDefault(b, Map.of()).entrySet().stream().map(e -> e.getKey().getItem().getDefaultStack().withCount(e.getValue())).toList())).collect(Collectors.toMap(Map.Entry::getKey, e -> ItemStackUtils.flattenStack(e.getValue())));
    }

    public int getSuccessBlocks() {
        return blockSummary.entrySet().stream().filter(e -> e.getKey().isSuccess()).map(Map.Entry::getValue).map(Map::entrySet).flatMap(Set::stream).mapToInt(Map.Entry::getValue).sum();
    }

    public int getFailedBlocks() {
        return blockSummary.entrySet().stream().filter(e -> e.getKey().isSuccess()).map(Map.Entry::getValue).map(Map::entrySet).flatMap(Set::stream).mapToInt(Map.Entry::getValue).sum();
    }

    public enum Type {
        BUILD("build"),
        UNDO_SUCCESS("undo_success"),
        REDO_SUCCESS("redo_success"),
        NOTHING_TO_UNDO("nothing_to_undo"),
        NOTHING_TO_REDO("nothing_to_redo"),
        ;

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


}
