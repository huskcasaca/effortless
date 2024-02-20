package dev.huskuraft.effortless.vanilla.renderer;

import java.util.OptionalDouble;

import com.google.auto.service.AutoService;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.renderer.RenderLayer;
import dev.huskuraft.effortless.api.renderer.RenderStateFactory;
import dev.huskuraft.effortless.api.renderer.Shader;
import dev.huskuraft.effortless.api.renderer.Shaders;
import dev.huskuraft.effortless.api.renderer.VertexFormat;
import dev.huskuraft.effortless.api.renderer.VertexFormats;
import dev.huskuraft.effortless.api.renderer.programs.CompositeRenderState;
import dev.huskuraft.effortless.api.renderer.programs.RenderState;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

@AutoService(RenderStateFactory.class)
public class MinecraftRenderStateFactory implements RenderStateFactory {

    @Override
    public RenderLayer createCompositeRenderLayer(String name,
                                                  VertexFormat vertexFormat,
                                                  VertexFormat.Mode vertexFormatMode,
                                                  int bufferSize,
                                                  boolean affectsCrumbling,
                                                  boolean sortOnUpload,
                                                  CompositeRenderState state) {
        var layer = RenderType.create(
                name,
                vertexFormat.reference(),
                vertexFormatMode.reference(),
                bufferSize,
                affectsCrumbling,
                sortOnUpload,
                state.reference()
        );
        return () -> layer;
    }

    @Override
    public CompositeRenderState createCompositeState(RenderState.TextureState textureState,
                                                     RenderState.ShaderState shaderState,
                                                     RenderState.TransparencyState transparencyState,
                                                     RenderState.DepthTestState depthTestState,
                                                     RenderState.CullState cullState,
                                                     RenderState.LightmapState lightmapState,
                                                     RenderState.OverlayState overlayState,
                                                     RenderState.LayeringState layeringState,
                                                     RenderState.OutputState outputState,
                                                     RenderState.TexturingState texturingState,
                                                     RenderState.WriteMaskState writeMaskState,
                                                     RenderState.LineState lineState,
                                                     RenderState.ColorLogicState colorLogicState,
                                                     boolean affectOutline) {
        var state = RenderType.CompositeState.builder()
                .setTextureState(textureState.reference())
                .setShaderState(shaderState.reference())
                .setTransparencyState(transparencyState.reference())
                .setDepthTestState(depthTestState.reference())
                .setCullState(cullState.reference())
                .setLightmapState(lightmapState.reference())
                .setOverlayState(overlayState.reference())
                .setLayeringState(layeringState.reference())
                .setOutputState(outputState.reference())
                .setTexturingState(texturingState.reference())
                .setWriteMaskState(writeMaskState.reference())
                .setLineState(lineState.reference())
//                .setColorLogicState(colorLogicState.reference())
                .createCompositeState(affectOutline);
        return () -> state;
    }

    @Override
    public RenderState createRenderState(String name, Runnable setupState, Runnable clearState) {
        var state = new RenderStateShard(name, setupState, clearState) {
        };
        return () -> state;
    }

    @Override
    public RenderState.TextureState createTextureState(String name, RenderState.TextureState.Texture texture) {
        var state = texture == null ? RenderType.NO_TEXTURE : new RenderType.TextureStateShard(texture.location().reference(), texture.blur(), texture.mipmap());
        return () -> state;
    }

    @Override
    public RenderState.ShaderState createShaderState(String name, Shader shader) {
        var state = shader == null ? new RenderType.ShaderStateShard() : new RenderType.ShaderStateShard(shader::reference);
        return () -> state;
    }

    @Override
    public RenderState.TransparencyState createTransparencyState(String name, RenderState.TransparencyState.Type type) {
        var state = switch (type) {
            case NO -> RenderType.NO_TRANSPARENCY;
            case ADDITIVE -> RenderType.ADDITIVE_TRANSPARENCY;
            case LIGHTNING -> RenderType.LIGHTNING_TRANSPARENCY;
            case GLINT -> RenderType.GLINT_TRANSPARENCY;
            case CRUMBLING -> RenderType.CRUMBLING_TRANSPARENCY;
            case TRANSLUCENT -> RenderType.TRANSLUCENT_TRANSPARENCY;
        };
        return () -> state;
    }

    @Override
    public RenderState.DepthTestState createDepthTestState(String name, int function) {
        var state = new RenderType.DepthTestStateShard(name, function);
        return () -> state;
    }

    @Override
    public RenderState.CullState createCullState(String name, boolean cull) {
        var state = cull ? RenderType.CULL : RenderType.NO_CULL;
        return () -> state;
    }

    @Override
    public RenderState.LightmapState createLightmapState(String name, boolean lightmap) {
        var state = lightmap ? RenderType.LIGHTMAP : RenderType.NO_LIGHTMAP;
        return () -> state;
    }

    @Override
    public RenderState.OverlayState createOverlayState(String name, boolean overlay) {
        var state = overlay ? RenderType.OVERLAY : RenderType.NO_OVERLAY;
        return () -> state;
    }

    @Override
    public RenderState.LayeringState createLayeringState(String name, RenderState.LayeringState.Type type) {
        var state = switch (type) {
            case NO -> RenderType.NO_LAYERING;
            case POLYGON_OFFSET -> RenderType.POLYGON_OFFSET_LAYERING;
            case VIEW_OFFSET_Z -> RenderType.VIEW_OFFSET_Z_LAYERING;
        };
        return () -> state;
    }

    @Override
    public RenderState.OutputState createOutputState(String name, RenderState.OutputState.Target target) {
        var state = switch (target) {
            case NO -> RenderType.MAIN_TARGET;
            case OUTLINE -> RenderType.OUTLINE_TARGET;
            case TRANSLUCENT -> RenderType.TRANSLUCENT_TARGET;
            case PARTICLES -> RenderType.PARTICLES_TARGET;
            case WEATHER -> RenderType.WEATHER_TARGET;
            case CLOUDS -> RenderType.CLOUDS_TARGET;
            case ITEM_ENTITY -> RenderType.ITEM_ENTITY_TARGET;
        };
        return () -> state;
    }

    @Override
    public RenderState.TexturingState createTexturingState(String name, Runnable setupState, Runnable clearState) {
        var state = new RenderType.TexturingStateShard(name, setupState, clearState);
        return () -> state;
    }

    @Override
    public RenderState.OffsetTexturingState createOffsetTexturingState(String name, float offsetX, float offsetY) {
        var state = new RenderType.OffsetTexturingStateShard(offsetX, offsetY);
        return () -> state;
    }

    @Override
    public RenderState.WriteMaskState createWriteMaskState(String name, boolean writeColor, boolean writeDepth) {
        var state = new RenderType.WriteMaskStateShard(writeColor, writeDepth);
        return () -> state;
    }

    @Override
    public RenderState.LineState createLineState(String name, Double width) {
        var state = new RenderType.LineStateShard(width == null ? OptionalDouble.empty() : OptionalDouble.of(width));
        return () -> state;
    }

    @Override
    public RenderState.ColorLogicState createColorLogicState(String name, RenderState.ColorLogicState.Op op) {
        var state = switch (op) {
            case NO_LOGIC -> PlatformReference.unavailable();
            case OR_REVERSE_LOGIC -> PlatformReference.unavailable();
        };
        return () -> state;
    }

    @Override
    public Shader getShader(Shaders shaders) {
        var shader = switch (shaders) {
            case POSITION_COLOR_LIGHTMAP -> GameRenderer.getPositionColorLightmapShader();
            case POSITION -> GameRenderer.getPositionShader();
            case POSITION_COLOR_TEX -> GameRenderer.getPositionColorTexShader();
            case POSITION_TEX -> GameRenderer.getPositionTexShader();
            case POSITION_COLOR_TEX_LIGHTMAP -> GameRenderer.getPositionColorTexLightmapShader();
            case POSITION_COLOR -> GameRenderer.getPositionColorShader();
            case SOLID -> GameRenderer.getRendertypeSolidShader();
            case CUTOUT_MIPPED -> GameRenderer.getRendertypeCutoutMippedShader();
            case CUTOUT -> GameRenderer.getRendertypeCutoutShader();
            case TRANSLUCENT -> GameRenderer.getRendertypeTranslucentShader();
            case TRANSLUCENT_MOVING_BLOCK -> GameRenderer.getRendertypeTranslucentMovingBlockShader();
            case TRANSLUCENT_NO_CRUMBLING -> GameRenderer.getRendertypeTranslucentNoCrumblingShader();
            case ARMOR_CUTOUT_NO_CULL -> GameRenderer.getRendertypeArmorCutoutNoCullShader();
            case ENTITY_SOLID -> GameRenderer.getRendertypeEntitySolidShader();
            case ENTITY_CUTOUT -> GameRenderer.getRendertypeEntityCutoutShader();
            case ENTITY_CUTOUT_NO_CULL -> GameRenderer.getRendertypeEntityCutoutNoCullShader();
            case ENTITY_CUTOUT_NO_CULL_Z_OFFSET -> GameRenderer.getRendertypeEntityCutoutNoCullZOffsetShader();
            case ITEM_ENTITY_TRANSLUCENT_CULL -> GameRenderer.getRendertypeItemEntityTranslucentCullShader();
            case ENTITY_TRANSLUCENT_CULL -> GameRenderer.getRendertypeEntityTranslucentCullShader();
            case ENTITY_TRANSLUCENT -> GameRenderer.getRendertypeEntityTranslucentShader();
            case ENTITY_TRANSLUCENT_EMISSIVE -> GameRenderer.getRendertypeEntityTranslucentEmissiveShader();
            case ENTITY_SMOOTH_CUTOUT -> GameRenderer.getRendertypeEntitySmoothCutoutShader();
            case BEACON_BEAM -> GameRenderer.getRendertypeBeaconBeamShader();
            case ENTITY_DECAL -> GameRenderer.getRendertypeEntityDecalShader();
            case ENTITY_NO_OUTLINE -> GameRenderer.getRendertypeEntityNoOutlineShader();
            case ENTITY_SHADOW -> GameRenderer.getRendertypeEntityShadowShader();
            case ENTITY_ALPHA -> GameRenderer.getRendertypeEntityAlphaShader();
            case EYES -> GameRenderer.getRendertypeEyesShader();
            case ENERGY_SWIRL -> GameRenderer.getRendertypeEnergySwirlShader();
            case LEASH -> GameRenderer.getRendertypeLeashShader();
            case WATER_MASK -> GameRenderer.getRendertypeWaterMaskShader();
            case OUTLINE -> GameRenderer.getRendertypeOutlineShader();
            case ARMOR_GLINT -> GameRenderer.getRendertypeArmorGlintShader();
            case ARMOR_ENTITY_GLINT -> GameRenderer.getRendertypeArmorEntityGlintShader();
            case GLINT_TRANSLUCENT -> GameRenderer.getRendertypeGlintTranslucentShader();
            case GLINT -> GameRenderer.getRendertypeGlintShader();
            case GLINT_DIRECT -> GameRenderer.getRendertypeGlintDirectShader();
            case ENTITY_GLINT -> GameRenderer.getRendertypeEntityGlintShader();
            case ENTITY_GLINT_DIRECT -> GameRenderer.getRendertypeEntityGlintDirectShader();
            case CRUMBLING -> GameRenderer.getRendertypeCrumblingShader();
            case TEXT -> GameRenderer.getRendertypeTextShader();
            case TEXT_BACKGROUND -> null;
            case TEXT_INTENSITY -> GameRenderer.getRendertypeTextIntensityShader();
            case TEXT_SEE_THROUGH -> GameRenderer.getRendertypeTextSeeThroughShader();
            case TEXT_BACKGROUND_SEE_THROUGH -> null;
            case TEXT_INTENSITY_SEE_THROUGH -> GameRenderer.getRendertypeTextIntensitySeeThroughShader();
            case LIGHTNING -> GameRenderer.getRendertypeLightningShader();
            case TRIPWIRE -> GameRenderer.getRendertypeTripwireShader();
            case END_PORTAL -> GameRenderer.getRendertypeEndPortalShader();
            case END_GATEWAY -> GameRenderer.getRendertypeEndGatewayShader();
            case LINES -> GameRenderer.getRendertypeLinesShader();
            case GUI -> null;
            case GUI_OVERLAY -> null;
            case GUI_TEXT_HIGHLIGHT -> null;
            case GUI_GHOST_RECIPE_OVERLAY -> null;
        };
        return new MinecraftShader(shader);
    }

    @Override
    public VertexFormat getVertexFormat(VertexFormats formats) {
        var format = switch (formats) {
            case BLIT_SCREEN -> DefaultVertexFormat.BLIT_SCREEN;
            case BLOCK -> DefaultVertexFormat.BLOCK;
            case NEW_ENTITY -> DefaultVertexFormat.NEW_ENTITY;
            case PARTICLE -> DefaultVertexFormat.PARTICLE;
            case POSITION -> DefaultVertexFormat.POSITION;
            case POSITION_COLOR -> DefaultVertexFormat.POSITION_COLOR;
            case POSITION_COLOR_NORMAL -> DefaultVertexFormat.POSITION_COLOR_NORMAL;
            case POSITION_COLOR_LIGHTMAP -> DefaultVertexFormat.POSITION_COLOR_LIGHTMAP;
            case POSITION_TEX -> DefaultVertexFormat.POSITION_TEX;
            case POSITION_COLOR_TEX -> DefaultVertexFormat.POSITION_COLOR_TEX;
            case POSITION_TEX_COLOR -> DefaultVertexFormat.POSITION_TEX_COLOR;
            case POSITION_COLOR_TEX_LIGHTMAP -> DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP;
            case POSITION_TEX_LIGHTMAP_COLOR -> DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR;
            case POSITION_TEX_COLOR_NORMAL -> DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL;
        };
        return () -> format;
    }

    @Override
    public VertexFormat.Mode getVertexFormatMode(VertexFormats.Modes modes) {
        var mode = switch (modes) {
            case LINES -> com.mojang.blaze3d.vertex.VertexFormat.Mode.LINES;
            case LINE_STRIP -> com.mojang.blaze3d.vertex.VertexFormat.Mode.LINE_STRIP;
            case DEBUG_LINES -> com.mojang.blaze3d.vertex.VertexFormat.Mode.DEBUG_LINES;
            case DEBUG_LINE_STRIP -> com.mojang.blaze3d.vertex.VertexFormat.Mode.DEBUG_LINE_STRIP;
            case TRIANGLES -> com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLES;
            case TRIANGLE_STRIP -> com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLE_STRIP;
            case TRIANGLE_FAN -> com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLE_FAN;
            case QUADS -> com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;
        };
        return () -> mode;
    }

}
