package dev.huskuraft.effortless.api.tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public interface InputStreamTagReader {
    TagRecord readCompressed(InputStream input) throws IOException;

    default TagRecord readCompressed(File file) throws IOException {
        try (var inputStream = new FileInputStream(file)) {
            return readCompressed(inputStream);
        }
    }

}
