package dev.huskuraft.effortless.screen.transformer;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.Entrance;
import dev.huskuraft.effortless.api.core.Tuple2;
import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.input.EditBox;
import dev.huskuraft.effortless.api.gui.slot.TextSlot;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.screen.settings.SettingsList;
import dev.huskuraft.effortless.text.Text;

import java.util.Arrays;
import java.util.function.Consumer;

public class EffortlessTransformerEditScreen extends AbstractScreen {

    private final Consumer<Transformer> applySettings;
    private Transformer lastSettings;
    private AbstractWidget titleTextWidget;
    private SettingsList entries;
    private Button saveButton;
    private Button cancelButton;
    private TextSlot textSlot;
    private EditBox nameEditBox;

    public EffortlessTransformerEditScreen(Entrance entrance, Consumer<Transformer> consumer, Transformer transformer) {
        super(entrance, Text.translate("effortless.transformer.edit.title"));
        this.applySettings = consumer;
        this.lastSettings = transformer;
    }

    @Override
    public void onCreate() {
        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, 24 - 16, getScreenTitle(), TextWidget.Gravity.CENTER));

        this.textSlot = addWidget(new TextSlot(getEntrance(), getWidth() / 2 - (Dimens.RegularEntry.ROW_WIDTH) / 2, 16 + 2, Dimens.ICON_WIDTH, Dimens.ICON_HEIGHT, Text.empty()));

        this.nameEditBox = addWidget(
                new EditBox(getEntrance(), getWidth() / 2 - (Dimens.RegularEntry.ROW_WIDTH) / 2 + 40, 24, Dimens.RegularEntry.ROW_WIDTH - 40, 20, null)
        );
        this.nameEditBox.setMaxLength(ItemRandomizer.MAX_NAME_LENGTH);
        this.nameEditBox.setHint(Text.translate("effortless.randomizer.edit.name_hint"));

        this.entries = addWidget(new SettingsList(getEntrance(), 0, 54, getWidth(), getHeight() - 54 - 36));

        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.edit.save"), button -> {
            applySettings.accept(lastSettings);
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.edit.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

        switch (lastSettings.getType()) {
            case ARRAY -> {
                this.entries.addDoubleEntry(Text.translate("effortless.transformer.array.offset.x"), Text.text("X"), ((ArrayTransformer) lastSettings).offset().x(), ArrayTransformer.OFFSET_BOUND.getMinX(), ArrayTransformer.OFFSET_BOUND.getMaxX(), value -> this.lastSettings = ((ArrayTransformer) lastSettings).withOffsetX(value));
                this.entries.addDoubleEntry(Text.translate("effortless.transformer.array.offset.y"), Text.text("Y"), ((ArrayTransformer) lastSettings).offset().y(), ArrayTransformer.OFFSET_BOUND.getMinY(), ArrayTransformer.OFFSET_BOUND.getMaxY(), value -> this.lastSettings = ((ArrayTransformer) lastSettings).withOffsetY(value));
                this.entries.addDoubleEntry(Text.translate("effortless.transformer.array.offset.z"), Text.text("Z"), ((ArrayTransformer) lastSettings).offset().z(), ArrayTransformer.OFFSET_BOUND.getMinZ(), ArrayTransformer.OFFSET_BOUND.getMaxZ(), value -> this.lastSettings = ((ArrayTransformer) lastSettings).withOffsetZ(value));
                this.entries.addIntegerEntry(Text.translate("effortless.transformer.array.count"), Text.text("C"), ((ArrayTransformer) lastSettings).count(), ArrayTransformer.MIN_COUNT, ArrayTransformer.MAX_COUNT, value -> this.lastSettings = ((ArrayTransformer) lastSettings).withCount(value));
            }
            case MIRROR -> {
                var mirrorTransformer = (MirrorTransformer) lastSettings;
                this.entries.addPositionEntry(mirrorTransformer.axis(), new Tuple2<>(mirrorTransformer.getPositionType(mirrorTransformer.axis()), mirrorTransformer.getPosition(mirrorTransformer.axis())), value -> {
                    this.lastSettings = mirrorTransformer.withPositionType(value.value1()).withPosition(new Vector3d(value.value2(), value.value2(), value.value2()));
                });
                this.entries.addValuesEntry(Text.translate("effortless.transformer.mirror.axis"), Text.text("A"), Arrays.stream(Axis.values()).map(Axis::getDisplayName).toList(), Arrays.stream(Axis.values()).toList(), mirrorTransformer.axis().ordinal(), value -> {
                    (((SettingsList.PositionEntry) this.entries.getWidget(0))).setAxis(value);
                    this.lastSettings = mirrorTransformer.withAxis(value);
                });
            }
            case RADIAL -> {
                this.entries.addPositionEntry(Axis.X, new Tuple2<>(((RadialTransformer) lastSettings).getPositionTypeX(), ((RadialTransformer) lastSettings).position().x()), value -> this.lastSettings = ((RadialTransformer) lastSettings).withPositionTypeX(value.value1()).withPositionX(value.value2()));
                this.entries.addPositionEntry(Axis.Y, new Tuple2<>(((RadialTransformer) lastSettings).getPositionTypeY(), ((RadialTransformer) lastSettings).position().y()), value -> this.lastSettings = ((RadialTransformer) lastSettings).withPositionTypeY(value.value1()).withPositionY(value.value2()));
                this.entries.addPositionEntry(Axis.Z, new Tuple2<>(((RadialTransformer) lastSettings).getPositionTypeZ(), ((RadialTransformer) lastSettings).position().z()), value -> this.lastSettings = ((RadialTransformer) lastSettings).withPositionTypeZ(value.value1()).withPositionZ(value.value2()));
                this.entries.addIntegerEntry(Text.translate("effortless.transformer.radial.slices"), Text.text("S"), ((RadialTransformer) lastSettings).slices(), RadialTransformer.SLICE_RANGE.min(), RadialTransformer.SLICE_RANGE.max(), value -> this.lastSettings = ((RadialTransformer) lastSettings).withSlice(value));
            }
            case ITEM_RAND -> {
            }
        }

    }

    @Override
    public void onReload() {
        textSlot.setMessage(TransformerList.Entry.getSymbol(lastSettings));
    }

}
