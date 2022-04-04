package parser;

import exception.IllegalArgValueException;
import exception.MissingArgValueException;
import exception.TooManyArgValueException;

import java.util.function.Function;
import java.util.function.Predicate;

public class SingleValueParser implements Parser<Object> {

    public SingleValueParser(Function<String, Object> givenParser, Object defaultValue, Predicate<String> isIllegalValue) {
        this.isIllegalValue = isIllegalValue;
        this.defaultValue = defaultValue;
        this.givenParser = givenParser;
    }

    private final Function<String, Object> givenParser;
    private final Object defaultValue;
    private final Predicate<String> isIllegalValue;

    @Override
    public Object parse(String given) {
        if (given == null) {
            return defaultValue;
        }
        if (given.isBlank()) {
            throw new MissingArgValueException("miss argument value");
        }
        if (given.split(" ").length > 1) {
            throw new TooManyArgValueException(String.format("given: %s", given));
        }
        if (isIllegalValue.test(given)) {
            throw new IllegalArgValueException(String.format("given:%s", given));
        }
        return givenParser.apply(given);
    }

}
