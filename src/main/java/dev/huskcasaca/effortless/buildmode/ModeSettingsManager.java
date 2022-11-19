package dev.huskcasaca.effortless.buildmode;

import dev.huskcasaca.effortless.Effortless;
import dev.huskcasaca.effortless.EffortlessDataProvider;
import dev.huskcasaca.effortless.helper.ReachHelper;
import dev.huskcasaca.effortless.network.protocol.player.ClientboundPlayerBuildModePacket;
import dev.huskcasaca.effortless.network.Packets;
import dev.huskcasaca.effortless.network.protocol.player.ServerboundPlayerSetBuildModePacket;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ModeSettingsManager {

    //Retrieves the buildsettings of a player through the modeCapability capability
    //Never returns null
    public static ModeSettings getModeSettings(Player player) {
        return ((EffortlessDataProvider) player).getModeSettings();
    }

    public static String getTranslatedModeOptionName(Player player) {
        BuildMode mode = ModeSettingsManager.getModeSettings(player).buildMode();
        if (mode == BuildMode.DISABLE) {
            return I18n.get(mode.getNameKey());
        } else {
            StringBuilder modeName = new StringBuilder();
            for (BuildOption option : mode.options) {
                modeName.append(I18n.get(BuildActionHandler.getOptionSetting(option).getNameKey()));
                modeName.append(" ");
            }
            return modeName + I18n.get(mode.getNameKey());
        }
    }


    public static void setModeSettings(Player player, ModeSettings modeSettings) {
        if (player == null) {
            Effortless.log("Cannot set buildmode settings, player is null");
            return;
        }
        ((EffortlessDataProvider) player).setModeSettings(modeSettings);
    }

    public static void syncMagnetSetting(Player player, boolean enable) {
        if (player == null) {
            return;
        }
        var modeSettings = getModeSettings(player);
        modeSettings = new ModeSettingsManager.ModeSettings(modeSettings.buildMode(), enable);
        ModeSettingsManager.setModeSettings(player, modeSettings);
        Packets.sendToServer(new ServerboundPlayerSetBuildModePacket(modeSettings));
    }


    public static String getSanitizeMessage(ModeSettings modeSettings, Player player) {
        int maxReach = ReachHelper.getMaxReach(player);
        String error = "";

        //TODO sanitize

        return error;
    }

    public static ModeSettings sanitize(ModeSettings modeSettings, Player player) {
        return modeSettings;
    }

    public static void handleNewPlayer(ServerPlayer player) {
        //Makes sure player has mode settings (if it doesnt it will create it)

        Packets.sendToClient(new ClientboundPlayerBuildModePacket(((EffortlessDataProvider) player).getModeSettings()), (ServerPlayer) player);
    }

    public record ModeSettings(
            BuildMode buildMode,
            boolean enableMagnet
    ) {

        public ModeSettings() {
            this(BuildMode.DISABLE, false);
        }

    }
}
