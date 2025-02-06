package xaero.pac.common.util.linked;

public interface ILinkedChainNode<V extends ILinkedChainNode<V>> {

	public void setNext(V element);

	public void setPrevious(V element);

	public V getNext();

	public V getPrevious();

	public boolean isDestroyed();

	public void onDestroyed();

}
