import annotation.Option;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;

public class Args {
    public static final String SPACE = " ";
    private final Map<Class<?>, Function<String, ?>> PARSERS = Map.of(
            boolean.class, s -> true,
            int.class, Integer::valueOf,
            String.class, s -> s,
            int[].class, s -> Arrays.stream(s.split(SPACE)).mapToInt(Integer::parseInt).toArray(),
            String[].class, s -> s.split(SPACE)
    );
    private final Map<String, String[]> argMap;

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
            String[] params = argMap.get(p.getAnnotation(Option.class).value());
            Class<?> paramType = p.getType();
            if (paramType.isArray()) return PARSERS.get(paramType).apply(String.join(" ", params));
            return PARSERS.get(paramType).apply(params[0]);
        }).toArray();
    }

    private Map<String, String[]> toMap(String[] args) {
        Map<String, String[]> result = new HashMap<>();
        if (args.length == 1) {
            return Map.of(args[0], new String[]{""});
        }
        List<String> values = new ArrayList<>();
        for (int i = 0; i < args.length - 1; i++) {
            String current = args[i];
            String regex = "^-[a-zA-Z]+$";
            if (current.matches(regex)) {
                if (args[i + 1].matches(regex)) {
                    result.put(current, new String[]{""});
                    continue;
                }
                for (int j = i + 1; j < args.length; j++) {
                    values.add(args[j]);
                    if (j == args.length - 1 || args[j + 1].matches(regex)) {
                        result.put(current, values.toArray(String[]::new));
                        break;
                    }
                }
            }
            values.clear();
        }
        return result;
    }
}
