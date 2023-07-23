package it.itj.academy.blogbe.util.email;

import it.itj.academy.blogbe.entity.Comment;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CommentEmailUtil implements EmailUtil<Comment> {
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, Comment comment) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom("pietch.blog@mail.test");
        message.setSubject(String.format("%s has commented on your post", comment.getUser().getUsername()));
        message.setRecipients(MimeMessage.RecipientType.TO, comment.getPost().getUser().getEmail());
        message.setContent(setMail(comment), "text/html; charset=utf-8");
        mailSender.send(message);
    }
    private String setMail(Comment comment){
        return String.format(
            """
            <html>
                <body>
                    <h1>Hi, %s.</h1>
                    <div>
                        <span>%s</span>
                        has commented on your post: %s
                    </div>
                    <a href="http://localhost:4200/posts/details/%s/%s/%s">Click here to see the comment</a>
                    <p>Thanks, Pitech Blog</p>
                </body>
            </html>
            """,
            comment.getPost().getUser().getUsername(),
            comment.getUser().getUsername(),
            comment.getPost().getTitle(),
            comment.getPost().getUser().getId(),
            comment.getPost().getId(),
            comment.getPost().getTitle().replace(" ", "-")
        );
    }
}
