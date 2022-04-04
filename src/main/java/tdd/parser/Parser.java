package tdd.parser;

import tdd.annotation.Option;

import java.util.Map;

public interface Parser<T> {
    T parse(String given);

    default T parse(Option option, Map<String, String> argsGiven) {
        return parse(argsGiven.get(option.value()));
    }
}
