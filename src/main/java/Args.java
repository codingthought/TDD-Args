import annotation.Option;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Args {
    private final Map<String, Function<String, ?>> PARSERS = Map.of(
            "-l", s -> true,
            "-p", Integer::valueOf,
            "-d", s -> s
    );
    private final Map<String, String> argMap;

    public Args(String input) {
        String[] args = input.split(" ");
        this.argMap = toMap(args);
    }

    public <T> T parse(Class<T> optionsClass) {
        try {
            Constructor<?> constructor = optionsClass.getDeclaredConstructors()[0];
            return (T) constructor.newInstance(toParams(constructor.getParameters()));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("parse exception", e);
        }
    }

    private Object[] toParams(Parameter[] parameters) {
        return Arrays.stream(parameters).map(p -> {
            String key = p.getAnnotation(Option.class).value();
            return PARSERS.get(key).apply(argMap.get(key));
        }).toArray();
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
}
