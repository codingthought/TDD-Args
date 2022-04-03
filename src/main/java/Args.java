import java.lang.reflect.InvocationTargetException;

public class Args {
    private String input;

    public Args(String input) {
        this.input = input;
    }

    public <T> T parse(Class<T> optionsClass) {
        try {
            String[] args = input.split(" ");
            if (args.length > 1) {
                return (T) optionsClass.getDeclaredConstructors()[0].newInstance(Integer.valueOf(args[1]));
            }
            return (T) optionsClass.getDeclaredConstructors()[0].newInstance(true);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("parse exception", e);
        }
    }
}
