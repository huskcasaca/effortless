package dev.huskuraft.effortless.building.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.huskuraft.universal.api.core.Item;
import dev.huskuraft.universal.api.core.ItemStack;
import dev.huskuraft.universal.api.math.MathUtils;
import dev.huskuraft.universal.api.tag.RecordTag;

public class ItemStackUtils {

    private ItemStackUtils() {
    }

    public static List<ItemStack> flattenStack(List<ItemStack> stacks) {
        record Pair<A, B>(A first, B second) {
        }
        var map = new HashMap<Pair<Item, RecordTag>, ItemStack>();
        for (var stack : stacks) {
            var item = stack.getItem();
            var key = new Pair<>(item, RecordTag.newRecord());
            if (map.containsKey(key)) {
                map.get(key).increase(stack.getCount());
            } else {
                map.put(key, stack.copy());
            }
        }
        var result = new ArrayList<ItemStack>();
        for (var stack : map.values()) {
            var count = stack.getCount();
            var maxStackSize = Math.max(64, stack.getMaxStackSize());
            while (count > 0) {
                var newStack = stack.copy();
                newStack.setCount(MathUtils.min(count, maxStackSize));
                result.add(newStack);
                count -= maxStackSize;
            }
        }
        return result;
    }

}
