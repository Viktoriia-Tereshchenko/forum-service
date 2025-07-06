package ait.cohort5860.post.controller;

import ait.cohort5860.post.dto.FileResponseDto;
import ait.cohort5860.post.dto.NewCommentDto;
import ait.cohort5860.post.dto.NewPostDto;
import ait.cohort5860.post.dto.PostDto;
import ait.cohort5860.post.model.FileEntity;
import ait.cohort5860.post.service.PostService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
    public PostDto addPost(@PathVariable("user") String author, @RequestBody @Valid NewPostDto newPostDto) {
        return postService.addPost(author, newPostDto);
    }

    @GetMapping("/post/{id}")
    public PostDto findPostById(@PathVariable Long id) {
        return postService.findPostById(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/post/{id}/like")
    public void addLike(@PathVariable Long id) {
        postService.addLike(id);
    }

    @GetMapping("/posts/author/{user}")
    public List<PostDto> findPostsByAuthor(@PathVariable("user") String author) {
        return postService.findPostsByAuthor(author);
    }

    @PatchMapping("/post/{id}/comment/{commenter}")
    public PostDto addComment(@PathVariable Long id, @PathVariable("commenter") String author, @RequestBody @Valid NewCommentDto newCommentDto) {
        return postService.addComment(id, author, newCommentDto);
    }

    @DeleteMapping("/post/{id}")
    public PostDto deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }

    @GetMapping("/posts/tags")
    public List<PostDto> findPostsByTags(@RequestParam("values") Set<String> tags) {
        return postService.findPostsByTags(tags);
    }

    @GetMapping("/posts/period")
    public List<PostDto> findPostsByPeriod(@RequestParam LocalDate dateFrom, @RequestParam LocalDate dateTo) {
        return postService.findPostsByPeriod(dateFrom, dateTo);
    }

    @PatchMapping("/post/{id}")
    public PostDto updatePost(@PathVariable Long id, @RequestBody /*@Valid*/ NewPostDto newPostDto) {
        return postService.updatePost(id, newPostDto);
    }

    @PostMapping("/post/{postId}/files/upload")
    public ResponseEntity<FileResponseDto> uploadFile(@PathVariable Long postId, @RequestParam("file") MultipartFile file) {
        FileResponseDto dto = postService.storeFile(postId, file);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/post/{postId}/files/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long postId, @PathVariable Long fileId) {
        FileEntity file = postService.getFile(postId, fileId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file.getData());
    }
}
