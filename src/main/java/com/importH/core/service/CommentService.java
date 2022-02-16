package com.importH.core.service;

import com.importH.core.domain.comment.Comment;
import com.importH.core.domain.comment.CommentRepository;
import com.importH.core.domain.post.Post;
import com.importH.core.domain.user.User;
import com.importH.core.dto.post.CommentDto.Request;
import com.importH.error.code.CommentErrorCode;
import com.importH.error.exception.CommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    /**
     * 댓글 등록
     */
    @Transactional
    public Long registerComment(Long postsId, User user, Request commentDto) {

        Post post = postService.findByPostId(postsId);

        Comment comment = commentDto.toEntity();
        setCommentRelation(user, post, comment);

        saveComment(comment);

        return comment.getId();
    }

    private void setCommentRelation(User user, Post post, Comment comment) {
        comment.setUser(user);
        post.addComment(comment);
    }

    private void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    /**
     * 댓글 수정
     */
    //TODO 조회 성능최적화
    @Transactional
    public void updateComment(Long postsId, Long commentId, User user, Request commentDto) {
        Post post = postService.findByPostId(postsId);
        Comment comment = findByCommentId(commentId);

        canModifiableComment(user, post, comment);

        comment.updateComment(commentDto);
    }

    private void canModifiableComment(User user, Post post, Comment comment) {
        if (!isEqualsAccount(user, comment)) {
            throw new CommentException(CommentErrorCode.NOT_EQUALS_USER);
        }
        if (!isEqualsPost(post, comment)) {
            throw new CommentException(CommentErrorCode.NOT_EQUALS_POST);
        }
    }

    private boolean isEqualsPost(Post post, Comment comment) {
        return comment.getPost().equals(post);
    }

    private boolean isEqualsAccount(User user, Comment comment) {
        return comment.getUser().equals(user);
    }

    private Comment findByCommentId(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new CommentException(CommentErrorCode.NOT_FOUND));
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void deleteComment(Long postsId, Long commentId, User user) {
        Post post = postService.findByPostId(postsId);
        Comment comment = findByCommentId(commentId);

        canModifiableComment(user, post, comment);

        post.deleteComment(comment);
        commentRepository.delete(comment);
    }
}
