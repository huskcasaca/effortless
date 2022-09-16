package dev.huskcasaca.effortless.mixin;

import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Screen.class)
public interface ScreenRenderablesAccessor {
    @Accessor
    List<Widget> getRenderables();
}