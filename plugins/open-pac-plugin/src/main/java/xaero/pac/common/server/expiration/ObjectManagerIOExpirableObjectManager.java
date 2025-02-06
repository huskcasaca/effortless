package xaero.pac.common.server.expiration;

import xaero.pac.common.server.io.ObjectManagerIOObject;

import java.util.Iterator;

public interface ObjectManagerIOExpirableObjectManager<T extends ObjectManagerIOObject> {

	public Iterator<T> getExpirationIterator();

}
