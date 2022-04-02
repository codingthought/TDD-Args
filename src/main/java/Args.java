import exception.IllegalInputException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Args {
    private String args;
    private static final Map<String, Function<String, ?>> PARSERS = Map.of(
            "-l", s -> TRUE,
            "-p", Integer::valueOf,
            "-d", s -> s
    );
    private static final Map<Class<?>, ?> DEFAULT_VALUE = Map.of(
            boolean.class, FALSE,
            int.class, 0,
            String.class, ""
    );

    public Args(String args) {
        this.args = args;
    }

    public <T> T parseInto(Class<T> optionsClass) {
        Map<String, String> argMap = toStringMap();
        if (optionsClass.isPrimitive() || String.class.equals(optionsClass)) {
            return parseSingleType(optionsClass, argMap);
        }
        return parseCustomType(optionsClass, argMap);
    }

    private <T> T parseCustomType(Class<T> optionsClass, Map<String, String> argMap) {
        try {
            Constructor<?>[] constructors = optionsClass.getDeclaredConstructors();
            List<?> params = Arrays.stream(constructors[0].getParameters())
                    .map(p -> {
                        Option annotation = p.getAnnotation(Option.class);
                        String optionName = annotation.value();
                        String key = "-" + optionName;
                        String value = argMap.get(key);
                        if (value == null) {
                            return DEFAULT_VALUE.get(p.getType());
                        }
                        checkArgStringValue(p.getType(), value);
                        return PARSERS.get(key).apply(value);
                    }).toList();
            return (T) constructors[0].newInstance(params.toArray());
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T> T parseSingleType(Class<T> optionsClass, Map<String, String> argMap) {
        return (T) argMap.entrySet().stream().findFirst().map(e -> {
            String value = e.getValue();
            checkArgStringValue(optionsClass, value);
            return PARSERS.get(e.getKey()).apply(value);
        }).orElse(null);
    }

    private <T> void checkArgStringValue(Class<T> optionsClass, String value) {
        int splitLength = value.split(" ").length;
        if (optionsClass.equals(boolean.class) && value.length() > 0) {
            throw new IllegalInputException(String.format("illegal value: %s", value));
        }
        if (optionsClass.equals(int.class) && !value.matches("\\d+")) {
            throw new IllegalInputException(String.format("illegal value: %s", value));
        }
        if (splitLength > 1) {
            throw new IllegalInputException(String.format("illegal value: %s", value));
        }
    }

    public Map<String, String> toStringMap() {
        Matcher matcher = Pattern.compile("-[a-zA-Z]+").matcher(args);
        String[] splits = (args + " ").split("-[a-zA-Z]+");
        if (splits.length == 0) return Map.of(args, "");
        Map<String, String> agrMap = new HashMap<>();
        int keyNumber = 1;
        while (matcher.find()) {
            agrMap.put(matcher.group(), splits[keyNumber++].trim());
        }
        return agrMap;
    }
}
