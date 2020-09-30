package com.jd.datamill9n.component.processor.annotation;

import com.jd.datamill9n.component.processor.post.Post;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO
 *
 * @author maliang56
 * @date 2020-09-30
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JsfSchema {
    String datasource();
    Class<?> tableBean() default Void.class;
    Class<? extends Post>[] postActions() default {};
    String transformSpecs();
}