package ait.cohort5860.post.controller;

import ait.cohort5860.post.dto.NewCommentDto;
import ait.cohort5860.post.dto.NewPostDto;
import ait.cohort5860.post.dto.PostDto;
import ait.cohort5860.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forum")
public class PostController {

    private final PostService postService;

    // @ResponseStatus - annotation can stand above a class and a method
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/post/{user}")
    public PostDto addPost(@PathVariable("user") String author, @RequestBody NewPostDto newPostDto) {
        return postService.addPost(author, newPostDto);
    }

    @GetMapping("/post/{postId}")
    public PostDto findPostById(@PathVariable("postId") Long id) {
        return postService.findPostById(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/post/{postId}/like")
    public void addLike(@PathVariable("postId") Long id) {
        postService.addLike(id);
    }

    @GetMapping("/posts/author/{user}")
    public List<PostDto> findPostsByAuthor(@PathVariable("user") String author) {
        return postService.findPostsByAuthor(author);
    }

    @PatchMapping("/post/{postId}/comment/{commenter}")
    public PostDto addComment(@PathVariable("postId") Long id, @PathVariable("commenter") String author, @RequestBody NewCommentDto newCommentDto) {
        return postService.addComment(id, author, newCommentDto);
    }

    @DeleteMapping("/post/{postId}")
    public PostDto deletePost(@PathVariable("postId") Long id) {
        return postService.deletePost(id);
    }

    @GetMapping("/posts/tags")
    public List<PostDto> findPostsByTags(@RequestParam Set<String> tags) {
        return postService.findPostsByTags(tags);
    }

    @GetMapping("/posts/period")
    public List<PostDto> findPostsByPeriod(@RequestParam LocalDateTime dateFrom, @RequestParam LocalDateTime dateTo) {
        return postService.findPostsByPeriod(dateFrom, dateTo);
    }

    @PatchMapping("/post/{postId}")
    public PostDto updatePost(@PathVariable("postId") Long id, @RequestBody NewPostDto newPostDto) {
        return postService.updatePost(id, newPostDto);
    }
}
