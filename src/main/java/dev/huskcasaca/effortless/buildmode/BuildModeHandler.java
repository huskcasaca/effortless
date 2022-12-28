package dev.huskcasaca.effortless.buildmode;

import dev.huskcasaca.effortless.Effortless;
import dev.huskcasaca.effortless.entity.player.EffortlessDataProvider;
import dev.huskcasaca.effortless.buildmodifier.BuildModifierHandler;
import dev.huskcasaca.effortless.buildmodifier.BuildModifierHelper;
import dev.huskcasaca.effortless.building.ReachHelper;
import dev.huskcasaca.effortless.utils.SurvivalHelper;
import dev.huskcasaca.effortless.network.Packets;
import dev.huskcasaca.effortless.network.protocol.player.ClientboundPlayerBuildModePacket;
import dev.huskcasaca.effortless.network.protocol.player.ServerboundPlayerBreakBlockPacket;
import dev.huskcasaca.effortless.network.protocol.player.ServerboundPlayerPlaceBlockPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class BuildModeHandler {

    //Static variables are shared between client and server in singleplayer
    //We need them separate
    private static final Dictionary<Player, Boolean> currentlyBreakingClient = new Hashtable<>();
    private static final Dictionary<Player, Boolean> currentlyBreakingServer = new Hashtable<>();

    //Uses a network message to get the previous raytraceresult from the player
    //The server could keep track of all raytraceresults but this might lag with many players
    //Raytraceresult is needed for hitSide and hitVec
    public static void onBlockPlacedPacketReceived(Player player, ServerboundPlayerPlaceBlockPacket packet) {

        //Check if not in the middle of breaking
        var currentlyBreaking = player.level.isClientSide ? currentlyBreakingClient : currentlyBreakingServer;
        if (currentlyBreaking.get(player) != null && currentlyBreaking.get(player)) {
            //Cancel breaking
            initializeMode(player);
            return;
        }

        var modifierSettings = BuildModifierHelper.getModifierSettings(player);
        var modeSettings = BuildModeHelper.getModeSettings(player);
        var buildMode = modeSettings.buildMode();

        BlockPos startPos = null;

        if (packet.blockHit() && packet.blockPos() != null) {
            startPos = packet.blockPos();

            //Offset in direction of hitSide if not quickreplace and not replaceable
            //TODO 1.13 replaceable
            boolean replaceable = player.level.getBlockState(startPos).getMaterial().isReplaceable();
            boolean becomesDoubleSlab = SurvivalHelper.doesBecomeDoubleSlab(player, startPos, packet.hitSide());
            if (!modifierSettings.enableQuickReplace() && !replaceable && !becomesDoubleSlab) {
                startPos = startPos.relative(packet.hitSide());
            }

            //Get under tall grass and other replaceable blocks
            if (modifierSettings.enableQuickReplace() && replaceable) {
                startPos = startPos.below();
            }

            //Check if player reach does not exceed startpos
            int maxReach = ReachHelper.getMaxReachDistance(player);
            if (buildMode != BuildMode.DISABLE && player.blockPosition().distSqr(startPos) > maxReach * maxReach) {
                Effortless.log(player, "Placement exceeds your reach.");
                return;
            }
        }

        //Even when no starting block is found, call buildmode instance
        //We might want to place things in the air
        var skipRaytrace = modifierSettings.enableQuickReplace();
        var coordinates = buildMode.getInstance().onUse(player, startPos, packet.hitSide(), packet.hitVec(), skipRaytrace);

        if (coordinates.isEmpty()) {
            currentlyBreaking.put(player, false);
            return;
        }

        //Limit number of blocks you can place
        int limit = ReachHelper.getMaxBlockPlaceAtOnce(player);
        if (coordinates.size() > limit) {
            coordinates = coordinates.subList(0, limit);
        }

        var hitSide = buildMode.getInstance().getHitSide(player);
        if (hitSide == null) hitSide = packet.hitSide();

        var hitVec = buildMode.getInstance().getHitVec(player);
        if (hitVec == null) hitVec = packet.hitVec();

        BuildModifierHandler.onBlockPlaced(player, coordinates, hitSide, hitVec, packet.placeStartPos());

        //Only works when finishing a buildmode is equal to placing some blocks
        //No intermediate blocks allowed
        currentlyBreaking.remove(player);

    }

    //Use a network packet to break blocks in the distance using clientside mouse input
    public static void onBlockBrokenPacketReceived(Player player, ServerboundPlayerBreakBlockPacket packet) {
        var startPos = packet.blockHit() ? packet.blockPos() : null;
        onBlockBroken(player, startPos, true);
    }

    public static void onBlockBroken(Player player, BlockPos startPos, boolean breakStartPos) {

        //Check if not in the middle of placing
        var currentlyBreaking = player.level.isClientSide ? currentlyBreakingClient : currentlyBreakingServer;
        if (currentlyBreaking.get(player) != null && !currentlyBreaking.get(player)) {
            //Cancel placing
            initializeMode(player);
            return;
        }

        if (!ReachHelper.isCanBreakFar(player)) return;

        //If first click
        if (currentlyBreaking.get(player) == null) {
            //If startpos is null, dont do anything
            if (startPos == null) return;
        }

        var modifierSettings = BuildModifierHelper.getModifierSettings(player);
        var modeSettings = BuildModeHelper.getModeSettings(player);

        //Get coordinates
        var buildMode = modeSettings.buildMode();
        var coordinates = buildMode.getInstance().onUse(player, startPos, Direction.UP, Vec3.ZERO, true);

        if (coordinates.isEmpty()) {
            currentlyBreaking.put(player, true);
            return;
        }

        //Let buildmodifiers break blocks
        BuildModifierHandler.onBlockBroken(player, coordinates, breakStartPos);

        //Only works when finishing a buildmode is equal to breaking some blocks
        //No intermediate blocks allowed
        currentlyBreaking.remove(player);
    }

    public static List<BlockPos> findCoordinates(Player player, BlockPos startPos, boolean skipRaytrace) {
        List<BlockPos> coordinates = new ArrayList<>();

        var modeSettings = BuildModeHelper.getModeSettings(player);
        coordinates.addAll(modeSettings.buildMode().getInstance().findCoordinates(player, startPos, skipRaytrace));

        return coordinates;
    }

    public static void initializeMode(Player player) {
        //Resetting mode, so not placing or breaking
        if (player == null) {
            return;
        }
        var currentlyBreaking = player.level.isClientSide ? currentlyBreakingClient : currentlyBreakingServer;
        currentlyBreaking.remove(player);

        BuildModeHelper.getModeSettings(player).buildMode().getInstance().initialize(player);
    }

    public static boolean isCurrentlyPlacing(Player player) {
        var currentlyBreaking = player.level.isClientSide ? currentlyBreakingClient : currentlyBreakingServer;
        return currentlyBreaking.get(player) != null && !currentlyBreaking.get(player);
    }

    public static boolean isCurrentlyBreaking(Player player) {
        var currentlyBreaking = player.level.isClientSide ? currentlyBreakingClient : currentlyBreakingServer;
        return currentlyBreaking.get(player) != null && currentlyBreaking.get(player);
    }

    //Either placing or breaking
    public static boolean isActive(Player player) {
        var currentlyBreaking = player.level.isClientSide ? currentlyBreakingClient : currentlyBreakingServer;
        return currentlyBreaking.get(player) != null;
    }

    //Find coordinates on a line bound by a plane
    public static Vec3 findXBound(double x, Vec3 start, Vec3 look) {
        //then y and z are
        double y = (x - start.x) / look.x * look.y + start.y;
        double z = (x - start.x) / look.x * look.z + start.z;

        return new Vec3(x, y, z);
    }


    //-- Common build mode functionality --//

    public static Vec3 findYBound(double y, Vec3 start, Vec3 look) {
        //then x and z are
        double x = (y - start.y) / look.y * look.x + start.x;
        double z = (y - start.y) / look.y * look.z + start.z;

        return new Vec3(x, y, z);
    }

    public static Vec3 findZBound(double z, Vec3 start, Vec3 look) {
        //then x and y are
        double x = (z - start.z) / look.z * look.x + start.x;
        double y = (z - start.z) / look.z * look.y + start.y;

        return new Vec3(x, y, z);
    }

    //Use this instead of player.getLookVec() in any buildmodes code
    public static Vec3 getPlayerLookVec(Player player) {
        Vec3 lookVec = player.getLookAngle();
        double x = lookVec.x;
        double y = lookVec.y;
        double z = lookVec.z;

        //Further calculations (findXBound etc) don't like any component being 0 or 1 (e.g. dividing by 0)
        //isCriteriaValid below will take up to 2 minutes to raytrace blocks towards infinity if that is the case
        //So make sure they are close to but never exactly 0 or 1
        if (Math.abs(x) < 0.0001) x = 0.0001;
        if (Math.abs(x - 1.0) < 0.0001) x = 0.9999;
        if (Math.abs(x + 1.0) < 0.0001) x = -0.9999;

        if (Math.abs(y) < 0.0001) y = 0.0001;
        if (Math.abs(y - 1.0) < 0.0001) y = 0.9999;
        if (Math.abs(y + 1.0) < 0.0001) y = -0.9999;

        if (Math.abs(z) < 0.0001) z = 0.0001;
        if (Math.abs(z - 1.0) < 0.0001) z = 0.9999;
        if (Math.abs(z + 1.0) < 0.0001) z = -0.9999;

        return new Vec3(x, y, z);
    }

    public static boolean isCriteriaValid(Vec3 start, Vec3 look, int reach, Player player, boolean skipRaytrace, Vec3 lineBound, Vec3 planeBound, double distToPlayerSq) {
        boolean intersects = false;
        if (!skipRaytrace) {
            //collision within a 1 block radius to selected is fine
            ClipContext rayTraceContext = new ClipContext(start, lineBound, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player);
            HitResult rayTraceResult = player.level.clip(rayTraceContext);
            intersects = rayTraceResult != null && rayTraceResult.getType() == HitResult.Type.BLOCK &&
                    planeBound.subtract(rayTraceResult.getLocation()).lengthSqr() > 4;
        }

        return planeBound.subtract(start).dot(look) > 0 &&
                distToPlayerSq > 2 && distToPlayerSq < reach * reach &&
                !intersects;
    }

    public static void handleNewPlayer(ServerPlayer player) {
        //Makes sure player has mode settings (if it doesnt it will create it)
        Packets.sendToClient(new ClientboundPlayerBuildModePacket(((EffortlessDataProvider) player).getModeSettings()), player);
    }
}
