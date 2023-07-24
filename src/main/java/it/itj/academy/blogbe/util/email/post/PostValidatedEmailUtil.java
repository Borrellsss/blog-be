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
public class PostValidatedEmailUtil implements EmailUtil<Post> {
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, Post post) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom("no-reply@pietch.blog.com");
        if (post.getValid()) {
            message.setSubject("Your post has been accepted!");
        } else {
            message.setSubject("Your post has been rejected");
        }
        message.setRecipients(MimeMessage.RecipientType.TO, post.getUser().getEmail());
        message.setContent(setMail(post), "text/html; charset=utf-8");
        mailSender.send(message);
    }
    @Override
    public String setMail(Post post) {
        String template;
        if (!post.getValid()) {
            template = String.format(
                """
                    <html>
                        <body>
                            <h1>Hi, %s.</h1>
                            <div>Sorry, your post: "%s" has been rejected.</div>
                            <div>You can still modify it</div>
                            <p>Thanks, Pitech Blog</p>
                        </body>
                    </html>
                """,
                post.getUser().getUsername(),
                post.getTitle()
            );
        } else {
            template = String.format(
                """
                    <html>
                        <body>
                            <h1>Hi, %s.</h1>
                            <div>Congratulations! Your post: "%s" has been accepted!</div>
                            <a href="http://localhost:4200/posts/details/%s/%s/%s">Click here to see your post</a>
                            <p>Thanks, Pitech Blog</p>
                        </body>
                    </html>
                """,
                post.getUser().getUsername(),
                post.getTitle(),
                post.getUser().getId(),
                post.getId(),
                post.getTitle().replace(" ", "-")
            );
        }
        return template;
    }
}
