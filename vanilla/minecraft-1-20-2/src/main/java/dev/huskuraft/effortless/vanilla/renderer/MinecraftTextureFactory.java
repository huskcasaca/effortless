package dev.huskuraft.effortless.vanilla.renderer;

import com.google.gson.Gson;
import dev.huskuraft.effortless.api.texture.SimpleTextureSprite;
import dev.huskuraft.effortless.api.texture.SpriteScaling;
import dev.huskuraft.effortless.api.texture.TextureFactory;
import dev.huskuraft.effortless.api.texture.TextureSprite;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftConvertor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.resources.ResourceLocation;

import java.util.logging.Logger;

public class MinecraftTextureFactory implements TextureFactory {

    private static final WidgetSprites BUTTON_SPRITES = new WidgetSprites(new ResourceLocation("widget/button"), new ResourceLocation("widget/button_disabled"), new ResourceLocation("widget/button_highlighted"));

    @Override
    public TextureSprite getBgTexture() {
        return null;
    }

    @Override
    public TextureSprite getButtonTexture(boolean enabled, boolean focused) {
        return createSprite(BUTTON_SPRITES.get(enabled, focused));
    }

    public TextureSprite createSprite(ResourceLocation name) {
        return createSprite(Minecraft.getInstance().getGuiSprites().getSprite(name));
    }

    public TextureSprite createSprite(TextureAtlasSprite sprite) {
        return new SimpleTextureSprite(
                MinecraftConvertor.fromPlatformResource(sprite.contents().name()),
                MinecraftConvertor.fromPlatformResource(sprite.atlasLocation()),
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
