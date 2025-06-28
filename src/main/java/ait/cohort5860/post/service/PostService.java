package ait.cohort5860.post.service;

import ait.cohort5860.post.dto.NewCommentDto;
import ait.cohort5860.post.dto.NewPostDto;
import ait.cohort5860.post.dto.PostDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface PostService {

    PostDto addPost(String author, NewPostDto newPostDto);

    PostDto findPostById(Long id);

    void addLike(Long id);

    List<PostDto> findPostsByAuthor(String author);

    PostDto addComment(Long id, String author, NewCommentDto newCommentDto);

    PostDto deletePost(Long id);

    List<PostDto> findPostsByTags(Set<String> tags);

    List<PostDto> findPostsByPeriod(LocalDate dateFrom, LocalDate dateTo);

    PostDto updatePost(Long id, NewPostDto newPostDto);
}

