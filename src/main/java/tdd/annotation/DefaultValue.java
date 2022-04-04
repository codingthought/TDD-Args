package tdd.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultValue {
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
            Integer[].class, d -> Arrays.stream(d.intArrayValue()).boxed().toArray(Integer[]::new),
            String[].class, DefaultValue::stringArrayValue);
}
