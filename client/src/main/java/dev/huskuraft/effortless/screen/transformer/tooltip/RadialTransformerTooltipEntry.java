package dev.huskuraft.effortless.screen.transformer.tooltip;

import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.gui.slot.SlotData;
import dev.huskuraft.effortless.text.Text;

import java.util.List;

public class RadialTransformerTooltipEntry extends TransformerTooltipEntry<RadialTransformer> {

    public RadialTransformerTooltipEntry(Entrance entrance, EntryList entryList, RadialTransformer radialTransformer) {
        super(entrance, entryList, radialTransformer);
    }

    @Override
    protected List<SlotData> getInfo() {
        return List.of(
                new SlotData.TextSymbol(Text.text(formatDouble(getItem().position().getX())), Text.translate("effortless.axis.x")),
                new SlotData.TextSymbol(Text.text(formatDouble(getItem().position().getY())), Text.translate("effortless.axis.y")),
                new SlotData.TextSymbol(Text.text(formatDouble(getItem().position().getZ())), Text.translate("effortless.axis.z")),
                new SlotData.TextSymbol(Text.text(formatDouble(getItem().slices())), Text.translate("effortless.transformer.radial.slices"))
        );
    }

}
