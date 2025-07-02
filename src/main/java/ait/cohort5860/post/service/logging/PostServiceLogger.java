package ait.cohort5860.post.service.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Service;

@Service
// default for logging in Spring
@Slf4j(topic = "Post Service")
// @Aspect - module defining cross-cutting concerns
@Aspect
public class PostServiceLogger {

    //Pointcut - an expression that specifies to which JoinPoints to apply the Advice
    @Pointcut("execution(public * ait.cohort5860.post.service.PostServiceImpl.*(Long)) && args(id))")
    public void findById(Long id) {
    }

    // all who are annotated by PostLogger
    @Pointcut("@annotation(ait.cohort5860.post.service.logging.PostLogger)")
    public void annotatePostLogger() {
    }

    @Pointcut("execution(public java.util.List<ait.cohort5860.post.dto.PostDto> ait.cohort5860.post.service.PostServiceImpl.findPosts*(..))")
    public void bulkFindPostsLogger() {
    }

    // @Before - before executing the method
    @Before("findById(id)")
    public void logById(Long id) {
        // write logging -> to the console
        log.info("Find post by id {}", id);
    }

    // @AfterReturning - after successful completion of the method
    // JoinPoint object contains all information about the method (+ its parameters and what it returns)
    @AfterReturning("annotatePostLogger()")
    public void logAnnotatePostLogger(JoinPoint joinPoint) {
        log.info("Annotated by PostLogger method: {}, done", joinPoint.getSignature().getName());
    }

    // ProceedingJoinPoint - gives control over the method
    @Around("bulkFindPostsLogger()")
    public Object loBulkFindPostsLogger(ProceedingJoinPoint joinPoint) throws Throwable {
        // array of arguments
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String str) {
                args[i] = str.toLowerCase();
            }
        }
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed(args);
        long end = System.currentTimeMillis();
        log.info("method: {}, time: {} ms", joinPoint.getSignature().getName(), end - start);
        return result;
    }
}
