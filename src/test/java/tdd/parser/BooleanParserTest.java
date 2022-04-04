package tdd.parser;

import tdd.annotation.Option;
import tdd.exception.TooManyArgValueException;
import org.junit.jupiter.api.Test;
import tdd.Args;

import static org.junit.jupiter.api.Assertions.*;

public class BooleanParserTest {

    @Test
    void should_get_true_when_parse_BoolOption() {
        assertTrue(new Args("-l").parse(BooleanOption.class).logging());
    }

    @Test
    void should_get_false_when_parse_BoolOption_if_miss_arg() {
        assertFalse(new Args("").parse(BooleanOption.class).logging());
    }

    @Test
    public void should_not_accept_extra_argument_for_BoolOption() {
        assertThrows(TooManyArgValueException.class, () -> new Args("-l t").parse(BooleanOption.class));
    }

    public record BooleanOption(@Option("l") boolean logging) {
    }
}