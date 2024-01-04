package dev.huskuraft.effortless.api.gui.slot;

import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.text.Text;

public sealed interface SlotData permits SlotData.TextSymbol, SlotData.ItemStackSymbol {

    record TextSymbol(
            Text text, Text symbol
    ) implements SlotData {
    }

    record ItemStackSymbol(
            ItemStack itemStack, Text symbol
    ) implements SlotData {
    }
}
