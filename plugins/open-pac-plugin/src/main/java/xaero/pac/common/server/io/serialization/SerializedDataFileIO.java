package xaero.pac.common.server.io.serialization;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public interface SerializedDataFileIO
<
	S,
	I
> {

	public S read(I id, BufferedInputStream fileInput) throws IOException;
	public void write(BufferedOutputStream fileOutput, S serializedData) throws IOException;

}
