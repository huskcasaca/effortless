package dev.huskuraft.effortless.api.file.adapters;

import java.io.File;
import java.io.IOException;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.json.FancyJsonWriter;
import com.electronwill.nightconfig.json.JsonParser;

import dev.huskuraft.effortless.api.file.FileAdapter;

public final class JsonFileAdapter extends FileAdapter<Config> {

    @Override
    public Config read(File config) throws IOException {
        return new JsonParser().parse(config, (file, configFormat) -> false);
    }

    @Override
    public void write(File file, Config config) throws IOException {
        new FancyJsonWriter().write(config, file, WritingMode.REPLACE);
    }

}
