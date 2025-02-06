package xaero.pac.common.server.io.serialization.human;

import xaero.pac.common.server.io.serialization.SerializedDataFileIO;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class HumanReadableSerializedDataFileIO
<
	S,
	I
>
	implements SerializedDataFileIO<S, I> {

	private final HumanReadableStringConverter<S,I> converter;

	public HumanReadableSerializedDataFileIO(HumanReadableStringConverter<S,I> converter) {
		this.converter = converter;
	}

	@Override
	public S read(I id, BufferedInputStream fileInput) throws IOException {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput, "UTF8"))){
			StringBuilder stringBuilder = new StringBuilder();
			reader.lines().forEach(line -> {stringBuilder.append(line); stringBuilder.append('\n');});
			return converter.convert(id, stringBuilder.toString());
		}
	}

	@Override
	public void write(BufferedOutputStream fileOutput, S serializedData) throws IOException {
		try(OutputStreamWriter writer = new OutputStreamWriter(fileOutput, StandardCharsets.UTF_8);){
			writer.write(converter.convert(serializedData));
			writer.close();
		}
	}

}
