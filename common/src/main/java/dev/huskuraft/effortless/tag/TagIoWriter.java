package dev.huskuraft.effortless.tag;

import java.io.IOException;
import java.io.OutputStream;

public interface TagIoWriter {

    void write(OutputStream output, TagRecord config) throws IOException;

}
