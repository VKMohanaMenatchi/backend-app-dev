package com.examly.springapp.dto;

import com.examly.springapp.model.BlogPost;
import com.examly.springapp.model.Comment;
import com.examly.springapp.model.Category;
import com.examly.springapp.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BlogPostViewDTO {
    private Long id;
    private String title;
    private String content;
    private String excerpt;
    private String featuredImage;
    private String tags;
    private Long viewCount;
    private LocalDateTime createdDate;
    private LocalDateTime publishDate;
    private String status;

    private AuthorDTO author;
    private CategoryDTO category;
    private List<CommentDTO> comments;

    public BlogPostViewDTO(BlogPost post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.excerpt = post.getExcerpt();
        this.featuredImage = post.getFeaturedImage();
        this.tags = post.getTags();
        this.viewCount = post.getViewCount();
        this.createdDate = post.getCreatedDate();
        this.publishDate = post.getPublishDate();
        this.status = post.getStatus().name();

        // Author
        User author = post.getAuthor();
        if (author != null) {
            this.author = new AuthorDTO(author);
        }

        // Category
        Category category = post.getCategory();
        if (category != null) {
            this.category = new CategoryDTO(category);
        }

        // Comments
        if (post.getComments() != null) {
            this.comments = post.getComments()
                .stream()
                .map(CommentDTO::new)
                .collect(Collectors.toList());
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getExcerpt() { return excerpt; }
    public String getFeaturedImage() { return featuredImage; }
    public String getTags() { return tags; }
    public Long getViewCount() { return viewCount; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getPublishDate() { return publishDate; }
    public String getStatus() { return status; }
    public AuthorDTO getAuthor() { return author; }
    public CategoryDTO getCategory() { return category; }
    public List<CommentDTO> getComments() { return comments; }
}
