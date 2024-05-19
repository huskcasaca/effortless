package dev.huskuraft.effortless.vanilla.core;

import java.util.List;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.vanilla.tag.MinecraftTagRecord;
import net.minecraft.world.item.TooltipFlag;

public record MinecraftItemStack(
        net.minecraft.world.item.ItemStack referenceValue
) implements ItemStack {

    @Override
    public Item getItem() {
        return MinecraftItem.ofNullable(referenceValue().getItem());
    }

    @Override
    public int getCount() {
        return referenceValue().getCount();
    }

    @Override
    public void setCount(int count) {
        referenceValue().setCount(count);
    }

    @Override
    public Text getHoverName() {
        return new MinecraftText(referenceValue().getHoverName());
    }

    @Override
    public List<Text> getTooltips(Player player, TooltipType flag) {
        var minecraftFlag = switch (flag) {
            case NORMAL -> TooltipFlag.Default.NORMAL;
            case NORMAL_CREATIVE -> TooltipFlag.Default.NORMAL;
            case ADVANCED -> TooltipFlag.Default.ADVANCED;
            case ADVANCED_CREATIVE -> TooltipFlag.Default.ADVANCED;
        };
        return referenceValue().getTooltipLines(player.reference(), minecraftFlag).stream().map(text -> new MinecraftText(text)).collect(Collectors.toList());
    }

    @Override
    public ItemStack copy() {
        return new MinecraftItemStack(referenceValue().copy());
    }

    @Override
    public TagRecord getTag() {
        return MinecraftTagRecord.ofNullable(referenceValue().getTag());
    }

    @Override
    public void setTag(TagRecord tagRecord) {
        referenceValue().setTag(tagRecord.reference());
    }

    @Override
    public boolean damageBy(Player player, int damage) {
        return referenceValue().hurt(damage, player.<net.minecraft.world.entity.player.Player>reference().getRandom(), null);
    }

}
