package ait.cohort5860.post.service.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// аннотация видна в RUNTIME
@Retention(RetentionPolicy.RUNTIME)
// можно ставить над методом
@Target({ElementType.METHOD})
public @interface PostLogger {
}
