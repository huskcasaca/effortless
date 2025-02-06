package xaero.pac.common.server.io.serialization;

import xaero.pac.common.server.io.ObjectManagerIOManager;
import xaero.pac.common.server.io.ObjectManagerIOObject;

public interface SimpleSerializer
<
	S,
	I,
	T extends ObjectManagerIOObject,
	M extends ObjectManagerIOManager<T, M>
> {

	public S serialize(T object);
	public T deserialize(I id, M manager, S serializedData);

}
