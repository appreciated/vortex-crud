package com.github.appreciated.vortex_crud.filesystem.entity;

import java.time.LocalDateTime;

/**
 * Represents a text file in the filesystem.
 */
public class Document {

    private Long id;
    private String fileName;
    private String title;
    private String content;
    private Long fileSize;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public Document() {
    }

    public Document(String fileName, String title, String content) {
        this.fileName = fileName;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
        this.fileSize = content != null ? (long) content.length() : 0L;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.fileSize = content != null ? (long) content.length() : 0L;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return "Document{" +
               "id=" + id +
               ", fileName='" + fileName + '\'' +
               ", title='" + title + '\'' +
               ", fileSize=" + fileSize +
               ", createdAt=" + createdAt +
               ", modifiedAt=" + modifiedAt +
               '}';
    }
}
