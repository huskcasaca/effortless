package xaero.pac.common.server.player.permission.impl;

import earth.terrarium.prometheus.api.roles.RoleApi;
import net.minecraft.server.level.ServerPlayer;
import xaero.pac.common.mods.prometheus.OPACOptions;
import xaero.pac.common.server.player.permission.api.IPermissionNodeAPI;
import xaero.pac.common.server.player.permission.api.IPlayerPermissionSystemAPI;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.OptionalInt;

public class PlayerPrometheusPermissions implements IPlayerPermissionSystemAPI {

	@Nonnull
	@Override
	public OptionalInt getIntPermission(@Nonnull ServerPlayer player, @Nonnull IPermissionNodeAPI<Integer> node) {
		OPACOptions options = RoleApi.API.getOption(player, OPACOptions.SERIALIZER);
		Integer value = options == null ? null : options.getValue(node);
		return value == null ? OptionalInt.empty() : OptionalInt.of(value);
	}

	@Override
	public boolean getPermission(@Nonnull ServerPlayer player, @Nonnull IPermissionNodeAPI<Boolean> node) {
		OPACOptions options = RoleApi.API.getOption(player, OPACOptions.SERIALIZER);
		Boolean value = options == null ? null : options.getValue(node);
		return value != null && value;
	}

	@Nonnull
	@Override
	public <T> Optional<T> getPermissionTyped(@Nonnull ServerPlayer player, @Nonnull IPermissionNodeAPI<T> node) {
		OPACOptions options = RoleApi.API.getOption(player, OPACOptions.SERIALIZER);
		T value = options == null ? null : options.getValue(node);
		return Optional.ofNullable(value);
	}

}
