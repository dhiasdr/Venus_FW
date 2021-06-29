package com.venus.core.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface Controller {
	boolean isSingleton() default true;
	String scope() default "Singleton";
	String factoryBean() default "";
	String factoryMethod() default "";
	String initMethod() default "";
	String destroyMethod() default "";
}
