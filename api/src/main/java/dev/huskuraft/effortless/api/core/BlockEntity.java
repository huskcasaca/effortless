package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.tag.TagRecord;

public interface BlockEntity extends PlatformReference {

    BlockState getBlockState();

    BlockPosition getBlockPosition();

    World getWorld();

    TagRecord getTag();

    void setTag(TagRecord tagRecord);

    default BlockEntity copy() {
        var tag = getTag();
        var newBlockEntity = getBlockState().getEntity(getBlockPosition());
        if (newBlockEntity != null) {
            newBlockEntity.setTag(tag);
        }
        return newBlockEntity;
    }

}
