package dev.huskuraft.effortless.screen.transformer;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.Tuple2;
import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.slot.TextSlot;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.screen.settings.SettingOptionsList;

public class EffortlessTransformerEditScreen extends AbstractPanelScreen {

    private final Consumer<Transformer> consumer;
    private Transformer transformer;
    private AbstractWidget titleTextWidget;
    private TransformerList transformerEntries;
    private SettingOptionsList entries;
    private Button saveButton;
    private Button cancelButton;
    private TextSlot textSlot;
//    private EditBox nameEditBox;

    public EffortlessTransformerEditScreen(Entrance entrance, Consumer<Transformer> consumer, Transformer transformer) {
        super(entrance, Text.translate("effortless.transformer.edit.title"), PANEL_WIDTH_EXPANDED, PANEL_HEIGHT_270);
        this.consumer = consumer;
        this.transformer = transformer;
    }

    @Override
    public void onCreate() {
        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(0x00404040), TextWidget.Gravity.CENTER));

//        this.textSlot = addWidget(new TextSlot(getEntrance(), getWidth() / 2 - (Dimens.Entry.ROW_WIDTH) / 2, 16 + 2, Dimens.ICON_WIDTH, Dimens.ICON_HEIGHT, Text.empty()));

//        this.nameEditBox = addWidget(
//                new EditBox(getEntrance(), getWidth() / 2 - (Dimens.Entry.ROW_WIDTH) / 2 + 40, Dimens.Screen.TITLE_24, Dimens.Entry.ROW_WIDTH - 40, 20, null)
//        );
//        this.nameEditBox.setMaxLength(ItemRandomizer.MAX_NAME_LENGTH);
//        this.nameEditBox.setHint(Text.translate("effortless.randomizer.edit.name_hint"));

        this.transformerEntries = addWidget(new TransformerList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS_H * 2, 38));
        this.transformerEntries.setShowScrollBar(false);
        this.transformerEntries.setRenderSelection(false);
        this.transformerEntries.reset(List.of(transformer));

        this.entries = addWidget(new SettingOptionsList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1 + transformerEntries.getHeight() + INNER_PADDINGS_V, getWidth() - PADDINGS_H * 2 - 8, getHeight() - PANEL_TITLE_HEIGHT_1 - transformerEntries.getHeight() - INNER_PADDINGS_V - PANEL_BUTTON_ROW_HEIGHT_1, true, false));
        this.entries.setAlwaysShowScrollbar(true);

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.edit.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.edit.save"), button -> {
            consumer.accept(transformer);
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

        switch (transformer.getType()) {
            case ARRAY -> {
                this.entries.addNumberEntry(Text.translate("effortless.transformer.array.offset.x"), Text.text("X"), ((ArrayTransformer) transformer).offset().x(), ArrayTransformer.OFFSET_BOUND.getMinX(), ArrayTransformer.OFFSET_BOUND.getMaxX(), value -> this.transformer = ((ArrayTransformer) transformer).withOffsetX(value));
                this.entries.addNumberEntry(Text.translate("effortless.transformer.array.offset.y"), Text.text("Y"), ((ArrayTransformer) transformer).offset().y(), ArrayTransformer.OFFSET_BOUND.getMinY(), ArrayTransformer.OFFSET_BOUND.getMaxY(), value -> this.transformer = ((ArrayTransformer) transformer).withOffsetY(value));
                this.entries.addNumberEntry(Text.translate("effortless.transformer.array.offset.z"), Text.text("Z"), ((ArrayTransformer) transformer).offset().z(), ArrayTransformer.OFFSET_BOUND.getMinZ(), ArrayTransformer.OFFSET_BOUND.getMaxZ(), value -> this.transformer = ((ArrayTransformer) transformer).withOffsetZ(value));
                this.entries.addIntegerEntry(Text.translate("effortless.transformer.array.count"), Text.text("C"), ((ArrayTransformer) transformer).count(), ArrayTransformer.MIN_COUNT, ArrayTransformer.MAX_COUNT, value -> this.transformer = ((ArrayTransformer) transformer).withCount(value));
            }
            case MIRROR -> {
                var mirrorTransformer = (MirrorTransformer) transformer;
                this.entries.addPositionNumberEntry(mirrorTransformer.axis(), new Tuple2<>(mirrorTransformer.getPositionType(mirrorTransformer.axis()), mirrorTransformer.getPosition(mirrorTransformer.axis())), value -> {
                    this.transformer = mirrorTransformer.withPositionType(value.value1()).withPosition(new Vector3d(value.value2(), value.value2(), value.value2()));
                });
                this.entries.addSelectorEntry(Text.translate("effortless.transformer.mirror.axis"), Text.text("A"), Arrays.stream(Axis.values()).map(Axis::getDisplayName).toList(), Arrays.stream(Axis.values()).toList(), mirrorTransformer.axis(), value -> {
                    (((SettingOptionsList.PositionNumberEntry) this.entries.getWidget(0))).setAxis(value);
                    this.transformer = mirrorTransformer.withAxis(value);
                });
            }
            case RADIAL -> {
                this.entries.addPositionNumberEntry(Axis.X, new Tuple2<>(((RadialTransformer) transformer).getPositionTypeX(), ((RadialTransformer) transformer).position().x()), value -> this.transformer = ((RadialTransformer) transformer).withPositionTypeX(value.value1()).withPositionX(value.value2()));
                this.entries.addPositionNumberEntry(Axis.Y, new Tuple2<>(((RadialTransformer) transformer).getPositionTypeY(), ((RadialTransformer) transformer).position().y()), value -> this.transformer = ((RadialTransformer) transformer).withPositionTypeY(value.value1()).withPositionY(value.value2()));
                this.entries.addPositionNumberEntry(Axis.Z, new Tuple2<>(((RadialTransformer) transformer).getPositionTypeZ(), ((RadialTransformer) transformer).position().z()), value -> this.transformer = ((RadialTransformer) transformer).withPositionTypeZ(value.value1()).withPositionZ(value.value2()));
                this.entries.addIntegerEntry(Text.translate("effortless.transformer.radial.slices"), Text.text("S"), ((RadialTransformer) transformer).slices(), RadialTransformer.SLICE_RANGE.min(), RadialTransformer.SLICE_RANGE.max(), value -> this.transformer = ((RadialTransformer) transformer).withSlice(value));
            }
            case ITEM_RANDOMIZER -> {
            }
        }

    }

    @Override
    public void onReload() {
        this.transformerEntries.reset(List.of(transformer));
    }

}
