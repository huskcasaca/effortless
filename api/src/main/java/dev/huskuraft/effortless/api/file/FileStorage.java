package dev.huskuraft.effortless.api.file;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Logger;

import dev.huskuraft.effortless.api.platform.Platform;

public abstract class FileStorage<T> implements Storage<T> {

    private T target;

    private void read() {
        try {
            var file = getTargetFile();
            if (!file.exists()) {
                this.target = getDefault();
                return;
            }
            // TODO: 27/2/24
//            read.validate();
            this.target = read(file);
        } catch (Exception e) {
            Logger.getAnonymousLogger().warning("Cannot read config file: " + e.getMessage());
            this.target = getDefault();
        }
        write();
    }

    private void write() {
        try {
            var file = getTargetFile();
            write(file, target);
        } catch (Exception e) {
            Logger.getAnonymousLogger().warning("Cannot save config file: " + e.getMessage());
        }
    }

    protected File getDir() throws IOException {
        var dataDir = Platform.getInstance().getConfigDir().toFile();
        if (!dataDir.exists() && !dataDir.mkdirs()) {
            throw new IOException("Could not create data directory: " + dataDir.getAbsolutePath());
        }
        return dataDir;
    }

    private File getTargetFile() throws IOException {
        return new File(getDir(), getFileName());
    }

    @Override
    public void use(Consumer<T> consumer) {
        synchronized (this) {
            consumer.accept(get());
            write();
        }
    }

    @Override
    public T get() {
        synchronized (this) {
//            if (target == null) {
                read();
//            }
            return target;
        }
    }

    @Override
    public void set(T config) {
        synchronized (this) {
            this.target = config;
            write();
        }
    }

    public abstract T getDefault();

    public abstract String getFileName();

    public abstract FileType getFileType();

    public abstract T read(File config) throws IOException;

    public abstract void write(File file, T t) throws IOException;

}
