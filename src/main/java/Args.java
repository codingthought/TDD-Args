import annotation.Option;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
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
            Parameter[] parameters = constructor.getParameters();
            Map<String, String> argMap = toMap(args);
            if (parameters.length > 1) {
                Object[] params = Arrays.stream(parameters).map(p -> {
                    String key = p.getAnnotation(Option.class).value();
                    return PARSERS.get(key).apply(argMap.get(key));
                }).toArray();
                return (T) constructor.newInstance(params);
            }
            return (T) constructor.newInstance(toParam(args));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("parse exception", e);
        }
    }

    private Map<String, String> toMap(String[] args) {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < args.length - 1; i++) {
            String current = args[i];
            if (current.startsWith("-")) {
                String value = args[i + 1];
                if (value.startsWith("-")) {
                    value = "";
                } else {
                    i++;
                }
                result.put(current, value);
            }
        }
        return result;
    }

    private Object toParam(String[] args) {
        String argValue = args.length > 1 ? args[1] : "";
        return PARSERS.get(args[0]).apply(argValue);
    }
}
