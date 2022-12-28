package dev.huskcasaca.effortless.buildmodifier;

import dev.huskcasaca.effortless.Effortless;
import dev.huskcasaca.effortless.entity.player.EffortlessDataProvider;
import dev.huskcasaca.effortless.buildmodifier.array.Array;
import dev.huskcasaca.effortless.buildmodifier.mirror.Mirror;
import dev.huskcasaca.effortless.buildmodifier.mirror.RadialMirror;
import dev.huskcasaca.effortless.entity.player.ModifierSettings;
import dev.huskcasaca.effortless.building.ReachHelper;
import dev.huskcasaca.effortless.network.Packets;
import dev.huskcasaca.effortless.network.protocol.player.ClientboundPlayerBuildModifierPacket;
import dev.huskcasaca.effortless.network.protocol.player.ServerboundPlayerSetBuildModifierPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class BuildModifierHelper {

    //Retrieves the build settings of a player through the modifierCapability capability
    //Never returns null
    public static ModifierSettings getModifierSettings(Player player) {
        return ((EffortlessDataProvider) player).getModifierSettings();
    }

    public static void setModifierSettings(Player player, ModifierSettings modifierSettings) {
        if (player == null) {
            Effortless.log("Cannot set build modifier settings, player is null");
            return;
        }
        ((EffortlessDataProvider) player).setModifierSettings(modifierSettings);

    }

    public static boolean isReplace(Player player) {
        return getModifierSettings(player).enableReplace();
    }

    public static boolean isQuickReplace(Player player) {
        return getModifierSettings(player).enableQuickReplace();
    }

    public static ReplaceMode getReplaceMode(Player player) {
        return getModifierSettings(player).replaceMode();
    }


    public static void setReplaceMode(Player player, ReplaceMode mode) {
        ModifierSettings modifierSettings = getModifierSettings(player);
        modifierSettings = new ModifierSettings(modifierSettings.arraySettings(), modifierSettings.mirrorSettings(), modifierSettings.radialMirrorSettings(), mode);
        BuildModifierHelper.setModifierSettings(player, modifierSettings);
        setModifierSettings(player, modifierSettings);
    }

    public static void cycleReplaceMode(Player player) {
        ModifierSettings modifierSettings = getModifierSettings(player);
        modifierSettings = new ModifierSettings(modifierSettings.arraySettings(), modifierSettings.mirrorSettings(), modifierSettings.radialMirrorSettings(), ReplaceMode.values()[(modifierSettings.replaceMode().ordinal() + 1) % ReplaceMode.values().length]);
        BuildModifierHelper.setModifierSettings(player, modifierSettings);
        setModifierSettings(player, modifierSettings);
    }

    public static void sync(Player player) {
        if (player instanceof ServerPlayer) {
            Packets.sendToClient(new ClientboundPlayerBuildModifierPacket(getModifierSettings(player)), (ServerPlayer) player);
        } else {
            Packets.sendToServer(new ServerboundPlayerSetBuildModifierPacket(getModifierSettings(player)));
        }
    }

    public static Component getReplaceModeName(Player player) {
        var modifierSettings = getModifierSettings(player);
        return Component.literal(ChatFormatting.GOLD + "Replace " + ChatFormatting.RESET + (modifierSettings.enableReplace() ? (modifierSettings.enableQuickReplace() ? (ChatFormatting.GREEN + "QUICK") : (ChatFormatting.GREEN + "ON")) : (ChatFormatting.RED + "OFF")) + ChatFormatting.RESET);
    }

    public static String getSanitizeMessage(ModifierSettings modifierSettings, Player player) {
        int maxReach = ReachHelper.getMaxReachDistance(player);
        String error = "";

        //Array settings
        var arraySettings = modifierSettings.arraySettings();
        if (arraySettings.count() < 1) {
            error += "Array count has to be at least 1. It has been reset to 1. \n";
        }

        if (arraySettings.reach() > maxReach) {
            error += "Array exceeds your maximum reach of " + maxReach + ". Array count has been reset to 0. \n";
        }

        //Mirror settings
        var mirrorSettings = modifierSettings.mirrorSettings();
        if (mirrorSettings.radius() < 1) {
            error += "Mirror size has to be at least 1. This has been corrected. \n";
        }
        if (mirrorSettings.reach() > maxReach) {
            error += "Mirror exceeds your maximum reach of " + (maxReach / 2) + ". Radius has been set to " + (maxReach / 2) + ". \n";
        }

        //Radial mirror settings
        var radialMirrorSettings = modifierSettings.radialMirrorSettings();
        if (radialMirrorSettings.slices() < 2) {
            error += "Radial mirror needs to have at least 2 slices. Slices has been set to 2. \n";
        }

        if (radialMirrorSettings.radius() < 1) {
            error += "Radial mirror radius has to be at least 1. This has been corrected. \n";
        }
        if (radialMirrorSettings.reach() > maxReach) {
            error += "Radial mirror exceeds your maximum reach of " + (maxReach / 2) + ". Radius has been set to " + (maxReach / 2) + ". \n";
        }

        return error;
    }

    // TODO: 17/9/22
    public static ModifierSettings sanitize(ModifierSettings modifierSettings, Player player) {
        int maxReach = ReachHelper.getMaxReachDistance(player);

        //Array settings
        var arraySettings = modifierSettings.arraySettings();
        int count = arraySettings.count();
        if (count < 1) {
            count = 1;
        }

        if (arraySettings.reach() > maxReach) {
            count = 1;
        }
        arraySettings = new Array.ArraySettings(
                arraySettings.enabled(),
                arraySettings.offset(),
                count
        );

        //Mirror settings
        var mirrorSettings = modifierSettings.mirrorSettings();
        int radius = mirrorSettings.radius();
        if (radius < 1) {
            radius = 1;
        }
        if (mirrorSettings.reach() > maxReach) {
            radius = maxReach / 2;
        }
        mirrorSettings = new Mirror.MirrorSettings(
                mirrorSettings.enabled(),
                mirrorSettings.position(),
                mirrorSettings.mirrorX(),
                mirrorSettings.mirrorY(),
                mirrorSettings.mirrorZ(),
                radius,
                mirrorSettings.drawLines(),
                mirrorSettings.drawPlanes()
        );

        //Radial mirror settings
        var radialMirrorSettings = modifierSettings.radialMirrorSettings();
        int slices = radialMirrorSettings.slices();
        if (slices < 2) {
            slices = 2;
        }
        int radius1 = radialMirrorSettings.radius();
        if (radius1 < 1) {
            radius1 = 1;
        }
        if (radialMirrorSettings.reach() > maxReach) {
            radius1 = maxReach / 2;
        }
        radialMirrorSettings = new RadialMirror.RadialMirrorSettings(
                radialMirrorSettings.enabled(),
                radialMirrorSettings.position(),
                slices,
                radialMirrorSettings.alternate(),
                radius1,
                radialMirrorSettings.drawLines(),
                radialMirrorSettings.drawPlanes()
        );

        //Other
        var replaceMode = modifierSettings.replaceMode();

        return new ModifierSettings(
                arraySettings,
                mirrorSettings,
                radialMirrorSettings,
                replaceMode
        );
    }

}

