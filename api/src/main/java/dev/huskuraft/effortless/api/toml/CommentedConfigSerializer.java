package dev.huskuraft.effortless.api.toml;

import com.electronwill.nightconfig.core.CommentedConfig;

public interface CommentedConfigSerializer<T> {

    CommentedConfig serialize(T t);

}
