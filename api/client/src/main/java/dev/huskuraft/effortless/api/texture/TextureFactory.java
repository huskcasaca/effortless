package dev.huskuraft.effortless.api.texture;

import dev.huskuraft.effortless.api.platform.SafeServiceLoader;

public interface TextureFactory {

    Texture getBlockAtlasTexture();

    TextureSprite getBackgroundTextureSprite();

    TextureSprite getButtonTextureSprite(boolean enabled, boolean focused);

    TextureFactory INSTANCE = SafeServiceLoader.load(TextureFactory.class).getFirst();

    static TextureFactory getInstance() {
        return INSTANCE;
    }

}
