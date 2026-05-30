package com.sreeram.documentmanagementsystem.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ApiResponse {

    private boolean success;

    private String message;

    private LocalDateTime timestamp;
}