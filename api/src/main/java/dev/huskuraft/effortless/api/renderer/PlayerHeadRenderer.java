package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.core.PlayerSkin;
import dev.huskuraft.effortless.api.core.ResourceLocation;

public interface PlayerHeadRenderer {

    int SKIN_HEAD_U = 8;
    int SKIN_HEAD_V = 8;
    int SKIN_HEAD_WIDTH = 8;
    int SKIN_HEAD_HEIGHT = 8;
    int SKIN_HAT_U = 40;
    int SKIN_HAT_V = 8;
    int SKIN_HAT_WIDTH = 8;
    int SKIN_HAT_HEIGHT = 8;
    int SKIN_TEX_WIDTH = 64;
    int SKIN_TEX_HEIGHT = 64;

    static void draw(Renderer renderer, PlayerSkin skin, int x, int y, int size) {
        draw(renderer, skin.texture(), x, y, size);
    }

    static void draw(Renderer renderer, ResourceLocation texture, int x, int y, int size) {
        draw(renderer, texture, x, y, size, true, false);
    }

    static void draw(Renderer renderer, ResourceLocation texture, int x, int y, int size, boolean drawHat, boolean upsideDown) {
        if (texture == null) return;
        renderer.renderTexture(texture, x, y, size, size, SKIN_HEAD_U, (float) (SKIN_HEAD_V + (upsideDown ? SKIN_HEAD_WIDTH : 0)), SKIN_HEAD_WIDTH, SKIN_HEAD_HEIGHT * (upsideDown ? -1 : 1), SKIN_TEX_WIDTH, SKIN_TEX_HEIGHT);
        if (drawHat) {
            drawHat(renderer, texture, x, y, size, upsideDown);
        }
    }

    static void drawHat(Renderer renderer, ResourceLocation texture, int x, int y, int size, boolean upsideDown) {
        if (texture == null) return;
        renderer.renderTexture(texture, x, y, size, size, SKIN_HAT_U, (float) (SKIN_HAT_V + (upsideDown ? SKIN_HAT_WIDTH : 0)), SKIN_HAT_WIDTH, SKIN_HAT_HEIGHT * (upsideDown ? -1 : 1), SKIN_TEX_WIDTH, SKIN_TEX_HEIGHT);
    }

}
