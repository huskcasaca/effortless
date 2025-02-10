package dev.huskuraft.effortless.screen.pattern;

import java.util.function.Consumer;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.universal.api.gui.AbstractPanelScreen;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.text.MessageTextWidget;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.ChatFormatting;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.screen.transformer.EffortlessItemRandomizerEditScreen;
import dev.huskuraft.effortless.screen.transformer.EffortlessTransformerEditScreen;
import dev.huskuraft.effortless.screen.transformer.EffortlessTransformerPresetsSelectScreen;
import dev.huskuraft.effortless.screen.transformer.TransformerList;

public class EffortlessPatternScreen extends AbstractPanelScreen {

    private final Consumer<Pattern> consumer;
    private Pattern pattern;
    private TextWidget titleTextWidget;
    private TransformerList entries;
    private MessageTextWidget textWidget;
    private Button upButton;
    private Button downButton;
    private Button editButton;
    private Button deleteButton;
    private Button clearButton;
    private Button addButton;
    private Button enableButton;
    private Button doneButton;

    public EffortlessPatternScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.pattern.title").withStyle(ChatFormatting.DARK_GRAY));
        this.consumer = pattern -> {
            getEntrance().getStructureBuilder().setPattern(getEntrance().getClient().getPlayer(), this.pattern);
        };
        this.pattern = getEntrance().getStructureBuilder().getContext(getEntrance().getClient().getPlayer()).pattern();
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {
        setHeight(pattern.enabled() ? PANEL_HEIGHT_FULL : PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_2);
        setWidth(pattern.enabled() ? PANEL_WIDTH_60 : PANEL_WIDTH_42);

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        this.enableButton = addWidget(Button.builder(getEntrance(), pattern.enabled() ? Text.translate("effortless.pattern.button.disable") : Text.translate("effortless.pattern.button.enable"), button -> {
            this.pattern = pattern.withEnabled(!pattern.enabled());
            recreate();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_1, 0f, 0f, 1f).build());

        this.doneButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detachAll();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 1f).build());

        this.entries = addWidget(new TransformerList(getEntrance(), getLeft() + AbstractPanelScreen.PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_1N, getWidth() - AbstractPanelScreen.PADDINGS_H * 2 - 8 /* scrollbar */, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_1N - PANEL_BUTTON_ROW_HEIGHT_2));
        this.entries.reset(pattern.transformers());
        this.entries.setAlwaysShowScrollbar(true);

        this.textWidget = addWidget(new MessageTextWidget(getEntrance(), getLeft() + AbstractPanelScreen.PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_1N, getWidth() - AbstractPanelScreen.PADDINGS_H * 2, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_1N - PANEL_BUTTON_ROW_HEIGHT_2, Text.translate("effortless.pattern.no_transformer"), MessageTextWidget.Gravity.CENTER));

        this.upButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.up"), button -> {
            if (entries.hasSelected()) {
                entries.moveUpSelected();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 0.25f).build());
        this.downButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.down"), button -> {
            if (entries.hasSelected()) {
                entries.moveDownSelected();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.25f, 0.25f).build());

        this.editButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.edit"), button -> {
            if (entries.hasSelected()) {
                editTransformer(entries.getSelected().getItem());

            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 0.25f).build());

        this.deleteButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.delete"), button -> {
            if (entries.hasSelected()) {
                entries.deleteSelected();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.25f, 0.25f).build());

        this.clearButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.clear"), button -> {
            entries.clear();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.5f, 0.25f).build());

        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.add"), button -> {
            new EffortlessTransformerPresetsSelectScreen(
                    getEntrance(),
                    transformer -> {
                        editTransformer(switch (transformer.getType()) {
                            case MIRROR ->
                                    ((MirrorTransformer) transformer.withRandomId()).withPosition(Transformer.roundAllHalf(getEntrance().getClient().getPlayer().getPosition()));
                            case RADIAL ->
                                    ((RadialTransformer) transformer.withRandomId()).withPosition(Transformer.roundAllHalf(getEntrance().getClient().getPlayer().getPosition()));
                            default -> transformer.withRandomId().withName(Text.empty());
                        });
                        onReload();
                    }
            ).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.75f, 0.25f).build());

    }

    @Override
    public void onReload() {
        this.pattern = new Pattern(pattern.enabled(), entries.items());
        this.consumer.accept(this.pattern);
        this.entries.setVisible(pattern.enabled());

        this.upButton.setActive(entries.hasSelected() && entries.indexOfSelected() > 0);
        this.downButton.setActive(entries.hasSelected() && entries.indexOfSelected() < entries.children().size() - 1);
        this.editButton.setActive(entries.hasSelected());
        this.deleteButton.setActive(entries.hasSelected());
        this.addButton.setActive(entries.items().size() < 4);

        this.upButton.setVisible(getEntrance().getClient().getWindow().isAltDown() && pattern.enabled());
        this.downButton.setVisible(getEntrance().getClient().getWindow().isAltDown() && pattern.enabled());
        this.editButton.setVisible(!getEntrance().getClient().getWindow().isAltDown() && pattern.enabled());
        this.deleteButton.setVisible(!getEntrance().getClient().getWindow().isAltDown() && pattern.enabled());
        this.clearButton.setVisible(pattern.enabled());
        this.addButton.setVisible(pattern.enabled());

        this.textWidget.setVisible(this.entries.isVisible() && this.entries.items().isEmpty());

        if (entries.consumeDoubleClick() && entries.hasSelected()) {
            editTransformer(entries.getSelected().getItem());
        }

    }

    private void editTransformer(Transformer transformer) {
        switch (transformer.getType()) {
            case ARRAY, MIRROR, RADIAL -> {
                new EffortlessTransformerEditScreen(
                        getEntrance(),
                        result -> {
                            if (entries.hasSelected()) {
                                entries.replaceSelect(result);
                            } else {
                                entries.insertSelected(result);
                            }
                            onReload();
                        },
                        transformer
                ).attach();
            }
            case RANDOMIZER -> {
                new EffortlessItemRandomizerEditScreen(
                        getEntrance(),
                        result -> {
                            if (entries.hasSelected()) {
                                entries.replaceSelect(result);
                            } else {
                                entries.insertSelected(result);
                            }
                            onReload();
                        },
                        (ItemRandomizer) transformer
                ).attach();
            }
        }
    }

}
