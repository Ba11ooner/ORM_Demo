package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //对象 即 表
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String value();
}
