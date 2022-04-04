package tdd;

import org.junit.jupiter.api.Test;
import tdd.annotation.Option;
import tdd.exception.IllegalArgValueException;
import tdd.exception.MissingArgValueException;
import tdd.exception.TooManyArgValueException;

import static org.junit.jupiter.api.Assertions.*;

public class ArgsTest {
    /**
     * 题目：
     * 我们中的大多数人都不得不时不时地解析一下命令行参数。
     * 如果我们没有一个方便的工具，那么我们就简单地处理一下传入 main 函数的字符串数组。
     * 有很多开源工具可以完成这个任务，但它们可能并不能完全满足我们的要求。
     * 所以我们再写一个吧。传递给程序的参数由标志和值组成。
     * 标志应该是一个字符，前面有一个减号。每个标志都应该有零个或多个与之相关的值。
     * 例如：
     * -l -p 8080 -d /usr/logs
     * "l"（日志）没有相关的值，它是一个布尔标志，如果存在则为 true，不存在则为 false。
     * "p"（端口）有一个整数值，"d"（目录）有一个字符串值。
     * 标志后面如果存在多个值，则该标志表示一个列表：
     * -g this is a list -d 1 2 -3 5
     * "g"表示一个字符串列表["this", "is", "a", "list"]，
     * "d"标志表示一个整数列表[1, 2, -3, 5]。
     * 如果参数中没有指定某个标志，那么解析器应该指定一个默认值。
     * 例如，false 代表布尔值，0 代表数字，""代表字符串，[]代表列表。
     * 如果给出的参数与模式不匹配，重要的是给出一个好的错误信息，准确地解释什么是错误的。
     * 确保你的代码是可扩展的，即如何增加新的数值类型是直接和明显的。
     */
//    happy path
    // done given -l when parse then get ture
    // done given -p 8080 when parse then get 8080
    // done given -d /usr/logs when parse then get /usr/logs
    // done given -l -p 8080 -d /usr/logs when parse then get {true,8080,/usr/logs}
    // done given -g this is a list when parse then get {this, is, a, list}
    // done given -d 1 2 -3 5 when parse then get {1, 2, -3, 5}
//    sad path
    // done miss -l when parse then get false
    // done miss -p when parse then get 0
    // done miss -d when parse then get ""
    // done given -p /usr/logs when parse then throw IllegalArgValueException
    // done given -p when parse then throw MissingArgValueException
    // done given -d /usr/logs /uer/vars when parse then throw TooManyArgValueException

    @Test
    void should_get_8080_when_parse_IntOption() {
        assertEquals(8080, new Args("-p 8080").parse(IntOption.class).port());
    }

    record IntOption(@Option("p") int port) {
    }

    @Test
    void should_get_usr_logs_when_parse_StringOption() {
        assertEquals("/usr/logs", new Args("-d /usr/logs").parse(StringOption.class).dir());
    }

    record StringOption(@Option("d") String dir) {
    }

    @Test
    void should_get_correct_options_when_parse_Options() {
        Options options = new Args("-l -p 8080 -d /usr/logs").parse(Options.class);
        assertTrue(options.logging());
        assertEquals(8080, options.port());
        assertEquals("/usr/logs", options.directory());
    }

    @Test
    void should_get_options_with_default_value_when_parse_Options_if_param_miss() {
        Options options = new Args("").parse(Options.class);
        assertFalse(options.logging());
        assertEquals(0, options.port());
        assertEquals("", options.directory());
    }

    record Options(@Option("l") boolean logging, @Option("p") int port, @Option("d") String directory) {

    }

    @Test
    void should_get_correct_array_options_when_parse_ArrayOptions() {
        ArrayOptions options = new Args("-g this is a list -d 1 2 -3 5").parse(ArrayOptions.class);
        assertArrayEquals(new String[]{"this", "is", "a", "list"}, options.groups());
        assertArrayEquals(new int[]{1, 2, -3, 5}, options.decimals());
    }

    record ArrayOptions(@Option("g") String[] groups, @Option("d") int[] decimals) {
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
