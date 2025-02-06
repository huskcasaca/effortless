package xaero.pac.common.server.player.localization;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import xaero.pac.common.server.player.data.ServerPlayerData;
import xaero.pac.common.server.player.localization.api.IAdaptiveLocalizerAPI;

import javax.annotation.Nonnull;
import java.util.Map;

public class AdaptiveLocalizer implements IAdaptiveLocalizerAPI {

	private final Map<String, String> defaultTranslations;

	public AdaptiveLocalizer(Map<String, String> defaultTranslations) {
		this.defaultTranslations = defaultTranslations;
	}

	@Override
	@Nonnull
	public MutableComponent getFor(@Nonnull ServerPlayer player, @Nonnull String key, @Nonnull Object... args){
		ServerPlayerData playerDataAPI = (ServerPlayerData) ServerPlayerData.from(player);
		if(playerDataAPI.hasMod())
			return Component.translatable(key, args);
		return getServerLocalizedComponent(key, args);
	}

	@Override
	@Nonnull
	public Component getFor(@Nonnull ServerPlayer player, @Nonnull Component component){
		if(!(component.getContents() instanceof TranslatableContents translatableContents))
			return component;
		ServerPlayerData playerDataAPI = (ServerPlayerData) ServerPlayerData.from(player);
		if(playerDataAPI.hasMod())
			return component;
		String key = translatableContents.getKey();
		Object[] args = translatableContents.getArgs();
		Component result = getServerLocalizedComponent(key, args).setStyle(component.getStyle());
		if(component.getSiblings() != null)
			result.getSiblings().addAll(component.getSiblings());
		return result;
	}

	private MutableComponent getServerLocalizedComponent(String key, Object... args){
		for(int i = 0; i < args.length; i++)
			if(args[i] instanceof Component component && component.getContents() instanceof TranslatableContents translatableContents)
				args[i] = getServerLocalizedComponent(translatableContents.getKey(), translatableContents.getArgs());
		return Component.translatable(defaultTranslations.getOrDefault(key, key), args);
	}

	public String getDefaultTranslation(String key){
		return defaultTranslations.getOrDefault(key, key);
	}

}
