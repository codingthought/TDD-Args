import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface Option {
    String value();
}
