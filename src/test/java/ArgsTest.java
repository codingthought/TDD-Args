import annotation.Option;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
     * “l”（日志）没有相关的值，它是一个布尔标志，如果存在则为 true，不存在则为 false。
     * “p”（端口）有一个整数值，“d”（目录）有一个字符串值。
     * 标志后面如果存在多个值，则该标志表示一个列表：
     * -g this is a list -d 1 2 -3 5
     * "g"表示一个字符串列表[“this”, “is”, “a”, “list”]，
     * “d"标志表示一个整数列表[1, 2, -3, 5]。
     * 如果参数中没有指定某个标志，那么解析器应该指定一个默认值。
     * 例如，false 代表布尔值，0 代表数字，”"代表字符串，[]代表列表。
     * 如果给出的参数与模式不匹配，重要的是给出一个好的错误信息，准确地解释什么是错误的。
     * 确保你的代码是可扩展的，即如何增加新的数值类型是直接和明显的。
     */
//    happy path
    // todo given -l when parse then get ture
    // todo given -p 8080 when parse then get 8080
    // todo given -d /usr/logs when parse then get /usr/logs
    // todo given -l -p 8080 -d /usr/logs when parse then get {true,8080,/usr/logs}
    // todo given -g this is a list when parse then get {this, is, a, list}
    // todo given -d 1 2 -3 5 when parse then get {1, 2, -3, 5}
//    sad path
    // todo miss -l when parse then get false
    // todo miss -p when parse then get 0
    // todo miss -d when parse then get ""
    // todo given -p /usr/logs when parse then throw IllegalArgValueException
    // todo given -p when parse then throw MissingArgValueException
    // todo given -d /usr/logs /uer/vars when parse then throw TooManyArgValueException
    @Test
    void should_get_true_when_parse_boolOption() {
        assertTrue(new Args("-l").parse(BooleanOption.class).logging());
    }

    record BooleanOption(@Option("-l") boolean logging) {
    }

    @Test
    @Disabled
    void test_1() {
        Options options = new Args("-l -p 8080 -d /usr/logs").parse(Options.class);
        assertTrue(options.logging());
        assertEquals(8080, options.port());
        assertEquals("usr/logs", options.directory());
    }

    record Options(@Option("-l") boolean logging, @Option("-p") int port, @Option("-d") String directory) {

    }

    @Test
    @Disabled
    void test_2() {
        ArrayOptions options = new Args("-g this is a list -d 1 2 -3 5").parse(ArrayOptions.class);
        assertArrayEquals(new String[]{"this", "is", "a", "list"}, options.groups());
        assertArrayEquals(new int[]{1, 2, -3, 5}, options.decimals());
    }

    record ArrayOptions(@Option("-g") String[] groups, @Option("-d") int[] decimals) {
    }
}
