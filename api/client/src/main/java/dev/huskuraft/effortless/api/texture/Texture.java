package dev.huskuraft.effortless.api.texture;

import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.api.platform.PlatformReference;

import java.util.Set;

public interface Texture extends PlatformReference {

    Resource resource();

    Set<Resource> sprites();

    TextureSprite getSprite(Resource name);

}
