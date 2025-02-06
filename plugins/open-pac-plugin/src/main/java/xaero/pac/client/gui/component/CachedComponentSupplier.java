package xaero.pac.client.gui.component;

import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.function.Function;

public class CachedComponentSupplier {

	private final Function<Object[], Component> factory;
	private Object[] registeredArgs;
	private Component cachedComponent;

	public CachedComponentSupplier(Function<Object[], Component> factory){
		this.factory = factory;
	}

	public Component get(Object... args){
		if(cachedComponent == null || !Arrays.equals(registeredArgs, args)) {
			cachedComponent = factory.apply(args);
			registeredArgs = args;
		}
		return cachedComponent;
	}

}
