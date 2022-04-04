package parser;

import exception.IllegalArgValueException;
import exception.MissingArgValueException;
import exception.TooManyArgValueException;

public class IntParser implements Parser<Integer> {
    @Override
    public Integer parse(String given) {
        if (given == null) {
            return 0;
        }
        if (given.isBlank()) {
            throw new MissingArgValueException("miss argument value");
        }
        if (given.split(" ").length > 1) {
            throw new TooManyArgValueException(String.format("given: %s", given));
        }
        if (!given.matches("\\d+")) {
            throw new IllegalArgValueException(String.format("given:%s", given));
        }
        return Integer.parseInt(given);
    }
}
