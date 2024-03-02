package dev.huskuraft.effortless.api.toml;

import com.electronwill.nightconfig.core.CommentedConfig;

public interface CommentedConfigDeserializer<T> {

    T deserialize(CommentedConfig config);

}
