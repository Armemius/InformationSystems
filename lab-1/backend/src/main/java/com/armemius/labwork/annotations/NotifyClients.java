package com.armemius.labwork.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotifyClients {
    String value() default "Operation successful";
}
