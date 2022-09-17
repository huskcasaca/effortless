package dev.huskcasaca.effortless.mixin;

import dev.huskcasaca.effortless.EffortlessDataProvider;
import dev.huskcasaca.effortless.buildmode.BuildMode;
import dev.huskcasaca.effortless.buildmode.ModeSettingsManager;
import dev.huskcasaca.effortless.buildmodifier.ModifierSettingsManager;
import dev.huskcasaca.effortless.buildmodifier.array.Array;
import dev.huskcasaca.effortless.buildmodifier.mirror.Mirror;
import dev.huskcasaca.effortless.buildmodifier.mirror.RadialMirror;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerTagMixin implements EffortlessDataProvider {

    @Unique
    private ModeSettingsManager.ModeSettings modeSettings = null;
    @Unique
    private ModifierSettingsManager.ModifierSettings modifierSettings = null;

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void readTag(CompoundTag tag, CallbackInfo info) {
        readModeSettings(tag.getCompound("EffortlessMode"));
        readModifierSettings(tag.getCompound("EffortlessModifier"));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void writeTag(CompoundTag tag, CallbackInfo info) {
        CompoundTag modeTag = new CompoundTag();
        writeModeSettings(modeTag);
        tag.put("EffortlessMode", modeTag);

        CompoundTag modifierTag = new CompoundTag();
        writeModifierSettings(modifierTag);
        tag.put("EffortlessModifier", modifierTag);
    }

    @Unique
    private void readModeSettings(CompoundTag tag) {
        modeSettings = new ModeSettingsManager.ModeSettings();
    }

    @Unique
    private void writeModeSettings(CompoundTag tag) {
        if (modeSettings == null) modeSettings = new ModeSettingsManager.ModeSettings();
        //tag.putInteger("buildMode", modeSettings.buildMode().ordinal());
        //TODO add mode settings
    }

    @Unique
    public void writeModifierSettings(CompoundTag tag) {
        if (modifierSettings == null) modifierSettings = new ModifierSettingsManager.ModifierSettings();

        //MIRROR
        Mirror.MirrorSettings m = modifierSettings.getMirrorSettings();
        if (m == null) m = new Mirror.MirrorSettings();
        tag.putBoolean("mirrorEnabled", m.enabled);
        tag.putDouble("mirrorPosX", m.position.x);
        tag.putDouble("mirrorPosY", m.position.y);
        tag.putDouble("mirrorPosZ", m.position.z);
        tag.putBoolean("mirrorX", m.mirrorX);
        tag.putBoolean("mirrorY", m.mirrorY);
        tag.putBoolean("mirrorZ", m.mirrorZ);
        tag.putInt("mirrorRadius", m.radius);
        tag.putBoolean("mirrorDrawLines", m.drawLines);
        tag.putBoolean("mirrorDrawPlanes", m.drawPlanes);

        //ARRAY
        Array.ArraySettings a = modifierSettings.getArraySettings();
        if (a == null) a = new Array.ArraySettings();
        tag.putBoolean("arrayEnabled", a.enabled);
        tag.putInt("arrayOffsetX", a.offset.getX());
        tag.putInt("arrayOffsetY", a.offset.getY());
        tag.putInt("arrayOffsetZ", a.offset.getZ());
        tag.putInt("arrayCount", a.count);

        tag.putInt("reachUpgrade", modifierSettings.getReachUpgrade());

        //compound.putBoolean("quickReplace", buildSettings.doQuickReplace()); dont save quickreplace

        //RADIAL MIRROR
        RadialMirror.RadialMirrorSettings r = modifierSettings.getRadialMirrorSettings();
        if (r == null) r = new RadialMirror.RadialMirrorSettings();
        tag.putBoolean("radialMirrorEnabled", r.enabled);
        tag.putDouble("radialMirrorPosX", r.position.x);
        tag.putDouble("radialMirrorPosY", r.position.y);
        tag.putDouble("radialMirrorPosZ", r.position.z);
        tag.putInt("radialMirrorSlices", r.slices);
        tag.putBoolean("radialMirrorAlternate", r.alternate);
        tag.putInt("radialMirrorRadius", r.radius);
        tag.putBoolean("radialMirrorDrawLines", r.drawLines);
        tag.putBoolean("radialMirrorDrawPlanes", r.drawPlanes);
    }

    @Unique
    public void readModifierSettings(CompoundTag tag) {

        //MIRROR
        boolean mirrorEnabled = tag.getBoolean("mirrorEnabled");
        Vec3 mirrorPosition = new Vec3(
                tag.getDouble("mirrorPosX"),
                tag.getDouble("mirrorPosY"),
                tag.getDouble("mirrorPosZ"));
        boolean mirrorX = tag.getBoolean("mirrorX");
        boolean mirrorY = tag.getBoolean("mirrorY");
        boolean mirrorZ = tag.getBoolean("mirrorZ");
        int mirrorRadius = tag.getInt("mirrorRadius");
        boolean mirrorDrawLines = tag.getBoolean("mirrorDrawLines");
        boolean mirrorDrawPlanes = tag.getBoolean("mirrorDrawPlanes");
        Mirror.MirrorSettings mirrorSettings = new Mirror.MirrorSettings(mirrorEnabled, mirrorPosition, mirrorX, mirrorY, mirrorZ, mirrorRadius, mirrorDrawLines, mirrorDrawPlanes);

        //ARRAY
        boolean arrayEnabled = tag.getBoolean("arrayEnabled");
        BlockPos arrayOffset = new BlockPos(
                tag.getInt("arrayOffsetX"),
                tag.getInt("arrayOffsetY"),
                tag.getInt("arrayOffsetZ"));
        int arrayCount = tag.getInt("arrayCount");
        Array.ArraySettings arraySettings = new Array.ArraySettings(arrayEnabled, arrayOffset, arrayCount);

        int reachUpgrade = tag.getInt("reachUpgrade");

        //boolean quickReplace = compound.getBoolean("quickReplace"); //dont load quickreplace

        //RADIAL MIRROR
        boolean radialMirrorEnabled = tag.getBoolean("radialMirrorEnabled");
        Vec3 radialMirrorPosition = new Vec3(
                tag.getDouble("radialMirrorPosX"),
                tag.getDouble("radialMirrorPosY"),
                tag.getDouble("radialMirrorPosZ"));
        int radialMirrorSlices = tag.getInt("radialMirrorSlices");
        boolean radialMirrorAlternate = tag.getBoolean("radialMirrorAlternate");
        int radialMirrorRadius = tag.getInt("radialMirrorRadius");
        boolean radialMirrorDrawLines = tag.getBoolean("radialMirrorDrawLines");
        boolean radialMirrorDrawPlanes = tag.getBoolean("radialMirrorDrawPlanes");
        RadialMirror.RadialMirrorSettings radialMirrorSettings = new RadialMirror.RadialMirrorSettings(radialMirrorEnabled, radialMirrorPosition,
                radialMirrorSlices, radialMirrorAlternate, radialMirrorRadius, radialMirrorDrawLines, radialMirrorDrawPlanes);

        modifierSettings = new ModifierSettingsManager.ModifierSettings(mirrorSettings, arraySettings, radialMirrorSettings, false, reachUpgrade);
    }


    @Override
    public ModeSettingsManager.ModeSettings getModeSettings() {
        if (modeSettings == null) modeSettings = new ModeSettingsManager.ModeSettings();
        return modeSettings;
    }

    @Override
    public void setModeSettings(ModeSettingsManager.ModeSettings modeSettings) {
        this.modeSettings = modeSettings;
    }

    @Override
    public ModifierSettingsManager.ModifierSettings getModifierSettings() {
        if (modifierSettings == null) modifierSettings = new ModifierSettingsManager.ModifierSettings();
        return modifierSettings;
    }

    @Override
    public void setModifierSettings(ModifierSettingsManager.ModifierSettings modifierSettings) {
        this.modifierSettings = modifierSettings;
    }
}
