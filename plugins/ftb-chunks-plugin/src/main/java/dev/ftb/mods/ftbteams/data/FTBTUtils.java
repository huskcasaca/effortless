package dev.ftb.mods.ftbteams.data;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.util.UndashedUuid;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.math.MathUtils;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FTBTUtils {
	public static final GameProfile NO_PROFILE = new GameProfile(new UUID(0L, 0L), "-");

	@Nullable
	public static ServerPlayer getPlayerByUUID(MinecraftServer server, @Nullable UUID id) {
		return id == null || id == Util.NIL_UUID ? null : server.getPlayerList().getPlayer(id);
	}

	public static GameProfile normalize(@Nullable GameProfile profile) {
		if (profile == null || profile.getId() == null || profile.getName() == null || profile.equals(NO_PROFILE)) {
			return NO_PROFILE;
		}

		if (!profile.getProperties().isEmpty()) {
			return new GameProfile(profile.getId(), profile.getName());
		}

		return profile;
	}

	public static Color4I randomColor() {
		return Color4I.hsb(MathUtils.RAND.nextFloat(), 0.65F, 1F);
	}

	public static boolean canPlayerUseCommand(ServerPlayer player, String command) {
		List<String> parts = Arrays.asList(command.split("\\."));
		CommandNode<CommandSourceStack> node = player.getServer().getCommands().getDispatcher().findNode(parts);
		return node != null && node.canUse(player.createCommandSourceStack());
	}

	public static String getDefaultPartyName(MinecraftServer server, UUID playerId, @Nullable ServerPlayer player) {
		String playerName;
		if (player != null) {
			playerName = player.getGameProfile().getName();
		} else {
			GameProfileCache profileCache = server.getProfileCache();
			if (profileCache == null) {
				playerName = playerId.toString();
			} else {
				playerName = profileCache.get(playerId).orElse(new GameProfile(playerId, playerId.toString())).getName();
			}
		}
		return playerName + "'s Party";
	}

	static MutableComponent makeCopyableComponent(String id) {
		return Component.literal(id)
				.withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.copy.click"))))
				.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, id)));
	}
}
