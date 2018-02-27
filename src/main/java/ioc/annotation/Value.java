package ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: vzard
 * Date: 2018/2/24
 * Time: 22:02
 * To change this template use File | Settings | File Templates.
 * Description:
 **/
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Value {
    /**
     * config for key
     * @return
     */
    String name() default "";
}
