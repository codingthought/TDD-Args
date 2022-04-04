package parser;

import exception.MissingArgValueException;
import exception.TooManyArgValueException;

public class StringParser implements Parser<String> {
    @Override
    public String parse(String given) {
        if (given == null) {
            return "";
        }
        if (given.isBlank()) {
            throw new MissingArgValueException("miss argument value");
        }
        if (given.split(" ").length > 1) {
            throw new TooManyArgValueException(String.format("given: %s", given));
        }
        return given;
    }
}
