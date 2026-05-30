package com.sreeram.documentmanagementsystem.controller;

import com.sreeram.documentmanagementsystem.dto.DocumentResponse;
import com.sreeram.documentmanagementsystem.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    public String uploadDocument(
            @RequestParam("file")
            MultipartFile file
    ) throws IOException {

        return documentService.uploadDocument(file);
    }

    @GetMapping("/my-documents")
    public List<DocumentResponse> getMyDocuments() {

        return documentService.getMyDocuments();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long id
    ) throws IOException {

        Resource resource =
                documentService.downloadDocument(id);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                resource.getFilename() + "\""
                )
                .body(resource);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteDocument(
            @PathVariable Long id
    ) {

        return documentService.deleteDocument(id);
    }
}