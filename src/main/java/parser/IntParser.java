package parser;

import exception.IllegalArgValueException;
import exception.MissingArgValueException;
import exception.TooManyArgValueException;

import java.util.function.Function;
import java.util.function.Predicate;

public class IntParser implements Parser<Object> {

    public IntParser(Function<String, Object> givenParser, Object defaultValue, Predicate<String> isIllegalValue) {
        this.isIllegalValue = isIllegalValue;
        this.defaultValue = defaultValue;
        this.givenParser = givenParser;
    }

    private Predicate<String> isIllegalValue;

    private Object defaultValue;

    public IntParser() {
        this.givenParser = Integer::parseInt;
        this.defaultValue = 0;
        this.isIllegalValue = given -> !given.matches("\\d+");
    }

    private Function<String, Object> givenParser;

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
        if (isIllegalValue(given)) {
            throw new IllegalArgValueException(String.format("given:%s", given));
        }
        return parseGiven(given);
    }

    private boolean isIllegalValue(String given) {
        return isIllegalValue.test(given);
    }

    private Object parseGiven(String given) {
        return givenParser.apply(given);
    }
}
