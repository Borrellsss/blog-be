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
                    <!DOCTYPE html>
                    <html lang="en">
                      <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Post Rejection Notification</title>
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
                          <h1>Post Rejection</h1>
                          <p>Dear %s,</p>
                          <p>We regret to inform you that your post titled "<strong>%s</strong>" has been rejected due to certain issues that need attention.</p>
                          <p>Please review the feedback provided and make the necessary modifications to your post to meet our community guidelines.</p>
                          <p>Once you have made the changes, you can resubmit your post for review.</p>
                          <p>To access your post and make modifications, click the button below:</p>
                          <p>
                            <a href="http://localhost:4200/posts/details/%s/%s/%s">Modify Post</a>
                          </p>
                          <p>If the button above does not work, you can also copy and paste the following link into your browser:</p>
                          <p>http://localhost:4200/posts/details/%s/%s/%s</p>
                          <p>We appreciate your understanding and look forward to seeing your revised post.</p>
                          <p>Best regards,</p>
                          <p>Pitech Blog</p>
                        </div>
                      </body>
                    </html>
                """,
                post.getUser().getUsername(),
                post.getTitle(),
                post.getUser().getId(),
                post.getId(),
                post.getTitle().replace(" ", "-"),
                post.getUser().getId(),
                post.getId(),
                post.getTitle().replace(" ", "-")
            );
        } else {
            template = String.format(
                """
                    <!DOCTYPE html>
                    <html lang="en">
                      <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Post Approval</title>
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
                          <h1>Post Approval Notification</h1>
                          <p>Dear %s,</p>
                          <p>Congratulations! Your post titled "<strong>%s</strong>" has been approved and is now live on our platform.</p>
                          <p>Thank you for contributing to our community. Your valuable content will be visible to other users.</p>
                          <p>To view your approved post, click the button below:</p>
                          <p>
                            <a href="http://localhost:4200/posts/details/%s/%s/%s">Click here to see your post</a>
                          </p>
                          <p>If the button above does not work, you can also copy and paste the following link into your browser:</p>
                          <p>http://localhost:4200/posts/details/%s/%s/%s</p>
                          <p>We hope to see more of your engaging contributions in the future!</p>
                          <p>Best regards,</p>
                          <p>Pitech Blog</p>
                        </div>
                      </body>
                    </html>
                """,
                post.getUser().getUsername(),
                post.getTitle(),
                post.getUser().getId(),
                post.getId(),
                post.getTitle().replace(" ", "-"),
                post.getUser().getId(),
                post.getId(),
                post.getTitle().replace(" ", "-")
            );
        }
        return template;
    }
}
