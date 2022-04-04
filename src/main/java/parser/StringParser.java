package parser;

import java.util.Optional;

public class StringParser implements Parser<String> {
    @Override
    public String parse(String given) {
        return Optional.ofNullable(given).orElse("");
    }
}
