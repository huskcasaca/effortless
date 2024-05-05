package dev.huskuraft.effortless.api.networking;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBufUtil;

public class Utf8String {
    public static String read(NetByteBuf byteBuf, int maxLength) {
        int i = ByteBufUtil.utf8MaxBytes(maxLength);
        int j = VarInt.read(byteBuf);
        if (j > i) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + i + ")");
        } else if (j < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            int k = byteBuf.readableBytes();
            if (j > k) {
                throw new DecoderException("Not enough bytes in buffer, expected " + j + ", but got " + k);
            } else {
                String s = byteBuf.toString(byteBuf.readerIndex(), j, StandardCharsets.UTF_8);
                byteBuf.readerIndex(byteBuf.readerIndex() + j);
                if (s.length() > maxLength) {
                    throw new DecoderException("The received string length is longer than maximum allowed (" + s.length() + " > " + maxLength + ")");
                } else {
                    return s;
                }
            }
        }
    }

    public static void write(NetByteBuf byteBuf, CharSequence string, int maxLength) {
        if (string.length() > maxLength) {
            throw new EncoderException("String too big (was " + string.length() + " characters, max " + maxLength + ")");
        } else {
            int i = ByteBufUtil.utf8MaxBytes(string);
            var bytebuf = byteBuf.alloc().buffer(i);

            try {
                int j = ByteBufUtil.writeUtf8(bytebuf, string);
                int k = ByteBufUtil.utf8MaxBytes(maxLength);
                if (j > k) {
                    throw new EncoderException("String too big (was " + j + " bytes encoded, max " + k + ")");
                }

                VarInt.write(byteBuf, j);
                byteBuf.writeBytes(bytebuf);
            } finally {
                bytebuf.release();
            }

        }
    }
}
