package tdd;

import tdd.annotation.Option;
import tdd.parser.Parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

import static tdd.parser.Parsers.*;

public class Args {
    private static final String SPACE = " ";
    private final Map<Class<?>, Parser<?>> PARSERS = Map.of(
            boolean.class, bool(),
            int.class, unary(Integer::parseInt, 0, given1 -> !given1.matches("\\d+")),
            String.class, unary(String::valueOf, "", given -> false),
            int[].class, s -> Optional.ofNullable(s)
                    .map(given -> Arrays.stream(given.split(SPACE)).mapToInt(Integer::parseInt).toArray())
                    .orElse(new int[0]),
            Integer[].class, array(Integer::parseInt, Integer[]::new),
            String[].class, array(String::valueOf, String[]::new)
    );

    private final Map<String, String> argMap;

    public Args(String input) {
        this.argMap = toMap(input.split(SPACE));
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
                .map(p -> PARSERS.get(p.getType()).parse(argMap.get(p.getAnnotation(Option.class).value())))
                .toArray();
    }

    private Map<String, String> toMap(String[] args) {
        Map<String, String> result = new HashMap<>();
        if (args.length == 1 && !args[0].isBlank()) {
            return Map.of(args[0].substring(1), "");
        }
        for (int i = 0; i < args.length - 1; i++) {
            if (isMatchesParamFlag(args[i])) {
                result.put(args[i].substring(1), matchParamValue(args, i));
            }
        }
        return result;
    }

    private String matchParamValue(String[] args, int paramIndex) {
        List<String> values = new ArrayList<>();
        for (int j = paramIndex + 1; j < args.length; j++) {
            if (isMatchesParamFlag(args[j])) break;
            values.add(args[j]);
        }
        return String.join(" ", values);
    }

    private boolean isMatchesParamFlag(String arg) {
        return arg.matches("^-[a-zA-Z]+$");
    }
}
