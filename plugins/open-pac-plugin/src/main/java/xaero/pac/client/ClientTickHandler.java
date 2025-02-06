package xaero.pac.client;

import net.minecraft.client.Minecraft;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.client.gui.MainMenu;

public class ClientTickHandler {

	private boolean firstTickHandled;

	public void tick(IClientData<?,?,?> clientData) {
		if(!firstTickHandled){
			OpenPartiesAndClaims.INSTANCE.getClientEvents().fireAddonRegisterEvent();
			firstTickHandled = true;
		}
		if(clientData.getKeyBindings().openModMenu.consumeClick())
			Minecraft.getInstance().setScreen(new MainMenu(null, null));
	}

}
