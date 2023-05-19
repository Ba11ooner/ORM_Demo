package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // 字段 即 属性
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String value() default "";//字段名称

    String type();//字段数据类型

    int length() default 11;//字段长度

    boolean primaryKey() default false;//主键

    boolean allowNull() default true;//是否为空

    boolean unique() default false;//是否唯一
}
