package tdd.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.function.Function;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultValue {
    Class<?> clazz() default Void.class;

    boolean booleanValue() default false;

    int intValue() default 0;

    String stringValue() default "";

    int[] intArrayValue() default {};

    String[] stringArrayValue() default {};

    Map<Class<?>, Function<DefaultValue, ?>> DEFAULT_FUNCTION_MAP = Map.of(Void.class, __ -> null,
            boolean.class, DefaultValue::booleanValue,
            int.class, DefaultValue::intValue,
            String.class, DefaultValue::stringValue,
            int[].class, DefaultValue::intArrayValue,
            String[].class, DefaultValue::stringArrayValue);
}
