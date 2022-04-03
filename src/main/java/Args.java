import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Function;

public class Args {
    private final String input;
    private final Map<String, Function<String, ?>> PARSERS = Map.of(
            "-l", s -> true,
            "-p", Integer::valueOf,
            "-d", s -> s
    );

    public Args(String input) {
        this.input = input;
    }

    public <T> T parse(Class<T> optionsClass) {
        try {
            String[] args = input.split(" ");
            Constructor<?> constructor = optionsClass.getDeclaredConstructors()[0];
            return (T) constructor.newInstance(toParam(args));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("parse exception", e);
        }
    }

    private Object toParam(String[] args) {
        String argValue = args.length > 1 ? args[1] : "";
        return PARSERS.get(args[0]).apply(argValue);
    }
}
