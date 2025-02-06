package xaero.pac.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.pac.client.gui.widget.dropdown.DropDownWidget;
import xaero.pac.client.gui.widget.dropdown.IDropDownContainer;

public class XPACScreen extends Screen implements IDropDownContainer {

	private static final Component XPAC_TITLE = Component.translatable("gui.xaero_pac_ui_parties_and_claims");
	protected final Screen escape;
	protected final Screen parent;
	protected DropDownWidget openDropdown;

	protected XPACScreen(Screen escape, Screen parent, Component p_96550_) {
		super(p_96550_);
		this.escape = escape;
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();
		openDropdown = null;
	}

	@Override
	public void onClose() {
		minecraft.setScreen(escape);
	}

	public void goBack() {
		minecraft.setScreen(parent);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(openDropdown != null){
			if(!openDropdown.onDropDown((int) mouseX, (int) mouseY, height)) {
				openDropdown.setClosed(true);
				openDropdown = null;
			} else
				openDropdown.mouseClicked(mouseX, mouseY, button);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double hWheel, double wheel) {
		if(openDropdown != null) {
			if(openDropdown.onDropDown((int) mouseX, (int) mouseY, height))
				return openDropdown.mouseScrolled(mouseX, mouseY, hWheel, wheel);
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, hWheel, wheel);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if(openDropdown != null)
			if(openDropdown.mouseReleased(mouseX, mouseY, button))
				return true;
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
		super.render(guiGraphics, mouseX, mouseY, partial);
		guiGraphics.drawCenteredString(font, XPAC_TITLE, width / 2, 5, -1);
		renderPreDropdown(guiGraphics, mouseX, mouseY, partial);
		if(openDropdown != null) {
			PoseStack poseStack = guiGraphics.pose();
			poseStack.pushPose();
			poseStack.translate(0, 0, 2);
			openDropdown.render(guiGraphics, mouseX, mouseY, height, false);
			poseStack.popPose();
		}
	}

	protected void renderPreDropdown(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial){
	}

	@Override
	public void onDropdownOpen(DropDownWidget menu) {
		if(this.openDropdown != null && this.openDropdown != menu)
			openDropdown.setClosed(true);
		this.openDropdown = menu;
	}

	@Override
	public void onDropdownClosed(DropDownWidget menu) {
		if(menu != this.openDropdown && this.openDropdown != null)
			this.openDropdown.setClosed(true);
		this.openDropdown = null;
	}

}
