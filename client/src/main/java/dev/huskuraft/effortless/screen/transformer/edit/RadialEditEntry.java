package dev.huskuraft.effortless.screen.transformer.edit;

import dev.huskuraft.effortless.building.pattern.raidal.Radial;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.text.Text;

public final class RadialEditEntry extends TransformerEditEntry<Radial> {

    public RadialEditEntry(Entrance entrance, EntryList entryList, Radial radial) {
        super(entrance, entryList, radial);
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public Text getNarration() {
        return null;
    }

//    protected static final ResourceLocation BUILDING_ICONS = Effortless.asResource("textures/gui/building_icons.png");
//
//    protected List<Button> radialMirrorButtonList = new ArrayList<>();
//    protected List<IconButton> radialMirrorIconButtonList = new ArrayList<>();
//    protected List<NumberField> radialMirrorNumberFieldList = new ArrayList<>();
//
//    private NumberField textRadialMirrorPosX, textRadialMirrorPosY, textRadialMirrorPosZ, textRadialMirrorSlices, textRadialMirrorRadius;
//    private Checkbox buttonRadialMirrorEnabled, buttonRadialMirrorAlternate;
//    private IconButton buttonCurrentPosition, buttonToggleOdd, buttonDrawPlanes, buttonDrawLines;
//    private boolean drawPlanes, drawLines, toggleOdd;
//
//    public RadialSettingsPane(ScrollPane scrollPane) {
//        super(scrollPane);
//    }
//
////    @Override
////    public void init(List<Renderable> renderables) {
////        super.init(renderables);
////
////        int y = top - 2;
////        buttonRadialMirrorEnabled = new Checkbox(left - 15 + 8, y, "", false) {
////            @Override
////            public void onClick(double mouseX, double mouseY) {
////                super.onClick(mouseX, mouseY);
////                setCollapsed(!buttonRadialMirrorEnabled.isChecked());
////            }
////        };
////        renderables.add(buttonRadialMirrorEnabled);
////
////        y = top + 18;
////        textRadialMirrorPosX = new NumberField(left + Dimen.BUTTON_OFFSET_X0, y, 90, 18);
////        textRadialMirrorPosX.setNumber(0);
////        textRadialMirrorPosX.setTooltip(
////                Arrays.asList(Component.literal("The position of the radial mirror."), Component.literal("For odd numbered builds add 0.5.").withStyle(ChatFormatting.GRAY)));
////        radialMirrorNumberFieldList.add(textRadialMirrorPosX);
////
////        textRadialMirrorPosY = new NumberField(left + Dimen.BUTTON_OFFSET_X0, y + Dimen.BUTTON_VERTICAL_OFFSET, 90, 18);
////        textRadialMirrorPosY.setNumber(64);
////        textRadialMirrorPosY.setTooltip(Arrays.asList(Component.literal("The position of the radial mirror."), Component.literal("For odd numbered builds add 0.5.").withStyle(ChatFormatting.GRAY)));
////        radialMirrorNumberFieldList.add(textRadialMirrorPosY);
////
////        textRadialMirrorPosZ = new NumberField(left + Dimen.BUTTON_OFFSET_X0, y + Dimen.BUTTON_VERTICAL_OFFSET * 2, 90, 18);
////        textRadialMirrorPosZ.setNumber(0);
////        textRadialMirrorPosZ.setTooltip(Arrays.asList(Component.literal("The position of the radial mirror."), Component.literal("For odd numbered builds add 0.5.").withStyle(ChatFormatting.GRAY)));
////        radialMirrorNumberFieldList.add(textRadialMirrorPosZ);
////
////        y = top + 47;
////        textRadialMirrorSlices = new NumberField(left + Dimen.BUTTON_OFFSET_X1, y + Dimen.BUTTON_VERTICAL_OFFSET, 80, 18);
////        textRadialMirrorSlices.setNumber(4);
////        textRadialMirrorSlices.setTooltip(Arrays.asList(Component.literal("The number of repeating slices."), Component.literal("Minimally 2.").withStyle(ChatFormatting.GRAY)));
////        radialMirrorNumberFieldList.add(textRadialMirrorSlices);
////
////        textRadialMirrorRadius = new NumberField(left + Dimen.BUTTON_OFFSET_X1, y, 80, 18);
////        textRadialMirrorRadius.setNumber(50);
////        // TODO change to diameter (remove /2)
////        textRadialMirrorRadius.setTooltip(Arrays.asList(Component.literal("How far the radial mirror reaches from its center position."),
////                Component.literal("Max: ").withStyle(ChatFormatting.GRAY).append(Component.literal(String.valueOf(ReachHelper.getMaxReachDistance(mc.player) / 2)).withStyle(ChatFormatting.GOLD)),
////                Component.literal("Upgradeable in survival with reach upgrades.").withStyle(ChatFormatting.GRAY)));
////        radialMirrorNumberFieldList.add(textRadialMirrorRadius);
////
////        y = top + 72;
////        buttonCurrentPosition = new IconButton(left + Dimen.SECTION_OFFSET_X1, y, 0, 0, BUILDING_ICONS, button -> {
////            var pos = new Vec3(BaseMth.floor(mc.player.getX()) + 0.5, BaseMth.floor(mc.player.getY()) + 0.5, BaseMth.floor(mc.player.getZ()) + 0.5);
////            textRadialMirrorPosX.setNumber(pos.x);
////            textRadialMirrorPosY.setNumber(pos.y);
////            textRadialMirrorPosZ.setNumber(pos.z);
////        });
////        buttonCurrentPosition.setTooltip(Component.literal("Set radial mirror position to current player position"));
////        radialMirrorIconButtonList.add(buttonCurrentPosition);
////
////        buttonToggleOdd = new IconButton(left + Dimen.SECTION_OFFSET_X1 + 24, y, 0, 20, BUILDING_ICONS, button -> {
////            toggleOdd = !toggleOdd;
////            buttonToggleOdd.setUseAlternateIcon(toggleOdd);
////            if (toggleOdd) {
////                buttonToggleOdd.setTooltip(Arrays.asList(Component.literal("Set mirror position to corner of block"), Component.literal("for even numbered builds")));
////                textRadialMirrorPosX.setNumber(textRadialMirrorPosX.getNumber() + 0.5);
////                textRadialMirrorPosY.setNumber(textRadialMirrorPosY.getNumber() + 0.5);
////                textRadialMirrorPosZ.setNumber(textRadialMirrorPosZ.getNumber() + 0.5);
////            } else {
////                buttonToggleOdd.setTooltip(Arrays.asList(Component.literal("Set mirror position to middle of block"), Component.literal("for odd numbered builds")));
////                textRadialMirrorPosX.setNumber(BaseMth.floor(textRadialMirrorPosX.getNumber()));
////                textRadialMirrorPosY.setNumber(BaseMth.floor(textRadialMirrorPosY.getNumber()));
////                textRadialMirrorPosZ.setNumber(BaseMth.floor(textRadialMirrorPosZ.getNumber()));
////            }
////        });
////        buttonToggleOdd.setTooltip(Arrays.asList(Component.literal("Set radial mirror position to middle of block"), Component.literal("for odd numbered builds")));
////        radialMirrorIconButtonList.add(buttonToggleOdd);
////
////        buttonDrawLines = new IconButton(left + Dimen.SECTION_OFFSET_X1 + 24 * 2, y, 0, 40, BUILDING_ICONS, button -> {
////            drawLines = !drawLines;
////            buttonDrawLines.setUseAlternateIcon(drawLines);
////            buttonDrawLines.setTooltip(Component.literal(drawLines ? "Hide lines" : "Show lines"));
////        });
////        buttonDrawLines.setTooltip(Component.literal("Show lines"));
////        radialMirrorIconButtonList.add(buttonDrawLines);
////
////        buttonDrawPlanes = new IconButton(left + Dimen.SECTION_OFFSET_X1 + 24 * 3, y, 0, 60, BUILDING_ICONS, button -> {
////            drawPlanes = !drawPlanes;
////            buttonDrawPlanes.setUseAlternateIcon(drawPlanes);
////            buttonDrawPlanes.setTooltip(Component.literal(drawPlanes ? "Hide area" : "Show area"));
////        });
////        buttonDrawPlanes.setTooltip(Component.literal("Show area"));
////        radialMirrorIconButtonList.add(buttonDrawPlanes);
////
////        y = top + 76;
////        buttonRadialMirrorAlternate = new Checkbox(left + Dimen.SECTION_OFFSET_X0, y, " Alternate", false);
////        radialMirrorButtonList.add(buttonRadialMirrorAlternate);
////
////        var transformerSettings = EffortlessBuilder.getInstance().getTransformerSettings(mc.player);
////        if (transformerSettings != null) {
////            var radialMirrorSettings = transformerSettings.radialMirrorSettings();
////            buttonRadialMirrorEnabled.setIsChecked(radialMirrorSettings.enabled());
////            textRadialMirrorPosX.setNumber(radialMirrorSettings.position().x);
////            textRadialMirrorPosY.setNumber(radialMirrorSettings.position().y);
////            textRadialMirrorPosZ.setNumber(radialMirrorSettings.position().z);
////            textRadialMirrorSlices.setNumber(radialMirrorSettings.slices());
////            buttonRadialMirrorAlternate.setIsChecked(radialMirrorSettings.alternate());
////            textRadialMirrorRadius.setNumber(radialMirrorSettings.radius());
////            drawLines = radialMirrorSettings.drawLines();
////            drawPlanes = radialMirrorSettings.drawPlanes();
////            buttonDrawLines.setUseAlternateIcon(drawLines);
////            buttonDrawPlanes.setUseAlternateIcon(drawPlanes);
////            buttonDrawLines.setTooltip(Component.literal(drawLines ? "Hide lines" : "Show lines"));
////            buttonDrawPlanes.setTooltip(Component.literal(drawPlanes ? "Hide area" : "Show area"));
////            if (textRadialMirrorPosX.getNumber() == BaseMth.floor(textRadialMirrorPosX.getNumber())) {
////                toggleOdd = false;
////                buttonToggleOdd.setTooltip(Arrays.asList(Component.literal("Set radial mirror position to middle of block"), Component.literal("for odd numbered builds")));
////            } else {
////                toggleOdd = true;
////                buttonToggleOdd.setTooltip(Arrays.asList(Component.literal("Set radial mirror position to corner of block"), Component.literal("for even numbered builds")));
////            }
////            buttonToggleOdd.setUseAlternateIcon(toggleOdd);
////        }
////
////        renderables.addAll(radialMirrorButtonList);
////        renderables.addAll(radialMirrorIconButtonList);
////
////        setCollapsed(!buttonRadialMirrorEnabled.isChecked());
////    }
//
//    public void updateScreen() {
//        radialMirrorNumberFieldList.forEach(NumberField::tick);
//    }
//
//
//    @Override
//    public void drawEntry(Renderer renderer, int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
//                          boolean isSelected, float partialTicks) {
//
//        int offset = 8;
//
//        buttonRadialMirrorEnabled.render(renderer, mouseX, mouseY, partialTicks);
//        if (buttonRadialMirrorEnabled.isChecked()) {
//            buttonRadialMirrorEnabled.setY(y);
//            font.draw(renderer, "Radial mirror enabled", left + offset, y + 2, 0xFFFFFF);
//
//            var positionOffsetX0 = left + Dimen.SECTION_OFFSET_X0;
//            var positionOffsetX1 = left + Dimen.SECTION_OFFSET_X1;
//            var positionOffsetY0 = y + 24;
//            var positionOffsetY1 = y + 24 * 2;
//            var positionOffsetY2 = y + 24 * 3;
//            var positionOffsetY3 = y + 24 * 4;
//
//            var textOffsetX = 40;
//            var componentOffsetY = -5;
//
//            font.draw(renderer, "Position", left + offset, positionOffsetY0, 0xFFFFFF);
//            font.draw(renderer, "X", positionOffsetX0 + textOffsetX, positionOffsetY0, 0xFFFFFF);
//            font.draw(renderer, "Y", positionOffsetX0 + textOffsetX, positionOffsetY1, 0xFFFFFF);
//            font.draw(renderer, "Z", positionOffsetX0 + textOffsetX, positionOffsetY2, 0xFFFFFF);
//            textRadialMirrorPosX.setY(positionOffsetY0 + componentOffsetY);
//            textRadialMirrorPosY.setY(positionOffsetY1 + componentOffsetY);
//            textRadialMirrorPosZ.setY(positionOffsetY2 + componentOffsetY);
//
//            font.draw(renderer, "Radius", positionOffsetX1, positionOffsetY0, 0xFFFFFF);
//            textRadialMirrorRadius.setY(positionOffsetY0 + componentOffsetY);
//
//            font.draw(renderer, "Slices", positionOffsetX1, positionOffsetY1, 0xFFFFFF);
//            textRadialMirrorSlices.setY(positionOffsetY1 + componentOffsetY);
//
//            buttonCurrentPosition.setY(positionOffsetY2 - 6);
//            buttonToggleOdd.setY(positionOffsetY2 - 6);
//            buttonDrawLines.setY(positionOffsetY2 - 6);
//            buttonDrawPlanes.setY(positionOffsetY2 - 6);
//
//            buttonRadialMirrorAlternate.setY(positionOffsetY3);
//
//            radialMirrorButtonList.forEach(button -> button.render(renderer, mouseX, mouseY, partialTicks));
//            radialMirrorIconButtonList.forEach(button -> button.render(renderer, mouseX, mouseY, partialTicks));
//            radialMirrorNumberFieldList
//                    .forEach(numberField -> numberField.render(renderer, mouseX, mouseY, partialTicks));
//        } else {
//            buttonRadialMirrorEnabled.setY(y);
//            font.draw(renderer, "Radial mirror disabled", left + offset, y + 2, 0x999999);
//        }
//
//    }
//
//    public void drawTooltip(Renderer renderer, Screen guiScreen, int mouseX, int mouseY) {
//        // Draw tooltips last
//        if (buttonRadialMirrorEnabled.isChecked()) {
//            radialMirrorIconButtonList.forEach(iconButton -> iconButton.drawTooltip(renderer, scrollPane.parent, mouseX, mouseY));
//            radialMirrorNumberFieldList.forEach(numberField -> numberField.drawTooltip(renderer, scrollPane.parent, mouseX, mouseY));
//        }
//    }
//
//    @Override
//    public boolean charTyped(char typedChar, int keyCode) {
//        super.charTyped(typedChar, keyCode);
//        for (NumberField numberField : radialMirrorNumberFieldList) {
//            numberField.charTyped(typedChar, keyCode);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
//        radialMirrorNumberFieldList.forEach(numberField -> numberField.mouseClicked(mouseX, mouseY, mouseEvent));
//
//        boolean insideRadialMirrorEnabledLabel = mouseX >= left && mouseX < right && relativeY >= -2 && relativeY < 12;
//
//        if (insideRadialMirrorEnabledLabel) {
//            buttonRadialMirrorEnabled.playDownSound(this.minecraft.getSoundManager());
//            buttonRadialMirrorEnabled.onClick(mouseX, mouseY);
//        }
//
//        return true;
//    }
//
//    @Override
//    protected String getName() {
//        return "Radial mirror";
//    }
//
//    @Override
//    protected int getExpandedHeight() {
//        return 128;
//    }
}
