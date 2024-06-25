package dev.huskuraft.effortless.api.networking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.platform.PlatformLoader;

public abstract class NetworkChannel<P extends PacketListener> implements PacketChannel {

    private final ResourceLocation channelId;
    private final Side side;
    private final Map<UUID, Consumer<? extends ResponsiblePacket<?>>> responseMap = Collections.synchronizedMap(new HashMap<>());
    private PacketSet<P> packetSet = new PacketSet<>();
    private Networking networking;

    protected NetworkChannel(ResourceLocation channelId, Side side) {
        this.channelId = channelId;
        this.side = side;
        this.networking = PlatformLoader.getSingleton();
    }

    public Networking getPlatformChannel() {
        return networking;
    }

    @Override
    public void sendPacket(Packet packet, Player player) {
        sendBuffer(createBuffer(packet), player);
    }

    @Override
    public void sendBuffer(NetByteBuf byteBuf, Player player) {
        switch (side) {
            case CLIENT -> getPlatformChannel().sendToServer(getChannelId(), byteBuf, player);
            case SERVER -> getPlatformChannel().sendToClient(getChannelId(), byteBuf, player);
        }
    }

    public <T extends ResponsiblePacket<?>> void sendPacket(T packet, Consumer<T> callback) {
        responseMap.put(packet.responseId(), callback);
        sendPacket(packet);
    }

    @Override
    public abstract void receivePacket(Packet packet, Player player);

    @Override
    public void receiveBuffer(NetByteBuf byteBuf, Player player) {
        var packet = (Packet<P>) null;
        try {
            packet = createPacket(byteBuf);
            Objects.requireNonNull(packet);
        } catch (Exception e) {
            throw new RuntimeException("Could not create packet in channel '" + channelId + "'", e);
        }
        try {
            var packet1 = packet;
            receivePacket(packet1, player);
            if (packet instanceof ResponsiblePacket<P> responsiblePacket) {
                var callback = responseMap.remove(responsiblePacket.responseId());
                if (callback != null) {
                    ((Consumer<ResponsiblePacket<P>>) callback).accept(responsiblePacket);
                }
            }
        } catch (RejectedExecutionException | ClassCastException ignored) {
        }
    }

    @SuppressWarnings("unchecked")
    public Packet<P> createPacket(NetByteBuf byteBuf) {
        return (Packet<P>) packetSet.createPacket(byteBuf);
    }

    public NetByteBuf createBuffer(Packet<P> packet) {
        return packetSet.createBuffer(packet);
    }

    public <T extends Packet<P>> void registerPacket(Class<T> clazz, NetByteBufSerializer<T> serializer) {
        try {
            packetSet.addPacket(clazz, serializer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unregisterPackets() {
        this.packetSet = new PacketSet<>();
    }

    public abstract int getCompatibilityVersion();

    public String getCompatibilityVersionStr() {
        return Integer.toString(getCompatibilityVersion());
    }

    public final ResourceLocation getChannelId() {
        return channelId;
    }

    private class PacketSet<T extends PacketListener> {

        private final Map<Class<?>, Integer> classToId = new LinkedHashMap<>();
        private final List<NetByteBufSerializer<? extends Packet<T>>> idToDeserializer = new ArrayList<>();

        public <P extends Packet<T>> PacketSet<T> addPacket(Class<P> clazz, NetByteBufSerializer<P> serializer) {

            if (classToId.containsKey(clazz)) {
                throw new IllegalArgumentException("Packet " + clazz + " is already registered to ID " + classToId.get(clazz));
            } else {
                classToId.put(clazz, idToDeserializer.size());
                idToDeserializer.add(serializer);
                return this;
            }
        }

        @Nullable
        public Integer getId(Class<?> clazz) {
            return classToId.getOrDefault(clazz, null);
        }

        public NetByteBuf createBuffer(Packet<T> packet) {
            var id = getId(packet.getClass());
            if (id == null) {
                throw new IllegalArgumentException("Packet " + packet.getClass() + " is not registered");
            }
            var buffer = NetByteBuf.newBuffer();
            var serializer = (NetByteBufSerializer<Packet<T>>) idToDeserializer.get(getId(packet.getClass()));
            buffer.writeInt(id);
            serializer.write(buffer, packet);
            return buffer;
        }

        @Nullable
        public Packet<?> createPacket(NetByteBuf byteBuf) {
            var id = byteBuf.readInt();
            var serializer = idToDeserializer.get(id);
            if (serializer != null) return serializer.read(byteBuf);
            return null;
        }
    }

}
