package dev.huskuraft.effortless.api.lang;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public interface Tuple extends Iterable<Object> {

    List<Object> asList();

    default Object get(int index) {
        return asList().get(index);
    }

    @Override
    default Iterator<Object> iterator() {
        return Collections.unmodifiableList(asList()).iterator();
    }

    default int size() {
        return asList().size();
    }


}