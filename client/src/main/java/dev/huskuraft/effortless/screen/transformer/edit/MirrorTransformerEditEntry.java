package dev.huskuraft.effortless.screen.transformer.edit;

import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.text.Text;

public final class MirrorTransformerEditEntry extends TransformerEditEntry<MirrorTransformer> {

    public MirrorTransformerEditEntry(Entrance entrance, EntryList entryList, MirrorTransformer mirrorTransformer) {
        super(entrance, entryList, mirrorTransformer);
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
//    protected List<Button> mirrorButtonList = new ArrayList<>();
//    protected List<IconButton> mirrorIconButtonList = new ArrayList<>();
//    protected List<NumberField> mirrorNumberFieldList = new ArrayList<>();
//
//    private NumberField textMirrorPosX, textMirrorPosY, textMirrorPosZ, textMirrorRadius;
//    private Checkbox buttonMirrorEnabled, buttonMirrorX, buttonMirrorY, buttonMirrorZ;
//    private IconButton buttonCurrentPosition, buttonToggleOdd, buttonDrawPlanes, buttonDrawLines;
//    private boolean drawPlanes, drawLines, toggleOdd;
//
//    public MirrorSettingsPane(ScrollPane scrollPane) {
//        super(scrollPane);
//    }
//
////    @Override
////    public void init(List<Renderable> renderables) {
////        super.init(renderables);
////
////        int y = top - 2;
////        buttonMirrorEnabled = new Checkbox(left - 15 + 8, y, "", false) {
////            @Override
////            public void onClick(double mouseX, double mouseY) {
////                super.onClick(mouseX, mouseY);
////                setCollapsed(!buttonMirrorEnabled.isChecked());
////            }
////        };
////        renderables.add(buttonMirrorEnabled);
////
////        y = top + 20;
////        textMirrorPosX = new NumberField(left + Dimen.BUTTON_OFFSET_X0, y, 90, 18);
////        textMirrorPosX.setNumber(0);
////        textMirrorPosX.setTooltip(
////                Arrays.asList(Component.literal("The position of the mirror."), Component.literal("For odd numbered builds add 0.5.").withStyle(ChatFormatting.GRAY)));
////        mirrorNumberFieldList.add(textMirrorPosX);
////
////        textMirrorPosY = new NumberField(left + Dimen.BUTTON_OFFSET_X0, y + Dimen.BUTTON_VERTICAL_OFFSET, 90, 18);
////        textMirrorPosY.setNumber(64);
////        textMirrorPosY.setTooltip(Arrays.asList(Component.literal("The position of the mirror."), Component.literal("For odd numbered builds add 0.5.").withStyle(ChatFormatting.GRAY)));
////        mirrorNumberFieldList.add(textMirrorPosY);
////
////        textMirrorPosZ = new NumberField(left + Dimen.BUTTON_OFFSET_X0, y + Dimen.BUTTON_VERTICAL_OFFSET * 2, 90, 18);
////        textMirrorPosZ.setNumber(0);
////        textMirrorPosZ.setTooltip(Arrays.asList(Component.literal("The position of the mirror."), Component.literal("For odd numbered builds add 0.5.").withStyle(ChatFormatting.GRAY)));
////        mirrorNumberFieldList.add(textMirrorPosZ);
////
////        y = top + 50;
////        buttonMirrorX = new Checkbox(left + Dimen.BUTTON_OFFSET_X1 - 10, y, " X", true);
////        mirrorButtonList.add(buttonMirrorX);
////
////        buttonMirrorY = new Checkbox(left + Dimen.BUTTON_OFFSET_X1 - 10 + 32, y, " Y", false);
////        mirrorButtonList.add(buttonMirrorY);
////
////        buttonMirrorZ = new Checkbox(left + Dimen.BUTTON_OFFSET_X1 - 10 + 32 * 2, y, " Z", false);
////        mirrorButtonList.add(buttonMirrorZ);
////
////        y = top + 47;
////        textMirrorRadius = new NumberField(left + Dimen.BUTTON_OFFSET_X1, y, 80, 18);
////        textMirrorRadius.setNumber(50);
////        // TODO change to diameter (remove /2)
////        textMirrorRadius.setTooltip(Arrays.asList(Component.literal("How far the mirror reaches in any direction."),
////                Component.literal("Max: ").withStyle(ChatFormatting.GRAY).append(Component.literal(String.valueOf(ReachHelper.getMaxReachDistance(mc.player) / 2)).withStyle(ChatFormatting.GOLD)),
////                Component.literal("Upgradeable in survival with reach upgrades.").withStyle(ChatFormatting.GRAY)));
////        mirrorNumberFieldList.add(textMirrorRadius);
////
////        y = top + 72;
////        buttonCurrentPosition = new IconButton(left + Dimen.SECTION_OFFSET_X1, y, 0, 0, BUILDING_ICONS, button -> {
////            var pos = new Vec3(BaseMth.floor(mc.player.getX()) + 0.5, BaseMth.floor(mc.player.getY()) + 0.5, BaseMth.floor(mc.player.getZ()) + 0.5);
////            textMirrorPosX.setNumber(pos.x);
////            textMirrorPosY.setNumber(pos.y);
////            textMirrorPosZ.setNumber(pos.z);
////        });
////        buttonCurrentPosition.setTooltip(Component.literal("Set mirror position to current player position"));
////        mirrorIconButtonList.add(buttonCurrentPosition);
////
////        buttonToggleOdd = new IconButton(left + Dimen.SECTION_OFFSET_X1 + 24, y, 0, 20, BUILDING_ICONS, button -> {
////            toggleOdd = !toggleOdd;
////            buttonToggleOdd.setUseAlternateIcon(toggleOdd);
////            if (toggleOdd) {
////                buttonToggleOdd.setTooltip(Arrays.asList(Component.literal("Set mirror position to corner of block"), Component.literal("for even numbered builds")));
////                textMirrorPosX.setNumber(textMirrorPosX.getNumber() + 0.5);
////                textMirrorPosY.setNumber(textMirrorPosY.getNumber() + 0.5);
////                textMirrorPosZ.setNumber(textMirrorPosZ.getNumber() + 0.5);
////            } else {
////                buttonToggleOdd.setTooltip(Arrays.asList(Component.literal("Set mirror position to middle of block"), Component.literal("for odd numbered builds")));
////                textMirrorPosX.setNumber(BaseMth.floor(textMirrorPosX.getNumber()));
////                textMirrorPosY.setNumber(BaseMth.floor(textMirrorPosY.getNumber()));
////                textMirrorPosZ.setNumber(BaseMth.floor(textMirrorPosZ.getNumber()));
////            }
////        });
////        buttonToggleOdd.setTooltip(Arrays.asList(Component.literal("Set mirror position to middle of block"), Component.literal("for odd numbered builds")));
////        mirrorIconButtonList.add(buttonToggleOdd);
////
////        buttonDrawLines = new IconButton(left + Dimen.SECTION_OFFSET_X1 + 24 * 2, y, 0, 40, BUILDING_ICONS, button -> {
////            drawLines = !drawLines;
////            buttonDrawLines.setUseAlternateIcon(drawLines);
////            buttonDrawLines.setTooltip(Component.literal(drawLines ? "Hide lines" : "Show lines"));
////        });
////        buttonDrawLines.setTooltip(Component.literal("Show lines"));
////        mirrorIconButtonList.add(buttonDrawLines);
////
////        buttonDrawPlanes = new IconButton(left + Dimen.SECTION_OFFSET_X1 + 24 * 3, y, 0, 60, BUILDING_ICONS, button -> {
////            drawPlanes = !drawPlanes;
////            buttonDrawPlanes.setUseAlternateIcon(drawPlanes);
////            buttonDrawPlanes.setTooltip(Component.literal(drawPlanes ? "Hide area" : "Show area"));
////        });
////        buttonDrawPlanes.setTooltip(Component.literal("Show area"));
////        mirrorIconButtonList.add(buttonDrawPlanes);
////
////        var transformerSettings = EffortlessBuilder.getInstance().getTransformerSettings(mc.player);
////        if (transformerSettings != null) {
////            var mirrorSettings = transformerSettings.mirrorSettings();
////            buttonMirrorEnabled.setIsChecked(mirrorSettings.enabled());
////            textMirrorPosX.setNumber(mirrorSettings.position().x);
////            textMirrorPosY.setNumber(mirrorSettings.position().y);
////            textMirrorPosZ.setNumber(mirrorSettings.position().z);
////            buttonMirrorX.setIsChecked(mirrorSettings.mirrorX());
////            buttonMirrorY.setIsChecked(mirrorSettings.mirrorY());
////            buttonMirrorZ.setIsChecked(mirrorSettings.mirrorZ());
////            textMirrorRadius.setNumber(mirrorSettings.radius());
////            drawLines = mirrorSettings.drawLines();
////            drawPlanes = mirrorSettings.drawPlanes();
////            buttonDrawLines.setUseAlternateIcon(drawLines);
////            buttonDrawPlanes.setUseAlternateIcon(drawPlanes);
////            buttonDrawLines.setTooltip(Component.literal(drawLines ? "Hide lines" : "Show lines"));
////            buttonDrawPlanes.setTooltip(Component.literal(drawPlanes ? "Hide area" : "Show area"));
////            if (textMirrorPosX.getNumber() == BaseMth.floor(textMirrorPosX.getNumber())) {
////                toggleOdd = false;
////                buttonToggleOdd.setTooltip(Arrays.asList(Component.literal("Set mirror position to middle of block"), Component.literal("for odd numbered builds")));
////            } else {
////                toggleOdd = true;
////                buttonToggleOdd.setTooltip(Arrays.asList(Component.literal("Set mirror position to corner of block"), Component.literal("for even numbered builds")));
////            }
////            buttonToggleOdd.setUseAlternateIcon(toggleOdd);
////        }
////
////        renderables.addAll(mirrorButtonList);
////        renderables.addAll(mirrorIconButtonList);
////
////        setCollapsed(!buttonMirrorEnabled.isChecked());
////    }
//
//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//        mirrorNumberFieldList.forEach(NumberField::tick);
//    }
//
//    @Override
//    public void drawEntry(Renderer renderer, int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
//                          boolean isSelected, float partialTicks) {
//
//        int offset = 8;
//
//        buttonMirrorEnabled.render(renderer, mouseX, mouseY, partialTicks);
//        if (buttonMirrorEnabled.isChecked()) {
//            buttonMirrorEnabled.setY(y);
//            font.draw(renderer, "Mirror enabled", left + offset, y + 2, 0xFFFFFF);
//
//            var positionOffsetX0 = left + Dimen.SECTION_OFFSET_X0;
//            var positionOffsetX1 = left + Dimen.SECTION_OFFSET_X1;
//            var positionOffsetY0 = y + 24;
//            var positionOffsetY1 = y + 24 * 2;
//            var positionOffsetY2 = y + 24 * 3;
//
//            var textOffsetX = 40;
//            var componentOffsetY = -5;
//
//            font.draw(renderer, "Position", positionOffsetX0, positionOffsetY0, 0xFFFFFF);
//            font.draw(renderer, "X", positionOffsetX0 + textOffsetX, positionOffsetY0, 0xFFFFFF);
//            font.draw(renderer, "Y", positionOffsetX0 + textOffsetX, positionOffsetY1, 0xFFFFFF);
//            font.draw(renderer, "Z", positionOffsetX0 + textOffsetX, positionOffsetY2, 0xFFFFFF);
//            textMirrorPosX.setY(positionOffsetY0 + componentOffsetY);
//            textMirrorPosY.setY(positionOffsetY1 + componentOffsetY);
//            textMirrorPosZ.setY(positionOffsetY2 + componentOffsetY);
//
//
//            font.draw(renderer, "Radius", positionOffsetX1, positionOffsetY0, 0xFFFFFF);
//            textMirrorRadius.setY(positionOffsetY0 + componentOffsetY);
//
//
//            font.draw(renderer, "Axis", positionOffsetX1, positionOffsetY1, 0xFFFFFF);
//            buttonMirrorX.setY(positionOffsetY1 - 2);
//            buttonMirrorY.setY(positionOffsetY1 - 2);
//            buttonMirrorZ.setY(positionOffsetY1 - 2);
//
//            buttonCurrentPosition.setY(positionOffsetY2 - 6);
//            buttonToggleOdd.setY(positionOffsetY2 - 6);
//            buttonDrawLines.setY(positionOffsetY2 - 6);
//            buttonDrawPlanes.setY(positionOffsetY2 - 6);
//
//            mirrorButtonList.forEach(button -> button.render(renderer, mouseX, mouseY, partialTicks));
//            mirrorIconButtonList.forEach(button -> button.render(renderer, mouseX, mouseY, partialTicks));
//            mirrorNumberFieldList.forEach(numberField -> numberField.render(renderer, mouseX, mouseY, partialTicks));
//        } else {
//            buttonMirrorEnabled.setY(y);
//            font.draw(renderer, "Mirror disabled", left + offset, y + 2, 0x999999);
//        }
//
//    }
//
//    public void drawTooltip(Renderer renderer, Screen guiScreen, int mouseX, int mouseY) {
//        // Draw tooltips last
//        if (buttonMirrorEnabled.isChecked()) {
//            mirrorIconButtonList.forEach(iconButton -> iconButton.drawTooltip(renderer, scrollPane.parent, mouseX, mouseY));
//            mirrorNumberFieldList.forEach(numberField -> numberField.drawTooltip(renderer, scrollPane.parent, mouseX, mouseY));
//        }
//    }
//
//    @Override
//    public boolean charTyped(char typedChar, int keyCode) {
//        super.charTyped(typedChar, keyCode);
//        for (NumberField numberField : mirrorNumberFieldList) {
//            numberField.charTyped(typedChar, keyCode);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
//        mirrorNumberFieldList.forEach(numberField -> numberField.mouseClicked(mouseX, mouseY, mouseEvent));
//
//        boolean insideMirrorEnabledLabel = mouseX >= left && mouseX < right && relativeY >= -2 && relativeY < 12;
//
//        if (insideMirrorEnabledLabel) {
//            buttonMirrorEnabled.playDownSound(this.minecraft.getSoundManager());
//            buttonMirrorEnabled.onClick(mouseX, mouseY);
//        }
//
//        return true;
//    }
//
//    @Override
//    protected String getName() {
//        return "Mirror";
//    }
//
//    @Override
//    protected int getExpandedHeight() {
//        return 96;
//    }
}
