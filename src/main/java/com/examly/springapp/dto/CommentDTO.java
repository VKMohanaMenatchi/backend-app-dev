package com.examly.springapp.dto;

import com.examly.springapp.model.Comment;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private String content;
    private AuthorDTO author;
    private LocalDateTime createdDate;
    private Long likesCount;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdDate = comment.getCreatedDate();
        this.likesCount = comment.getLikesCount();

        if (comment.getAuthor() != null) {
            this.author = new AuthorDTO(comment.getAuthor());
        }
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public AuthorDTO getAuthor() { return author; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public Long getLikesCount() { return likesCount; }
}
