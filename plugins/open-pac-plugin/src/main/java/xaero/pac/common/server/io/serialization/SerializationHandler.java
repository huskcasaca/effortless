package xaero.pac.common.server.io.serialization;

import xaero.pac.common.server.io.ObjectManagerIOManager;
import xaero.pac.common.server.io.ObjectManagerIOObject;

public abstract class SerializationHandler
<
	S,
	I,
	T extends ObjectManagerIOObject,
	M extends ObjectManagerIOManager<T, M>
> {

	public abstract S serialize(T object);
	public abstract T deserialize(I id, M manager, S serializedData);

}
