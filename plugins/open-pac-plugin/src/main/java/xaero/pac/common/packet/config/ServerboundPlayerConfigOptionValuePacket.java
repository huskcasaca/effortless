package xaero.pac.common.packet.config;

import net.minecraft.server.level.ServerPlayer;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.claims.player.IPlayerChunkClaim;
import xaero.pac.common.claims.player.IPlayerClaimPosList;
import xaero.pac.common.claims.player.IPlayerDimensionClaims;
import xaero.pac.common.parties.party.IPartyPlayerInfo;
import xaero.pac.common.parties.party.ally.IPartyAlly;
import xaero.pac.common.parties.party.member.IPartyMember;
import xaero.pac.common.server.IServerData;
import xaero.pac.common.server.ServerData;
import xaero.pac.common.server.claims.IServerClaimsManager;
import xaero.pac.common.server.claims.IServerDimensionClaimsManager;
import xaero.pac.common.server.claims.IServerRegionClaims;
import xaero.pac.common.server.claims.player.IServerPlayerClaimInfo;
import xaero.pac.common.server.parties.party.IServerParty;
import xaero.pac.common.server.player.config.IPlayerConfig;
import xaero.pac.common.server.player.config.IPlayerConfigManager;
import xaero.pac.common.server.player.config.PlayerConfig;
import xaero.pac.common.server.player.config.api.IPlayerConfigAPI;
import xaero.pac.common.server.player.config.api.IPlayerConfigOptionSpecAPI;
import xaero.pac.common.server.player.config.api.PlayerConfigType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ServerboundPlayerConfigOptionValuePacket extends PlayerConfigOptionValuePacket {

	public ServerboundPlayerConfigOptionValuePacket(PlayerConfigType type, String subId, UUID owner, List<Entry> entries) {
		super(type, subId, owner, entries);
	}

	public static class Codec extends PlayerConfigOptionValuePacket.Codec<ServerboundPlayerConfigOptionValuePacket> {

		@Override
		protected int getSizeLimit() {
			return 262144;
		}

		@Override
		protected ServerboundPlayerConfigOptionValuePacket create(PlayerConfigType type, String subId, UUID owner, List<Entry> entries) {
			return new ServerboundPlayerConfigOptionValuePacket(type, subId, owner, entries);
		}

	}

	public static class ServerHandler implements BiConsumer<ServerboundPlayerConfigOptionValuePacket, ServerPlayer> {

		@SuppressWarnings("unchecked")
		private <T extends Comparable<T>> IPlayerConfigAPI.SetResult setConfigUnchecked(IPlayerConfig config, IPlayerConfigOptionSpecAPI<T> option, Object value) {
			return config.tryToSet(option, (T) value);
		}

		@Override
		public void accept(ServerboundPlayerConfigOptionValuePacket t, ServerPlayer serverPlayer) {
			if(t.entries.size() > 1) {
				OpenPartiesAndClaims.LOGGER.info("A player is attempting to modify multiple options in a single packet! Name: " + serverPlayer.getGameProfile().getName());
				return;
			}
			boolean isOP = serverPlayer.hasPermissions(2);
			Entry optionEntry = t.entries.get(0);
			UUID ownerId = t.getType() != PlayerConfigType.PLAYER ? null : t.owner == null ? serverPlayer.getUUID() : t.owner;
			if(!isOP) {
				if(t.getType() != PlayerConfigType.PLAYER) {
					OpenPartiesAndClaims.LOGGER.info("Non-op player is attempting to modify a config without required permissions! Name: " + serverPlayer.getGameProfile().getName());
					return;
				}
				if(PlayerConfig.isOptionOPConfigurable(optionEntry.getId())) {
					OpenPartiesAndClaims.LOGGER.info("Non-op player is attempting to modify a op-only option! Name: " + serverPlayer.getGameProfile().getName());
					return;
				}
				if(!Objects.equals(ownerId, serverPlayer.getUUID())) {
					OpenPartiesAndClaims.LOGGER.info("Non-op player is attempting to modify another player's config! Name: " + serverPlayer.getGameProfile().getName());
					return;
				}
			}
			IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> serverData = ServerData.from(serverPlayer.getServer());
			IPlayerConfigManager playerConfigs = serverData.getPlayerConfigs();
			IPlayerConfig config =
					t.getType() == PlayerConfigType.PLAYER ?
							playerConfigs.getLoadedConfig(ownerId) :
							t.getType() == PlayerConfigType.SERVER ?
									playerConfigs.getServerClaimConfig() :
									t.getType() == PlayerConfigType.EXPIRED ?
											playerConfigs.getExpiredClaimConfig() :
											t.getType() == PlayerConfigType.WILDERNESS ?
													playerConfigs.getWildernessConfig() :
													playerConfigs.getDefaultConfig();
			if(t.subId != null)
				config = config.getSubConfig(t.subId);
			if(config != null) {
				IPlayerConfigOptionSpecAPI<?> option = playerConfigs.getOptionForId(optionEntry.getId());
				if(option != null) {
					IPlayerConfigAPI.SetResult result = setConfigUnchecked(config, option, optionEntry.getValue());
					if (result != IPlayerConfigAPI.SetResult.SUCCESS && (config.getType() != PlayerConfigType.PLAYER || serverPlayer.getUUID().equals(config.getPlayerId())))
						playerConfigs.getSynchronizer().syncOptionToClient(serverPlayer, config, option);//restore the correct value
				}
			}
		}
	}

}
