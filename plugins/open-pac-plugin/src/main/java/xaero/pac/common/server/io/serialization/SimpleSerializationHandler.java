package xaero.pac.common.server.io.serialization;

import xaero.pac.common.server.io.ObjectManagerIOManager;
import xaero.pac.common.server.io.ObjectManagerIOObject;

public class SimpleSerializationHandler
<
S,
I,
T extends ObjectManagerIOObject,
M extends ObjectManagerIOManager<T, M>
> extends SerializationHandler<S, I, T, M> {

	private final SimpleSerializer<S, I, T, M> serializer;

	public SimpleSerializationHandler(SimpleSerializer<S, I, T, M> serializer) {
		super();
		this.serializer = serializer;
	}

	@Override
	public S serialize(T object) {
		return serializer.serialize(object);
	}

	@Override
	public T deserialize(I id, M manager, S serializedData) {
		return serializer.deserialize(id, manager, serializedData);
	}



}
