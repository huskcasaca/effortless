package dev.huskuraft.effortless.api.platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.ServiceConfigurationError;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class PlatformLoader<S> implements Iterable<PlatformLoader.Loader<S>> {

    private static final String PREFIX = "META-INF/services/";
    private static final Map<Class<?>, Object> INSTANCES = Collections.synchronizedMap((new HashMap<>()));
    private final Class<S> service;
    private final ClassLoader loader;

    private PlatformLoader(Class<S> svc, ClassLoader cl) {
        service = Objects.requireNonNull(svc, "Service interface cannot be null");
        loader = (cl == null) ? ClassLoader.getSystemClassLoader() : cl;
    }

    public static <S> PlatformLoader<S> load(Class<S> service, ClassLoader loader) {
        return new PlatformLoader<>(service, loader);
    }

    public static <S> PlatformLoader<S> load(Class<S> service) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return PlatformLoader.load(service, cl);
    }

    public static <S> PlatformLoader<S> loadInstalled(Class<S> service) {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        ClassLoader prev = null;
        while (cl != null) {
            prev = cl;
            cl = cl.getParent();
        }
        return PlatformLoader.load(service, prev);
    }

    public static <S> S getSingleton(Class<S> service) {
        if (!INSTANCES.containsKey(service)) {
            INSTANCES.put(service, load(service).get());
        }
        return service.cast(INSTANCES.get(service));
    }

    public static <S> S getSingleton(S... typeGetter) {
        return getSingleton((Class<S>) typeGetter.getClass().getComponentType());
    }

    public Iterator<Loader<S>> iterator() {
        return new LazyIterator<S>(service, loader);
    }

    public Stream<Loader<S>> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), 0), false);
    }

    public Loader<S> findFirst() {
        return stream().filter(Loader::isPresent).findFirst().orElseThrow();
    }

    public S get() {
        return findFirst().get();
    }

    public String toString() {
        return "PlatformLoader[" + service.getName() + "]";
    }

    public record Loader<S>(Class<S> clazz, String className, ClassLoader loader) {

        public LoaderType getClassTypeByName() {
            if (className.contains("vanilla")) {
                return LoaderType.VANILLA;
            }
            if (className.contains("fabric")) {
                return LoaderType.FABRIC;
            }
            if (className.contains("forge")) {
                return LoaderType.FORGE;
            }
            return LoaderType.VANILLA;
        }

        private S create() throws ClassNotFoundException, ClassCastException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            var childClass = Class.forName(className, false, loader);
            if (!clazz.isAssignableFrom(childClass)) {
                throw new ClassCastException(className + " is not a subtype of " + clazz.getName());
            }
            return clazz.cast(childClass.getDeclaredConstructor().newInstance());
        }

        public boolean isPresent() {
            if (getClassTypeByName() == LoaderType.VANILLA) {
                return true;
            } else {
                return getClassTypeByName() == getLoaderTypeByThread();
            }
        }

        public S get() {
            if (!isPresent()) {
                throw new ServiceConfigurationError("Service " + clazz.getName() + " is not available in loader " + getLoaderTypeByThread());
            }
            try {
                return create();
            } catch (Exception e) {
                throw new ServiceConfigurationError(e.getMessage());
            }
        }

        private static LoaderType getLoaderTypeByThread() {
            var loader = Thread.currentThread().getContextClassLoader();
            if (loader.getClass().getPackageName().equals("net.fabricmc.loader.impl.launch.knot")) {
                return LoaderType.FABRIC;
            }
            var p = loader.getClass().getPackageName();
            if (loader.getClass().getPackageName().equals("cpw.mods.modlauncher")) {
                return LoaderType.FORGE;
            }
            throw new IllegalStateException("Unknown loader: " + p);
        }

    }

    private static class LazyIterator<S> implements Iterator<Loader<S>> {

        private final Class<S> clazz;
        private final ClassLoader loader;
        private Enumeration<URL> configs = null;
        private Iterator<String> pending = null;
        private String nextClassName = null;

        private LazyIterator(Class<S> clazz, ClassLoader loader) {
            this.clazz = clazz;
            this.loader = loader;
        }

        public boolean hasNext() {
            if (nextClassName != null) {
                return true;
            }
            if (configs == null) {
                try {
                    String fullName = PREFIX + clazz.getName();
                    if (loader == null) configs = ClassLoader.getSystemResources(fullName);
                    else configs = loader.getResources(fullName);
                } catch (IOException x) {
                    fail(clazz, "Error locating configuration files", x);
                }
            }
            while (pending == null || !pending.hasNext()) {
                if (!configs.hasMoreElements()) {
                    return false;
                }
                pending = parse(clazz, configs.nextElement());
            }
            nextClassName = pending.next();
            return true;
        }

        public Loader<S> next() {
            if (!hasNext()) throw new NoSuchElementException();
            var className = nextClassName;
            nextClassName = null;
            return new Loader<>(clazz, className, loader);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        private static int parseLine(Class<?> service, URL u, BufferedReader r, int lc, List<String> names) throws IOException, ServiceConfigurationError {
            var ln = r.readLine();
            if (ln == null) {
                return -1;
            }
            int ci = ln.indexOf('#');
            if (ci >= 0) ln = ln.substring(0, ci);
            ln = ln.trim();
            int n = ln.length();
            if (n != 0) {
                if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0))
                    fail(service, u, lc, "Illegal configuration-file syntax");
                int cp = ln.codePointAt(0);
                if (!Character.isJavaIdentifierStart(cp)) fail(service, u, lc, "Illegal provider-class name: " + ln);
                for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
                    cp = ln.codePointAt(i);
                    if (!Character.isJavaIdentifierPart(cp) && (cp != '.'))
                        fail(service, u, lc, "Illegal provider-class name: " + ln);
                }
                if (!names.contains(ln)) names.add(ln);
            }
            return lc + 1;
        }

        private static Iterator<String> parse(Class<?> service, URL url) throws ServiceConfigurationError {
            InputStream inputStream = null;
            BufferedReader reader = null;
            var names = new ArrayList<String>();
            try {
                inputStream = url.openStream();
                reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                int lc = 1;
                while ((lc = parseLine(service, url, reader, lc, names)) >= 0) ;
            } catch (IOException x) {
                fail(service, "Error reading configuration file", x);
            } finally {
                try {
                    if (reader != null) reader.close();
                    if (inputStream != null) inputStream.close();
                } catch (IOException y) {
                    fail(service, "Error closing configuration file", y);
                }
            }
            return names.iterator();
        }

        private static void fail(Class<?> service, String msg, Throwable cause) throws ServiceConfigurationError {
            throw new ServiceConfigurationError(service.getName() + ": " + msg, cause);
        }

        private static void fail(Class<?> service, String msg) throws ServiceConfigurationError {
            throw new ServiceConfigurationError(service.getName() + ": " + msg);
        }

        private static void fail(Class<?> service, URL u, int line, String msg) throws ServiceConfigurationError {
            fail(service, u + ":" + line + ": " + msg);
        }

    }

}
