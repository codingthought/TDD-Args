package parser;

import java.util.Objects;

public class BooleanParser implements Parser<Boolean> {
    @Override
    public Boolean parse(String given) {
        return Objects.nonNull(given);
    }
}
