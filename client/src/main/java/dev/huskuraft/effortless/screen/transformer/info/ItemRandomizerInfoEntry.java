package dev.huskuraft.effortless.screen.transformer.info;

import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.pattern.randomize.Randomizer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemRandomizerInfoEntry extends TransformerInfoEntry<ItemRandomizer> {

    public ItemRandomizerInfoEntry(Entrance entrance, EntryList entryList, ItemRandomizer transformer) {
        super(entrance, entryList, transformer);
    }

    @Override
    protected List<Text> getBasicComponents(ItemRandomizer transformer) {
        var group = Text.text("Group: %s", getGroupDescription(transformer.getTarget())).withStyle(TextStyle.GRAY);
        var order = Text.text("Order: %s", getOrderDescription(transformer.getOrder())).withStyle(TextStyle.GRAY);
        return Stream.of(group, order).collect(Collectors.toList());
    }

    @Override
    protected List<Text> getExtraComponents(ItemRandomizer transformer) {
        var chances = Stream.<Text>empty();
        if (getItem().getChances().isEmpty()) {
            chances = Stream.of(Text.text("Empty").withStyle(TextStyle.DARK_GRAY).withStyle(TextStyle.ITALIC));
        } else {
            // FIXME: 29/8/23 shulker box name color
            chances = getItem().getChances().stream().map(chance -> chance.content().getHoverName().copy().append(Text.text(" x" + chance.chance())).withStyle(TextStyle.DARK_GRAY));
        }
        return chances.collect(Collectors.toList());
    }

    @Override
    public Text getDisplayName(ItemRandomizer transformer) {
        return super.getDisplayName(transformer);
    }

    private Text getCategoryDescription(Randomizer.Category category) {
        return Text.translate(category.getNameKey());
    }

    private Text getOrderDescription(Randomizer.Order order) {
        return Text.translate(order.getNameKey());
    }

    private Text getGroupDescription(Randomizer.Target target) {
        return Text.translate(target.getNameKey());
    }
}
