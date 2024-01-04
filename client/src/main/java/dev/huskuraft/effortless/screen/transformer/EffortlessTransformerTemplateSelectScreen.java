package dev.huskuraft.effortless.screen.transformer;

import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.input.EditBox;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class EffortlessTransformerTemplateSelectScreen extends AbstractScreen {

    private final Consumer<Transformer> applySettings;
    private final List<Button> tabButtons = new ArrayList<>();
    private final List<Transformer> transformers;
    private TransformerList entries;
    private TextWidget titleTextWidget;
    private EditBox searchEditBox;
    private Button useTemplateButton;
    private Button cancelButton;
    private Transformers selectedType;

    public EffortlessTransformerTemplateSelectScreen(Entrance entrance, Consumer<Transformer> consumer, List<Transformer> transformers) {
        super(entrance, Text.translate("effortless.transformer.template_select.title"));
        this.applySettings = consumer;
        this.transformers = transformers;
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, 24 - 16, getScreenTitle(), TextWidget.Gravity.CENTER));

        this.searchEditBox = addWidget(
                new EditBox(getEntrance(), getWidth() / 2 - (Dimens.RegularEntry.ROW_WIDTH - 2) / 2, 24, Dimens.RegularEntry.ROW_WIDTH - 2, 20, Text.translate("effortless.transformer.template_select.search"))
        );

        this.useTemplateButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.template_select.use_template"), button -> {
            if (entries.hasSelected()) {
                var item = entries.getSelected().getItem();
                switch (item.getType()) {
                    case ARRAY, MIRROR, RADIAL -> {
                        detach();
                        new EffortlessTransformerEditScreen(
                                getEntrance(),
                                settings -> {
                                    applySettings.accept(settings);
                                },
                                item
                        ).attach();
                    }
                    case ITEM_RAND -> {
                        detach();
                        new EffortlessRandomizerEditScreen(
                                getEntrance(),
                                settings -> {
                                    applySettings.accept(settings);
                                },
                                (ItemRandomizer) item
                        ).attach();
                    }
                }
            }

        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        // FIXME: 21/10/23
        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("gui.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

        this.searchEditBox.setValue("");

        for (var type : Transformers.values()) {
            tabButtons.add(
                    addWidget(Button.builder(getEntrance(), type.getDisplayName(), button -> {
                        setSelectedType(type);
                    }).setBoundsGrid(getWidth(), 78, 0, 1f * type.ordinal() / Transformers.values().length, 1f / Transformers.values().length).build())
            );
        }

        this.entries = addWidget(new TransformerList(getEntrance(), 0, 78, getWidth(), getHeight() - 78 - 36));
        this.searchEditBox.setMaxLength(ItemRandomizer.MAX_NAME_LENGTH);
        this.searchEditBox.setHint(Text.translate("effortless.transformer.template_select.search_hint"));
        this.searchEditBox.setResponder(text -> {
            setSearchResult(text);
        });

        setSelectedType(Transformers.ARRAY);
    }

    @Override
    public void onReload() {
        useTemplateButton.setActive(entries.hasSelected());
        for (var tabButton : tabButtons) {
            tabButton.setActive(!tabButton.getMessage().equals(selectedType.getDisplayName()));
        }
    }

    private void setSelectedType(Transformers type) {
        selectedType = type;
        setSearchResult(searchEditBox.getValue());
    }

    private void setSearchResult(String string) {
        var searchTree = getEntrance().getPlatform().newSearchTree(transformers, Transformer::getSearchableTags);
        entries.reset(searchTree.search(string.toLowerCase(Locale.ROOT)).stream().filter(transformer -> transformer.getType() == selectedType).toList());
        entries.setSelected(null);
        entries.setScrollAmount(0);
    }


}

