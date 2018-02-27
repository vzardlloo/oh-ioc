package ioc.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: vzard
 * Date: 2018/2/24
 * Time: 21:55
 * To change this template use File | Settings | File Templates.
 * Description:
 **/

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

    String value() default "";

}
