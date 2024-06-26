package com.yubit.util.http;

import com.yubit.api.exceptions.BadRequestException;
import com.yubit.api.exceptions.InvalidInputException;
import com.yubit.api.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody HttpErrorInfo handleBadRequestException(ServerHttpRequest request, NotFoundException exception){
        return createHttpErrorInfo(HttpStatus.BAD_REQUEST, request, exception);
    }

    // Interceptor que maneja la excepcion NotFoundException cuando esta se prouzca
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody HttpErrorInfo handlerNotFoundException(ServerHttpRequest request, NotFoundException exception){
        // atrapamos la excepcion, tomamos sus datos y retornamos un objeto HttpErrorInfo
        return createHttpErrorInfo(HttpStatus.NOT_FOUND, request, exception);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody HttpErrorInfo handlerUnprocessableEntityException(ServerHttpRequest request, NotFoundException exception){
        // atrapamos la excepcion, tomamos sus datos y retornamos un objeto HttpErrorInfo
        return createHttpErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, request, exception);
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, ServerHttpRequest request, NotFoundException exception) {
        final String path = request.getPath().pathWithinApplication().value();
        final String message = exception.getMessage();
        LOGGER.debug("Returning http status {} for path {}, message {}", httpStatus, path, message);
        return new HttpErrorInfo(httpStatus, path, message);
    }
}
