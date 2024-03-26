package dev.huskuraft.effortless.api.file.adapters;

import java.io.File;
import java.io.IOException;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlParser;
import com.electronwill.nightconfig.toml.TomlWriter;

import dev.huskuraft.effortless.api.file.FileAdapter;

public final class TomlFileAdapter extends FileAdapter<Config> {

    @Override
    public Config read(File config) throws IOException {
        return new TomlParser().parse(config, (file, configFormat) -> false);
    }

    @Override
    public void write(File file, Config config) throws IOException {
        new TomlWriter().write(config, file, WritingMode.REPLACE);
    }

}
