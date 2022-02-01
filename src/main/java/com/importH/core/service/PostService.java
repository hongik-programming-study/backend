package com.importH.core.service;

import com.importH.core.domain.account.Account;
import com.importH.core.domain.post.Post;
import com.importH.core.domain.post.PostRepository;
import com.importH.core.domain.tag.Tag;
import com.importH.core.dto.post.CommentDto;
import com.importH.core.dto.post.PostRequestDto;
import com.importH.core.dto.post.PostResponseDto;
import com.importH.core.dto.tag.TagDto;
import com.importH.core.error.code.PostErrorCode;
import com.importH.core.error.exception.PostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.importH.core.error.code.PostErrorCode.NOT_FOUND_POST;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TagService tagService;
    private final UserService userService;

    /**
     * 게시글 저장
     */
    @Transactional
    public Long registerPost(Account account, int type, PostRequestDto postRequestDto) {

        //TODO 이미지 파일 저장
        Set<Tag> tags = getTags(postRequestDto);
        Post post = postRequestDto.toEntity(account, new ArrayList<>(), tags, type);
        return savePost(post);
    }

    private Set<Tag> getTags(PostRequestDto postRequestDto) {
        Set<Tag> tags = postRequestDto.getTags().stream().map(TagDto::toEntity).map(tag -> tagService.getTag(tag)).collect(Collectors.toSet());
        return tags;
    }

    private Long savePost(Post post) {
        Post save = postRepository.save(post);

        return save.getId();
    }

    /**
     * 게시글 조회
     */
    public PostResponseDto getPost(int boardId, Long postId) {

        Post post = findByTypeAndId(boardId, postId);
        Account findAccount = userService.findById(post.getAccount().getId());

        Set<TagDto> tags = post.getTags().stream().map(tag -> TagDto.fromEntity(tag)).collect(Collectors.toSet());
        List<CommentDto> comments = post.getComments().stream().map(comment -> CommentDto.fromEntity(comment)).collect(Collectors.toList());


        return PostResponseDto.fromEntity(post, findAccount, tags, comments);
    }

    private Post findByTypeAndId(int boardId, Long postId) {
        return postRepository.findByIdAndType(postId, boardId).orElseThrow(() -> new PostException(NOT_FOUND_POST));
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public Long updatePost(Account account, int boardId, Long postId, PostRequestDto postRequestDto) {

        Post findPost = findByTypeAndId(boardId, postId);

        Set<Tag> tags = getTags(postRequestDto);

        validateAccount(account, findPost);

        return findPost.updatePost(postRequestDto, tags);
    }

    private void validateAccount(Account account, Post findPost) {
        if (!isEqualsAccount(account, findPost)) {
            throw new PostException(PostErrorCode.NOT_ACCORD_ACCOUNT);
        }
    }

    private boolean isEqualsAccount(Account account, Post findPost) {
        return findPost.getAccount().equals(account);
    }


    /**
     * 게시글 삭제
     */
    @Transactional
    public void deletePost(Account account, int boardId, Long postId) {
        Post findPost = findByTypeAndId(boardId, postId);
        validateAccount(account, findPost);
        postRepository.delete(findPost);
    }


    /*    *//**
     *
     * 전체 게시글 조회
     *//*
    public List<PostResponseDto> findAllPost(String boardId) {

    }*/


}
