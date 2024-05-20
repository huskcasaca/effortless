package dev.huskuraft.effortless.building.history;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.operation.ItemSummaryType;
import dev.huskuraft.effortless.building.operation.OperationResult;

public record BuildTooltip(
        Type type,
        Context context,
        Map<ItemSummaryType, List<ItemStack>> itemSummary
) {
    public BuildTooltip(
            Type type,
            OperationResult result
    ) {
        this(
                type,
                result.getOperation().getContext(),
                Arrays.stream(ItemSummaryType.values()).collect(Collectors.toMap(Function.identity(), result::getProducts))
        );

    }

    public BuildTooltip(
            Type type
    ) {
        this(
                type,
                Context.defaultSet(),
                Map.of()
        );

    }

    public enum Type {
        BUILD_SUCCESS("build_success"),
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