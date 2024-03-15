package dev.huskuraft.effortless.api.platform;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.text.Text;

@FunctionalInterface
public interface SearchTree<T> {

    static <T> SearchTree<T> empty() {
        return query -> List.of();
    }

    static <T> SearchTree<T> of(List<T> list, Function<T, Stream<String>> keyExtractor, boolean caseSensitive) {
        return query -> list.stream().filter(t -> keyExtractor.apply(t).anyMatch(text -> caseSensitive ? text.contains(query) : text.toLowerCase().contains(query.toLowerCase()))).toList();
    }

    static <T> SearchTree<T> of(List<T> list, Function<T, Stream<String>> keyExtractor) {
        return of(list, keyExtractor, false);
    }

    static  <T> SearchTree<T> ofText(List<T> list, Function<T, Stream<Text>> keyExtractor) {
        return of(list, item -> keyExtractor.apply(item).map(Text::getString));
    }

    List<T> search(String query);

}
