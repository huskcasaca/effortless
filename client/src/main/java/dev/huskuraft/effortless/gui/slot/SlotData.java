package dev.huskuraft.effortless.gui.slot;

import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.text.Text;

public sealed interface SlotData permits SlotData.TextSymbol, SlotData.ItemSymbol {

    record TextSymbol(
            Text text, Text symbol
    ) implements SlotData {
    }

    record ItemSymbol(
            ItemStack item, Text symbol
    ) implements SlotData {
    }
}
