package dev.huskuraft.effortless.vanilla.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.api.renderer.*;
import dev.huskuraft.effortless.api.renderer.programs.CompositeRenderState;
import dev.huskuraft.effortless.api.renderer.programs.RenderState;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftResource;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftShader;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.OptionalDouble;

public class MinecraftRenderFactory extends RenderType implements RenderFactory {

    public MinecraftRenderFactory() {
        super(null, null, null, 0, false, false, null, null);
//        super("", new VertexFormat(ImmutableMap.ofEntries()), VertexFormat.Mode.DEBUG_LINE_STRIP, 0, false, false, () -> {}, () -> {});
    }

    @Override
    public RenderLayer createCompositeRenderLayer(String name,
                                                  VertexFormat vertexFormat,
                                                  VertexFormat.Mode vertexFormatMode,
                                                  int bufferSize,
                                                  boolean affectsCrumbling,
                                                  boolean sortOnUpload,
                                                  CompositeRenderState state) {
        return () -> RenderType.create(
                name,
                vertexFormat.reference(),
                vertexFormatMode.reference(),
                bufferSize,
                affectsCrumbling,
                sortOnUpload,
                state.reference()
        );
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
        return () -> CompositeState.builder()
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
                .setColorLogicState(colorLogicState.reference())
                .createCompositeState(affectOutline);
    }

    @Override
    public RenderState createRenderState(String name, Runnable setupState, Runnable clearState) {
        return () -> new RenderStateShard(name, setupState, clearState){};
    }

    @Override
    public RenderState.TextureState createTextureState(String name, RenderState.TextureState.Texture texture) {
        return texture == null ? () -> NO_TEXTURE : () -> new TextureStateShard(texture.resource().reference(), texture.blur(), texture.mipmap());
    }

    @Override
    public RenderState.ShaderState createShaderState(String name, Shader shader) {
        return () -> new ShaderStateShard(shader::reference);
    }

    @Override
    public RenderState.TransparencyState createTransparencyState(String name, RenderState.TransparencyState.Type type) {
        return switch (type) {
            case NO ->          () -> NO_TRANSPARENCY;
            case ADDITIVE ->    () -> ADDITIVE_TRANSPARENCY;
            case LIGHTNING ->   () -> LIGHTNING_TRANSPARENCY;
            case GLINT ->       () -> GLINT_TRANSPARENCY;
            case CRUMBLING ->   () -> CRUMBLING_TRANSPARENCY;
            case TRANSLUCENT -> () -> TRANSLUCENT_TRANSPARENCY;
        };
    }

    @Override
    public RenderState.DepthTestState createDepthTestState(String name, int function) {
        return () -> new DepthTestStateShard(name, function);
    }

    @Override
    public RenderState.CullState createCullState(String name, boolean cull) {
        if (cull) return () -> CULL;
        return () -> NO_CULL;
    }

    @Override
    public RenderState.LightmapState createLightmapState(String name, boolean lightmap) {
        if (lightmap) return () -> LIGHTMAP;
        return () -> NO_LIGHTMAP;
    }

    @Override
    public RenderState.OverlayState createOverlayState(String name, boolean overlay) {
        if (overlay) return () -> OVERLAY;
        return () -> NO_OVERLAY;
    }

    @Override
    public RenderState.LayeringState createLayeringState(String name, RenderState.LayeringState.Type type) {
        return switch (type) {
            case NO ->              () -> NO_LAYERING;
            case POLYGON_OFFSET ->  () -> POLYGON_OFFSET_LAYERING;
            case VIEW_OFFSET_Z ->   () -> VIEW_OFFSET_Z_LAYERING;
        };
    }

    @Override
    public RenderState.OutputState createOutputState(String name, RenderState.OutputState.Target target) {
        return switch (target) {
            case NO ->        () -> MAIN_TARGET;
            case OUTLINE ->     () -> OUTLINE_TARGET;
            case TRANSLUCENT -> () -> TRANSLUCENT_TARGET;
            case PARTICLES ->   () -> PARTICLES_TARGET;
            case WEATHER ->     () -> WEATHER_TARGET;
            case CLOUDS ->      () -> CLOUDS_TARGET;
            case ITEM_ENTITY -> () -> ITEM_ENTITY_TARGET;
        };
    }

    @Override
    public RenderState.TexturingState createTexturingState(String name, Runnable setupState, Runnable clearState) {
        return () -> new TexturingStateShard(name, setupState, clearState);
    }

    @Override
    public RenderState.OffsetTexturingState createOffsetTexturingState(String name, float offsetX, float offsetY) {
        return () -> new OffsetTexturingStateShard(offsetX, offsetY);
    }

    @Override
    public RenderState.WriteMaskState createWriteMaskState(String name, boolean writeColor, boolean writeDepth) {
        return () -> new WriteMaskStateShard(writeColor, writeDepth);
    }

    @Override
    public RenderState.LineState createLineState(String name, Double width) {
        return () -> new LineStateShard(width == null ? OptionalDouble.empty() : OptionalDouble.of(width));
    }

    @Override
    public RenderState.ColorLogicState createColorLogicState(String name, RenderState.ColorLogicState.Op op) {
        return switch (op) {
            case NO_LOGIC ->         () -> NO_COLOR_LOGIC;
            case OR_REVERSE_LOGIC -> () -> OR_REVERSE_COLOR_LOGIC;
        };
    }

    @Override
    public Shader getShader(Shaders shaders) {
        return switch (shaders) {
            case POSITION_COLOR_LIGHTMAP ->         (MinecraftShader) GameRenderer::getPositionColorLightmapShader;
            case POSITION ->                        (MinecraftShader) GameRenderer::getPositionShader;
            case POSITION_COLOR_TEX ->              (MinecraftShader) GameRenderer::getPositionColorTexShader;
            case POSITION_TEX ->                    (MinecraftShader) GameRenderer::getPositionTexShader;
            case POSITION_COLOR_TEX_LIGHTMAP ->     (MinecraftShader) GameRenderer::getPositionColorTexLightmapShader;
            case POSITION_COLOR ->                  (MinecraftShader) GameRenderer::getPositionColorShader;
            case SOLID ->                           (MinecraftShader) GameRenderer::getRendertypeSolidShader;
            case CUTOUT_MIPPED ->                   (MinecraftShader) GameRenderer::getRendertypeCutoutMippedShader;
            case CUTOUT ->                          (MinecraftShader) GameRenderer::getRendertypeCutoutShader;
            case TRANSLUCENT ->                     (MinecraftShader) GameRenderer::getRendertypeTranslucentShader;
            case TRANSLUCENT_MOVING_BLOCK ->        (MinecraftShader) GameRenderer::getRendertypeTranslucentMovingBlockShader;
            case TRANSLUCENT_NO_CRUMBLING ->        (MinecraftShader) GameRenderer::getRendertypeTranslucentNoCrumblingShader;
            case ARMOR_CUTOUT_NO_CULL ->            (MinecraftShader) GameRenderer::getRendertypeArmorCutoutNoCullShader;
            case ENTITY_SOLID ->                    (MinecraftShader) GameRenderer::getRendertypeEntitySolidShader;
            case ENTITY_CUTOUT ->                   (MinecraftShader) GameRenderer::getRendertypeEntityCutoutShader;
            case ENTITY_CUTOUT_NO_CULL ->           (MinecraftShader) GameRenderer::getRendertypeEntityCutoutNoCullShader;
            case ENTITY_CUTOUT_NO_CULL_Z_OFFSET ->  (MinecraftShader) GameRenderer::getRendertypeEntityCutoutNoCullZOffsetShader;
            case ITEM_ENTITY_TRANSLUCENT_CULL ->    (MinecraftShader) GameRenderer::getRendertypeItemEntityTranslucentCullShader;
            case ENTITY_TRANSLUCENT_CULL ->         (MinecraftShader) GameRenderer::getRendertypeEntityTranslucentCullShader;
            case ENTITY_TRANSLUCENT ->              (MinecraftShader) GameRenderer::getRendertypeEntityTranslucentShader;
            case ENTITY_TRANSLUCENT_EMISSIVE ->     (MinecraftShader) GameRenderer::getRendertypeEntityTranslucentEmissiveShader;
            case ENTITY_SMOOTH_CUTOUT ->            (MinecraftShader) GameRenderer::getRendertypeEntitySmoothCutoutShader;
            case BEACON_BEAM ->                     (MinecraftShader) GameRenderer::getRendertypeBeaconBeamShader;
            case ENTITY_DECAL ->                    (MinecraftShader) GameRenderer::getRendertypeEntityDecalShader;
            case ENTITY_NO_OUTLINE ->               (MinecraftShader) GameRenderer::getRendertypeEntityNoOutlineShader;
            case ENTITY_SHADOW ->                   (MinecraftShader) GameRenderer::getRendertypeEntityShadowShader;
            case ENTITY_ALPHA ->                    (MinecraftShader) GameRenderer::getRendertypeEntityAlphaShader;
            case EYES ->                            (MinecraftShader) GameRenderer::getRendertypeEyesShader;
            case ENERGY_SWIRL ->                    (MinecraftShader) GameRenderer::getRendertypeEnergySwirlShader;
            case LEASH ->                           (MinecraftShader) GameRenderer::getRendertypeLeashShader;
            case WATER_MASK ->                      (MinecraftShader) GameRenderer::getRendertypeWaterMaskShader;
            case OUTLINE ->                         (MinecraftShader) GameRenderer::getRendertypeOutlineShader;
            case ARMOR_GLINT ->                     (MinecraftShader) GameRenderer::getRendertypeArmorGlintShader;
            case ARMOR_ENTITY_GLINT ->              (MinecraftShader) GameRenderer::getRendertypeArmorEntityGlintShader;
            case GLINT_TRANSLUCENT ->               (MinecraftShader) GameRenderer::getRendertypeGlintTranslucentShader;
            case GLINT ->                           (MinecraftShader) GameRenderer::getRendertypeGlintShader;
            case GLINT_DIRECT ->                    (MinecraftShader) GameRenderer::getRendertypeGlintDirectShader;
            case ENTITY_GLINT ->                    (MinecraftShader) GameRenderer::getRendertypeEntityGlintShader;
            case ENTITY_GLINT_DIRECT ->             (MinecraftShader) GameRenderer::getRendertypeEntityGlintDirectShader;
            case CRUMBLING ->                       (MinecraftShader) GameRenderer::getRendertypeCrumblingShader;
            case TEXT ->                            (MinecraftShader) GameRenderer::getRendertypeTextShader;
            case TEXT_BACKGROUND ->                 (MinecraftShader) GameRenderer::getRendertypeTextBackgroundShader;
            case TEXT_INTENSITY ->                  (MinecraftShader) GameRenderer::getRendertypeTextIntensityShader;
            case TEXT_SEE_THROUGH ->                (MinecraftShader) GameRenderer::getRendertypeTextSeeThroughShader;
            case TEXT_BACKGROUND_SEE_THROUGH ->     (MinecraftShader) GameRenderer::getRendertypeTextBackgroundSeeThroughShader;
            case TEXT_INTENSITY_SEE_THROUGH ->      (MinecraftShader) GameRenderer::getRendertypeTextIntensitySeeThroughShader;
            case LIGHTNING ->                       (MinecraftShader) GameRenderer::getRendertypeLightningShader;
            case TRIPWIRE ->                        (MinecraftShader) GameRenderer::getRendertypeTripwireShader;
            case END_PORTAL ->                      (MinecraftShader) GameRenderer::getRendertypeEndPortalShader;
            case END_GATEWAY ->                     (MinecraftShader) GameRenderer::getRendertypeEndGatewayShader;
            case LINES ->                           (MinecraftShader) GameRenderer::getRendertypeLinesShader;
            case GUI ->                             (MinecraftShader) GameRenderer::getRendertypeGuiShader;
            case GUI_OVERLAY ->                     (MinecraftShader) GameRenderer::getRendertypeGuiOverlayShader;
            case GUI_TEXT_HIGHLIGHT ->              (MinecraftShader) GameRenderer::getRendertypeGuiTextHighlightShader;
            case GUI_GHOST_RECIPE_OVERLAY ->        (MinecraftShader) GameRenderer::getRendertypeGuiGhostRecipeOverlayShader;
        };
    }

    @Override
    public VertexFormat getVertexFormat(VertexFormats formats) {
        return switch (formats) {
            case BLIT_SCREEN ->                 () -> DefaultVertexFormat.BLIT_SCREEN;
            case BLOCK ->                       () -> DefaultVertexFormat.BLOCK;
            case NEW_ENTITY ->                  () -> DefaultVertexFormat.NEW_ENTITY;
            case PARTICLE ->                    () -> DefaultVertexFormat.PARTICLE;
            case POSITION ->                    () -> DefaultVertexFormat.POSITION;
            case POSITION_COLOR ->              () -> DefaultVertexFormat.POSITION_COLOR;
            case POSITION_COLOR_NORMAL ->       () -> DefaultVertexFormat.POSITION_COLOR_NORMAL;
            case POSITION_COLOR_LIGHTMAP ->     () -> DefaultVertexFormat.POSITION_COLOR_LIGHTMAP;
            case POSITION_TEX ->                () -> DefaultVertexFormat.POSITION_TEX;
            case POSITION_COLOR_TEX ->          () -> DefaultVertexFormat.POSITION_COLOR_TEX;
            case POSITION_TEX_COLOR ->          () -> DefaultVertexFormat.POSITION_TEX_COLOR;
            case POSITION_COLOR_TEX_LIGHTMAP -> () -> DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP;
            case POSITION_TEX_LIGHTMAP_COLOR -> () -> DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR;
            case POSITION_TEX_COLOR_NORMAL ->   () -> DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL;
        };
    }

    @Override
    public VertexFormat.Mode getVertexFormatMode(VertexFormats.Modes modes) {
        return switch (modes) {
            case LINES ->               () -> com.mojang.blaze3d.vertex.VertexFormat.Mode.LINES;
            case LINE_STRIP ->          () -> com.mojang.blaze3d.vertex.VertexFormat.Mode.LINE_STRIP;
            case DEBUG_LINES ->         () -> com.mojang.blaze3d.vertex.VertexFormat.Mode.DEBUG_LINES;
            case DEBUG_LINE_STRIP ->    () -> com.mojang.blaze3d.vertex.VertexFormat.Mode.DEBUG_LINE_STRIP;
            case TRIANGLES ->           () -> com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLES;
            case TRIANGLE_STRIP ->      () -> com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLE_STRIP;
            case TRIANGLE_FAN ->        () -> com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLE_FAN;
            case QUADS ->               () -> com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;
        };
    }

    @Override
    public Resource getBlockAtlasResource() {
        return new MinecraftResource(InventoryMenu.BLOCK_ATLAS);
    }

    @Override
    public RenderLayer getGuiRenderLayer() {
        return RenderType::gui;
    }

    @Override
    public RenderLayer getGuiOverlayRenderLayer() {
        return RenderType::guiOverlay;
    }

    @Override
    public RenderLayer getGuiTextHighlightRenderLayer() {
        return RenderType::guiTextHighlight;
    }

}
