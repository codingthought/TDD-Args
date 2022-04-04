package tdd.parser;

import tdd.exception.IllegalArgValueException;
import tdd.exception.MissingArgValueException;
import tdd.exception.TooManyArgValueException;

import java.util.function.Function;
import java.util.function.Predicate;

public class SingleValueParser<T> implements Parser<T> {

    public SingleValueParser(Function<String, T> givenParser, T defaultValue, Predicate<String> isIllegalValuePredicate) {
        this.isIllegalValuePredicate = isIllegalValuePredicate;
        this.defaultValue = defaultValue;
        this.givenParser = givenParser;
    }

    private final Function<String, T> givenParser;
    private final T defaultValue;
    private final Predicate<String> isIllegalValuePredicate;

    @Override
    public T parse(String given) {
        if (given == null) {
            return defaultValue;
        }
        if (given.isBlank()) {
            throw new MissingArgValueException("miss argument value");
        }
        if (given.split(" ").length > 1) {
            throw new TooManyArgValueException(String.format("given: %s", given));
        }
        if (isIllegalValuePredicate.test(given)) {
            throw new IllegalArgValueException(String.format("given:%s", given));
        }
        return givenParser.apply(given);
    }

}
