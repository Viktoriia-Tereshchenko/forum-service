package ait.cohort5860.post.service;

import ait.cohort5860.post.dao.CommentRepository;
import ait.cohort5860.post.dao.PostRepository;
import ait.cohort5860.post.dao.TagRepository;
import ait.cohort5860.post.dto.NewCommentDto;
import ait.cohort5860.post.dto.NewPostDto;
import ait.cohort5860.post.dto.PostDto;
import ait.cohort5860.post.dto.exceptions.NotFoundException;
import ait.cohort5860.post.model.Comment;
import ait.cohort5860.post.model.Post;
import ait.cohort5860.post.model.Tag;
import ait.cohort5860.post.service.logging.PostLogger;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor // for the final fields
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional  // to make the whole method as a single transaction
    public PostDto addPost(String author, NewPostDto newPostDto) {
        Post post = new Post(newPostDto.getTitle(), newPostDto.getContent(), author);
        Set<String> tags = newPostDto.getTags();
        // Handle tags
        return getPostDto(post, tags);
    }

    private PostDto getPostDto(Post post, Set<String> tags) {
        if (tags != null) {
            for (String tagName : tags) {
                //orElseGet - save and return the result
                Tag tag = tagRepository.findById(tagName).orElseGet(
                        () -> tagRepository.save(new Tag(tagName)));
                post.addTag(tag);
            }
        }
        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto findPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(NotFoundException::new);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    @Transactional
    @PostLogger
    public void addLike(Long id) {
        Post post = postRepository.findById(id).orElseThrow(NotFoundException::new);
        post.addLike();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> findPostsByAuthor(String author) {
        return postRepository.findByAuthorIgnoreCase(author)
                .map(p -> modelMapper.map(p, PostDto.class))
                .toList();
    }

    @Override
    @Transactional
    public PostDto addComment(Long id, String author, NewCommentDto newCommentDto) {
        Post post = postRepository.findById(id).orElseThrow(NotFoundException::new);
        Comment comment = new Comment(author, newCommentDto.getMessage());
        comment.setPost(post);
        commentRepository.save(comment);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    @Transactional
    public PostDto deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(NotFoundException::new);
        postRepository.delete(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> findPostsByTags(Set<String> tags) {
        return postRepository.findDistinctByTagsNameInIgnoreCase(tags)
                .map(p -> modelMapper.map(p, PostDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> findPostsByPeriod(LocalDate dateFrom, LocalDate dateTo) {
        LocalDateTime from = dateFrom.atStartOfDay();
        //LocalDateTime to = dateTo.plusDays(1).atStartOfDay();
        LocalDateTime to = dateTo.atTime(LocalTime.MAX);
        return postRepository.findByDateCreatedBetween(from, to)
                .map(p -> modelMapper.map(p, PostDto.class))
                .toList();
    }

    @Override
    @Transactional
    @PostLogger
    public PostDto updatePost(Long id, NewPostDto newPostDto) {
        Post post = postRepository.findById(id).orElseThrow(NotFoundException::new);
        String title = newPostDto.getTitle();
        String content = newPostDto.getContent();
        Set<String> tags = newPostDto.getTags();

        if (content != null) {
            post.setContent(content);
        }
        if (title != null) {
            post.setTitle(title);
        }
        return getPostDto(post, tags);
    }
}
