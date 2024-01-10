package dev.huskuraft.effortless.api.renderer;

public enum Shaders implements Shader {
    GUI,
    ;

    @Override
    public Object value() {
        return RenderFactory.INSTANCE.getShader(this).value();
    }
}
