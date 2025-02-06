package xaero.pac.client.controls.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public interface IKeyBindingHelper {

	public void register(KeyMapping keyBinding);

	public InputConstants.Key getBoundKey(KeyMapping keyBinding);

}
