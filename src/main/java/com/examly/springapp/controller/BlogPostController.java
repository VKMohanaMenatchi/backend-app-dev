package com.examly.springapp.controller;

import com.examly.springapp.model.BlogPost;
import com.examly.springapp.model.Category;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.CategoryRepository;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.service.BlogPostService;
import com.examly.springapp.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/posts")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    public ResponseEntity<BlogPost> createPost(@Valid @RequestBody BlogPost post, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User author = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        
        if (post.getCategory() != null && post.getCategory().getId() != null) {
            Category category = categoryRepository.findById(post.getCategory().getId()).orElse(null);
            post.setCategory(category);
        }
        
        BlogPost createdPost = blogPostService.createPost(post, author);
        return ResponseEntity.ok(createdPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPost> updatePost(@PathVariable Long id, @Valid @RequestBody BlogPost post, 
                                             Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        
        if (post.getCategory() != null && post.getCategory().getId() != null) {
            Category category = categoryRepository.findById(post.getCategory().getId()).orElse(null);
            post.setCategory(category);
        }
        
        BlogPost updatedPost = blogPostService.updatePost(id, post, currentUser);
        return ResponseEntity.ok(updatedPost);
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<BlogPost> publishPost(@PathVariable Long id, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        
        BlogPost publishedPost = blogPostService.publishPost(id, currentUser);
        return ResponseEntity.ok(publishedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUser = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        
        blogPostService.deletePost(id, currentUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPost> getPost(@PathVariable Long id) {
        BlogPost post = blogPostService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/public")
    public ResponseEntity<Page<BlogPost>> getPublishedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BlogPost> posts = blogPostService.getPublishedPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/my-posts")
    public ResponseEntity<Page<BlogPost>> getMyPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User author = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<BlogPost> posts = blogPostService.getPostsByAuthor(author, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BlogPost>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BlogPost> posts = blogPostService.searchPosts(keyword, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/trending")
    public ResponseEntity<List<BlogPost>> getTrendingPosts() {
        List<BlogPost> posts = blogPostService.getTrendingPosts();
        return ResponseEntity.ok(posts);
    }
}