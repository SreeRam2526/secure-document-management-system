package com.sreeram.documentmanagementsystem.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String message;

    private int status;

    private LocalDateTime timestamp;
}