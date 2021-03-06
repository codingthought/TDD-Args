package tdd;

import org.junit.jupiter.api.Test;
import tdd.annotation.Option;

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
        ArrayOptions options = new Args("-g this is a list -d 1 2 -3 5 -n 1 -3 5").parse(ArrayOptions.class);
        assertArrayEquals(new String[]{"this", "is", "a", "list"}, options.groups());
        assertArrayEquals(new Integer[]{1, 2, -3, 5}, options.decimals());
        assertArrayEquals(new int[]{1, -3, 5}, options.numbers());
    }

    @Test
    void should_get_array_options_with_default_value_when_parse_ArrayOptions_if_param_miss() {
        ArrayOptions options = new Args("").parse(ArrayOptions.class);
        assertArrayEquals(new String[]{}, options.groups());
        assertArrayEquals(new Integer[]{}, options.decimals());
        assertArrayEquals(new int[]{}, options.numbers());
    }

    record ArrayOptions(@Option("g") String[] groups, @Option("d") Integer[] decimals, @Option("n") int[] numbers) {
    }
}
