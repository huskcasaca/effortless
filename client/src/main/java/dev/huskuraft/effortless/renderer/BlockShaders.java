package dev.huskuraft.effortless.renderer;

import dev.huskuraft.effortless.api.renderer.Shader;
import dev.huskuraft.effortless.api.renderer.VertexFormats;

public interface BlockShaders {

    Shader TINTED_OUTLINE = Shader.lazy("rendertype_tinted_solid", VertexFormats.BLOCK);

}
