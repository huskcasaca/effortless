package dev.huskuraft.effortless.api.file;

import java.io.File;
import java.io.IOException;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.api.tag.TagSerializer;

public abstract class TagElementFileStorage<T> extends FileStorage<T> {

    private final TagSerializer<T> serializer;

    protected TagElementFileStorage(String fileName, FileType fileType, TagSerializer<T> serializer) {
        super(fileName, fileType);
        this.serializer = serializer;
    }

    @Override
    protected T read(File config) throws IOException {
        var tag = (TagElement) getFileType().getAdapter().read(config);
        return getSerializer().read(tag, true);
    }

    @Override
    protected void write(File file, T t) throws IOException {
        var tag = TagRecord.newRecord();
        getSerializer().write(tag, t, true);
        getFileType().getAdapter().write(file, tag);
    }

    private TagSerializer<T> getSerializer() {
        return serializer;
    }

}
