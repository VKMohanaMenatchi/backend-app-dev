package com.examly.springapp.service;

import com.examly.springapp.exception.ContentNotFoundException;
import com.examly.springapp.exception.UnauthorizedAccessException;
import com.examly.springapp.model.BlogPost;
import com.examly.springapp.model.Category;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.BlogPostRepository;
import com.examly.springapp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public BlogPost createPost(BlogPost post, User author) {
        post.setAuthor(author);
        post.setCreatedDate(LocalDateTime.now());
        post.setLastModified(LocalDateTime.now());
        return blogPostRepository.save(post);
    }

    public BlogPost updatePost(Long id, BlogPost updatedPost, User currentUser) {
        BlogPost existingPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException("Post not found"));

        if (!existingPost.getAuthor().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new UnauthorizedAccessException("You can only edit your own posts");
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setExcerpt(updatedPost.getExcerpt());
        existingPost.setCategory(updatedPost.getCategory());
        existingPost.setTags(updatedPost.getTags());
        existingPost.setFeaturedImage(updatedPost.getFeaturedImage());
        existingPost.setLastModified(LocalDateTime.now());

        return blogPostRepository.save(existingPost);
    }

    public BlogPost publishPost(Long id, User currentUser) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException("Post not found"));

        if (!post.getAuthor().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new UnauthorizedAccessException("You can only publish your own posts");
        }

        post.setStatus(BlogPost.Status.PUBLISHED);
        post.setPublishDate(LocalDateTime.now());
        return blogPostRepository.save(post);
    }

    public void deletePost(Long id, User currentUser) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException("Post not found"));

        if (!post.getAuthor().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new UnauthorizedAccessException("You can only delete your own posts");
        }

        blogPostRepository.delete(post);
    }

    public BlogPost getPostById(Long id) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException("Post not found"));
        
        // Increment view count
        post.setViewCount(post.getViewCount() + 1);
        return blogPostRepository.save(post);
    }

    public Page<BlogPost> getPublishedPosts(Pageable pageable) {
        return blogPostRepository.findByStatusOrderByCreatedDateDesc(BlogPost.Status.PUBLISHED, pageable);
    }

    public Page<BlogPost> getPostsByAuthor(User author, Pageable pageable) {
        return blogPostRepository.findByAuthorOrderByCreatedDateDesc(author, pageable);
    }

    public Page<BlogPost> searchPosts(String keyword, Pageable pageable) {
        return blogPostRepository.searchPublishedPosts(keyword, pageable);
    }

    public List<BlogPost> getTrendingPosts() {
        return blogPostRepository.findTop5ByStatusOrderByViewCountDesc(BlogPost.Status.PUBLISHED);
    }
}