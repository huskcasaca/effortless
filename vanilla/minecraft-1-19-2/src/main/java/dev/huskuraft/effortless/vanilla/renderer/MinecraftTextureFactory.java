package dev.huskuraft.effortless.vanilla.renderer;

import dev.huskuraft.effortless.api.texture.SimpleTextureSprite;
import dev.huskuraft.effortless.api.texture.SpriteScaling;
import dev.huskuraft.effortless.api.texture.TextureFactory;
import dev.huskuraft.effortless.api.texture.TextureSprite;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftConvertor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public class MinecraftTextureFactory implements TextureFactory {

    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

    @Override
    public TextureSprite getBgTexture() {
        return null;
    }

    @Override
    public TextureSprite getButtonTexture(boolean enabled, boolean focused) {
        return createSpriteFromTexture(
                null,
                WIDGETS_LOCATION,
                200,
                20,
                0,
                46 + (enabled ? focused ? 2 : 1 : 0) * 20,
                256,
                256,
                new SpriteScaling.NineSlice(200, 20, enabled ? 3 : 1)
        );
    }

    public TextureSprite createSpriteFromTexture(ResourceLocation name, ResourceLocation texture, int width, int height, int x, int y, int textureWidth, int textureHeight, SpriteScaling scaling) {
        return new SimpleTextureSprite(
                MinecraftConvertor.fromPlatformResourceLocation(name),
                MinecraftConvertor.fromPlatformResourceLocation(texture),
                width,
                height,
                x,
                y,
                textureWidth,
                textureHeight,
                scaling);
    }

    public TextureSprite createSprite(TextureAtlasSprite sprite, SpriteScaling scaling) {
        return new SimpleTextureSprite(
                MinecraftConvertor.fromPlatformResourceLocation(sprite.getName()),
                MinecraftConvertor.fromPlatformResourceLocation(sprite.atlas().location()),
                sprite.getWidth(),
                sprite.getHeight(),
                sprite.getX(),
                sprite.getY(),
                sprite.getU0(),
                sprite.getU1(),
                sprite.getV0(),
                sprite.getV1(),
                scaling
        );
    }

}
