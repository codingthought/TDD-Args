package tdd.parser;

import tdd.exception.TooManyArgValueException;

public class BooleanParser implements Parser<Boolean> {
    @Override
    public Boolean parse(String given) {
        if (given == null) {
            return false;
        }
        if (given.isBlank()) {
            return true;
        }
        throw new TooManyArgValueException("bool parse need not given value");
    }
}
