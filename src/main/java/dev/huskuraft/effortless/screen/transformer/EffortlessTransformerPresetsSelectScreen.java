package dev.huskuraft.effortless.screen.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.universal.api.gui.AbstractPanelScreen;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.ChatFormatting;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;

public class EffortlessTransformerPresetsSelectScreen extends AbstractPanelScreen {

    private final Consumer<Transformer> consumer;
    private final List<Button> tabButtons = new ArrayList<>();
    private Map<Transformers, List<? extends Transformer>> builtInTransformers;
    private Map<Transformers, List<? extends Transformer>> transformers;
    private TransformerList entries;
    private TextWidget titleTextWidget;
    private Button useTemplateButton;
    private Button cancelButton;
    private Transformers selectedType = Transformers.ARRAY;

    public EffortlessTransformerPresetsSelectScreen(Entrance entrance, Consumer<Transformer> consumer) {
        super(entrance, Text.translate("effortless.transformer.template_select.title").withStyle(ChatFormatting.DARK_GRAY), PANEL_WIDTH_60, PANEL_HEIGHT_FULL);
        this.consumer = consumer;
        this.builtInTransformers = PatternConfig.getBuiltInPresets().getByType();
        this.transformers = getEntrance().getConfigStorage().get().patternConfig().getByType();
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.useTemplateButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.template_select.use_template"), button -> {
            if (entries.hasSelected()) {
                detach();
                consumer.accept(entries.getSelected().getItem());
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

        this.tabButtons.clear();
        for (var type : Transformers.values()) {
            this.tabButtons.add(
                    addWidget(Button.builder(getEntrance(), type.getTitleText(), button -> {
                        setSelectedType(type);
                    }).setBoundsGrid(getLeft(), getTop(), getWidth(), PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_1, 0, 1f * type.ordinal() / Transformers.values().length, 1f / Transformers.values().length).build())
            );
        }

        this.entries = addWidget(new TransformerList(getEntrance(), getLeft() + AbstractPanelScreen.PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_1N, getWidth() - AbstractPanelScreen.PADDINGS_H * 2 - 8 /* scrollbar */, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_1N - PANEL_BUTTON_ROW_HEIGHT_1));
        this.entries.setAlwaysShowScrollbar(true);

        setSelectedType(selectedType);
    }

    @Override
    public void onReload() {
        useTemplateButton.setActive(entries.hasSelected());
        for (var tabButton : tabButtons) {
            tabButton.setActive(!tabButton.getMessage().equals(selectedType.getTitleText()));
        }
        this.entries.reset(Stream.concat(this.builtInTransformers.get(selectedType).stream(), this.transformers.get(selectedType).stream()).toList());
        if (entries.consumeDoubleClick() && entries.hasSelected()) {
            detach();
            consumer.accept(entries.getSelected().getItem());
        }
    }

    private void setSelectedType(Transformers type) {
        this.selectedType = type;
        entries.setSelected(null);
        entries.setScrollAmount(0);
    }

}

