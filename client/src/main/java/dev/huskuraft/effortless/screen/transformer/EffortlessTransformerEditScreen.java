package dev.huskuraft.effortless.screen.transformer;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractScreen;
import dev.huskuraft.effortless.gui.AbstractWidget;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.button.Button;
import dev.huskuraft.effortless.gui.input.EditBox;
import dev.huskuraft.effortless.gui.slot.TextSlot;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.screen.settings.SettingsList;
import dev.huskuraft.effortless.text.Text;

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
                this.entries.addDoubleEntry(Text.translate("effortless.transformer.array.offset.x"), Text.text("X"), ((ArrayTransformer) lastSettings).x(), value -> this.lastSettings = ((ArrayTransformer) lastSettings).withOffsetX(value));
                this.entries.addDoubleEntry(Text.translate("effortless.transformer.array.offset.y"), Text.text("Y"), ((ArrayTransformer) lastSettings).y(), value -> this.lastSettings = ((ArrayTransformer) lastSettings).withOffsetY(value));
                this.entries.addDoubleEntry(Text.translate("effortless.transformer.array.offset.z"), Text.text("Z"), ((ArrayTransformer) lastSettings).z(), value -> this.lastSettings = ((ArrayTransformer) lastSettings).withOffsetZ(value));
                this.entries.addIntegerEntry(Text.translate("effortless.transformer.array.count"), Text.text("C"), ((ArrayTransformer) lastSettings).count(), value -> this.lastSettings = ((ArrayTransformer) lastSettings).withCount(value));
            }
            case MIRROR -> {
            }
            case RADIAL -> {
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
