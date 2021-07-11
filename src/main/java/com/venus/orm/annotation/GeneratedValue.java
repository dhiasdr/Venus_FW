package com.venus.orm.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import com.venus.orm.enum_.GenerationType;
@Retention(RUNTIME)
public @interface GeneratedValue {
	GenerationType strategy() default GenerationType.IDENTITY;

}
