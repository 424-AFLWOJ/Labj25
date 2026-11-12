package edu.course.lab07;


import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public final class WordProcessor {
    public List<String> process(List<String> words) {
        return words.stream().filter(word -> !word.isEmpty())
                .map(word -> word.toUpperCase(Locale.ROOT))
                .sorted(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()))
                .toList();
    }
}
