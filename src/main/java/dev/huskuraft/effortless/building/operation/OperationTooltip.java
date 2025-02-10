package dev.huskuraft.effortless.building.operation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dev.huskuraft.universal.api.core.ItemStack;
import dev.huskuraft.effortless.building.Context;

public record OperationTooltip(
        Type type,
        Context context,
        Map<ItemSummary, List<ItemStack>> itemSummary
) {

    public static OperationTooltip reduce(Type type, Context context, Map<ItemSummary, List<ItemStack>> blockStateSummary) {
        return new OperationTooltip(type, context, blockStateSummary.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> ItemStackUtils.flattenStack(entry.getValue()))));
    }

    public static OperationTooltip build(Context context, ItemSummary itemSummary, List<ItemStack> itemStacks) {
        return reduce(Type.BUILD, context, Map.of(itemSummary, itemStacks));
    }

    public static OperationTooltip build(Context context) {
        return new OperationTooltip(Type.BUILD, context, Map.of());
    }

    public static OperationTooltip build(
            Context context,
            Map<ItemSummary, List<ItemStack>> blockStateSummary
    ) {
        return reduce(Type.BUILD, context, blockStateSummary);
    }

    public static OperationTooltip empty(Type type) {
        return new OperationTooltip(type, Context.defaultSet(), Map.of());
    }

    public OperationTooltip withType(Type type) {
        return new OperationTooltip(type, context, itemSummary);
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
