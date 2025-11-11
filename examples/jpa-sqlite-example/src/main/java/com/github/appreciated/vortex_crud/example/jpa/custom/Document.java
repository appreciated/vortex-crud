package com.github.appreciated.vortex_crud.example.jpa.custom;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a text file in the filesystem.
 */
@Data
@NoArgsConstructor
public class Document {

    private Long id;
    private String fileName;
    private String title;
    private String content;
    private Long fileSize;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public Document(String fileName, String title, String content) {
        this.fileName = fileName;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
        this.fileSize = content != null ? (long) content.length() : 0L;
    }

    public void setContent(String content) {
        this.content = content;
        this.fileSize = content != null ? (long) content.length() : 0L;
    }
}
