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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public void addLike(Long id) {
        Post post = postRepository.findById(id).orElseThrow(NotFoundException::new);
        post.addLike();
    }

    @Override
    public List<PostDto> findPostsByAuthor(String author) {
        return postRepository.findByAuthorIgnoreCase(author)
                .stream()
                .map(p -> modelMapper.map(p, PostDto.class))
                .toList();
    }

    @Override
    @Transactional
    public PostDto addComment(Long id, String author, NewCommentDto newCommentDto) {
        Post post = postRepository.findById(id).orElseThrow(NotFoundException::new);
        Comment comment = new Comment(author, newCommentDto.getMessage());
        commentRepository.save(comment);
        comment.setPost(post);
        post.addComment(comment);

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
    public List<PostDto> findPostsByTags(Set<String> tags) {
        return postRepository.findDistinctByTagsNameInIgnoreCase(tags)
                .stream()
                .map(p -> modelMapper.map(p, PostDto.class))
                .toList();
    }

    // FIXME (Bad request)
    @Override
    public List<PostDto> findPostsByPeriod(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return postRepository.findByDateCreatedBetween(dateFrom, dateTo)
                .stream()
                .map(p -> modelMapper.map(p, PostDto.class))
                .toList();
    }

    @Override
    @Transactional
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
        if (tags != null) {
            for (String tagName : tags) {
                Tag tag = tagRepository.findById(tagName).orElseGet(
                        () -> tagRepository.save(new Tag(tagName)));
                post.addTag(tag);
            }
        }
        return modelMapper.map(post, PostDto.class);
    }
}
