package xaero.pac.client.gui.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import xaero.pac.client.gui.TextWidgetListElement;

public class TextWidgetEditBox extends EditBox {

	private TextWidgetListElement element;

	public TextWidgetEditBox(TextWidgetListElement element, Font p_94114_, int p_94115_, int p_94116_, int p_94117_, int p_94118_,
			Component p_94119_) {
		super(p_94114_, p_94115_, p_94116_, p_94117_, p_94118_, p_94119_);
		this.element = element;
	}

	@Override
	public boolean keyPressed(int p_94132_, int p_94133_, int p_94134_) {
		if((p_94132_ == GLFW.GLFW_KEY_ENTER || p_94132_ == GLFW.GLFW_KEY_KP_ENTER) && element.onEnterPressed())
			return true;
		return super.keyPressed(p_94132_, p_94133_, p_94134_);
	}

}
