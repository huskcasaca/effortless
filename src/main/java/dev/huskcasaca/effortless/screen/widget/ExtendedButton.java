package dev.huskcasaca.effortless.screen.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.huskcasaca.effortless.screen.ScreenUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import javax.annotation.ParametersAreNonnullByDefault;

@Environment(EnvType.CLIENT)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ExtendedButton extends Button {
    public ExtendedButton(int posX, int posY, int width, int height, Component displayString, OnPress handler) {
        super(posX, posY, width, height, displayString, handler);
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        int k = this.getYImage(this.isHovered);
        ScreenUtils.blitWithBorder(poseStack, WIDGETS_LOCATION, this.x, this.y, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.getBlitOffset());
        this.renderBg(poseStack, mc, mouseX, mouseY);

        Component buttonText = this.getMessage();
        int strWidth = mc.font.width(buttonText);
        int ellipsisWidth = mc.font.width("...");

        if (strWidth > width - 6 && strWidth > ellipsisWidth)
            //TODO, srg names make it hard to figure out how to append to an ITextProperties from this trim operation, wraping this in StringTextComponent is kinda dirty.
            buttonText = Component.literal(mc.font.substrByWidth(buttonText, width - 6 - ellipsisWidth).getString() + "...");

        drawCenteredString(poseStack, mc.font, buttonText, this.x + this.width / 2, this.y + (this.height - 8) / 2, 14737632);
    }
}
