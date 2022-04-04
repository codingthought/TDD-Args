package tdd.parser;

import org.junit.jupiter.api.Nested;
import tdd.annotation.Option;
import tdd.exception.IllegalArgValueException;
import tdd.exception.MissingArgValueException;
import tdd.exception.TooManyArgValueException;
import org.junit.jupiter.api.Test;
import tdd.Args;

import static org.junit.jupiter.api.Assertions.*;

public class SingleValueParsersTest {

    @Nested
    class BoolOptionTest {
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

    @Nested
    class UnaryOptionTest {

        @Test
        void should_get_8080_when_parse_IntOption() {
            assertEquals(8080, new Args("-p 8080").parse(IntOption.class).port());
        }

        public record IntOption(@Option("p") int port) {
        }

        @Test
        void should_get_usr_logs_when_parse_StringOption() {
            assertEquals("/usr/logs", new Args("-d /usr/logs").parse(StringOption.class).dir());
        }

        public record StringOption(@Option("d") String dir) {
        }

        @Test
        void should_throw_IllegalArgumentException_when_parse_Illegal_Input() {
            assertThrows(IllegalArgValueException.class, () -> new Args("-p /usr/logs").parse(IntOption.class));
        }

        @Test
        void should_throw_MissingArgValueException_when_parse_if_miss_arg_value() {
            assertThrows(MissingArgValueException.class, () -> new Args("-p").parse(IntOption.class));
            assertThrows(MissingArgValueException.class, () -> new Args("-d").parse(StringOption.class));
        }

        @Test
        void should_throw_TooManyArgValueException_when_parse_if_too_many_arg_value() {
            assertThrows(TooManyArgValueException.class, () -> new Args("-d /usr/logs /uer/vars").parse(StringOption.class));
            assertThrows(TooManyArgValueException.class, () -> new Args("-p 8080 8081").parse(IntOption.class));
        }
    }
}