package dev.huskuraft.effortless.api.tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public interface InputStreamTagReader {
    RecordTag readCompressed(InputStream input) throws IOException;

    default RecordTag readCompressed(File file) throws IOException {
        try (var inputStream = new FileInputStream(file)) {
            return readCompressed(inputStream);
        }
    }

}
