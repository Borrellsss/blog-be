package it.itj.academy.blogbe.util.email;

import jakarta.mail.MessagingException;

public interface EmailUtil<T> {
    void sendEmail(String to, T entity) throws MessagingException;
    String setEmailTemplate(T entity);
}
