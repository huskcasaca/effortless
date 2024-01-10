package dev.huskuraft.effortless.api.renderer;

public enum VertexModes implements VertexMode {
    LINES,
    LINE_STRIP,
    DEBUG_LINES,
    DEBUG_LINE_STRIP,
    TRIANGLES,
    TRIANGLE_STRIP,
    TRIANGLE_FAN,
    QUADS,
    ;

    @Override
    public Object value() {
        return RenderFactory.INSTANCE.getVertexMode(this).value();
    }
}
