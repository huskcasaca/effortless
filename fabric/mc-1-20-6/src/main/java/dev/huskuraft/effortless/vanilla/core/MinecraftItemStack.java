package dev.huskuraft.effortless.vanilla.core;

import java.util.List;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.tag.RecordTag;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.vanilla.tag.MinecraftRecordTag;
import net.minecraft.world.item.TooltipFlag;

public record MinecraftItemStack(
        net.minecraft.world.item.ItemStack refs
) implements ItemStack {

    public static ItemStack ofNullable(net.minecraft.world.item.ItemStack refs) {
        if (refs == null) return null;
        return new MinecraftItemStack(refs);
    }

    @Override
    public Item getItem() {
        return MinecraftItem.ofNullable(refs.getItem());
    }

    @Override
    public int getCount() {
        return refs.getCount();
    }

    @Override
    public void setCount(int count) {
        refs.setCount(count);
    }

    @Override
    public Text getHoverName() {
        return new MinecraftText(refs.getHoverName());
    }

    @Override
    public List<Text> getTooltips(Player player, TooltipType flag) {
        var minecraftFlag = switch (flag) {
            case NORMAL -> TooltipFlag.NORMAL;
            case NORMAL_CREATIVE -> TooltipFlag.NORMAL.asCreative();
            case ADVANCED -> TooltipFlag.ADVANCED;
            case ADVANCED_CREATIVE -> TooltipFlag.ADVANCED.asCreative();
        };
        return refs.getTooltipLines(net.minecraft.world.item.Item.TooltipContext.EMPTY, player.reference(), minecraftFlag).stream().map(text -> new MinecraftText(text)).collect(Collectors.toList());
    }

    @Override
    public ItemStack copy() {
        return new MinecraftItemStack(refs.copy());
    }

    @Override
    public RecordTag getTag() {
        return MinecraftRecordTag.ofNullable((net.minecraft.nbt.CompoundTag) refs.save(net.minecraft.core.RegistryAccess.EMPTY));
    }

    @Override
    public void setTag(RecordTag recordTag) {
//        refs.setTag(recordTag.reference());
    }

    @Override
    public boolean damageBy(Player player, int damage) {
        refs.setDamageValue(getDamageValue() + damage); return true;
    }

}
