package dev.ftb.mods.ftbteams.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.ftb.mods.ftblibrary.ui.CustomClickEvent;
import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.TeamMessage;
import dev.ftb.mods.ftbteams.api.client.KnownClientPlayer;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyCollection;
import dev.ftb.mods.ftbteams.client.gui.MyTeamScreen;
import dev.ftb.mods.ftbteams.data.ClientTeamManagerImpl;
import dev.ftb.mods.ftbteams.data.PlayerPermissions;
import dev.ftb.mods.ftbteams.net.OpenGUIMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class FTBTeamsClient {
	public static final ResourceLocation OPEN_GUI_ID = FTBTeamsAPI.rl("open_gui");

	public static KeyMapping openTeamsKey;

	public static void init() {
		registerKeys();

		CustomClickEvent.EVENT.register(event -> {
			if (event.id().equals(OPEN_GUI_ID)) {
				OpenGUIMessage.sendToServer();
				return EventResult.interruptTrue();
			}
			return EventResult.pass();
		});

		ClientRawInputEvent.KEY_PRESSED.register(FTBTeamsClient::keyPressed);
	}

	private static void registerKeys() {
		openTeamsKey = new KeyMapping("key.ftbteams.open_gui", InputConstants.Type.KEYSYM, -1, "key.categories.ftbteams");
		KeyMappingRegistry.register(openTeamsKey);
	}

	private static EventResult keyPressed(Minecraft client, int keyCode, int scanCode, int action, int modifiers) {
		if (openTeamsKey.isDown()) {
			OpenGUIMessage.sendToServer();
			return EventResult.interruptTrue();
		}
		return EventResult.pass();
	}

	public static void openMyTeamGui(TeamPropertyCollection properties, PlayerPermissions permissions) {
		new MyTeamScreen(properties, permissions).openGui();
	}

	public static void updateSettings(UUID id, TeamPropertyCollection properties) {
		if (ClientTeamManagerImpl.getInstance() == null) {
			return;
		}

		ClientTeamManagerImpl.getInstance().getTeam(id)
				.ifPresent(team -> team.updateProperties(properties));
	}

	public static void sendMessage(UUID from, Component text) {
		if (ClientTeamManagerImpl.getInstance() == null) {
			return;
		}

		TeamMessage msg = FTBTeamsAPI.api().createMessage(from, text);
		ClientTeamManagerImpl.getInstance().selfTeam().addMessage(msg);

		MyTeamScreen screen = ClientUtils.getCurrentGuiAs(MyTeamScreen.class);
		if (screen != null) {
			screen.refreshChat();
		}
	}

	public static void updatePresence(KnownClientPlayer update) {
		if (ClientTeamManagerImpl.getInstance() != null) {
			ClientTeamManagerImpl.getInstance().updatePresence(update);
		}
	}
}
