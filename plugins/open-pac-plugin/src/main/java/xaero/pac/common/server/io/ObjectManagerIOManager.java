package xaero.pac.common.server.io;

public interface ObjectManagerIOManager
<
	T extends ObjectManagerIOObject,
	M extends ObjectManagerIOManager<T, M>
> {

	public void addToSave(T object);
	public Iterable<T> getToSave();

}
