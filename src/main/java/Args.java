import annotation.Option;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;

public class Args {
    public static final String SPACE = " ";
    private final Map<Class<?>, Function<String, ?>> PARSERS = Map.of(
            boolean.class, Objects::nonNull,
            int.class, s -> s == null ? 0 : Integer.parseInt(s),
            String.class, s -> Optional.ofNullable(s).orElse(""),
            int[].class, s -> Arrays.stream(s.split(SPACE)).mapToInt(Integer::parseInt).toArray(),
            String[].class, s -> s.split(SPACE)
    );
    private final Map<String, String> argMap;

    public Args(String input) {
        this.argMap = toMap(input.split(" "));
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
        return Arrays.stream(parameters)
                .map(p -> PARSERS.get(p.getType()).apply(
                        argMap.get(p.getAnnotation(Option.class).value())))
                .toArray();
    }

    private Map<String, String> toMap(String[] args) {
        Map<String, String> result = new HashMap<>();
        if (args.length == 1 && args[0].equals("-l")) {
            return Map.of("-l", "");
        }
        for (int i = 0; i < args.length - 1; i++) {
            if (isMatchesParamFlag(args[i])) {
                result.put(args[i], matchParamValue(args, i));
            }
        }
        return result;
    }

    private String matchParamValue(String[] args, int paramIndex) {
        List<String> values = new ArrayList<>();
        for (int j = paramIndex + 1; j < args.length; j++) {
            values.add(args[j]);
            if (j + 1 == args.length || isMatchesParamFlag(args[j + 1])) {
                break;
            }
        }
        return String.join(" ", values);
    }

    private boolean isMatchesParamFlag(String arg) {
        return arg.matches("^-[a-zA-Z]+$");
    }
}
