package tdd.parser;

public interface Parser<T> {
    T parse(String given);
}
