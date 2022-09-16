package dev.huskuraft.effortless.config;

import dev.huskuraft.effortless.tag.TagRecord;

import java.io.IOException;
import java.io.OutputStream;

public interface ConfigWriter {

    void write(OutputStream output, TagRecord config) throws IOException;

}
