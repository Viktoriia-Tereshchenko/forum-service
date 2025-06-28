package ait.cohort5860.post.dao;

import ait.cohort5860.post.dto.PostDto;
import ait.cohort5860.post.model.Post;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorIgnoreCase(String author);

    List<Post> findDistinctByTagsNameInIgnoreCase(Set<String> tags);

    List<Post> findByDateCreatedBetween(LocalDateTime dateFrom, LocalDateTime dateTo);
}
