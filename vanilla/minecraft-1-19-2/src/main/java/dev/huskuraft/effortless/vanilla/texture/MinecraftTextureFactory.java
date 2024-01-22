package dev.huskuraft.effortless.vanilla.texture;

import dev.huskuraft.effortless.api.texture.*;
import dev.huskuraft.effortless.vanilla.core.MinecraftResourceLocation;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class MinecraftTextureFactory implements TextureFactory {

    public static final MinecraftTextureFactory INSTANCE = new MinecraftTextureFactory();

    @Override
    public Texture getBlockAtlasTexture() {
        return new SimpleTexture(new MinecraftResourceLocation(InventoryMenu.BLOCK_ATLAS));
    }

    @Override
    public TextureSprite getBackgroundTextureSprite() {
        return null;
    }

    @Override
    public TextureSprite getButtonTextureSprite(boolean enabled, boolean focused) {
        return createSpriteFromTexture(AbstractWidget.WIDGETS_LOCATION, null, 200, 20, 0, 46 + (enabled ? focused ? 2 : 1 : 0) * 20, 256, 256, new SpriteScaling.NineSlice(200, 20, enabled ? 3 : 1));
    }

    public TextureSprite createSpriteFromTexture(ResourceLocation texture, ResourceLocation name, int width, int height, int x, int y, int textureWidth, int textureHeight, SpriteScaling scaling) {
        return new SimpleTextureSprite(
                new MinecraftResourceLocation(texture),
                new MinecraftResourceLocation(name),
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
                new MinecraftResourceLocation(sprite.atlas().location()),
                new MinecraftResourceLocation(sprite.getName()),
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
