package dev.huskuraft.effortless.api.file;

import java.io.File;
import java.io.IOException;

import dev.huskuraft.effortless.api.tag.Tag;
import dev.huskuraft.effortless.api.tag.TagSerializer;

public abstract class TagElementFileStorage<T> extends FileStorage<T> {

    private final TagSerializer<T> serializer;

    protected TagElementFileStorage(String fileName, FileType fileType, TagSerializer<T> serializer) {
        super(fileName, fileType);
        this.serializer = serializer;
    }

    @Override
    protected T read(File config) throws IOException {
        return serializer.decode((Tag) getFileType().getAdapter().read(config), true);
    }

    @Override
    protected void write(File file, T t) throws IOException {
        getFileType().getAdapter().write(file, serializer.encode(t, true));
    }

}
