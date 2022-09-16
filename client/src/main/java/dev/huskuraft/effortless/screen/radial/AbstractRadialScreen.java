package dev.huskuraft.effortless.screen.radial;

import dev.huskuraft.effortless.building.Option;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.gui.AbstractScreen;
import dev.huskuraft.effortless.gui.selection.RadialSection;
import dev.huskuraft.effortless.text.Text;

import java.awt.*;
import java.util.List;

public class AbstractRadialScreen<S, B> extends AbstractScreen {

    private static final float FADE_SPEED = 0.5f;
    private static final int WATERMARK_TEXT_COLOR = 0x8d7f7f7f;
    protected RadialSection<S, B> radial;

    public AbstractRadialScreen(Entrance entrance, Text text) {
        super(entrance, text);
    }

    protected static <T> RadialSection.Slot<T> slot(Text name, Resource icon, Color tintColor, T slot) {
        return new RadialSection.Slot<>() {
            @Override
            public Text getNameComponent() {
                return name;
            }

            @Override
            public Text getCategoryComponent() {
                return null;
            }

            @Override
            public Resource getIcon() {
                return icon;
            }

            @Override
            public Color getTintColor() {
                return tintColor;
            }

            @Override
            public T getContent() {
                return slot;
            }

        };
    }

    protected static <T> RadialSection.Button<T> button(Text category, Text name, Resource icon, T option) {
        return new RadialSection.Button<>() {
            @Override
            public Text getNameComponent() {
                return name;
            }

            @Override
            public Text getCategoryComponent() {
                return category;
            }

            @Override
            public Resource getIcon() {
                return icon;
            }

            @Override
            public Color getTintColor() {
                return null;
            }

            @Override
            public T getContent() {
                return option;
            }

        };
    }

    @SafeVarargs
    protected static <T> RadialSection.ButtonSet<T> buttonSet(RadialSection.Button<T>... entries) {
        return buttonSet(List.of(entries));
    }

    protected static <T> RadialSection.ButtonSet<T> buttonSet(List<? extends RadialSection.Button<T>> entries) {
        return new RadialSection.ButtonSet<>() {
            @Override
            public Text getNameComponent() {
                return null;
            }

            @Override
            public List<? extends RadialSection.Button<T>> getButtons() {
                return entries;
            }
        };
    }

    // TODO: 20/9/23 move
    public static <T extends Option> RadialSection.Button<T> option(T option) {
        return button(
                Text.translate(Text.asKey("option", option.getCategory())),
                Text.translate(Text.asKey("action", option.getName())),
                Resource.of("textures/option/" + option.getName() + ".png"),
                option
        );
    }

    @Override
    public void onCreate() {
        this.radial = addWidget(new RadialSection<>(getEntrance(), 0, 0, this.getWidth(), this.getHeight(), Text.empty()));
        this.radial.setVisibility(1f);
    }

    @Override
    public boolean isPauseGame() {
        return true;
    }

}

