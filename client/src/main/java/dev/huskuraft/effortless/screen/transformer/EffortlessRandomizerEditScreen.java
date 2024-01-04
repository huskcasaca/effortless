package dev.huskuraft.effortless.screen.transformer;

import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.input.EditBox;
import dev.huskuraft.effortless.api.gui.slot.TextSlot;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.pattern.randomize.Chance;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.pattern.randomize.Randomizer;
import dev.huskuraft.effortless.screen.item.EffortlessItemPickerScreen;

import java.util.function.Consumer;

public class EffortlessRandomizerEditScreen extends AbstractScreen {

    private final Consumer<ItemRandomizer> applySettings;
    private final ItemRandomizer defaultSettings;
    private ItemRandomizer lastSettings;
    private TextWidget titleTextWidget;
    private ItemChanceList entries;
    private EditBox nameEditBox;
    private Button orderButton;
    private Button supplierButton;
    private Button upButton;
    private Button downButton;
    private Button deleteButton;
    private Button addButton;
    private Button saveButton;
    private Button cancelButton;
    private TextSlot textSlot;

    private Randomizer.Order lastOrder;
    private Randomizer.Target lastTarget;

    public EffortlessRandomizerEditScreen(Entrance entrance, Consumer<ItemRandomizer> consumer, ItemRandomizer randomizer) {
        super(entrance, Text.translate("effortless.randomizer.edit.title"));
        this.applySettings = consumer;
        this.defaultSettings = randomizer;
        this.lastSettings = randomizer;
        this.lastOrder = randomizer.getOrder();
        this.lastTarget = randomizer.getTarget();
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
        this.nameEditBox.setValue(lastSettings.getName().getString());

        this.orderButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.order", lastSettings.getOrder().getDisplayName()), button -> {
            lastOrder = Randomizer.Order.values()[(lastOrder.ordinal() + 1) % Randomizer.Order.values().length];
            orderButton.setMessage(Text.translate("effortless.randomizer.edit.order", lastOrder.getDisplayName()));
        }).setBoundsGrid(getWidth(), 82, 0f, 0f, 0.5f).build());
        this.supplierButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.target", lastTarget.getDisplayName()), button -> {
            lastTarget = Randomizer.Target.values()[(lastTarget.ordinal() + 1) % Randomizer.Target.values().length];
            supplierButton.setMessage(Text.translate("effortless.randomizer.edit.target", lastTarget.getDisplayName()));
        }).setBoundsGrid(getWidth(), 82, 0f, 0.5f, 0.5f).build());

        this.entries = addWidget(new ItemChanceList(getEntrance(), 0, 82, getWidth(), getHeight() - 82 - 60));
        this.entries.reset(lastSettings.getChances());

        this.upButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.up"), button -> {
            if (entries.hasSelected()) {
                entries.moveUpSelected();
            }
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0f, 0.25f).build());
        this.downButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.down"), button -> {
            if (entries.hasSelected()) {
                entries.moveDownSelected();
            }
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0.25f, 0.25f).build());

        this.deleteButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.delete"), button -> {
            entries.deleteSelected();
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0.5f, 0.25f).build());
        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.add"), button -> {
            new EffortlessItemPickerScreen(
                    getEntrance(),
                    item -> {
                        entries.insertSelected(Chance.of(item, (byte) 1));
                        onReload();
                    }
            ).attach();
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0.75f, 0.25f).build());

        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.save"), button -> {
            applySettings.accept(lastSettings);
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());
    }

    @Override
    public void onReload() {
        textSlot.setMessage(TransformerList.Entry.getSymbol(lastSettings));

        upButton.setActive(entries.hasSelected() && entries.indexOfSelected() > 0);
        downButton.setActive(entries.hasSelected() && entries.indexOfSelected() < entries.children().size() - 1);
        deleteButton.setActive(entries.hasSelected());
        addButton.setActive(entries.children().size() <= ItemRandomizer.MAX_CHANCE_SIZE);

        lastSettings = ItemRandomizer.create(
                Text.text(nameEditBox.getValue()), // TODO: 6/12/23 use default if no changes
                lastOrder,
                lastTarget,
                Randomizer.Category.ITEM,
                entries.items());
    }

}
