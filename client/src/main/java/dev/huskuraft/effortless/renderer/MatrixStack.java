package dev.huskuraft.effortless.renderer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public interface MatrixStack {

    void push();

    void pop();

    Matrix last();

    void translate(float x, float y, float z);

    void scale(float x, float y, float z);

    void rotate(Quaternionf quaternion);

    void rotate(Quaternionf quaternion, float x, float y, float z);

    void multiply(Matrix4f matrix);

    void identity();

    interface Matrix {

        Matrix4f pose();

        Matrix3f normal();

    }

}
