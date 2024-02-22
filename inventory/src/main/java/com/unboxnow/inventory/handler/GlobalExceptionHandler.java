package com.unboxnow.inventory.handler;

import com.unboxnow.common.exception.*;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, Object> map = new HashMap<>();
        List<String> list = new LinkedList<>();
        ex.getBindingResult().getFieldErrors().forEach(x -> {
            String message = String.format("Field -> %s, Invalid value -> %s, Reason -> %s",
                    x.getField(),
                    x.getRejectedValue(),
                    x.getDefaultMessage());
            log.error(message);
            list.add(message);
        });
        map.put("msg", list);
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> map = new HashMap<>();
        List<String> list = new LinkedList<>();
        ex.getConstraintViolations().forEach(x -> {
            String message = String.format("Path -> %s, Invalid value -> %s, Reason -> %s",
                    x.getPropertyPath(),
                    x.getInvalidValue(),
                    x.getMessage());
            log.error(message);
            list.add(message);
        });
        map.put("msg", list);
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ActiveDiscountException.class)
    public ResponseEntity<Object> handleActiveDiscount(ActiveDiscountException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ApplicableException.class)
    public ResponseEntity<Object> handleApplicable(ApplicableException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomExecutionException.class)
    public ResponseEntity<Object> handleExecution(CustomExecutionException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(CustomHttpRequestException.class)
    public ResponseEntity<Object> handleHttpRequest(CustomHttpRequestException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(CustomInterruptedException.class)
    public ResponseEntity<Object> handleInterrupted(CustomInterruptedException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Object> handleDuplicate(DuplicateException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalMessageException.class)
    public ResponseEntity<Object> handleIllegalMessage(IllegalMessageException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalValueException.class)
    public ResponseEntity<Object> handleIllegalParams(IllegalValueException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(JsonException.class)
    public ResponseEntity<Object> handleJson(JsonException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Object> handleLogin(LoginException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxQuantityException.class)
    public ResponseEntity<Object> handleMaxQuantity(MaxQuantityException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MinQuantityException.class)
    public ResponseEntity<Object> handleMinQuantity(MinQuantityException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotCompletedException.class)
    public ResponseEntity<Object> handleNotCompleted(NotCompletedException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<Object> handleNotAuthorized(NotAuthorizedException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFound(NotFoundException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotRespondedException.class)
    public ResponseEntity<Object> handleNotResponded(NotRespondedException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(PublishException.class)
    public ResponseEntity<Object> handlePublish(PublishException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<Object> handleToken(TokenException ex) {
        Map<String, String> map = new HashMap<>();
        log.error(ex.getMessage());
        map.put("msg", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
