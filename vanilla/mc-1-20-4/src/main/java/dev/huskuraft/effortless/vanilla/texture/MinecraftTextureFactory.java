package dev.huskuraft.effortless.vanilla.texture;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.texture.SimpleTexture;
import dev.huskuraft.effortless.api.texture.SimpleTextureSprite;
import dev.huskuraft.effortless.api.texture.SpriteScaling;
import dev.huskuraft.effortless.api.texture.Texture;
import dev.huskuraft.effortless.api.texture.TextureFactory;
import dev.huskuraft.effortless.api.texture.TextureSprite;
import dev.huskuraft.effortless.vanilla.platform.MinecraftResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
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
        return createSprite(AbstractButton.SPRITES.get(enabled, focused));
    }

    public TextureSprite createSprite(ResourceLocation name) {
        return createSprite(Minecraft.getInstance().getGuiSprites().getSprite(name));
    }

    public TextureSprite createSprite(TextureAtlasSprite sprite) {
        return new SimpleTextureSprite(
                new MinecraftResourceLocation(sprite.atlasLocation()),
                new MinecraftResourceLocation(sprite.contents().name()),
                sprite.contents().width(),
                sprite.contents().height(),
                sprite.getX(),
                sprite.getY(),
                sprite.getU0(),
                sprite.getU1(),
                sprite.getV0(),
                sprite.getV1(),
                getSpriteScaling(sprite)
        );
    }

    public SpriteScaling getSpriteScaling(TextureAtlasSprite sprite) {
        var scaling = Minecraft.getInstance().getGuiSprites().getSpriteScaling(sprite);
        if (scaling instanceof GuiSpriteScaling.Stretch stretch) {
            return new SpriteScaling.Stretch();
        }
        if (scaling instanceof GuiSpriteScaling.Tile tile) {
            return new SpriteScaling.Tile(tile.width(), tile.height());
        }
        if (scaling instanceof GuiSpriteScaling.NineSlice nineSlice) {
            return new SpriteScaling.NineSlice(nineSlice.width(), nineSlice.height(), nineSlice.border().top(), nineSlice.border().right(), nineSlice.border().bottom(), nineSlice.border().left());
        }
        return new SpriteScaling.Stretch();
    }

}
