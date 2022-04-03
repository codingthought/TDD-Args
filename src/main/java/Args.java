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
            return (T) constructor.newInstance(toParam(args));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("parse exception", e);
        }
    }

    private Object toParam(String[] args) {
        Object result = null;
        String arg = args[0];
        if (arg.equals("-p")) {
            result = Integer.valueOf(args[1]);
        }
        if (arg.equals("-d")) {
            result = args[1];
        }
        if (arg.equals("-l")) {
            result = true;
        }
        return result;
    }
}
