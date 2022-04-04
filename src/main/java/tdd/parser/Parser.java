package tdd.parser;

import tdd.annotation.DefaultValue;
import tdd.annotation.Option;

import java.util.Map;
import java.util.Optional;

public interface Parser<T> {
    T parse(String given);

    default T parse(Option option, Map<String, String> argsGiven) {
        return Optional.ofNullable(argsGiven.get(option.value())).map(this::parse).orElseGet(() -> {
            DefaultValue defaultValue = option.defaultValue();
            if (defaultValue.clazz().equals(Void.class)) {
                return parse(null);
            }
            return (T) Integer.valueOf(defaultValue.intValue());
        });
    }
}
