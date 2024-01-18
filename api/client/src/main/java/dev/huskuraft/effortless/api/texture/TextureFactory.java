package dev.huskuraft.effortless.api.texture;

import dev.huskuraft.effortless.api.platform.ClientPlatform;

public interface TextureFactory {

    TextureSprite getBgTexture();

    TextureSprite getButtonTexture(boolean enabled, boolean focused);

    TextureFactory INSTANCE = ClientPlatform.INSTANCE.getTextureFactory();

}
