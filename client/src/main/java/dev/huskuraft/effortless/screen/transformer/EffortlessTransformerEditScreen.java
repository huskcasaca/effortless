package dev.huskuraft.effortless.screen.transformer;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractScreen;
import dev.huskuraft.effortless.gui.button.Button;
import dev.huskuraft.effortless.text.Text;

import java.util.function.Consumer;

public class EffortlessTransformerEditScreen extends AbstractScreen {

    private final Consumer<Transformer> applySettings;
    private final Transformer defaultSettings;
    private TransformerList entries;
    private Button saveButton;
    private Button cancelButton;

    public EffortlessTransformerEditScreen(Entrance entrance, Consumer<Transformer> consumer, Transformer transformer) {
        super(entrance, Text.translate("transformer.edit.title"));
        this.applySettings = consumer;
        this.defaultSettings = transformer;
    }

    @Override
    public void onCreate() {
        this.entries = addWidget(new TransformerList(getEntrance(), 0, 50, getWidth(), getHeight() - 50 - 36, this, false, TransformerList.EntryType.EDIT));
        this.entries.insertSelected(defaultSettings);

        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("transformer.edit.save"), button -> {
            applySettings.accept(entries.getSelected().getItem());
            detach();
        }).bounds(getWidth() / 2 - 154, getHeight() - 28, 150, 20).build());
        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("transformer.edit.cancel"), button -> {
            detach();
        }).bounds(getWidth() / 2 + 4, getHeight() - 28, 150, 20).build());
    }

}
