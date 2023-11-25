package dev.huskuraft.effortless.screen.transformer;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractScreen;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.button.Button;
import dev.huskuraft.effortless.gui.input.EditBox;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.screen.randomizer.EffortlessRandomizerEditScreen;
import dev.huskuraft.effortless.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class EffortlessTransformerTemplateSelectScreen extends AbstractScreen {

    private static final int MAX_SEARCH_NAME_LENGTH = 255;
    private static final int TAB_WIDTH = 312;
    private static final int GAP_WIDTH = 4;
    private static final int ROW_WIDTH = Dimens.RegularEntry.ROW_WIDTH;

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
        super(entrance, Text.translate("transformer.template_select.title"));
        this.applySettings = consumer;
        this.transformers = transformers;
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, 24 - 16, getScreenTitle(), TextWidget.Gravity.CENTER));

        this.searchEditBox = addWidget(
                new EditBox(getEntrance(), getWidth() / 2 - (ROW_WIDTH - 2) / 2, 24, ROW_WIDTH - 2, 20, Text.translate("transformer.template_select.search"))
        );

        this.useTemplateButton = addWidget(Button.builder(getEntrance(), Text.translate("transformer.template_select.use_template"), button -> {
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

        }).bounds(getWidth() / 2 - 154, getHeight() - 28, 150, 20).build());
        // FIXME: 21/10/23
        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("gui.cancel"), button -> {
            detach();
        }).bounds(getWidth() / 2 + 4, getHeight() - 28, 150, 20).build());

        this.searchEditBox.setValue("");

        for (var type : Transformers.values()) {
            tabButtons.add(
                    addWidget(Button.builder(getEntrance(), Text.translate(type.getNameKey()), button -> {
                        setSelectedType(type);
                    }).bounds(getWidth() / 2 - TAB_WIDTH / 2 + TAB_WIDTH / Transformers.values().length * type.ordinal() + GAP_WIDTH / 2, 48 + 4, TAB_WIDTH / Transformers.values().length - GAP_WIDTH, 20).build())
            );
        }
        this.entries = addWidget(new TransformerList(getEntrance(), 0, 78, getWidth(), getHeight() - 78 - 36, this, false, TransformerList.EntryType.INFO));
        this.searchEditBox.setMaxLength(MAX_SEARCH_NAME_LENGTH);
        this.searchEditBox.setHint(Text.translate("transformer.template_select.search_hint"));
        this.searchEditBox.setResponder(text -> {
            setSearchResult(text);
        });

        setSelectedType(Transformers.ARRAY);
    }

    @Override
    public void onReload() {
        useTemplateButton.setActive(entries.hasSelected());
        for (var tabButton : tabButtons) {
            tabButton.setActive(!tabButton.getMessage().equals(Text.translate(selectedType.getNameKey())));
        }
    }

    private void setSelectedType(Transformers type) {
        selectedType = type;
        setSearchResult(searchEditBox.getValue());
    }

    private void setSearchResult(String string) {
        var searchTree = getEntrance().getContentCreator().createSearchTree(transformers, Transformer::getSearchableTags);
        entries.reset(searchTree.search(string.toLowerCase(Locale.ROOT)).stream().filter(transformer -> transformer.getType() == selectedType).toList());
        entries.setSelected(null);
        entries.setScrollAmount(0);
    }


}

