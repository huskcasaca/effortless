package dev.huskcasaca.effortless.mixin;

import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractSelectionList.class)
public interface ScreenRenderHeaderAccessor {
    @Accessor
    boolean isRenderHeader();
}