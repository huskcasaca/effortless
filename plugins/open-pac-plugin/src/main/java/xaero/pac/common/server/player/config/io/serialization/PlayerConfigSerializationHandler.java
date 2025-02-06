package xaero.pac.common.server.player.config.io.serialization;

import xaero.pac.common.server.claims.IServerClaimsManager;
import xaero.pac.common.server.io.serialization.SerializationHandler;
import xaero.pac.common.server.parties.party.IServerParty;
import xaero.pac.common.server.player.config.PlayerConfig;
import xaero.pac.common.server.player.config.PlayerConfigManager;
import xaero.pac.common.server.player.config.api.PlayerConfigType;

public final class PlayerConfigSerializationHandler<
	P extends IServerParty<?, ?, ?>,
	CM extends IServerClaimsManager<?, ?, ?>//needed in this class because of some weird compilation error when gradle building (not displayed by the IDE)
> extends SerializationHandler<String, PlayerConfigDeserializationInfo, PlayerConfig<P>, PlayerConfigManager<P, CM>> {

	private final PlayerConfigSerializer serializer;

	private PlayerConfigSerializationHandler(PlayerConfigSerializer serializer) {
		this.serializer = serializer;
	}

	@Override
	public String serialize(PlayerConfig<P> object) {
		return serializer.serialize(object);
	}

	@Override
	public PlayerConfig<P> deserialize(PlayerConfigDeserializationInfo info, PlayerConfigManager<P, CM> manager, String serializedData) {
		PlayerConfig<P> config = info.getSubId() != null || info.getType() == PlayerConfigType.PLAYER ? manager.getConfig(info.getId()) : PlayerConfig.FinalBuilder.<P>begin().setType(info.getType()).setPlayerId(info.getId()).setManager(manager).build();
		PlayerConfig<P> targetConfig = config;
		if(info.getSubId() != null)
			targetConfig = config.createSubConfig(info.getSubId(), info.getSubIndex());
		if(targetConfig != null)
			serializer.deserializeInto(targetConfig, serializedData);
		return targetConfig;
	}

	public static final class Builder<
		P extends IServerParty<?, ?, ?>,
		CM extends IServerClaimsManager<?, ?, ?>//needed in this class because of some weird compilation error when gradle building (not displayed by the IDE)
	>  {

		private Builder() {
		}

		private Builder<P, CM> setDefault() {
			return this;
		}

		public PlayerConfigSerializationHandler<P, CM> build() {
			return new PlayerConfigSerializationHandler<>(new PlayerConfigSerializer());
		}

		public static
		<
			P extends IServerParty<?, ?, ?>,
			CM extends IServerClaimsManager<?, ?, ?>//needed in this class because of some weird compilation error when gradle building (not displayed by the IDE)
		> Builder<P, CM> begin() {
			return new Builder<P, CM>().setDefault();
		}

	}

}
