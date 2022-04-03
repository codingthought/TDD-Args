import java.lang.reflect.InvocationTargetException;

public class Args {
    public Args(String input) {
    }

    public <T> T parse(Class<T> optionsClass) {
        try {
            return (T) optionsClass.getDeclaredConstructors()[0].newInstance(true);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("parse exception", e);
        }
    }
}
