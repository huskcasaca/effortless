package xaero.pac.common.server.io.serialization.human.gson;

import xaero.pac.common.server.io.serialization.human.HumanReadableStringConverter;

public class GsonStringConverter<S extends GsonSnapshot, I> extends HumanReadableStringConverter<S, I> {

	private GsonSnapshotSerializer<S> gson;

	public GsonStringConverter(GsonSnapshotSerializer<S> gson) {
		super();
		this.gson = gson;
	}

	@Override
	public S convert(I id, String humanReadable) {
		return (S) gson.deserialize(humanReadable);
	}

	@Override
	public String convert(S data) {
		return gson.serialize(data);
	}

}
