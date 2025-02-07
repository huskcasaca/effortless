package dev.ftb.mods.ftbteams.net;

import dev.ftb.mods.ftblibrary.util.NetworkHelper;

public class FTBTeamsNet {
	public static void register() {
		NetworkHelper.registerS2C(SyncTeamsMessage.TYPE, SyncTeamsMessage.STREAM_CODEC, SyncTeamsMessage::handle);
		NetworkHelper.registerS2C(SyncMessageHistoryMessage.TYPE, SyncMessageHistoryMessage.STREAM_CODEC, SyncMessageHistoryMessage::handle);
		NetworkHelper.registerS2C(OpenMyTeamGUIMessage.TYPE, OpenMyTeamGUIMessage.STREAM_CODEC, OpenMyTeamGUIMessage::handle);
		NetworkHelper.registerS2C(UpdatePropertiesResponseMessage.TYPE, UpdatePropertiesResponseMessage.STREAM_CODEC, UpdatePropertiesResponseMessage::handle);
		NetworkHelper.registerS2C(SendMessageResponseMessage.TYPE, SendMessageResponseMessage.STREAM_CODEC, SendMessageResponseMessage::handle);
		NetworkHelper.registerS2C(UpdatePresenceMessage.TYPE, UpdatePresenceMessage.STREAM_CODEC, UpdatePresenceMessage::handle);

		NetworkHelper.registerC2S(OpenGUIMessage.TYPE, OpenGUIMessage.STREAM_CODEC, OpenGUIMessage::handle);
		NetworkHelper.registerC2S(UpdatePropertiesRequestMessage.TYPE, UpdatePropertiesRequestMessage.STREAM_CODEC, UpdatePropertiesRequestMessage::handle);
		NetworkHelper.registerC2S(SendMessageMessage.TYPE, SendMessageMessage.STREAM_CODEC, SendMessageMessage::handle);
		NetworkHelper.registerC2S(CreatePartyMessage.TYPE, CreatePartyMessage.STREAM_CODEC, CreatePartyMessage::handle);
		NetworkHelper.registerC2S(PlayerGUIOperationMessage.TYPE, PlayerGUIOperationMessage.STREAM_CODEC, PlayerGUIOperationMessage::handle);
	}
}
