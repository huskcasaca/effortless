package dev.huskuraft.effortless.api.texture;

import dev.huskuraft.effortless.api.platform.PlatformServiceLoader;

public interface TextureFactory {

    Texture getBlockAtlasTexture();

    TextureSprite getBackgroundTextureSprite();

    TextureSprite getButtonTextureSprite(boolean enabled, boolean focused);

    TextureFactory INSTANCE = PlatformServiceLoader.load(TextureFactory.class).get();

    static TextureFactory getInstance() {
        return INSTANCE;
    }

}
