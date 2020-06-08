package indi.faniche.anonyshop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* File:   LoginRequired.java
 * -------------------------
 * Author: faniche
 * Date:   4/30/20
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
    boolean loginSuccess() default true;
}
