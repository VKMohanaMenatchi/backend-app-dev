package com.examly.springapp.service;

import com.examly.springapp.exception.ContentNotFoundException;
import com.examly.springapp.exception.UnauthorizedAccessException;
import com.examly.springapp.model.BlogPost;
import com.examly.springapp.model.Comment;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.BlogPostRepository;
import com.examly.springapp.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    public Comment createComment(Long postId, Comment comment, User author) {
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ContentNotFoundException("Post not found"));

        comment.setPost(post);
        comment.setAuthor(author);
        comment.setCreatedDate(LocalDateTime.now());
        comment.setStatus(Comment.Status.APPROVED); // Auto-approve for now
        
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long id, Comment updatedComment, User currentUser) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException("Comment not found"));

        if (!existingComment.getAuthor().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().equals(User.Role.ADMIN) &&
            !currentUser.getRole().equals(User.Role.MODERATOR)) {
            throw new UnauthorizedAccessException("You can only edit your own comments");
        }

        existingComment.setContent(updatedComment.getContent());
        return commentRepository.save(existingComment);
    }

    public void deleteComment(Long id, User currentUser) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException("Comment not found"));

        if (!comment.getAuthor().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().equals(User.Role.ADMIN) &&
            !currentUser.getRole().equals(User.Role.MODERATOR)) {
            throw new UnauthorizedAccessException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    public List<Comment> getCommentsByPost(Long postId) {
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ContentNotFoundException("Post not found"));

        return commentRepository.findByPostAndStatusAndParentCommentIdIsNullOrderByCreatedDateDesc(
                post, Comment.Status.APPROVED);
    }

    public List<Comment> getReplies(Long parentCommentId) {
        return commentRepository.findByParentCommentIdOrderByCreatedDateAsc(parentCommentId);
    }

    public Comment likeComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException("Comment not found"));

        comment.setLikesCount(comment.getLikesCount() + 1);
        return commentRepository.save(comment);
    }
}