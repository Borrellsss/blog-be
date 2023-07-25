package it.itj.academy.blogbe.util.email.comment;

import it.itj.academy.blogbe.entity.Comment;
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
public class CommentEmailUtil implements EmailUtil<Comment> {
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, Comment comment) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom("no-reply@pietch.blog.com");
        message.setSubject(String.format("%s has commented on your post", comment.getUser().getUsername()));
        message.setRecipients(MimeMessage.RecipientType.TO, comment.getPost().getUser().getEmail());
        message.setContent(setMail(comment), "text/html; charset=utf-8");
        mailSender.send(message);
    }
    @Override
    public String setMail(Comment comment){
        return String.format(
            """
                <!DOCTYPE html>
                <html lang="en">
                  <head>
                      <meta charset="UTF-8">
                      <meta name="viewport" content="width=device-width, initial-scale=1.0">
                      <title>New Comment Notification</title>
                      <style>
                        body,
                        p,
                        h1 {
                          margin: 0;
                          padding: 0;
                        }
                        body {
                          font-family: Arial, sans-serif;
                          line-height: 1.6;
                        }
                        .container {
                          max-width: 600px;
                          margin: 0 auto;
                          padding: 20px;
                          border: 1px solid #ddd;
                          border-radius: 5px;
                        }
                        h1 {
                          text-align: center;
                        }
                        p {
                          margin-bottom: 20px;
                        }
                      </style>
                  </head>
                  <body>
                    <div class="container">
                      <h1>New Comment</h1>
                      <p>Dear %s,</p>
                      <p>%s has commented on your post titled "<strong>%s</strong>".</p>
                      <p>Comment: "<em>%s</em>"</p>
                      <p>To view the comment and respond, click the button below:</p>
                      <p>
                        <a href="http://localhost:4200/posts/details/%s/%s/%s">View comment</a>
                      </p>
                      <p>If the button above does not work, you can also copy and paste the following link into your browser:</p>
                      <p>http://localhost:4200/posts/details/%s/%s/%s</p>
                      <p>Thank you for using our platform!</p>
                      <p>Best regards,</p>
                      <p>Pitech Blog</p>
                    </div>
                  </body>
                </html>
            """,
            comment.getPost().getUser().getUsername(),
            comment.getUser().getUsername(),
            comment.getPost().getTitle(),
            comment.getContent(),
            comment.getPost().getUser().getId(),
            comment.getPost().getId(),
            comment.getPost().getTitle().replace(" ", "-"),
            comment.getPost().getUser().getId(),
            comment.getPost().getId(),
            comment.getPost().getTitle().replace(" ", "-")
        );
    }
}
