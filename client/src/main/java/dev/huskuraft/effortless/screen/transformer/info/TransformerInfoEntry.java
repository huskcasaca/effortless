package dev.huskuraft.effortless.screen.transformer.info;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.Orientation;
import dev.huskuraft.effortless.gui.AbstractWidget;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.gui.button.MoveButton;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.icon.TextIcon;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransformerInfoEntry<T extends Transformer> extends EditableEntry<T> {

    private TextIcon icon;
    private MoveButton button;
    private List<AbstractWidget> basicDescWidgets = new ArrayList<>();
    private List<AbstractWidget> extraDescWidgets = new ArrayList<>();

    public TransformerInfoEntry(Entrance entrance, EntryList entryList, T transformer) {
        super(entrance, entryList, transformer);
    }

    protected static Text getPositionDescription(Vector3d position) {
        if (position != null) {
            return Text.text(position.getX() + ", " + position.getY() + ", " + position.getZ());
        } else {
            return Text.translate("effortless.position.undefined");
        }
    }

    protected static Text getIntegerDescription(Integer slice) {
        if (slice != null) {
            return Text.text(String.valueOf(slice));
        } else {
            return Text.translate("effortless.integer.undefined");
        }
    }

    @Override
    public void onCreate() {
        icon = addWidget(new TextIcon(getEntrance(), getX(), getY(), Dimens.ICON_WIDTH, Dimens.ICON_HEIGHT, getSymbol(getItem())));
        button = addWidget(new MoveButton(getEntrance(), getX(), getY(), direction -> {
            switch (direction) {
                case UP -> getEntryList().moveUp(this);
                case DOWN -> getEntryList().moveDown(this);
            }
        }, Orientation.UP, Orientation.DOWN));
        addWidget(new TextWidget(getEntrance(), getX() + 2 + 32 + 1, getY() + 1, getDisplayName(getItem())));
        updateDescriptionWidgets();
    }

    @Override
    public void onReload() {
        button.setActive(getEntryList().isEditable());
        button.setVisible(getEntryList().isEditable());
        for (var widget : extraDescWidgets) {
            widget.setVisible(getEntryList().getSelected() == this);
        }
    }

    private void updateDescriptionWidgets() {
        for (var basicDescWidget : basicDescWidgets) {
            removeWidget(basicDescWidget);
        }
        for (var extraDescWidget : extraDescWidgets) {
            removeWidget(extraDescWidget);
        }
        var basicDescWidgets = new ArrayList<AbstractWidget>();
        var extraDescWidgets = new ArrayList<AbstractWidget>();
        var index = 0;
        for (var component : getBasicComponents(getItem())) {
            var widget = addWidget(new TextWidget(getEntrance(), 2 + 32 + 1, 1 + 11 * ++index, component));
            basicDescWidgets.add(widget);
        }
        for (var component : getExtraComponents(getItem())) {
            var widget = addWidget(new TextWidget(getEntrance(), 2 + 32 + 1, 1 + 11 * ++index, component));
            widget.setVisible(false);
            extraDescWidgets.add(widget);
        }
        this.basicDescWidgets = basicDescWidgets;
        this.extraDescWidgets = extraDescWidgets;
    }

    @Override
    public void setItem(T item) {
        super.setItem(item);
        icon.setMessage(getSymbol(item));
        updateDescriptionWidgets();
    }

    @Override
    public Text getNarration() {
        return Text.translate("narrator.select", getDisplayName(getItem()));
    }

    protected List<Text> getBasicComponents(T transformer) {
        return Collections.emptyList();
    }

    protected List<Text> getExtraComponents(T transformer) {
        return Collections.emptyList();
    }

    @Override
    public int getWidth() {
        return Dimens.RegularEntry.ROW_WIDTH;
    }

    @Override
    public int getHeight() {
        var count = basicDescWidgets.size() + (getEntryList().getSelected() == this ? extraDescWidgets.size() : 0);
        return MathUtils.max(36, 14 + count * 11);
    }

    protected Text getDisplayName(T transformer) {
        if (transformer.getName().isBlank()) {
            return Text.text("No Name").withStyle(TextStyle.GRAY, TextStyle.ITALIC);
        }
        return Text.text(transformer.getName());
    }

    public static Text getSymbol(Transformer transformer) {
        return switch (transformer.getType()) {
            case ARRAY -> Text.text("AT");
            case MIRROR -> Text.text("MT");
            case RADIAL -> Text.text("RT");
            case ITEM_RAND -> Text.text("IR");
        };
//            if (getDisplayName(transformer).getString().isBlank()) {
//                return Component.empty();
//            }
//            return Component.literal(getDisplayName(transformer).getString().substring(0, 1));
    }

}
