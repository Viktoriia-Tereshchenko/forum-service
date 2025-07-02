package ait.cohort5860.post.service.logging;

import ait.cohort5860.post.dto.PostDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Service;

@Service
// по умолчанию для логирования в Spring-е
@Slf4j(topic = "Post Service")  // topic = название, идет перед логом
// @Aspect - модуль, определяющий сквозную функциональность
@Aspect
public class PostServiceLogger {

    //Pointcut - выражение, определяющее, к каким точкам соединения применять совет
    @Pointcut("execution(public * ait.cohort5860.post.service.PostServiceImpl.*(Long)) && args(id))")
    public void findById(Long id) {
    }
    // ait.cohort5860.post.service.PostServiceImpl.findPostById(Long) - если конткретный метод

    // все, кто помечен аннотацией PostLogger
    @Pointcut("@annotation(ait.cohort5860.post.service.logging.PostLogger)")
    public void annotatePostLogger() {
    }

    @Pointcut("execution(public java.util.List<ait.cohort5860.post.dto.PostDto> ait.cohort5860.post.service.PostServiceImpl.findPosts*(..))")
    public void bulkFindPostsLogger() {
    }


    // Advice - действие, выполняемое в определённый момент выполнения (например, до или после метода)
    // @Before - перед выполнением метода
    @Before("findById(id)")
    public void logById(Long id) {
        // запись логирования - в консоли
        log.info("Find post by id {}", id);
    }

    // @AfterReturning - после завершения метода
    // объект JoinPoint - содержит всю информацию о методе (+ его параметры и что возвращает)
    @AfterReturning("annotatePostLogger()")
    public void logAnnotatePostLogger(JoinPoint joinPoint) {
        log.info("Annotated by PostLogger method: {}, done", joinPoint.getSignature().getName());
    }

    // ProceedingJoinPoint - какие аргументы в методе
    @Around("bulkFindPostsLogger()")
    public Object loBulkFindPostsLogger(ProceedingJoinPoint joinPoint) throws Throwable {
        // массив аргументов
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
