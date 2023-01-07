package com.tinyurl.exception;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(ValidationException.class)
    public @ResponseBody
    ApiError handleValidationException(ValidationException e) {
        log.error(e.getMessage(), e);
        return new ApiError(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody
    ApiError handleNotFoundException(NotFoundException e) {
        log.error(e.getMessage(), e);
        return new ApiError(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT) // 409
    @ExceptionHandler(AlreadyExistsException.class)
    public @ResponseBody
    ApiError handleAlreadyExistsException(AlreadyExistsException e) {
        log.error(e.getMessage(), e);
        return new ApiError(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler(Exception.class)
    public @ResponseBody
    ApiError handleAllException(Exception e) {
        log.error(e.getMessage(), e);
        return new ApiError(e.getMessage());
    }
}
