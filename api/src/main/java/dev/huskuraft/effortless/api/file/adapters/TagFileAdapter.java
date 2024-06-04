package dev.huskuraft.effortless.api.file.adapters;

import java.io.File;
import java.io.IOException;

import dev.huskuraft.effortless.api.file.FileAdapter;
import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.tag.Tag;
import dev.huskuraft.effortless.api.tag.RecordTag;

public final class TagFileAdapter extends FileAdapter<Tag> {

    @Override
    public Tag read(File file) throws IOException {
        return ContentFactory.getInstance().getInputStreamTagReader().readCompressed(file);
    }

    @Override
    public void write(File file, Tag tag) throws IOException {
        ContentFactory.getInstance().getOutputStreamTagWriter().writeCompressed(file, (RecordTag) tag);
    }

}
