package ait.cohort5860.accounting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter // for jackson
public class EmailDto {
    @NotBlank(message = "To is required")
    @Email
    private String to;
    @NotBlank(message = "Subject is required")
    private String subject;
    @NotBlank(message = "Message is required")
    private String message;
}
