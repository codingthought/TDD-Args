package parser;

import exception.IllegalArgValueException;
import exception.MissingArgValueException;

public class IntParser implements Parser<Integer> {
    @Override
    public Integer parse(String given) {
        if (given == null) {
            return 0;
        }
        if (given.isBlank()) {
            throw new MissingArgValueException("miss argument value");
        }
        if (!given.matches("\\d+")) {
            throw new IllegalArgValueException(String.format("given:%s", given));
        }
        return Integer.parseInt(given);
    }
}
