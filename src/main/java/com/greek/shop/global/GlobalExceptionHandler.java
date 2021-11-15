package com.greek.shop.global;

import com.greek.shop.entity.Result;
import com.greek.shop.exception.NotAuthorizedException;
import com.greek.shop.exception.ResourceNotFoundException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author Zhaofeng Zhou
 * @date 2021/11/9/009 14:49
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void paramIsInvalid(MethodArgumentNotValidException ex) {
        ex.getBindingResult().getFieldError().getDefaultMessage();
    }

    @ExceptionHandler(NotAuthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result notAuthorized(NotAuthorizedException ex) {
        return Result.failure(ex.getMessage());
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result notFound(ResourceNotFoundException ex) {
        return Result.failure(ex.getMessage());
    }
}
