package dev.huskuraft.effortless.vanilla.core;

import java.util.List;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.text.Text;
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
            case NORMAL -> TooltipFlag.Default.NORMAL;
            case NORMAL_CREATIVE -> TooltipFlag.Default.NORMAL;
            case ADVANCED -> TooltipFlag.Default.ADVANCED;
            case ADVANCED_CREATIVE -> TooltipFlag.Default.ADVANCED;
        };
        return refs.getTooltipLines(player.reference(), minecraftFlag).stream().map(text -> new MinecraftText(text)).collect(Collectors.toList());
    }

    @Override
    public ItemStack copy() {
        return new MinecraftItemStack(refs.copy());
    }

    @Override
    public int getDamageValue() {
        return refs.getDamageValue();
    }

    @Override
    public void setDamageValue(int damage) {
        refs.setDamageValue(damage);
    }

    @Override
    public int getMaxDamage() {
        return refs.getMaxDamage();
    }

    @Override
    public boolean isDamageableItem() {
        return refs.isDamageableItem();
    }
}
