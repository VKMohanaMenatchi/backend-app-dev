package com.examly.springapp.repository;

import com.examly.springapp.model.BlogPost;
import com.examly.springapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    Page<BlogPost> findByStatusOrderByCreatedDateDesc(BlogPost.Status status, Pageable pageable);
    Page<BlogPost> findByAuthorAndStatusOrderByCreatedDateDesc(User author, BlogPost.Status status, Pageable pageable);
    Page<BlogPost> findByAuthorOrderByCreatedDateDesc(User author, Pageable pageable);
    
    @Query("SELECT p FROM BlogPost p WHERE p.status = 'PUBLISHED' AND " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<BlogPost> searchPublishedPosts(@Param("keyword") String keyword, Pageable pageable);
    
    List<BlogPost> findTop5ByStatusOrderByViewCountDesc(BlogPost.Status status);
}