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
                <!DOCTYPE html>
                <html lang="en">
                  <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Post Deletion Due to Policy Violations</title>
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
                        margin-bottom: 20px;
                        text-align: center;
                      }
                      p {
                        margin-bottom: 20px;
                      }
                    </style>
                  </head>
                  <body>
                    <div class="container">
                      <h1>Post Deletion</h1>
                      <p>Dear %s,</p>
                      <p>
                        We regret to inform you that your post titled "<strong>%s</strong>" has
                        been deleted from our platform due to violations of our community guidelines and policies.
                      </p>
                      <p>
                        We take our guidelines seriously to maintain a safe and respectful environment for all users.
                        Unfortunately, your post did not comply with our policies, leading to its removal.
                      </p>
                      <p>If you have any questions or concerns regarding this decision, please don't hesitate to contact our support team for further clarification.</p>
                      <p>Thank you for your understanding.</p>
                      <p>Best regards,</p>
                      <p>Pitech Blog</p>
                    </div>
                  </body>
                </html>
            """,
            post.getUser().getUsername(),
            post.getTitle()
        );
    }
}
