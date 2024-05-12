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
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.config.TransformerPresets;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;

public class EffortlessTransformerPresetsSelectScreen extends AbstractPanelScreen {

    private final Consumer<Transformer> consumer;
    private final List<Button> tabButtons = new ArrayList<>();
    private Map<Transformers, List<Transformer>> builtInTransformers;
    private Map<Transformers, List<Transformer>> transformers;
    private TransformerList entries;
    private TextWidget titleTextWidget;
    private Button useTemplateButton;
    private Button cancelButton;
    private Transformers selectedType = Transformers.ARRAY;

    public EffortlessTransformerPresetsSelectScreen(Entrance entrance, Consumer<Transformer> consumer) {
        super(entrance, Text.translate("effortless.transformer.template_select.title").withStyle(ChatFormatting.DARK_GRAY), AbstractPanelScreen.PANEL_WIDTH_EXPANDED, AbstractPanelScreen.PANEL_HEIGHT_270);
        this.consumer = consumer;
        this.builtInTransformers = TransformerPresets.getBuiltInPresets().getByType();
        this.transformers = getEntrance().getConfigStorage().get().transformerPresets().getByType();
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + AbstractPanelScreen.PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle(), TextWidget.Gravity.CENTER));

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.useTemplateButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.template_select.use_template"), button -> {
            if (entries.hasSelected()) {
                detach();
                consumer.accept(entries.getSelected().getItem());
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());


        for (var type : Transformers.values()) {
            tabButtons.add(
                    addWidget(Button.builder(getEntrance(), type.getDisplayName(), button -> {
                        setSelectedType(type);
                    }).setBoundsGrid(getLeft(), getTop(), getWidth(), AbstractPanelScreen.PANEL_TITLE_HEIGHT_1 + AbstractPanelScreen.PANEL_BUTTON_ROW_HEIGHT_1, 0, 1f * type.ordinal() / Transformers.values().length, 1f / Transformers.values().length).build())
            );
        }

        this.entries = addWidget(new TransformerList(getEntrance(), getLeft() + AbstractPanelScreen.PADDINGS, getTop() + AbstractPanelScreen.PANEL_TITLE_HEIGHT_1 + AbstractPanelScreen.PANEL_BUTTON_ROW_HEIGHT_1N, getWidth() - AbstractPanelScreen.PADDINGS * 2 - 8 /* scrollbar */, getHeight() - AbstractPanelScreen.PANEL_TITLE_HEIGHT_1 - AbstractPanelScreen.PANEL_BUTTON_ROW_HEIGHT_1N - AbstractPanelScreen.PANEL_BUTTON_ROW_HEIGHT_1));
        this.entries.setAlwaysShowScrollbar(true);

        setSelectedType(selectedType);
    }

    @Override
    public void onReload() {
        useTemplateButton.setActive(entries.hasSelected());
        for (var tabButton : tabButtons) {
            tabButton.setActive(!tabButton.getMessage().equals(selectedType.getDisplayName()));
        }
        this.entries.reset(Stream.concat(this.builtInTransformers.get(selectedType).stream(), this.transformers.get(selectedType).stream()).toList());
    }

    private void setSelectedType(Transformers type) {
        this.selectedType = type;
        entries.setSelected(null);
        entries.setScrollAmount(0);
    }

}

