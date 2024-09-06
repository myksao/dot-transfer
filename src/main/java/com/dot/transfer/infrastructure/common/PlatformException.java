package com.dot.transfer.infrastructure.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class PlatformException extends RuntimeException {
    public HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    public LocalDateTime timeStamp;
    public String message;
    public String debugMessage;
    public PlatformException(String message, HttpStatus status, String debugMessage) {
        super(message);
        this.message = message;
        this.status = status;
        this.debugMessage = debugMessage;
        timeStamp = LocalDateTime.now();
    }
}
