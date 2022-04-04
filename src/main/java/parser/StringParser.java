package parser;

import exception.TooManyArgValueException;

public class StringParser implements Parser<String> {
    @Override
    public String parse(String given) {
        if (given == null) {
            return "";
        }
        if (given.split(" ").length > 1) {
            throw new TooManyArgValueException(String.format("given: %s", given));
        }
        return given;
    }
}
