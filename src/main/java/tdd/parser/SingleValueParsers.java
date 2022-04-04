package tdd.parser;

import tdd.exception.IllegalArgValueException;
import tdd.exception.MissingArgValueException;
import tdd.exception.TooManyArgValueException;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class SingleValueParsers {

    public static Parser<Boolean> bool() {
        return given -> Optional.ofNullable(given).map(s -> matchValue(s, 0, t -> false).isBlank()).orElse(false);
    }

    public static <T> Parser<T> unary(Function<String, T> givenParser, T defaultValue, Predicate<String> isIllegalValuePredicate) {
        return given -> Optional.ofNullable(given).map(s -> givenParser.apply(matchValue(s, 1, isIllegalValuePredicate))).orElse(defaultValue);
    }

    private static String matchValue(String given, int expectedSize, Predicate<String> isIllegalValuePredicate) {
        String[] argValues = given.split(" ");
        if (given.length() < expectedSize || argValues.length < expectedSize)
            throw new MissingArgValueException("miss argument value");
        if (given.length() > expectedSize && argValues.length > expectedSize)
            throw new TooManyArgValueException(String.format("given: %s", given));
        if (isIllegalValuePredicate.test(argValues[0]))
            throw new IllegalArgValueException(String.format("given:%s", argValues[0]));
        return argValues[0];
    }

}
