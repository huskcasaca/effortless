package xaero.pac.client.command.util;

import net.minecraft.client.Minecraft;

public class CommandUtil {

	public static void sendCommand(Minecraft minecraft, String command){
		if(!minecraft.player.connection.sendUnsignedCommand(command))
			minecraft.player.connection.sendCommand(command);
	}

}
