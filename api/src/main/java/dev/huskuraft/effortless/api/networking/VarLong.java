package dev.huskuraft.effortless.api.networking;

public class VarLong {
    private static final int MAX_VARLONG_SIZE = 10;
    private static final int DATA_BITS_MASK = 127;
    private static final int CONTINUATION_BIT_MASK = 128;
    private static final int DATA_BITS_PER_BYTE = 7;

    public static int getByteSize(long pData) {
        for (int i = 1; i < 10; ++i) {
            if ((pData & -1L << i * 7) == 0L) {
                return i;
            }
        }

        return 10;
    }

    public static boolean hasContinuationBit(byte pData) {
        return (pData & 128) == 128;
    }

    public static long read(NetByteBuf byteBuf) {
        long i = 0L;
        int j = 0;

        byte b0;
        do {
            b0 = byteBuf.readByte();
            i |= (long) (b0 & 127) << j++ * 7;
            if (j > 10) {
                throw new RuntimeException("VarLong too big");
            }
        } while (hasContinuationBit(b0));

        return i;
    }

    public static NetByteBuf write(NetByteBuf byteBuf, long pValue) {
        while ((pValue & -128L) != 0L) {
            byteBuf.writeByte((byte) ((int) (pValue & 127L) | 128));
            pValue >>>= 7;
        }

        byteBuf.writeByte((byte) pValue);
        return byteBuf;
    }
}
