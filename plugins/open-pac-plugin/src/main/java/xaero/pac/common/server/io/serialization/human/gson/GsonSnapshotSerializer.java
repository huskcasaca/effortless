package xaero.pac.common.server.io.serialization.human.gson;

import com.google.gson.Gson;

public class GsonSnapshotSerializer<T extends GsonSnapshot> {

	private final Gson gson;
	private final Class<T> dataClass;

	public GsonSnapshotSerializer(Gson gson, Class<T> dataClass) {
		super();
		this.gson = gson;
		this.dataClass = dataClass;
	}

	public String serialize(T data) {
		return gson.toJson(data);
	}

	public T deserialize(String serializedData) {
		return gson.fromJson(serializedData, dataClass);
	}

}
