package xaero.pac.common.server.io.serialization.nbt;

import net.minecraft.nbt.NbtIo;
import xaero.pac.common.server.io.serialization.SerializedDataFileIO;

import java.io.*;

public class NBTSerializedDataFileIO
<
	S,
	I
>
implements SerializedDataFileIO<S, I> {

	private final NBTConverter<S, I> converter;

	public NBTSerializedDataFileIO(NBTConverter<S, I> converter){
		this.converter = converter;
	}

	@Override
	public S read(I id, BufferedInputStream fileInput) throws IOException {
		try(BufferedInputStream bufferedStream = new BufferedInputStream(fileInput); DataInputStream dataInput = new DataInputStream(bufferedStream)){
			return converter.convert(id, NbtIo.read(dataInput));
		}
	}

	@Override
	public void write(BufferedOutputStream fileOutput, S serializedData) throws IOException {
		try(BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput); DataOutputStream dataOutput = new DataOutputStream(bufferedOutput)){
			NbtIo.write(converter.convert(serializedData), dataOutput);
		}
	}

}
