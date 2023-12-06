package dev.huskuraft.effortless.screen.transformer.tooltip;

import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.core.Axis;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.gui.slot.SlotData;
import dev.huskuraft.effortless.text.Text;

import java.util.List;

public class MirrorTransformerTooltipEntry extends TransformerTooltipEntry<MirrorTransformer> {

    public MirrorTransformerTooltipEntry(Entrance entrance, EntryList entryList, MirrorTransformer mirrorTransformer) {
        super(entrance, entryList, mirrorTransformer);
    }

    @Override
    protected List<SlotData> getInfo() {
        return List.of(
                new SlotData.TextSymbol(Text.text(formatDouble(getItem().position().getX())), Text.translate("effortless.axis.x")),
                new SlotData.TextSymbol(Text.text(formatDouble(getItem().position().getY())), Text.translate("effortless.axis.y")),
                new SlotData.TextSymbol(Text.text(formatDouble(getItem().position().getZ())), Text.translate("effortless.axis.z")),
                new SlotData.TextSymbol(getPlaneDescription(getItem().axis()), Text.translate("effortless.transformer.mirror.axis"))
        );
    }

    private Text getPlaneDescription(Axis axis) {
        if (axis == null) return Text.translate("effortless.axis.undefined");
        return switch (axis) {
            case X -> Text.translate("effortless.axis.x");
            case Y -> Text.translate("effortless.axis.y");
            case Z -> Text.translate("effortless.axis.z");
        };
    }

}
