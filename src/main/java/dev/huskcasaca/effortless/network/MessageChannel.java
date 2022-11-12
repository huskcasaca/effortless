package dev.huskcasaca.effortless.network;


import dev.huskcasaca.effortless.Effortless;
import net.minecraft.resources.ResourceLocation;

public interface MessageChannel {
    ResourceLocation MODIFIER_SETTINGS = new ResourceLocation(Effortless.MOD_ID, "channel.modifier_settings");
    ResourceLocation MODE_SETTINGS = new ResourceLocation(Effortless.MOD_ID, "channel.mode_settings");
    ResourceLocation REACH_SETTINGS = new ResourceLocation(Effortless.MOD_ID, "channel.reach_settings");
    ResourceLocation MODE_ACTION = new ResourceLocation(Effortless.MOD_ID, "channel.mode_action");
    ResourceLocation BLOCK_PLACED = new ResourceLocation(Effortless.MOD_ID, "channel.block_placed");
    ResourceLocation BLOCK_BROKEN = new ResourceLocation(Effortless.MOD_ID, "channel.block_broken");
    ResourceLocation CANCEL_MODE = new ResourceLocation(Effortless.MOD_ID, "channel.cancel_mode");
    ResourceLocation REQUEST_LOOK_AT = new ResourceLocation(Effortless.MOD_ID, "channel.request_look_at");
    ResourceLocation ADD_UNDO = new ResourceLocation(Effortless.MOD_ID, "channel.add_undo");
    ResourceLocation CLEAR_UNDO = new ResourceLocation(Effortless.MOD_ID, "channel.clear_undo");
}
