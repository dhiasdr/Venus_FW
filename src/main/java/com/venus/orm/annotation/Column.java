package com.venus.orm.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface Column {
	String name() default "";
	boolean notNull() default false;
	boolean unique() default false;
    boolean autoIncrement () default false;

    
}
