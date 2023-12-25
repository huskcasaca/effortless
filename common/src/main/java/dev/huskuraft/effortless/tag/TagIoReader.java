package dev.huskuraft.effortless.tag;

import java.io.IOException;
import java.io.InputStream;

public interface TagIoReader {

    TagRecord read(InputStream input) throws IOException;

}
