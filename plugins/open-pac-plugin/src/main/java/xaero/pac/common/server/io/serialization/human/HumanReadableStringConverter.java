package xaero.pac.common.server.io.serialization.human;

import xaero.pac.common.server.io.serialization.SerializedDataFileIOConverter;

public abstract class HumanReadableStringConverter
<
	S,
	I
> extends SerializedDataFileIOConverter<S, String, I>{

	public abstract S convert(I id, String humanReadable);
	public abstract String convert(S data);

}
