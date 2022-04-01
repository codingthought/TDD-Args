import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

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
// happy path
// done given -l when parse then true
// done given -p 8080 when parse then 8080
// done given -d /usr/logs when parse then "/usr/logs"
// done multi： given -l -p 8080 -d /usr/logs when parse then true 8080 "/usr/logs"
// sad path
// todo -l 1
// todo -p /usr/logs
// todo -d /usr/logs /usr/vars
// default path
// todo given -p when parse then 0
// todo given -d when parse then ""


public class ArgsTest {
    @Test
    @DisplayName("当输入 -l 时，解析为 bool 时，应该得到 true")
    void should_get_true_when_parse_into_bool() {
        assertTrue(new Args("-l").parseInto(boolean.class));
    }

    @Test
    @DisplayName("当输入 -p 8080 时，解析为 int 时，应该得到 8080")
    void should_get_8080_when_parse_into_int() {
        assertEquals(8080, new Args("-p 8080").parseInto(int.class));
    }

    @Test
    @DisplayName("当输入 -d /usr/logs 时，解析为 String 时，应该得到 /usr/logs")
    void should_get_correct_string_when_parse_into_string() {
        assertEquals("/usr/logs", new Args("-d /usr/logs").parseInto(String.class));
    }

    @Test
    @DisplayName("当输入 -l -p 8080 -d /usr/logs 时，解析为 Options 时，应该得到正确的对象")
    void should_get_correct_options_when_parse_into_options() {
        // -l -p 8080 -d /usr/logs
        Args args = spy(new Args("-l -p 8080 -d /usr/logs"));
        when(args.toStringMap(anyString())).thenReturn(Map.of("-l", "",
                "-p", "8080",
                "-d", "/usr/logs"));
        Options options = args.parseInto(Options.class);

        assertTrue(options.booleanOption());
        assertEquals(8080, options.intOption());
        assertEquals("/usr/logs", options.stringOption());
    }

    @Disabled
    @Test
    void example2() {
        // -g this is a list -d 1 2 -3 5
        ArrayOptions options = new Args("-g this is a list -d 1 2 -3 5").parseInto(ArrayOptions.class);
        assertArrayEquals(new String[]{"this", "is", "a", "list"}, options.strings());
        assertArrayEquals(new Integer[]{1, 2, -3, 5}, options.integers());
    }

    record Options(@Option("l") boolean booleanOption, @Option("p") int intOption, @Option("d") String stringOption) {
    }

    record ArrayOptions(@Option("g") String[] strings, @Option("d") Integer[] integers) {
    }
}
