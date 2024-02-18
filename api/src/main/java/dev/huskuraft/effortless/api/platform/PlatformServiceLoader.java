package dev.huskuraft.effortless.api.platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class PlatformServiceLoader<S> implements Iterable<Optional<S>> {

    private static final String PREFIX = "META-INF/services/";

    private final Class<S> service;

    private final ClassLoader loader;

    private PlatformServiceLoader(Class<S> svc, ClassLoader cl) {
        service = Objects.requireNonNull(svc, "Service interface cannot be null");
        loader = (cl == null) ? ClassLoader.getSystemClassLoader() : cl;
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

    private static int parseLine(Class<?> service, URL u, BufferedReader r, int lc, List<String> names) throws IOException, ServiceConfigurationError {
        String ln = r.readLine();
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
            reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
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

    private class LazyIterator implements Iterator<S> {

        Class<S> service;
        ClassLoader loader;
        Enumeration<URL> configs = null;
        Iterator<String> pending = null;
        String nextName = null;

        private LazyIterator(Class<S> service, ClassLoader loader) {
            this.service = service;
            this.loader = loader;
        }

        private boolean hasNextService() {
            if (nextName != null) {
                return true;
            }
            if (configs == null) {
                try {
                    String fullName = PREFIX + service.getName();
                    if (loader == null) configs = ClassLoader.getSystemResources(fullName);
                    else configs = loader.getResources(fullName);
                } catch (IOException x) {
                    fail(service, "Error locating configuration files", x);
                }
            }
            while ((pending == null) || !pending.hasNext()) {
                if (!configs.hasMoreElements()) {
                    return false;
                }
                pending = parse(service, configs.nextElement());
            }
            nextName = pending.next();
            return true;
        }

        private S nextService() {
            if (!hasNextService()) throw new NoSuchElementException();
            String cn = nextName;
            nextName = null;
            Class<?> c = null;
            try {
                c = Class.forName(cn, false, loader);
            } catch (ClassNotFoundException x) {
                fail(service, "Provider " + cn + " not found");
            }
            if (!service.isAssignableFrom(c)) {
                fail(service, "Provider " + cn + " not a subtype");
            }
            try {
                return service.cast(c.getDeclaredConstructor().newInstance());
            } catch (Throwable x) {
                fail(service, "Provider " + cn + " could not be instantiated", x);
            }
            throw new Error();          // This cannot happen
        }

        public boolean hasNext() {
            return hasNextService();
        }

        public S next() {
            return nextService();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    public Iterator<Optional<S>> iterator() {
        return new Iterator<>() {

            final LazyIterator lookupIterator = new LazyIterator(service, loader);

            public boolean hasNext() {
                return lookupIterator.hasNext();
            }

            public Optional<S> next() {
                try {
                    return Optional.ofNullable(lookupIterator.next());
                } catch (ServiceConfigurationError e) {
                    return Optional.empty();
                }
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    public static <S> PlatformServiceLoader<S> load(Class<S> service, ClassLoader loader) {
        return new PlatformServiceLoader<>(service, loader);
    }

    public static <S> PlatformServiceLoader<S> load(Class<S> service) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return PlatformServiceLoader.load(service, cl);
    }

    public static <S> PlatformServiceLoader<S> loadInstalled(Class<S> service) {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        ClassLoader prev = null;
        while (cl != null) {
            prev = cl;
            cl = cl.getParent();
        }
        return PlatformServiceLoader.load(service, prev);
    }

    public Stream<Optional<S>> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), 0), false);
    }

    public Optional<S> findFirst() {
        return stream().filter(Optional::isPresent).findFirst().orElseThrow();
    }

    public S get() {
        for (var s : load(service)) {
            if (s.isPresent()) {
                if (s.get().getClass().getPackageName().contains("vanilla")) {
                    return s.get();
                }
                if (s.get().getClass().getPackageName().contains("fabric")) {
                    if (getLoaderTypeByThread() == LoaderType.FABRIC) {
                        return s.get();
                    }
                }
                if (s.get().getClass().getPackageName().contains("forge")) {
                    if (getLoaderTypeByThread() == LoaderType.FORGE) {
                        return s.get();
                    }
                }
                return s.get();
            }
        }
        throw new IllegalStateException("No provider found for " + service.getName());
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


    public String toString() {
        return "PlatformServiceLoader[" + service.getName() + "]";
    }

}
