package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.api.renderer.programs.RenderState;
import org.lwjgl.opengl.GL11;

public abstract class RenderLayers {

    protected static final Resource BLOCK_ATLAS_LOCATION = RenderFactory.INSTANCE.getBlockAtlasResource();

    protected static final RenderState.TextureState NO_TEXTURE = RenderState.TextureState.create("no_texture", null);
    protected static final RenderState.TextureState BLOCK_SHEET_MIPPED_TEXTURE = RenderState.TextureState.create("block_sheet_mipped", new RenderState.TextureState.Texture(BLOCK_ATLAS_LOCATION, false, true));
    protected static final RenderState.TextureState BLOCK_SHEET_TEXTURE = RenderState.TextureState.create("block_sheet_mipped", new RenderState.TextureState.Texture(BLOCK_ATLAS_LOCATION, false, false));

    protected static final RenderState.TransparencyState NO_TRANSPARENCY = RenderState.TransparencyState.create("no_transparency", RenderState.TransparencyState.Type.NO);
    protected static final RenderState.TransparencyState ADDITIVE_TRANSPARENCY = RenderState.TransparencyState.create("additive_transparency", RenderState.TransparencyState.Type.ADDITIVE);
    protected static final RenderState.TransparencyState LIGHTNING_TRANSPARENCY = RenderState.TransparencyState.create("lightning_transparency", RenderState.TransparencyState.Type.LIGHTNING);
    protected static final RenderState.TransparencyState GLINT_TRANSPARENCY = RenderState.TransparencyState.create("glint_transparency", RenderState.TransparencyState.Type.GLINT);
    protected static final RenderState.TransparencyState CRUMBLING_TRANSPARENCY = RenderState.TransparencyState.create("crumbling_transparency", RenderState.TransparencyState.Type.CRUMBLING);
    protected static final RenderState.TransparencyState TRANSLUCENT_TRANSPARENCY = RenderState.TransparencyState.create("translucent_transparency", RenderState.TransparencyState.Type.TRANSLUCENT);

    protected static final RenderState.ShaderState NO_SHADER_STATE = RenderState.ShaderState.create("none", null);
    protected static final RenderState.ShaderState POSITION_COLOR_LIGHTMAP_SHADER_STATE = RenderState.ShaderState.create("position_color_lightmap", Shaders.POSITION_COLOR_LIGHTMAP);
    protected static final RenderState.ShaderState POSITION_SHADER_STATE = RenderState.ShaderState.create("position", Shaders.POSITION);
    protected static final RenderState.ShaderState POSITION_COLOR_TEX_SHADER_STATE = RenderState.ShaderState.create("position_color_tex", Shaders.POSITION_COLOR_TEX);
    protected static final RenderState.ShaderState POSITION_TEX_SHADER_STATE = RenderState.ShaderState.create("position_tex", Shaders.POSITION_TEX);
    protected static final RenderState.ShaderState POSITION_COLOR_TEX_LIGHTMAP_SHADER_STATE = RenderState.ShaderState.create("position_color_tex_lightmap", Shaders.POSITION_COLOR_TEX_LIGHTMAP);
    protected static final RenderState.ShaderState POSITION_COLOR_SHADER_STATE = RenderState.ShaderState.create("position_color", Shaders.POSITION_COLOR);
    protected static final RenderState.ShaderState SOLID_SHADER_STATE = RenderState.ShaderState.create("solid", Shaders.SOLID);
    protected static final RenderState.ShaderState CUTOUT_MIPPED_SHADER_STATE = RenderState.ShaderState.create("cutout_mipped", Shaders.CUTOUT_MIPPED);
    protected static final RenderState.ShaderState CUTOUT_SHADER_STATE = RenderState.ShaderState.create("cutout", Shaders.CUTOUT);
    protected static final RenderState.ShaderState TRANSLUCENT_SHADER_STATE = RenderState.ShaderState.create("translucent", Shaders.TRANSLUCENT);
    protected static final RenderState.ShaderState TRANSLUCENT_MOVING_BLOCK_SHADER_STATE = RenderState.ShaderState.create("translucent_moving_block", Shaders.TRANSLUCENT_MOVING_BLOCK);
    protected static final RenderState.ShaderState TRANSLUCENT_NO_CRUMBLING_SHADER_STATE = RenderState.ShaderState.create("translucent_no_crumbling", Shaders.TRANSLUCENT_NO_CRUMBLING);
    protected static final RenderState.ShaderState ARMOR_CUTOUT_NO_CULL_SHADER_STATE = RenderState.ShaderState.create("armor_cutout_no_cull", Shaders.ARMOR_CUTOUT_NO_CULL);
    protected static final RenderState.ShaderState ENTITY_SOLID_SHADER_STATE = RenderState.ShaderState.create("entity_solid", Shaders.ENTITY_SOLID);
    protected static final RenderState.ShaderState ENTITY_CUTOUT_SHADER_STATE = RenderState.ShaderState.create("entity_cutout", Shaders.ENTITY_CUTOUT);
    protected static final RenderState.ShaderState ENTITY_CUTOUT_NO_CULL_SHADER_STATE = RenderState.ShaderState.create("entity_cutout_no_cull", Shaders.ENTITY_CUTOUT_NO_CULL);
    protected static final RenderState.ShaderState ENTITY_CUTOUT_NO_CULL_Z_OFFSET_SHADER_STATE = RenderState.ShaderState.create("entity_cutout_no_cull_z_offset", Shaders.ENTITY_CUTOUT_NO_CULL_Z_OFFSET);
    protected static final RenderState.ShaderState ITEM_ENTITY_TRANSLUCENT_CULL_SHADER_STATE = RenderState.ShaderState.create("item_entity_translucent_cull", Shaders.ITEM_ENTITY_TRANSLUCENT_CULL);
    protected static final RenderState.ShaderState ENTITY_TRANSLUCENT_CULL_SHADER_STATE = RenderState.ShaderState.create("entity_translucent_cull", Shaders.ENTITY_TRANSLUCENT_CULL);
    protected static final RenderState.ShaderState ENTITY_TRANSLUCENT_SHADER_STATE = RenderState.ShaderState.create("entity_translucent", Shaders.ENTITY_TRANSLUCENT);
    protected static final RenderState.ShaderState ENTITY_TRANSLUCENT_EMISSIVE_SHADER_STATE = RenderState.ShaderState.create("entity_translucent_emissive", Shaders.ENTITY_TRANSLUCENT_EMISSIVE);
    protected static final RenderState.ShaderState ENTITY_SMOOTH_CUTOUT_SHADER_STATE = RenderState.ShaderState.create("entity_smooth_cutout", Shaders.ENTITY_SMOOTH_CUTOUT);
    protected static final RenderState.ShaderState BEACON_BEAM_SHADER_STATE = RenderState.ShaderState.create("beacon_beam", Shaders.BEACON_BEAM);
    protected static final RenderState.ShaderState ENTITY_DECAL_SHADER_STATE = RenderState.ShaderState.create("entity_decal", Shaders.ENTITY_DECAL);
    protected static final RenderState.ShaderState ENTITY_NO_OUTLINE_SHADER_STATE = RenderState.ShaderState.create("entity_no_outline", Shaders.ENTITY_NO_OUTLINE);
    protected static final RenderState.ShaderState ENTITY_SHADOW_SHADER_STATE = RenderState.ShaderState.create("entity_shadow", Shaders.ENTITY_SHADOW);
    protected static final RenderState.ShaderState ENTITY_ALPHA_SHADER_STATE = RenderState.ShaderState.create("entity_alpha", Shaders.ENTITY_ALPHA);
    protected static final RenderState.ShaderState EYES_SHADER_STATE = RenderState.ShaderState.create("eyes", Shaders.EYES);
    protected static final RenderState.ShaderState ENERGY_SWIRL_SHADER_STATE = RenderState.ShaderState.create("energy_swirl", Shaders.ENERGY_SWIRL);
    protected static final RenderState.ShaderState LEASH_SHADER_STATE = RenderState.ShaderState.create("leash", Shaders.LEASH);
    protected static final RenderState.ShaderState WATER_MASK_SHADER_STATE = RenderState.ShaderState.create("water_mask", Shaders.WATER_MASK);
    protected static final RenderState.ShaderState OUTLINE_SHADER_STATE = RenderState.ShaderState.create("outline", Shaders.OUTLINE);
    protected static final RenderState.ShaderState ARMOR_GLINT_SHADER_STATE = RenderState.ShaderState.create("armor_glint", Shaders.ARMOR_GLINT);
    protected static final RenderState.ShaderState ARMOR_ENTITY_GLINT_SHADER_STATE = RenderState.ShaderState.create("armor_entity_glint", Shaders.ARMOR_ENTITY_GLINT);
    protected static final RenderState.ShaderState GLINT_TRANSLUCENT_SHADER_STATE = RenderState.ShaderState.create("glint_translucent", Shaders.GLINT_TRANSLUCENT);
    protected static final RenderState.ShaderState GLINT_SHADER_STATE = RenderState.ShaderState.create("glint", Shaders.GLINT);
    protected static final RenderState.ShaderState GLINT_DIRECT_SHADER_STATE = RenderState.ShaderState.create("glint_direct", Shaders.GLINT_DIRECT);
    protected static final RenderState.ShaderState ENTITY_GLINT_SHADER_STATE = RenderState.ShaderState.create("entity_glint", Shaders.ENTITY_GLINT);
    protected static final RenderState.ShaderState ENTITY_GLINT_DIRECT_SHADER_STATE = RenderState.ShaderState.create("entity_glint_direct", Shaders.ENTITY_GLINT_DIRECT);
    protected static final RenderState.ShaderState CRUMBLING_SHADER_STATE = RenderState.ShaderState.create("crumbling", Shaders.CRUMBLING);
    protected static final RenderState.ShaderState TEXT_SHADER_STATE = RenderState.ShaderState.create("text", Shaders.TEXT);
    protected static final RenderState.ShaderState TEXT_BACKGROUND_SHADER_STATE = RenderState.ShaderState.create("text_background", Shaders.TEXT_BACKGROUND);
    protected static final RenderState.ShaderState TEXT_INTENSITY_SHADER_STATE = RenderState.ShaderState.create("text_intensity", Shaders.TEXT_INTENSITY);
    protected static final RenderState.ShaderState TEXT_SEE_THROUGH_SHADER_STATE = RenderState.ShaderState.create("text_see_through", Shaders.TEXT_SEE_THROUGH);
    protected static final RenderState.ShaderState TEXT_BACKGROUND_SEE_THROUGH_SHADER_STATE = RenderState.ShaderState.create("text_background_see_through", Shaders.TEXT_BACKGROUND_SEE_THROUGH);
    protected static final RenderState.ShaderState TEXT_INTENSITY_SEE_THROUGH_SHADER_STATE = RenderState.ShaderState.create("text_intensity_see_through", Shaders.TEXT_INTENSITY_SEE_THROUGH);
    protected static final RenderState.ShaderState LIGHTNING_SHADER_STATE = RenderState.ShaderState.create("lightning", Shaders.LIGHTNING);
    protected static final RenderState.ShaderState TRIPWIRE_SHADER_STATE = RenderState.ShaderState.create("tripwire", Shaders.TRIPWIRE);
    protected static final RenderState.ShaderState END_PORTAL_SHADER_STATE = RenderState.ShaderState.create("end_portal", Shaders.END_PORTAL);
    protected static final RenderState.ShaderState END_GATEWAY_SHADER_STATE = RenderState.ShaderState.create("end_gateway", Shaders.END_GATEWAY);
    protected static final RenderState.ShaderState LINES_SHADER_STATE = RenderState.ShaderState.create("lines", Shaders.LINES);
   public static final RenderState.ShaderState GUI_SHADER_STATE = RenderState.ShaderState.create("gui", Shaders.GUI);
   public static final RenderState.ShaderState GUI_OVERLAY_SHADER_STATE = RenderState.ShaderState.create("gui_overlay", Shaders.GUI_OVERLAY);
   public static final RenderState.ShaderState GUI_TEXT_HIGHLIGHT_SHADER_STATE = RenderState.ShaderState.create("gui_text_highlight", Shaders.GUI_TEXT_HIGHLIGHT);
   public static final RenderState.ShaderState GUI_GHOST_RECIPE_OVERLAY_SHADER_STATE = RenderState.ShaderState.create("gui_ghost_recipe_overlay", Shaders.GUI_GHOST_RECIPE_OVERLAY);

    protected static final RenderState.DepthTestState NEVER_DEPTH_TEST = RenderState.DepthTestState.create("never", GL11.GL_NEVER);
    protected static final RenderState.DepthTestState LESS_DEPTH_TEST = RenderState.DepthTestState.create("less", GL11.GL_LESS);
    protected static final RenderState.DepthTestState EQUAL_DEPTH_TEST = RenderState.DepthTestState.create("equal", GL11.GL_EQUAL);
    protected static final RenderState.DepthTestState LEQUAL_DEPTH_TEST = RenderState.DepthTestState.create("lequal", GL11.GL_LEQUAL);
    protected static final RenderState.DepthTestState GREATER_DEPTH_TEST = RenderState.DepthTestState.create("greater", GL11.GL_GREATER);
    protected static final RenderState.DepthTestState NOTEQUAL_DEPTH_TEST = RenderState.DepthTestState.create("notequal", GL11.GL_NOTEQUAL);
    protected static final RenderState.DepthTestState GEQUAL_DEPTH_TEST = RenderState.DepthTestState.create("gequal", GL11.GL_GEQUAL);
    protected static final RenderState.DepthTestState ALWAYS_DEPTH_TEST = RenderState.DepthTestState.create("always", GL11.GL_ALWAYS);

    protected static final RenderState.CullState CULL = RenderState.CullState.create("cull", true);
    protected static final RenderState.CullState NO_CULL = RenderState.CullState.create("no_cull", false);

    protected static final RenderState.LightmapState LIGHTMAP = RenderState.LightmapState.create("lightmap", true);
    protected static final RenderState.LightmapState NO_LIGHTMAP = RenderState.LightmapState.create("no_lightmap", false);

    protected static final RenderState.OverlayState OVERLAY = RenderState.OverlayState.create("overlay", true);
    protected static final RenderState.OverlayState NO_OVERLAY = RenderState.OverlayState.create("no_overlay", false);

    protected static final RenderState.LayeringState NO_LAYERING = RenderState.LayeringState.create("none", RenderState.LayeringState.Type.NO);
    protected static final RenderState.LayeringState POLYGON_OFFSET_LAYERING = RenderState.LayeringState.create("polygon_offset", RenderState.LayeringState.Type.POLYGON_OFFSET);
    protected static final RenderState.LayeringState VIEW_OFFSET_Z_LAYERING = RenderState.LayeringState.create("view_offset_z", RenderState.LayeringState.Type.VIEW_OFFSET_Z);

    protected static final RenderState.OutputState NO_TARGET = RenderState.OutputState.create("no_target", RenderState.OutputState.Target.NO);
    protected static final RenderState.OutputState OUTLINE_TARGET = RenderState.OutputState.create("outline_target", RenderState.OutputState.Target.OUTLINE);
    protected static final RenderState.OutputState TRANSLUCENT_TARGET = RenderState.OutputState.create("translucent_target", RenderState.OutputState.Target.TRANSLUCENT);
    protected static final RenderState.OutputState PARTICLES_TARGET = RenderState.OutputState.create("particles_target", RenderState.OutputState.Target.PARTICLES);
    protected static final RenderState.OutputState WEATHER_TARGET = RenderState.OutputState.create("weather_target", RenderState.OutputState.Target.WEATHER);
    protected static final RenderState.OutputState CLOUDS_TARGET = RenderState.OutputState.create("clouds_target", RenderState.OutputState.Target.CLOUDS);
    protected static final RenderState.OutputState ITEM_ENTITY_TARGET = RenderState.OutputState.create("item_entity_target", RenderState.OutputState.Target.ITEM_ENTITY);

    protected static final RenderState.TexturingState NO_TEXTURING = RenderState.TexturingState.create("none", () -> {}, () -> {});

    protected static final RenderState.WriteMaskState COLOR_WRITE = RenderState.WriteMaskState.create("color_write", true, false);
    protected static final RenderState.WriteMaskState DEPTH_WRITE = RenderState.WriteMaskState.create("depth_write", false, true);
    protected static final RenderState.WriteMaskState COLOR_DEPTH_WRITE = RenderState.WriteMaskState.create("color_depth_write", true, true);

    protected static final RenderState.LineState NO_WIDTH = RenderState.LineState.create("no_width", null);
    protected static final RenderState.LineState DEFAULT_WIDTH = RenderState.LineState.create("default_width", 1.0d);

    protected static final RenderState.ColorLogicState NO_COLOR_LOGIC = RenderState.ColorLogicState.create("none", RenderState.ColorLogicState.Op.NO_LOGIC);
    protected static final RenderState.ColorLogicState OR_REVERSE_COLOR_LOGIC = RenderState.ColorLogicState.create("or_reverse", RenderState.ColorLogicState.Op.OR_REVERSE_LOGIC);

    public static final RenderLayer GUI = RenderLayer.createComposite("gui", VertexFormats.POSITION_COLOR, VertexFormats.Modes.QUADS, 256, RenderState.builder().setShaderState(GUI_SHADER_STATE).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDepthTestState(LEQUAL_DEPTH_TEST).create(false));
    public static final RenderLayer GUI_OVERLAY = RenderLayer.createComposite("gui_text_highlight", VertexFormats.POSITION_COLOR, VertexFormats.Modes.QUADS, 256, RenderState.builder().setShaderState(GUI_TEXT_HIGHLIGHT_SHADER_STATE).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDepthTestState(NEVER_DEPTH_TEST).setColorLogicState(OR_REVERSE_COLOR_LOGIC).create(false));
    public static final RenderLayer GUI_TEXT_HIGHLIGHT = RenderLayer.createComposite("gui_overlay", VertexFormats.POSITION_COLOR, VertexFormats.Modes.QUADS, 256, RenderState.builder().setShaderState(GUI_OVERLAY_SHADER_STATE).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDepthTestState(NEVER_DEPTH_TEST).setWriteMaskState(COLOR_WRITE).create(false));
    public static final RenderLayer CUSTOM = RenderLayer.createComposite("custom", VertexFormats.BLOCK, VertexFormats.Modes.TRIANGLES, 0, true, true, RenderState.builder().setDepthTestState(EQUAL_DEPTH_TEST).create(false));

    public static RenderLayer texture(Resource resource, boolean blur, boolean mipmap) {
        return RenderLayer.createComposite("texture", VertexFormats.POSITION_TEX, VertexFormats.Modes.QUADS, 256, RenderState.builder().setTextureState(RenderState.TextureState.create("texture", new RenderState.TextureState.Texture(resource, blur, mipmap))).setShaderState(POSITION_TEX_SHADER_STATE).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDepthTestState(LEQUAL_DEPTH_TEST).create(false));
    }

}
