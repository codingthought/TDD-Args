import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Args {
    private final String input;

    public Args(String input) {
        this.input = input;
    }

    public <T> T parse(Class<T> optionsClass) {
        try {
            String[] args = input.split(" ");
            Constructor<?> constructor = optionsClass.getDeclaredConstructors()[0];
            if (args.length > 1) {
                return (T) constructor.newInstance(Integer.valueOf(args[1]));
            }
            return (T) constructor.newInstance(true);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("parse exception", e);
        }
    }
}
