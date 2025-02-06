package xaero.pac.common.server.io.serialization.human;

public class SimpleHumanReadableStringConverter<I> extends HumanReadableStringConverter<String, I>{

	@Override
	public String convert(I id, String humanReadable) {
		return humanReadable;
	}

	@Override
	public String convert(String data) {
		return data;
	}

}
