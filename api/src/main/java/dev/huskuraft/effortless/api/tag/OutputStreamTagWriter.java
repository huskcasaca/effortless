package dev.huskuraft.effortless.api.tag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface OutputStreamTagWriter {
    void writeCompressed(OutputStream output, RecordTag config) throws IOException;

    default void writeCompressed(File file, RecordTag record) throws IOException {
        try (var outputStream = new FileOutputStream(file)) {
            writeCompressed(outputStream, record);
        }
    }

}
