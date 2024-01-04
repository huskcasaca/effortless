package dev.huskuraft.effortless.api.events.api;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public final class EventFactory {

    private EventFactory() {
    }

    public static <T> Event<T> create(Function<List<T>, T> function) {
        return new EventImpl<>(function);
    }

    @SafeVarargs
    public static <T> Event<T> createLoop(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createLoop((Class<T>) typeGetter.getClass().getComponentType());
    }

    private static <T, R> R invokeMethod(T listener, Method method, Object[] args) throws Throwable {
        return (R) MethodHandles.lookup().unreflect(method).bindTo(listener).invokeWithArguments(args);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<T> createLoop(Class<T> clazz) {
        return create(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (var listener : listeners) {
                    invokeMethod(listener, method, args);
                }
                return null;
            }
        }));
    }

    @SafeVarargs
    public static <T> Event<T> createEventResult(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createEventResult((Class<T>) typeGetter.getClass().getComponentType());
    }

    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<T> createEventResult(Class<T> clazz) {
        return create(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (var listener : listeners) {
                    var result = (EventResult) Objects.requireNonNull(invokeMethod(listener, method, args));
                    if (result.interruptsFurtherEvaluation()) {
                        return result;
                    }
                }
                return EventResult.pass();
            }
        }));
    }

    @SafeVarargs
    public static <T> Event<T> createCompoundEventResult(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createCompoundEventResult((Class<T>) typeGetter.getClass().getComponentType());
    }

    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<T> createCompoundEventResult(Class<T> clazz) {
        return create(listeners -> (T) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{clazz}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (var listener : listeners) {
                    var result = (CompoundEventResult) Objects.requireNonNull(invokeMethod(listener, method, args));
                    if (result.interruptsFurtherEvaluation()) {
                        return result;
                    }
                }
                return CompoundEventResult.pass();
            }
        }));
    }

    @SafeVarargs
    public static <T> Event<Consumer<T>> createConsumerLoop(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createConsumerLoop((Class<T>) typeGetter.getClass().getComponentType());
    }

    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<Consumer<T>> createConsumerLoop(Class<T> clazz) {
        return create(listeners -> (Consumer<T>) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{Consumer.class}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (var listener : listeners) {
                    invokeMethod(listener, method, args);
                }
                return null;
            }
        }));
    }

    @SafeVarargs
    public static <T> Event<EventActor<T>> createEventActorLoop(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return createEventActorLoop((Class<T>) typeGetter.getClass().getComponentType());
    }

    @SuppressWarnings("UnstableApiUsage")
    public static <T> Event<EventActor<T>> createEventActorLoop(Class<T> clazz) {
        return create(listeners -> (EventActor<T>) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{EventActor.class}, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (var listener : listeners) {
                    var result = (EventResult) invokeMethod(listener, method, args);
                    if (result.interruptsFurtherEvaluation()) {
                        return result;
                    }
                }
                return EventResult.pass();
            }
        }));
    }

    private static class EventImpl<T> implements Event<T> {
        private final Function<List<T>, T> function;
        private final ArrayList<T> listeners;
        private T invoker = null;

        EventImpl(Function<List<T>, T> function) {
            this.function = function;
            this.listeners = new ArrayList<>();
        }

        @Override
        public T invoker() {
            if (invoker == null) {
                update();
            }
            return invoker;
        }

        @Override
        public void register(T listener) {
            listeners.add(listener);
            invoker = null;
        }

        @Override
        public void unregister(T listener) {
            listeners.remove(listener);
            listeners.trimToSize();
            invoker = null;
        }

        @Override
        public boolean isRegistered(T listener) {
            return listeners.contains(listener);
        }

        @Override
        public void clear() {
            listeners.clear();
            listeners.trimToSize();
            invoker = null;
        }

        public void update() {
            if (listeners.size() == 1) {
                invoker = listeners.get(0);
            } else {
                invoker = function.apply(listeners);
            }
        }
    }

    private abstract static class AbstractInvocationHandler implements InvocationHandler {

        private static final Object[] NO_ARGS = {};

        private static boolean isProxyOfSameInterfaces(Object arg, Class<?> proxyClass) {
            return proxyClass.isInstance(arg)
                    || (Proxy.isProxyClass(arg.getClass())
                    && Arrays.equals(arg.getClass().getInterfaces(), proxyClass.getInterfaces()));
        }

        @Override
        @CheckForNull
        public final Object invoke(Object proxy, Method method, @CheckForNull @Nullable Object[] args)
                throws Throwable {
            if (args == null) {
                args = NO_ARGS;
            }
            if (args.length == 0 && method.getName().equals("hashCode")) {
                return hashCode();
            }
            if (args.length == 1
                    && method.getName().equals("equals")
                    && method.getParameterTypes()[0] == Object.class) {
                Object arg = args[0];
                if (arg == null) {
                    return false;
                }
                if (proxy == arg) {
                    return true;
                }
                return isProxyOfSameInterfaces(arg, proxy.getClass())
                        && equals(Proxy.getInvocationHandler(arg));
            }
            if (args.length == 0 && method.getName().equals("toString")) {
                return toString();
            }
            return handleInvocation(proxy, method, args);
        }

        @CheckForNull
        protected abstract Object handleInvocation(Object proxy, Method method, @Nullable Object[] args)
                throws Throwable;

        @Override
        public boolean equals(@CheckForNull Object obj) {
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
