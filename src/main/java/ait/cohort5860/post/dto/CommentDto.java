package ait.cohort5860.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    @JsonProperty("user")
    private String username;
    private String message;
    private LocalDateTime dateCreated;
    private Integer likes;
}
