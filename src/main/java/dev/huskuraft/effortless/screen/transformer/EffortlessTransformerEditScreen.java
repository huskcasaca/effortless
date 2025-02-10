package dev.huskuraft.effortless.screen.transformer;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import dev.huskuraft.universal.api.core.Axis;
import dev.huskuraft.universal.api.gui.AbstractPanelScreen;
import dev.huskuraft.universal.api.gui.AbstractWidget;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.slot.TextSlot;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
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
    private Button moveToPlayerButton;
    private Button moveToCornerOrCenterButton;
    private Button saveButton;
    private Button cancelButton;
    private TextSlot textSlot;
//    private EditBox nameEditBox;

    public EffortlessTransformerEditScreen(Entrance entrance, Consumer<Transformer> consumer, Transformer transformer) {
        super(entrance, Text.translate("effortless.transformer.edit.title"), PANEL_WIDTH_60, PANEL_HEIGHT_FULL);
        this.consumer = consumer;
        this.transformer = transformer;
    }

    @Override
    public void onCreate() {
        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

//        this.textSlot = addWidget(new TextSlot(getEntrance(), getWidth() / 2 - (Dimens.Entry.ROW_WIDTH) / 2, 16 + 2, Dimens.ICON_WIDTH, Dimens.ICON_HEIGHT, Text.empty()));

//        this.nameEditBox = addWidget(
//                new EditBox(getEntrance(), getWidth() / 2 - (Dimens.Entry.ROW_WIDTH) / 2 + 40, Dimens.Screen.TITLE_24, Dimens.Entry.ROW_WIDTH - 40, 20, null)
//        );
//        this.nameEditBox.setMaxLength(ItemRandomizer.MAX_NAME_LENGTH);
//        this.nameEditBox.setHint(Text.translate("effortless.transformer.randomizer.edit.name_hint"));

        this.transformerEntries = addWidget(new TransformerList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS_H * 2, 38));
        this.transformerEntries.setShowScrollBar(false);
        this.transformerEntries.setRenderSelection(false);
        this.transformerEntries.reset(List.of(transformer));

        this.entries = addWidget(new SettingOptionsList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1 + transformerEntries.getHeight() + INNER_PADDINGS_V, getWidth() - PADDINGS_H * 2 - 8, getHeight() - PANEL_TITLE_HEIGHT_1 - transformerEntries.getHeight() - INNER_PADDINGS_V - (transformer.getType() == Transformers.MIRROR || transformer.getType() == Transformers.RADIAL ? PANEL_BUTTON_ROW_HEIGHT_2 : PANEL_BUTTON_ROW_HEIGHT_1), true, false));
        this.entries.setAlwaysShowScrollbar(true);

        this.moveToPlayerButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.position.move_to_player"), button -> {
            var playerPosition = getEntrance().getClient().getPlayer().getPosition();
            this.transformer = switch (transformer.getType()) {
                case ARRAY -> transformer;
                case MIRROR -> ((MirrorTransformer) transformer).withPosition(Transformer.roundAllHalf(playerPosition));
                case RADIAL -> ((RadialTransformer) transformer).withPosition(Transformer.roundAllHalf(playerPosition));
                case RANDOMIZER -> transformer;
            };
            recreate();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 0.5f).build());
        this.moveToPlayerButton.setVisible(transformer.getType() == Transformers.MIRROR || transformer.getType() == Transformers.RADIAL);

        this.moveToPlayerButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.position.move_to_player"), button -> {
            var playerPosition = getEntrance().getClient().getPlayer().getPosition();
            this.transformer = switch (transformer.getType()) {
                case ARRAY -> transformer;
                case MIRROR -> ((MirrorTransformer) transformer).withPosition(Transformer.roundAllHalf(playerPosition));
                case RADIAL -> ((RadialTransformer) transformer).withPosition(Transformer.roundAllHalf(playerPosition));
                case RANDOMIZER -> transformer;
            };
            recreate();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 0.5f).build());
        this.moveToPlayerButton.setVisible(transformer.getType() == Transformers.MIRROR || transformer.getType() == Transformers.RADIAL);

        this.moveToCornerOrCenterButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.position.move_to_center_or_corner"), button -> {
            this.transformer = switch (transformer.getType()) {
                case ARRAY -> transformer;
                case MIRROR -> ((MirrorTransformer) transformer).withPosition(Transformer.roundHalf(((MirrorTransformer) transformer).position()));
                case RADIAL -> ((RadialTransformer) transformer).withPosition(Transformer.roundHalf(((RadialTransformer) transformer).position()));
                case RANDOMIZER -> transformer;
            };
            recreate();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.5f, 0.5f).build());
        this.moveToCornerOrCenterButton.setVisible(transformer.getType() == Transformers.MIRROR || transformer.getType() == Transformers.RADIAL);

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.edit.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.edit.save"), button -> {
            consumer.accept(transformer);
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

        switch (transformer.getType()) {
            case ARRAY -> {
                this.entries.addIntegerEntry(Text.translate("effortless.transformer.array.offset.x"), Text.text("X"), ((ArrayTransformer) transformer).offset().x(), ArrayTransformer.OFFSET_BOUND.minX(), ArrayTransformer.OFFSET_BOUND.maxX(), value -> this.transformer = ((ArrayTransformer) transformer).withOffsetX(value));
                this.entries.addIntegerEntry(Text.translate("effortless.transformer.array.offset.y"), Text.text("Y"), ((ArrayTransformer) transformer).offset().y(), ArrayTransformer.OFFSET_BOUND.minY(), ArrayTransformer.OFFSET_BOUND.maxY(), value -> this.transformer = ((ArrayTransformer) transformer).withOffsetY(value));
                this.entries.addIntegerEntry(Text.translate("effortless.transformer.array.offset.z"), Text.text("Z"), ((ArrayTransformer) transformer).offset().z(), ArrayTransformer.OFFSET_BOUND.minZ(), ArrayTransformer.OFFSET_BOUND.maxZ(), value -> this.transformer = ((ArrayTransformer) transformer).withOffsetZ(value));
                this.entries.addIntegerEntry(Text.translate("effortless.transformer.array.count"), Text.text("C"), ((ArrayTransformer) transformer).count(), ArrayTransformer.COUNT_RANGE.min(), ArrayTransformer.COUNT_RANGE.max(), value -> this.transformer = ((ArrayTransformer) transformer).withCount(value));
            }
            case MIRROR -> {
                this.entries.addPositionEntry(Axis.X.getPositionName(), Axis.X.getDisplayName(), ((MirrorTransformer) transformer).position().x(), Transformer.POSITION_RANGE.low(), Transformer.POSITION_RANGE.high(), value -> this.transformer = ((MirrorTransformer) transformer).withPositionX(Transformer.roundHalf(value)));
                this.entries.addPositionEntry(Axis.Y.getPositionName(), Axis.Y.getDisplayName(), ((MirrorTransformer) transformer).position().y(), Transformer.POSITION_RANGE.low(), Transformer.POSITION_RANGE.high(), value -> this.transformer = ((MirrorTransformer) transformer).withPositionY(Transformer.roundHalf(value)));
                this.entries.addPositionEntry(Axis.Z.getPositionName(), Axis.Z.getDisplayName(), ((MirrorTransformer) transformer).position().z(), Transformer.POSITION_RANGE.low(), Transformer.POSITION_RANGE.high(), value -> this.transformer = ((MirrorTransformer) transformer).withPositionZ(Transformer.roundHalf(value)));
                this.entries.addSelectorEntry(Text.translate("effortless.transformer.mirror.axis"), Text.text("A"), Arrays.stream(Axis.values()).map(Axis::getDisplayName).toList(), Arrays.stream(Axis.values()).toList(), ((MirrorTransformer) transformer).axis(), value -> this.transformer = ((MirrorTransformer) transformer).withAxis(value));
                this.entries.addIntegerEntry(Text.translate("effortless.transformer.mirror.size"), Text.text("S"), ((MirrorTransformer) transformer).size(), MirrorTransformer.SIZE_RANGE.min(), MirrorTransformer.SIZE_RANGE.max(), value -> this.transformer = ((MirrorTransformer) transformer).withSize(value));
            }
            case RADIAL -> {
                this.entries.addPositionEntry(Axis.X.getPositionName(), Axis.X.getDisplayName(), ((RadialTransformer) transformer).position().x(), Transformer.POSITION_RANGE.low(), Transformer.POSITION_RANGE.high(), value -> this.transformer = ((RadialTransformer) transformer).withPositionX(Transformer.roundHalf(value)));
                this.entries.addPositionEntry(Axis.Y.getPositionName(), Axis.Y.getDisplayName(), ((RadialTransformer) transformer).position().y(), Transformer.POSITION_RANGE.low(), Transformer.POSITION_RANGE.high(), value -> this.transformer = ((RadialTransformer) transformer).withPositionY(Transformer.roundHalf(value)));
                this.entries.addPositionEntry(Axis.Z.getPositionName(), Axis.Z.getDisplayName(), ((RadialTransformer) transformer).position().z(), Transformer.POSITION_RANGE.low(), Transformer.POSITION_RANGE.high(), value -> this.transformer = ((RadialTransformer) transformer).withPositionZ(Transformer.roundHalf(value)));
                this.entries.addSelectorEntry(Text.translate("effortless.transformer.radial.axis"), Text.text("A"), Arrays.stream(Axis.values()).map(Axis::getDisplayName).toList(), Arrays.stream(Axis.values()).toList(), ((RadialTransformer) transformer).axis(), value -> this.transformer = ((RadialTransformer) transformer).withAxis(value));
                this.entries.addIntegerEntry(Text.translate("effortless.transformer.radial.slices"), Text.text("S"), ((RadialTransformer) transformer).slices(), RadialTransformer.SLICE_RANGE.min(), RadialTransformer.SLICE_RANGE.max(), value -> this.transformer = ((RadialTransformer) transformer).withSlice(value));
                this.entries.addIntegerEntry(Text.translate("effortless.transformer.radial.radius"), Text.text("R"), ((RadialTransformer) transformer).radius(), RadialTransformer.RADIUS_RANGE.min(), RadialTransformer.RADIUS_RANGE.max(), value -> this.transformer = ((RadialTransformer) transformer).withRadius(value));
                this.entries.addIntegerEntry(Text.translate("effortless.transformer.radial.length"), Text.text("L"), ((RadialTransformer) transformer).length(), RadialTransformer.LENGTH_RANGE.min(), RadialTransformer.LENGTH_RANGE.max(), value -> this.transformer = ((RadialTransformer) transformer).withLength(value));
            }
            case RANDOMIZER -> {
            }
        }
    }

    @Override
    public void onReload() {
        this.transformerEntries.reset(List.of(transformer));
        for (var entry : this.entries.children()) {
            if (entry instanceof SettingOptionsList.PositionEntry positionEntry) {
                if (!positionEntry.isFocused()) {
                    positionEntry.setItem(Transformer.roundHalf(positionEntry.getItem()));
                }
            }
        }
    }

}
