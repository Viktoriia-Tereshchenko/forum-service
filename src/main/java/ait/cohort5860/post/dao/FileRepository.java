package ait.cohort5860.post.dao;

import ait.cohort5860.post.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByIdAndPostId(Long fileId, Long postId);
}
