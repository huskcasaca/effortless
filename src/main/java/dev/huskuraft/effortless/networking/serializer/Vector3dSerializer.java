package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.universal.api.math.Vector3d;
import dev.huskuraft.universal.api.networking.NetByteBuf;
import dev.huskuraft.universal.api.networking.NetByteBufSerializer;

public class Vector3dSerializer implements NetByteBufSerializer<Vector3d> {

    @Override
    public Vector3d read(NetByteBuf byteBuf) {
        return Vector3d.at(
                byteBuf.readDouble(),
                byteBuf.readDouble(),
                byteBuf.readDouble()
        );
    }


    @Override
    public void write(NetByteBuf byteBuf, Vector3d vector) {
        byteBuf.writeDouble(vector.x());
        byteBuf.writeDouble(vector.y());
        byteBuf.writeDouble(vector.z());
    }

}
