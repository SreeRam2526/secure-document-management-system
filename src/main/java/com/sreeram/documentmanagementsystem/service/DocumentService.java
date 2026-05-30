package com.sreeram.documentmanagementsystem.service;

import com.sreeram.documentmanagementsystem.dto.DocumentResponse;
import com.sreeram.documentmanagementsystem.entity.Document;
import com.sreeram.documentmanagementsystem.entity.User;
import com.sreeram.documentmanagementsystem.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(DocumentService.class);

    public String uploadDocument(
            MultipartFile file
    ) throws IOException {

        logger.info(
                "Uploading document: {}",
                file.getOriginalFilename()
        );

        User user = (User)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        String uploadDir =
                System.getProperty("user.dir")
                        + "/uploads/";

        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath =
                uploadDir + file.getOriginalFilename();

        file.transferTo(new File(filePath));

        Document document = Document.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .filePath(filePath)
                .uploadedAt(LocalDateTime.now())
                .user(user)
                .build();

        documentRepository.save(document);

        logger.info(
                "Document uploaded successfully: {}",
                file.getOriginalFilename()
        );

        return "File uploaded successfully";
    }

    public List<DocumentResponse> getMyDocuments() {

        User user = (User)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        logger.info(
                "Fetching documents for user: {}",
                user.getEmail()
        );

        List<Document> documents =
                documentRepository.findByUser(user);

        return documents.stream()
                .map(document -> DocumentResponse.builder()
                        .id(document.getId())
                        .fileName(document.getFileName())
                        .fileType(document.getFileType())
                        .uploadedAt(document.getUploadedAt())
                        .build())
                .toList();
    }

    public Resource downloadDocument(
            Long documentId
    ) throws IOException {

        User user = (User)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        logger.info(
                "Download request for document id: {}",
                documentId
        );

        Document document =
                documentRepository
                        .findByIdAndUser(documentId, user)
                        .orElseThrow(() -> {

                            logger.error(
                                    "Document not found with id: {}",
                                    documentId
                            );

                            return new RuntimeException(
                                    "Document not found"
                            );
                        });

        Path path = Paths.get(
                document.getFilePath()
        );

        logger.info(
                "Document downloaded successfully: {}",
                document.getFileName()
        );

        return new UrlResource(path.toUri());
    }

    public String deleteDocument(
            Long documentId
    ) {

        logger.info(
                "Deleting document with id: {}",
                documentId
        );

        User user = (User)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        Document document =
                documentRepository
                        .findByIdAndUser(documentId, user)
                        .orElseThrow(() -> {

                            logger.error(
                                    "Delete failed. Document not found: {}",
                                    documentId
                            );

                            return new RuntimeException(
                                    "Document not found"
                            );
                        });

        File file = new File(
                document.getFilePath()
        );

        if (file.exists()) {
            file.delete();
        }

        documentRepository.delete(document);

        logger.info(
                "Document deleted successfully: {}",
                documentId
        );

        return "Document deleted successfully";
    }
}