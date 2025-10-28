package com.examly.springapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
@Entity
@Table(name = "comments")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "post_id")
@JsonBackReference  // prevents loop with BlogPost
private BlogPost post;


@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "author_id")
@JsonBackReference(value = "user-comments")  // matches the reference name in User
private User author;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "likes_count")
    private Long likesCount = 0L;

    public enum Status {
        PENDING, APPROVED, REJECTED, DELETED
    }

    // Constructors
    public Comment() {}

    public Comment(String content, BlogPost post, User author) {
        this.content = content;
        this.post = post;
        this.author = author;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BlogPost getPost() { return post; }
    public void setPost(BlogPost post) { this.post = post; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getParentCommentId() { return parentCommentId; }
    public void setParentCommentId(Long parentCommentId) { this.parentCommentId = parentCommentId; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public Long getLikesCount() { return likesCount; }
    public void setLikesCount(Long likesCount) { this.likesCount = likesCount; }
}