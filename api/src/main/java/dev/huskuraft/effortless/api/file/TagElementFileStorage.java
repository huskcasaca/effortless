package dev.huskuraft.effortless.api.file;

import java.io.File;
import java.io.IOException;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.api.tag.TagSerializer;

public abstract class TagElementFileStorage<T> extends FileStorage<T>  {
    @Override
    public FileType getFileType() {
        return FileType.NBT;
    }

    @Override
    public T read(File config) throws IOException {
        var tag = (TagElement) getFileType().getAdapter().read(config);
        return getSerializer().read(tag);
    }

    @Override
    public void write(File file, T t) throws IOException {
        var tag = TagRecord.newRecord();
        getSerializer().write(tag, t);
        getFileType().getAdapter().write(file, tag);
    }

    public abstract TagSerializer<T> getSerializer();
}
