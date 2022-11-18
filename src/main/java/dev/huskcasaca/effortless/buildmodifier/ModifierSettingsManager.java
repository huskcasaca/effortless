package dev.huskcasaca.effortless.buildmodifier;

import dev.huskcasaca.effortless.Effortless;
import dev.huskcasaca.effortless.EffortlessDataProvider;
import dev.huskcasaca.effortless.buildmodifier.array.Array;
import dev.huskcasaca.effortless.buildmodifier.mirror.Mirror;
import dev.huskcasaca.effortless.buildmodifier.mirror.RadialMirror;
import dev.huskcasaca.effortless.helper.ReachHelper;
import dev.huskcasaca.effortless.network.protocol.player.ClientboundPlayerBuildModifierPacket;
import dev.huskcasaca.effortless.network.Packets;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ModifierSettingsManager {

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

    public static String getSanitizeMessage(ModifierSettings modifierSettings, Player player) {
        int maxReach = ReachHelper.getMaxReach(player);
        String error = "";

        //Array settings
        var arraySettings = modifierSettings.arraySettings();
        if (arraySettings.count() < 0) {
            error += "Array count may not be negative. It has been reset to 0. \n";
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
        int maxReach = ReachHelper.getMaxReach(player);

        //Array settings
        var arraySettings = modifierSettings.arraySettings();
        int count = arraySettings.count();
        if (count < 0) {
            count = 0;
        }

        if (arraySettings.reach() > maxReach) {
            count = 0;
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
        boolean quickReplace = modifierSettings.quickReplace;

        return new ModifierSettings(
                arraySettings,
                mirrorSettings,
                radialMirrorSettings,
                quickReplace
        );
    }

    public static void handleNewPlayer(ServerPlayer player) {
        //Only on server
        Packets.sendToClient(new ClientboundPlayerBuildModifierPacket(((EffortlessDataProvider) player).getModifierSettings()), player);
    }

    public record ModifierSettings(
            Array.ArraySettings arraySettings,
            Mirror.MirrorSettings mirrorSettings,
            RadialMirror.RadialMirrorSettings radialMirrorSettings,
            boolean quickReplace
    ) {

        public ModifierSettings() {
            this(new Array.ArraySettings(), new Mirror.MirrorSettings(), new RadialMirror.RadialMirrorSettings(), false);
        }

        public ModifierSettings(Mirror.MirrorSettings mirrorSettings, Array.ArraySettings arraySettings,
                                RadialMirror.RadialMirrorSettings radialMirrorSettings) {
            this(arraySettings, mirrorSettings, radialMirrorSettings, false);
        }

        public static ModifierSettings decodeBuf(FriendlyByteBuf friendlyByteBuf) {

            //ARRAY
            var arraySettings = new Array.ArraySettings();
            if (friendlyByteBuf.readBoolean()) {
                boolean arrayEnabled = friendlyByteBuf.readBoolean();
                var arrayOffset = new BlockPos(friendlyByteBuf.readInt(), friendlyByteBuf.readInt(), friendlyByteBuf.readInt());
                int arrayCount = friendlyByteBuf.readInt();
                arraySettings = new Array.ArraySettings(arrayEnabled, arrayOffset, arrayCount);
            }

            //MIRROR
            var mirrorSettings = new Mirror.MirrorSettings();
            if (friendlyByteBuf.readBoolean()) {
                boolean mirrorEnabled = friendlyByteBuf.readBoolean();
                var mirrorPosition = new Vec3(friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble());
                boolean mirrorX = friendlyByteBuf.readBoolean();
                boolean mirrorY = friendlyByteBuf.readBoolean();
                boolean mirrorZ = friendlyByteBuf.readBoolean();
                int mirrorRadius = friendlyByteBuf.readInt();
                boolean mirrorDrawLines = friendlyByteBuf.readBoolean();
                boolean mirrorDrawPlanes = friendlyByteBuf.readBoolean();
                mirrorSettings = new Mirror.MirrorSettings(mirrorEnabled, mirrorPosition, mirrorX, mirrorY, mirrorZ, mirrorRadius,
                        mirrorDrawLines, mirrorDrawPlanes);
            }

            //RADIAL MIRROR
            var radialMirrorSettings = new RadialMirror.RadialMirrorSettings();
            if (friendlyByteBuf.readBoolean()) {
                boolean radialMirrorEnabled = friendlyByteBuf.readBoolean();
                var radialMirrorPosition = new Vec3(friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble());
                int radialMirrorSlices = friendlyByteBuf.readInt();
                boolean radialMirrorAlternate = friendlyByteBuf.readBoolean();
                int radialMirrorRadius = friendlyByteBuf.readInt();
                boolean radialMirrorDrawLines = friendlyByteBuf.readBoolean();
                boolean radialMirrorDrawPlanes = friendlyByteBuf.readBoolean();
                radialMirrorSettings = new RadialMirror.RadialMirrorSettings(radialMirrorEnabled, radialMirrorPosition, radialMirrorSlices, radialMirrorAlternate, radialMirrorRadius, radialMirrorDrawLines, radialMirrorDrawPlanes);
            }

            boolean quickReplace = friendlyByteBuf.readBoolean();

            return new ModifierSettings(arraySettings, mirrorSettings, radialMirrorSettings, quickReplace);
        }
        public static void write(FriendlyByteBuf friendlyByteBuf, ModifierSettings modifierSettings) {

            //ARRAY
            var arraySettings = modifierSettings.arraySettings();
            friendlyByteBuf.writeBoolean(arraySettings != null);
            if (arraySettings != null) {
                friendlyByteBuf.writeBoolean(arraySettings.enabled());
                friendlyByteBuf.writeInt(arraySettings.offset().getX());
                friendlyByteBuf.writeInt(arraySettings.offset().getY());
                friendlyByteBuf.writeInt(arraySettings.offset().getZ());
                friendlyByteBuf.writeInt(arraySettings.count());
            }

            //MIRROR
            var mirrorSettings = modifierSettings.mirrorSettings();
            friendlyByteBuf.writeBoolean(mirrorSettings != null);
            if (mirrorSettings != null) {
                friendlyByteBuf.writeBoolean(mirrorSettings.enabled());
                friendlyByteBuf.writeDouble(mirrorSettings.position().x);
                friendlyByteBuf.writeDouble(mirrorSettings.position().y);
                friendlyByteBuf.writeDouble(mirrorSettings.position().z);
                friendlyByteBuf.writeBoolean(mirrorSettings.mirrorX());
                friendlyByteBuf.writeBoolean(mirrorSettings.mirrorY());
                friendlyByteBuf.writeBoolean(mirrorSettings.mirrorZ());
                friendlyByteBuf.writeInt(mirrorSettings.radius());
                friendlyByteBuf.writeBoolean(mirrorSettings.drawLines());
                friendlyByteBuf.writeBoolean(mirrorSettings.drawPlanes());
            }

            //RADIAL MIRROR
            var radialMirrorSettings = modifierSettings.radialMirrorSettings();
            friendlyByteBuf.writeBoolean(radialMirrorSettings != null);
            if (radialMirrorSettings != null) {
                friendlyByteBuf.writeBoolean(radialMirrorSettings.enabled());
                friendlyByteBuf.writeDouble(radialMirrorSettings.position().x);
                friendlyByteBuf.writeDouble(radialMirrorSettings.position().y);
                friendlyByteBuf.writeDouble(radialMirrorSettings.position().z);
                friendlyByteBuf.writeInt(radialMirrorSettings.slices());
                friendlyByteBuf.writeBoolean(radialMirrorSettings.alternate());
                friendlyByteBuf.writeInt(radialMirrorSettings.radius());
                friendlyByteBuf.writeBoolean(radialMirrorSettings.drawLines());
                friendlyByteBuf.writeBoolean(radialMirrorSettings.drawPlanes());
            }
            friendlyByteBuf.writeBoolean(modifierSettings.quickReplace());
        }

    }
}

