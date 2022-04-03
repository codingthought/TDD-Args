import java.lang.reflect.InvocationTargetException;

public class Args {
    public Args(String input) {
    }

    public <T> T parse(Class<T> optionsClass) {
        try {
            return (T) optionsClass.getDeclaredConstructors()[0].newInstance(true);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
