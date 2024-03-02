package dev.huskuraft.effortless.api.file;

import java.io.File;
import java.io.IOException;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagIo;
import dev.huskuraft.effortless.api.tag.TagRecord;

public final class TagElementFileAdapter extends FileAdapter<TagElement> {

    @Override
    public TagElement read(File config) throws IOException {
        return TagIo.read(config);
    }

    @Override
    public void write(File file, TagElement tagElement) throws IOException {
        TagIo.write(file, (TagRecord) tagElement);
    }

}
