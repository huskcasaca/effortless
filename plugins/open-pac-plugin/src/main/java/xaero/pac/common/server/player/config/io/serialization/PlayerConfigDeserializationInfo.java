package xaero.pac.common.server.player.config.io.serialization;

import xaero.pac.common.server.player.config.api.PlayerConfigType;

import java.util.UUID;

public class PlayerConfigDeserializationInfo {

	private final UUID id;
	private final PlayerConfigType type;
	private final String subId;

	private final int subIndex;

	public PlayerConfigDeserializationInfo(UUID id, PlayerConfigType type, String subId, int subIndex) {
		super();
		this.id = id;
		this.type = type;
		this.subId = subId;
		this.subIndex = subIndex;
	}

	public UUID getId() {
		return id;
	}

	public PlayerConfigType getType() {
		return type;
	}

	public String getSubId() {
		return subId;
	}

	public int getSubIndex() {
		return subIndex;
	}

}
