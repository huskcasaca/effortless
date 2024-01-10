package dev.huskuraft.effortless.api.renderer.programs;

import dev.huskuraft.effortless.api.renderer.RenderFactory;
import org.lwjgl.opengl.GL11;

public interface DepthTestState extends RenderState {

    DepthTestState NEVER_DEPTH_TEST = create("never", GL11.GL_NEVER);
    DepthTestState LESS_DEPTH_TEST = create("less", GL11.GL_LESS);
    DepthTestState EQUAL_DEPTH_TEST = create("equal", GL11.GL_EQUAL);
    DepthTestState LEQUAL_DEPTH_TEST = create("lequal", GL11.GL_LEQUAL);
    DepthTestState GREATER_DEPTH_TEST = create("greater", GL11.GL_GREATER);
    DepthTestState NOTEQUAL_DEPTH_TEST = create("notequal", GL11.GL_NOTEQUAL);
    DepthTestState GEQUAL_DEPTH_TEST = create("gequal", GL11.GL_GEQUAL);
    DepthTestState ALWAYS_DEPTH_TEST = create("always", GL11.GL_ALWAYS);

    static DepthTestState create(String name, int function) {
        return RenderFactory.INSTANCE.createDepthTestState(name, function);
    }
}
