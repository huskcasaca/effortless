package dev.huskuraft.effortless.api.texture;

import dev.huskuraft.effortless.api.platform.ClientContentFactory;

public interface TextureFactory {

    Texture getBlockAtlasTexture();

    TextureSprite getBackgroundTextureSprite();

    TextureSprite getButtonTextureSprite(boolean enabled, boolean focused);

    TextureFactory INSTANCE = ClientContentFactory.getInstance().getTextureFactory();

}
