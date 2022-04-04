package tdd.parser;

import tdd.annotation.Option;

import java.util.Map;

import static java.util.Optional.ofNullable;
import static tdd.annotation.DefaultValue.DEFAULT_FUNCTION_MAP;

public interface Parser<T> {

    T parse(String given);

    default T parse(Option option, Map<String, String> argsGiven) {
        return ofNullable(argsGiven.get(option.value())).map(this::parse)
                .orElseGet(() -> (T) ofNullable(DEFAULT_FUNCTION_MAP.get(option.defaultValue().clazz()).apply(option.defaultValue()))
                        .orElseGet(() -> parse(null)));
    }
}
