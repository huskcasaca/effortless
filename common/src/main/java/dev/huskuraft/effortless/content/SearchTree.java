package dev.huskuraft.effortless.content;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@FunctionalInterface
public interface SearchTree<T> {

    static <T> SearchTree<T> empty() {
        return query -> List.of();
    }

    static <T> SearchTree<T> of(List<T> list, Function<T, Stream<String>> keyExtractor) {
        return query -> list.stream().filter(t -> keyExtractor.apply(t).anyMatch(text -> text.contains(query))).toList();
    }

    List<T> search(String query);

}
