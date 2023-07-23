package it.itj.academy.blogbe.util.email;

import it.itj.academy.blogbe.dto.input.CommentInputDto;
import jakarta.mail.MessagingException;

public interface EmailUtil<T> {
    void sendEmail(String to, T entity) throws MessagingException;
}
