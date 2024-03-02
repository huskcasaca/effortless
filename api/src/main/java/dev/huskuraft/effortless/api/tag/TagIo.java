package dev.huskuraft.effortless.api.tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import dev.huskuraft.effortless.api.platform.ContentFactory;

public final class TagIo {

    public interface Writer {
        void write(OutputStream output, TagRecord config) throws IOException;

    }

    public interface Reader {
        TagRecord read(InputStream input) throws IOException;

    }

    public static void write(OutputStream outputStream, TagRecord record) throws IOException {
        ContentFactory.getInstance().getTagIOWriter().write(outputStream, record);
    }

    public static void write(File file, TagRecord record) throws IOException {
        try (var outputStream = new FileOutputStream(file)) {
            write(outputStream, record);
        }
    }

    public static TagRecord read(InputStream inputStream) throws IOException {
        return ContentFactory.getInstance().getTagIOReader().read(inputStream);
    }

    public static TagRecord read(File file) throws IOException {
        try (var inputStream = new FileInputStream(file)) {
            return read(inputStream);
        }
    }


}
