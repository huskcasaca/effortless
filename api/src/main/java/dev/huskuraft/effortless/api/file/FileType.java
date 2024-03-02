package dev.huskuraft.effortless.api.file;

public enum FileType {
    JSON("json", null),
    TOML("toml", new CommentedConfigFileAdapter()),
    NBT("dat", new TagElementFileAdapter());

    private final String extension;
    private final FileAdapter adapter;

    <T> FileType(String extension, FileAdapter<T> adapter) {
        this.extension = extension;
        this.adapter = adapter;
    }

    public String getExtension() {
        return extension;
    }

    public <T> FileAdapter<T> getAdapter() {
        return adapter;
    }

}
