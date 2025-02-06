package xaero.pac.common.server.io.serialization.data;

import xaero.pac.common.server.io.ObjectManagerIOManager;
import xaero.pac.common.server.io.ObjectManagerIOObject;

public abstract class SnapshotConverter
<
	S,
	I,
	T extends ObjectManagerIOObject,
	M extends ObjectManagerIOManager<T, M>
>
{

	public abstract S convert(T object);
	public abstract T convert(I id, M manager, S data);

}
