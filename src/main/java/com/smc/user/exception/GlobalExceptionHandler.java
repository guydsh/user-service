package com.smc.user.exception;

import com.smc.user.dto.ResponseResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        Set<String> errorMessages = new HashSet<>();
        fieldErrors.forEach(fieldError -> errorMessages.add(fieldError.getDefaultMessage()));

        Map<String, Object> extraInfo = new HashMap<>();
        extraInfo.put("fieldErrors", errorMessages);
        return ResponseResult.fail("method argument not valid");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseResult handlerException(RuntimeException e) {
        return ResponseResult.fail(e.getMessage());
    }

}
