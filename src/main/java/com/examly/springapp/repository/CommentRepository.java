package com.examly.springapp.repository;

import com.examly.springapp.model.BlogPost;
import com.examly.springapp.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostAndStatusOrderByCreatedDateDesc(BlogPost post, Comment.Status status, Pageable pageable);
    List<Comment> findByPostAndStatusAndParentCommentIdIsNullOrderByCreatedDateDesc(BlogPost post, Comment.Status status);
    List<Comment> findByParentCommentIdOrderByCreatedDateAsc(Long parentCommentId);
}