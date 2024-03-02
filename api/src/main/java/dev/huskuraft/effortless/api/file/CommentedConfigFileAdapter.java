package dev.huskuraft.effortless.api.file;

import java.io.File;
import java.io.IOException;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlParser;
import com.electronwill.nightconfig.toml.TomlWriter;

public final class CommentedConfigFileAdapter extends FileAdapter<CommentedConfig> {

    @Override
    public CommentedConfig read(File config) throws IOException {
        return new TomlParser().parse(config, (file, configFormat) -> false);
    }

    @Override
    public void write(File file, CommentedConfig config) throws IOException {
        new TomlWriter().write(config, file, WritingMode.REPLACE);
    }

}
