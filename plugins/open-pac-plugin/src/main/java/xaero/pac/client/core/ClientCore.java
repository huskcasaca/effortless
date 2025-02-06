package xaero.pac.client.core;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.client.world.IClientWorldData;
import xaero.pac.client.world.capability.ClientWorldMainCapability;
import xaero.pac.client.world.capability.api.ClientWorldCapabilityTypes;

public class ClientCore {

	public static void onInitializeWorldBorder(ClientboundInitializeBorderPacket packet){
		ClientWorldMainCapability capability = (ClientWorldMainCapability) OpenPartiesAndClaims.INSTANCE.getCapabilityHelper().getCapability(Minecraft.getInstance().level, ClientWorldCapabilityTypes.MAIN_CAP);
		IClientWorldData worldData = capability.getClientWorldDataInternal();
		boolean serverHasMod = worldData.serverHasMod();
		if(!serverHasMod) {
			//the border packet is sent after the handshake, so if we didn't get a handshake up until this point, then there is no mod on the server side
			OpenPartiesAndClaims.LOGGER.info("No Open Parties and Claims on the server! Resetting.");
			OpenPartiesAndClaims.INSTANCE.getClientDataInternal().reset();
		}
	}

}
