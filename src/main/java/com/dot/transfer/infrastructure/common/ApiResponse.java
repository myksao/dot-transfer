package com.dot.transfer.infrastructure.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ApiResponse<T> {
    public boolean success;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timeStamp;
    public String message;
    public T data;


    public static <T> ApiResponse<T> empty() {
        return success(null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .message("SUCCESS!")
                .data(data)
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error() {
        return ApiResponse.<T>builder()
                .message("ERROR!")
                .success(false)
                .timeStamp(LocalDateTime.now())
                .build();
    }
}