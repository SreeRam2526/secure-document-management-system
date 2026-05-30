package com.sreeram.documentmanagementsystem.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DocumentResponse {

    private Long id;

    private String fileName;

    private String fileType;

    private LocalDateTime uploadedAt;
}