package dev.huskuraft.effortless.screen.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import dev.huskuraft.effortless.api.gui.AbstractContainerScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;

public class EffortlessTransformerPresetSelectScreen extends AbstractContainerScreen {

    private final Consumer<Transformer> consumer;
    private final List<Button> tabButtons = new ArrayList<>();
    private final List<Transformer> transformers;
    private TransformerList entries;
    private TextWidget titleTextWidget;
    private Button useTemplateButton;
    private Button cancelButton;
    private Transformers selectedType = Transformers.ARRAY;

    public EffortlessTransformerPresetSelectScreen(Entrance entrance, Consumer<Transformer> consumer) {
        super(entrance, Text.translate("effortless.transformer.template_select.title").withStyle(ChatFormatting.DARK_GRAY), AbstractContainerScreen.CONTAINER_WIDTH_EXPANDED, AbstractContainerScreen.CONTAINER_HEIGHT_180);
        this.consumer = consumer;
        this.transformers = Transformer.getDefaultTransformers();
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + AbstractContainerScreen.TITLE_CONTAINER - 10, getScreenTitle(), TextWidget.Gravity.CENTER));

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
                    }).setBoundsGrid(getLeft(), getTop(), getWidth(), AbstractContainerScreen.TITLE_CONTAINER + AbstractContainerScreen.BUTTON_CONTAINER_ROW_1, 0, 1f * type.ordinal() / Transformers.values().length, 1f / Transformers.values().length).build())
            );
        }

        this.entries = addWidget(new TransformerList(getEntrance(), getLeft() + AbstractContainerScreen.PADDINGS, getTop() + AbstractContainerScreen.TITLE_CONTAINER + AbstractContainerScreen.BUTTON_CONTAINER_ROW_1N, getWidth() - AbstractContainerScreen.PADDINGS * 2 - 8 /* scrollbar */, getHeight() - AbstractContainerScreen.TITLE_CONTAINER - AbstractContainerScreen.BUTTON_CONTAINER_ROW_1N - AbstractContainerScreen.BUTTON_CONTAINER_ROW_1));
        this.entries.setAlwaysShowScrollbar(true);

        setSelectedType(Transformers.ARRAY);
    }

    @Override
    public void onReload() {
        useTemplateButton.setActive(entries.hasSelected());
        for (var tabButton : tabButtons) {
            tabButton.setActive(!tabButton.getMessage().equals(selectedType.getDisplayName()));
        }
        entries.reset(transformers.stream().filter(transformer -> transformer.getType() == selectedType).toList());
    }

    private void setSelectedType(Transformers type) {
        this.selectedType = type;
        entries.setSelected(null);
        entries.setScrollAmount(0);
    }

}

