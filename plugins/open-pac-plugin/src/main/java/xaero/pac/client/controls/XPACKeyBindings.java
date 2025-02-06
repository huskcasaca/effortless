package xaero.pac.client.controls;


import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import xaero.pac.client.controls.api.OPACKeyBindingsAPI;
import xaero.pac.client.controls.keybinding.IKeyBindingHelper;
import xaero.pac.common.platform.Services;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class XPACKeyBindings implements OPACKeyBindingsAPI {

	private final List<KeyMapping> keyBindings;
	public final KeyMapping openModMenu;

	public XPACKeyBindings() {
		keyBindings = new ArrayList<>();
		keyBindings.add(openModMenu = new KeyMapping("gui.xaero_pac_key_open_menu", GLFW.GLFW_KEY_APOSTROPHE, "Open Parties and Claims"));
	}

	public void register() {
		IKeyBindingHelper registry = Services.PLATFORM.getKeyBindingHelper();
		keyBindings.forEach(registry::register);
	}

	@Nonnull
	@Override
	public KeyMapping getOpenModMenuKeyBinding() {
		return openModMenu;
	}

}
