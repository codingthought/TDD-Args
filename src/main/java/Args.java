import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.lang.Boolean.TRUE;

public class Args {
    private String args;
    private static final Map<String, Function<String, ?>> PARSERS = Map.of(
            "-l", s -> TRUE,
            "-p", Integer::valueOf,
            "-d", s -> s
    );

    public Args(String args) {
        this.args = args;
    }

    public <T> T parseInto(Class<T> optionsClass) {
        try {
            Map<String, String> argMap = toStringMap(args);
            if (optionsClass.isPrimitive() || String.class.equals(optionsClass)) {
                return (T) argMap.entrySet().stream().findFirst().map(e -> PARSERS.get(e.getKey()).apply(e.getValue())).orElse(null);
            }
            Constructor<?>[] constructors = optionsClass.getDeclaredConstructors();
            List<?> paras = Arrays.stream(constructors[0].getParameters())
                    .map(p -> {
                        Option annotation = p.getAnnotation(Option.class);
                        if (annotation == null) {
                            String uniqueKey = argMap.keySet().iterator().next();
                            System.out.println(uniqueKey);
                            return PARSERS.get(uniqueKey).apply(argMap.get(uniqueKey));
                        }
                        String optionName = annotation.value();
                        String key = "-" + optionName;
                        return PARSERS.get(key).apply(argMap.get(key));
                    }).toList();
            System.out.println(paras);
            return (T) constructors[0].newInstance(paras.toArray());
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, String> toStringMap(String args) {
        String[] splits = args.split(" ");
        if (splits.length < 1) {
            return Map.of();
        }
        if (splits.length < 2) {
            return Map.of(splits[0], "");
        }
        return Map.of(splits[0], splits[1]);
    }
}
