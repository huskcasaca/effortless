package dev.huskuraft.effortless.api.file.adapters;

import java.io.File;
import java.io.IOException;

import dev.huskuraft.effortless.api.file.FileAdapter;
import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagRecord;

public final class TagFileAdapter extends FileAdapter<TagElement> {

    @Override
    public TagElement read(File file) throws IOException {
        return ContentFactory.getInstance().getInputStreamTagReader().readCompressed(file);
    }

    @Override
    public void write(File file, TagElement tagElement) throws IOException {
        ContentFactory.getInstance().getOutputStreamTagWriter().writeCompressed(file, (TagRecord) tagElement);
    }

}
