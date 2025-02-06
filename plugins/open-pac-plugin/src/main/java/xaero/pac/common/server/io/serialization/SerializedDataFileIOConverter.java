package xaero.pac.common.server.io.serialization;

public abstract class SerializedDataFileIOConverter
<
	S,
	FC,
	I
> {

	public abstract S convert(I id, FC fileContent);
	public abstract FC convert(S data);

}
