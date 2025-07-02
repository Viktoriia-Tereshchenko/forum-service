package ait.cohort5860.post.service.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// the annotation is visible in RUNTIME
@Retention(RetentionPolicy.RUNTIME)
// can be placed above the method
@Target({ElementType.METHOD})
public @interface PostLogger {
}
