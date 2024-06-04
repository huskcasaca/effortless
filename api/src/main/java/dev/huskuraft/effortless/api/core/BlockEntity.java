package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.tag.RecordTag;

public interface BlockEntity extends PlatformReference {

    BlockState getBlockState();

    BlockPosition getBlockPosition();

    World getWorld();

    RecordTag getTag();

    void setTag(RecordTag recordTag);

    default BlockEntity copy() {
        var tag = getTag();
        var newBlockEntity = getBlockState().getEntity(getBlockPosition());
        if (newBlockEntity != null) {
            newBlockEntity.setTag(tag);
        }
        return newBlockEntity;
    }

}
