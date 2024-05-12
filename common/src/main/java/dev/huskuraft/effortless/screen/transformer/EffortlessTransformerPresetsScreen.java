package dev.huskuraft.effortless.screen.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractContainerScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.TransformerPresets;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;

public class EffortlessTransformerPresetsScreen extends AbstractContainerScreen {

    private final Consumer<List<Transformer>> applySettings;
    private final List<Button> tabButtons = new ArrayList<>();
    private List<Transformer> builtInTransformers;
    private List<Transformer> defaultConfig;
    private List<Transformer> originalConfig;
    private List<Transformer> config;
    private TransformerList entries;
    private TextWidget titleTextWidget;
    private Button saveButton;
    private Button cancelButton;
    private Transformers selectedType = Transformers.ARRAY;

    public EffortlessTransformerPresetsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.transformer_presets.title").withStyle(ChatFormatting.DARK_GRAY), AbstractContainerScreen.CONTAINER_WIDTH_EXPANDED, AbstractContainerScreen.CONTAINER_HEIGHT_270);
        this.applySettings = transformers -> {
            getEntrance().getConfigStorage().update(config -> new ClientConfig(config.renderConfig(), new TransformerPresets(transformers), config.passiveMode()));
        };
        this.defaultConfig = getEntrance().getConfigStorage().get().transformerPresets().transformers();
        this.originalConfig = getEntrance().getConfigStorage().get().transformerPresets().transformers();
        this.config = getEntrance().getConfigStorage().get().transformerPresets().transformers();
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + AbstractContainerScreen.TITLE_CONTAINER - 10, getScreenTitle(), TextWidget.Gravity.CENTER));

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.save"), button -> {
            detach();
            applySettings.accept(config);

        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());


        for (var type : Transformers.values()) {
            tabButtons.add(
                    addWidget(Button.builder(getEntrance(), type.getDisplayName(), button -> {
                        setSelectedType(type);
                    }).setBoundsGrid(getLeft(), getTop(), getWidth(), AbstractContainerScreen.TITLE_CONTAINER + AbstractContainerScreen.BUTTON_CONTAINER_ROW_1, 0, 1f * type.ordinal() / Transformers.values().length, 1f / Transformers.values().length).build())
            );
        }

        this.entries = addWidget(new TransformerList(getEntrance(), getLeft() + AbstractContainerScreen.PADDINGS, getTop() + AbstractContainerScreen.TITLE_CONTAINER + AbstractContainerScreen.BUTTON_CONTAINER_ROW_1N, getWidth() - AbstractContainerScreen.PADDINGS * 2 - 8 /* scrollbar */, getHeight() - AbstractContainerScreen.TITLE_CONTAINER - AbstractContainerScreen.BUTTON_CONTAINER_ROW_1N - AbstractContainerScreen.BUTTON_CONTAINER_ROW_2));
        this.entries.setAlwaysShowScrollbar(true);

        setSelectedType(Transformers.ARRAY);
        this.saveButton.setActive(false);
    }

    @Override
    public void onReload() {
        for (var tabButton : tabButtons) {
            tabButton.setActive(!tabButton.getMessage().equals(selectedType.getDisplayName()));
        }
        this.entries.reset(Stream.concat(this.builtInTransformers.stream(), this.defaultConfig.stream()).toList().stream().filter(transformer -> transformer.getType() == selectedType).toList());
        this.saveButton.setActive(this.config.equals(originalConfig) || this.saveButton.isActive());
    }

    private void setSelectedType(Transformers type) {
        this.selectedType = type;
        entries.setSelected(null);
        entries.setScrollAmount(0);
    }

}

