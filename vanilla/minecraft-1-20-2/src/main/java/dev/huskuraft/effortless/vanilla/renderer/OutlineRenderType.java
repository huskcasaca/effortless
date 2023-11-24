package dev.huskuraft.effortless.vanilla.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import static org.lwjgl.opengl.GL11.*;

// FIXME: 21/10/23 port
public class OutlineRenderType extends RenderType {

    public static final ResourceLocation BLANK_TEXTURE_LOCATION = new ResourceLocation("effortless", "textures/misc/blank.png");
    public static final ResourceLocation CHECKERED_TEXTURE_LOCATION = new ResourceLocation("effortless", "textures/misc/checkerboard.png");
    public static final ResourceLocation CHECKERED_HIGHLIGHT_TEXTURE_LOCATION = new ResourceLocation("effortless", "textures/misc/checkerboard_highlight.png");

    public static final ResourceLocation CHECKERED_THIN_TEXTURE_LOCATION = new ResourceLocation("effortless", "textures/misc/checkerboard_thin_64.png");
    public static final ResourceLocation CHECKERED_CUTOUT_TEXTURE_LOCATION = new ResourceLocation("effortless", "textures/misc/checkerboard_cutout.png");
    public static final ResourceLocation SELECTION_TEXTURE_LOCATION = new ResourceLocation("effortless", "textures/misc/selection_64.png");

    public static final DepthTestStateShard NEVER_DEPTH_TEST = new DepthTestStateShard("never", GL_NEVER);
    public static final DepthTestStateShard LESS_DEPTH_TEST = new DepthTestStateShard("less", GL_LESS);
    public static final DepthTestStateShard EQUAL_DEPTH_TEST = new DepthTestStateShard("equal", GL_EQUAL);
    public static final DepthTestStateShard LEQUAL_DEPTH_TEST = new DepthTestStateShard("lequal", GL_LEQUAL);
    public static final DepthTestStateShard GREATER_DEPTH_TEST = new DepthTestStateShard("greater", GL_GREATER);
    public static final DepthTestStateShard NOTEQUAL_DEPTH_TEST = new DepthTestStateShard("notequal", GL_NOTEQUAL);
    public static final DepthTestStateShard GEQUAL_DEPTH_TEST = new DepthTestStateShard("gequal", GL_GEQUAL);
    public static final DepthTestStateShard ALWAYS_DEPTH_TEST = new DepthTestStateShard("always", GL_ALWAYS);

    private static final RenderType OUTLINE_SOLID = create(createLayerName("outline_solid"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false,
            false, CompositeState.builder()
                    .setShaderState(RENDERTYPE_ENTITY_SOLID_SHADER)
                    .setTextureState(new TextureStateShard(BLANK_TEXTURE_LOCATION, false, false))
                    .setCullState(CULL)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .createCompositeState(true));
    private static final RenderType GLOWING_SOLID_DEFAULT = glowingSolid(InventoryMenu.BLOCK_ATLAS);
    private static final RenderType ADDITIVE = create(createLayerName("additive"), DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS, 256, true, true, CompositeState.builder()
                    .setShaderState(RENDERTYPE_SOLID_SHADER)
                    .setTextureState(new TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false))
                    .setTransparencyState(ADDITIVE_TRANSPARENCY)
                    .setCullState(NO_CULL)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .createCompositeState(true));
    private static final RenderType GLOWING_TRANSLUCENT_DEFAULT = glowingTranslucent(InventoryMenu.BLOCK_ATLAS);
    private static final RenderType ITEM_PARTIAL_SOLID =
            create(createLayerName("item_partial_solid"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true,
                    false, CompositeState.builder()
                            .setShaderState(RENDERTYPE_ENTITY_SOLID_SHADER)
                            .setTextureState(BLOCK_SHEET)
                            .setCullState(CULL)
                            .setLightmapState(LIGHTMAP)
                            .setOverlayState(OVERLAY)
                            .createCompositeState(true));
    private static final RenderType ITEM_PARTIAL_TRANSLUCENT = create(createLayerName("item_partial_translucent"),
            DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, CompositeState.builder()
                    .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER)
                    .setTextureState(BLOCK_SHEET)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .createCompositeState(true));
    private static final RenderType FLUID = create(createLayerName("fluid"),
            DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder()
                    .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .createCompositeState(true));

    public OutlineRenderType(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
    }

    public static RenderType outlineSolid() {
        return OUTLINE_SOLID;
    }

    public static RenderType outlineSolid(boolean overlap) {
        return create(createLayerName("outline_solid" + (overlap ? "_overlap" : "")), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false,
                false, CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_SOLID_SHADER)
                        .setTransparencyState(NO_TRANSPARENCY)
                        .setDepthTestState(overlap ? NOTEQUAL_DEPTH_TEST : NEVER_DEPTH_TEST)
                        .setTextureState(new TextureStateShard(BLANK_TEXTURE_LOCATION, false, false))
                        .setCullState(CULL)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .createCompositeState(true));
    }

    public static RenderType outlineTranslucent(ResourceLocation texture, boolean cull) {
        return create(createLayerName("outline_translucent" + (cull ? "_cull" : "")),
                DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder()
                        .setShaderState(cull ? RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER : RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(cull ? CULL : NO_CULL)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .setWriteMaskState(COLOR_WRITE)
                        .createCompositeState(false));
    }

    public static RenderType glowingSolid(ResourceLocation texture) {
        return create(createLayerName("glowing_solid"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256,
                true, false, CompositeState.builder()
                        //				.setShaderState(GLOWING_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setCullState(CULL)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .createCompositeState(true));
    }

    public static RenderType glowingSolid() {
        return GLOWING_SOLID_DEFAULT;
    }

    public static RenderType glowingTranslucent(ResourceLocation texture) {
        return create(createLayerName("glowing_translucent"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS,
                256, true, true, CompositeState.builder()
                        //				.setShaderState(GLOWING_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .createCompositeState(true));
    }

    public static RenderType additive() {
        return ADDITIVE;
    }

    public static RenderType glowingTranslucent() {
        return GLOWING_TRANSLUCENT_DEFAULT;
    }

    public static RenderType itemPartialSolid() {
        return ITEM_PARTIAL_SOLID;
    }

    public static RenderType itemPartialTranslucent() {
        return ITEM_PARTIAL_TRANSLUCENT;
    }

    public static RenderType fluid() {
        return FLUID;
    }

    private static String createLayerName(String name) {
        return new ResourceLocation("effortless", name).toString();
    }

}
