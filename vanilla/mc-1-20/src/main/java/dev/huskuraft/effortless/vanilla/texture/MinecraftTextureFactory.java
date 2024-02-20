package dev.huskuraft.effortless.vanilla.texture;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.texture.SimpleTexture;
import dev.huskuraft.effortless.api.texture.SimpleTextureSprite;
import dev.huskuraft.effortless.api.texture.SpriteScaling;
import dev.huskuraft.effortless.api.texture.Texture;
import dev.huskuraft.effortless.api.texture.TextureFactory;
import dev.huskuraft.effortless.api.texture.TextureSprite;
import dev.huskuraft.effortless.vanilla.platform.MinecraftResourceLocation;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

@AutoService(TextureFactory.class)
public class MinecraftTextureFactory implements TextureFactory {

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
        return new SimpleTextureSprite(new MinecraftResourceLocation(texture), new MinecraftResourceLocation(name), width, height, x, y, textureWidth, textureHeight, scaling);
    }

}
