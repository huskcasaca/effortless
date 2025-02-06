package xaero.pac.common.server.player.localization;

import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.server.config.ServerConfig;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerTranslationLoader {

	public Map<String, String> loadFromResources(ResourceManager resourceManager){
		Map<String, String> result = new HashMap<>();
		try {
			Resource enUSLanguageFileResource = resourceManager.getResourceOrThrow(ResourceLocation.fromNamespaceAndPath(OpenPartiesAndClaims.MOD_ID, "lang/en_us.json"));
			try(BufferedInputStream inputStream = new BufferedInputStream(enUSLanguageFileResource.open())){
				Language.loadFromJson(inputStream, result::put);
			}
			String configuredLanguage = ServerConfig.CONFIG.defaultLanguage.get();
			if(!configuredLanguage.equalsIgnoreCase("en_us")) {
				Resource languageFileResource = resourceManager.getResourceOrThrow(ResourceLocation.fromNamespaceAndPath(OpenPartiesAndClaims.MOD_ID, "lang/" + configuredLanguage + ".json"));
				try (BufferedInputStream inputStream = new BufferedInputStream(languageFileResource.open())) {
					Language.loadFromJson(inputStream, result::put);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Error loading the default OPAC server localization!", e);
		}
		return result;
	}

}
