package dev.huskuraft.effortless.api.file;

import java.io.File;
import java.io.IOException;

import com.electronwill.nightconfig.core.CommentedConfig;

import dev.huskuraft.effortless.api.toml.CommentedConfigDeserializer;
import dev.huskuraft.effortless.api.toml.CommentedConfigSerializer;

public abstract class CommentedConfigFileStorage<T> extends FileStorage<T> {

    @Override
    public FileType getFileType() {
        return FileType.TOML;
    }

    @Override
    public T read(File config) throws IOException {
        return getDeserializer().deserialize((CommentedConfig) getFileType().getAdapter().read(config));
    }

    @Override
    public void write(File file, T t) throws IOException {
        getFileType().getAdapter().write(file, getSerializer().serialize(t));
    }

    public abstract CommentedConfigSerializer<T> getSerializer();

    public abstract CommentedConfigDeserializer<T> getDeserializer();
}
