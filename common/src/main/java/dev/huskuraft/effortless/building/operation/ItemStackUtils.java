package dev.huskuraft.effortless.building.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.tag.TagRecord;

public class ItemStackUtils {

    private ItemStackUtils() {
    }

    public static List<ItemStack> reduceStack(List<ItemStack> stacks) {
        record Pair<A, B>(A first, B second) {
        }
        var map = new HashMap<Pair<Item, TagRecord>, ItemStack>();
        for (var stack : stacks) {
            var item = stack.getItem();
            var key = new Pair<>(item, stack.getTag());
            if (map.containsKey(key)) {
                map.get(key).increase(stack.getCount());
            } else {
                map.put(key, stack.copy());
            }
        }
        var result = new ArrayList<ItemStack>();
        for (var stack : map.values()) {
            var count = stack.getCount();
            var maxStackSize = stack.getMaxStackSize();
            while (count > 0) {
                var newStack = stack.copy();
                newStack.setCount(MathUtils.min(count, maxStackSize));
                result.add(newStack);
                count -= maxStackSize;
            }
        }
        return result;
    }

    public static ItemStack putColorTag(ItemStack itemStack, int color) {
        itemStack.getTag().putInt("RenderColor", color);
        return itemStack;
    }

    public static Integer getColorTag(ItemStack itemStack) {
        return itemStack.getTag().getInt("RenderColor");
    }

}
