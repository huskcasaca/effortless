package dev.ftb.mods.ftbchunks.api.client.minimap;

import net.minecraft.resources.ResourceLocation;

/**
 * An entry point for developers to create custom minimap info components.
 */
public interface MinimapInfoComponent {
    /**
     * The ID of this component.
     */
    ResourceLocation id();
}
