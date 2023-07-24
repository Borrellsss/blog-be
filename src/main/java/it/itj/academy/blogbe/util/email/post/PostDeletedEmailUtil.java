package it.itj.academy.blogbe.util.email.post;

import it.itj.academy.blogbe.entity.Post;
import it.itj.academy.blogbe.util.email.EmailUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PostDeletedEmailUtil implements EmailUtil<Post> {
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, Post post) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom("no-reply@pietch.blog.com");
        message.setSubject("Your post has been deleted");
        message.setRecipients(MimeMessage.RecipientType.TO, post.getUser().getEmail());
        message.setContent(setMail(post), "text/html; charset=utf-8");
        mailSender.send(message);
    }
    @Override
    public String setMail(Post post) {
        return String.format(
            """
                <html>
                    <body>
                        <h1>Hi, %s.</h1>
                        <div>Sorry, your post: "%s" has been deleted due to Terms of Service violations</div>
                        <p>Thanks, Pitech Blog</p>
                    </body>
                </html>
            """,
            post.getUser().getUsername(),
            post.getTitle()
        );
    }
}
