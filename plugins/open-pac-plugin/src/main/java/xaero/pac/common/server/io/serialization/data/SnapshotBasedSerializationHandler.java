package xaero.pac.common.server.io.serialization.data;

import xaero.pac.common.server.io.ObjectManagerIOManager;
import xaero.pac.common.server.io.ObjectManagerIOObject;
import xaero.pac.common.server.io.serialization.SerializationHandler;

public class SnapshotBasedSerializationHandler
<
	S,
	I,
	T extends ObjectManagerIOObject,
	M extends ObjectManagerIOManager<T, M>
> extends SerializationHandler<S, I, T, M> {

	private final SnapshotConverter<S, I, T, M> converter;

	public SnapshotBasedSerializationHandler(SnapshotConverter<S, I, T, M> converter) {
		super();
		this.converter = converter;
	}

	@Override
	public S serialize(T object) {
		return converter.convert(object);
	}

	@Override
	public T deserialize(I id, M manager, S serializedData) {
		return converter.convert(id, manager, serializedData);
	}

}
