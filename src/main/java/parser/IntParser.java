package parser;

import exception.IllegalArgValueException;

public class IntParser implements Parser<Integer> {
    @Override
    public Integer parse(String given) {
        if (given == null) {
            return 0;
        }
        if (!given.matches("\\d+")) {
            throw new IllegalArgValueException(String.format("given:%s", given));
        }
        return Integer.parseInt(given);
    }
}
