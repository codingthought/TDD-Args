package tdd.parser;

import tdd.annotation.Option;

import java.util.Map;

import static java.util.Optional.ofNullable;
import static tdd.annotation.DefaultValue.DEFAULT_FUNCTION_MAP;

public interface Parser<T> {

    T parse(String given);

    default T parse(Option option, Map<String, String> argsGiven, Class<?> clazz) {
        String value = argsGiven.get(option.value());
        return ofNullable(value).map(this::parse).orElseGet(() ->
                ofNullable((T) DEFAULT_FUNCTION_MAP.get(clazz).apply(option.defaultValue()))
                        .orElseGet(() -> parse(null)));
    }
}
