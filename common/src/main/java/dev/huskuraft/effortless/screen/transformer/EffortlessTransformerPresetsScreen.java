package dev.huskuraft.effortless.screen.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.gui.tooltip.TooltipHelper;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public class EffortlessTransformerPresetsScreen extends AbstractPanelScreen {

    private final Consumer<List<Transformer>> applySettings;
    private final List<Button> tabButtons = new ArrayList<>();
    private Map<Transformers, List<Transformer>> builtInTransformers;
    private Map<Transformers, List<Transformer>> defaultConfig;
    private Map<Transformers, List<Transformer>> originalConfig;
    private Map<Transformers, List<Transformer>> config;
    private TransformerList entries;
    private TextWidget titleTextWidget;
    private Button editButton;
    private Button deleteButton;
    private Button clearButton;
    private Button addButton;
    private Button saveButton;
    private Transformers selectedType = Transformers.ARRAY;

    public EffortlessTransformerPresetsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.transformer_presets.title").withStyle(ChatFormatting.DARK_GRAY), PANEL_WIDTH_EXPANDED, PANEL_HEIGHT_270);
        this.applySettings = transformers -> {
            getEntrance().getConfigStorage().update(config -> config.withPatternConfig(new PatternConfig(transformers)));
        };
        this.builtInTransformers = PatternConfig.getBuiltInPresets().getByType();
        this.defaultConfig = getEntrance().getConfigStorage().get().patternConfig().getByType();
        this.originalConfig = getEntrance().getConfigStorage().get().patternConfig().getByType();
        this.config = getEntrance().getConfigStorage().get().patternConfig().getByType();
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(0x00404040), TextWidget.Gravity.CENTER));

        this.editButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.edit"), button -> {
            if (entries.getSelected() != null && !entries.getSelected().getItem().isBuiltIn()) {
                editTransformer(entries.getSelected().getItem());
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 0.25f).build());

        this.deleteButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.delete"), button -> {
            if (entries.getSelected() != null && !entries.getSelected().getItem().isBuiltIn()) {
                entries.deleteSelected();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.25f, 0.25f).build());


        this.clearButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.clear"), button -> {
            this.entries.reset(this.entries.items().stream().filter(transformer1 -> transformer1.isBuiltIn()).toList());
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.5f, 0.25f).build());


        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.add"), button -> {
            editTransformer(switch (selectedType) {
                case ARRAY -> ArrayTransformer.DEFAULT.withName(Text.empty()).withRandomId();
                case MIRROR -> MirrorTransformer.DEFAULT_X.withName(Text.empty()).withRandomId();
                case RADIAL -> RadialTransformer.DEFAULT.withName(Text.empty()).withRandomId();
                case ITEM_RANDOMIZER -> ItemRandomizer.EMPTY.withName(Text.empty()).withRandomId();
            });
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.75f, 0.25f).build());

        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.save"), button -> {
            detach();
            applySettings.accept(config.values().stream().flatMap(List::stream).toList());

        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());


        this.tabButtons.clear();
        for (var type : Transformers.values()) {
            tabButtons.add(
                    addWidget(Button.builder(getEntrance(), type.getDisplayName(), button -> {
                        setSelectedType(type);
                    }).setBoundsGrid(getLeft(), getTop(), getWidth(), PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_1, 0, 1f * type.ordinal() / Transformers.values().length, 1f / Transformers.values().length).build())
            );
        }

        this.entries = addWidget(new TransformerList(getEntrance(), getLeft() + AbstractPanelScreen.PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_1N, getWidth() - AbstractPanelScreen.PADDINGS_H * 2 - 8 /* scrollbar */, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_1N - PANEL_BUTTON_ROW_HEIGHT_2));
        this.entries.setAlwaysShowScrollbar(true);

        setSelectedType(selectedType);
    }

    @Override
    public void onReload() {
        for (var tabButton : tabButtons) {
            tabButton.setActive(!tabButton.getMessage().equals(selectedType.getDisplayName()));
        }
        this.editButton.setActive(entries.getSelected() != null && !entries.getSelected().getItem().isBuiltIn());
        this.deleteButton.setActive(entries.getSelected() != null && !entries.getSelected().getItem().isBuiltIn());
        this.config.put(selectedType, this.entries.items().stream().filter(transformer1 -> !transformer1.isBuiltIn()).filter(transformer1 -> transformer1.getType() == selectedType).toList());

        if (entries.getSelected() != null && entries.getSelected().getItem().isBuiltIn()) {
            this.editButton.setTooltip(
                    Stream.concat(
                            Stream.of(Text.translate("effortless.transformer.edit.tooltip.cannot_edit_built_in.title")),
                            TooltipHelper.wrapLines(getTypeface(), Text.translate("effortless.transformer.edit.tooltip.cannot_edit_built_in.message").withStyle(ChatFormatting.GRAY)).stream()
                    ).toList()
            );
        } else {
            this.editButton.clearTooltip();
        }
        if (entries.getSelected() != null && entries.getSelected().getItem().isBuiltIn()) {
            this.deleteButton.setTooltip(
                    Stream.concat(
                            Stream.of(Text.translate("effortless.transformer.delete.tooltip.cannot_delete_built_in.title")),
                            TooltipHelper.wrapLines(getTypeface(), Text.translate("effortless.transformer.delete.tooltip.cannot_delete_built_in.message").withStyle(ChatFormatting.GRAY)).stream()
                    ).toList()
            );
        } else {
            this.deleteButton.clearTooltip();
        }
        this.addButton.setTooltip(
                Stream.concat(
                        switch (selectedType) {
                            case ARRAY -> Stream.of(Text.translate("effortless.transformer.add.tooltip.array.title"));
                            case MIRROR -> Stream.of(Text.translate("effortless.transformer.add.tooltip.mirror.title"));
                            case RADIAL -> Stream.of(Text.translate("effortless.transformer.add.tooltip.radial.title"));
                            case ITEM_RANDOMIZER -> Stream.of(Text.translate("effortless.transformer.add.tooltip.random.title"));
                        },
                        TooltipHelper.wrapLines(getTypeface(), (switch (selectedType) {
                            case ARRAY -> Text.translate("effortless.transformer.add.tooltip.array.message");
                            case MIRROR -> Text.translate("effortless.transformer.add.tooltip.mirror.message");
                            case RADIAL -> Text.translate("effortless.transformer.add.tooltip.radial.message");
                            case ITEM_RANDOMIZER -> Text.translate("effortless.transformer.add.tooltip.random.message");
                        }).withStyle(ChatFormatting.GRAY)).stream()
                ).toList()
        );

        if (entries.consumeDoubleClick() && entries.hasSelected()) {
            detach();
            editTransformer(entries.getSelected().getItem());
        }

    }

    private void setSelectedType(Transformers type) {
        this.selectedType = type;
        this.entries.setSelected(null);
        this.entries.reset(Stream.concat(this.builtInTransformers.get(selectedType).stream(), this.config.get(selectedType).stream()).toList());
        this.entries.setScrollAmount(0);
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
            case ITEM_RANDOMIZER -> {
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

