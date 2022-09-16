package dev.huskuraft.effortless.config;

import dev.huskuraft.effortless.tag.TagRecord;

import java.io.IOException;
import java.io.InputStream;

public interface ConfigReader {

    TagRecord read(InputStream input) throws IOException;

}
