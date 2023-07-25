package it.itj.academy.blogbe.util.email.user;

import it.itj.academy.blogbe.entity.User;
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
public class UserSignUpEmailUtil implements EmailUtil<User> {
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, User user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom("no-reply@pietch.blog.com");
        message.setSubject("Welcome to Pitech Blog!");
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setContent(setMail(user), "text/html; charset=utf-8");
        mailSender.send(message);
    }
    @Override
    public String setMail(User user) {
        return String.format("""
                <!DOCTYPE html>
                <html lang="en">
                  <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Sign Up Confirmation</title>
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
                      <h1>Sign Up Confirmation</h1>
                      <p>Dear %s,</p>
                      <p>Thank you for signing up with us! Your account is now active and ready to use.</p>
                      <p>We're thrilled to have you join our community. If you have any questions or need assistance, feel free to contact our support team.</p>
                      <p>Get started now and explore the exciting features of our platform.</p>
                      <p>
                        <a href="http://localhost:4200/sign-in">Sign in now</a>
                      </p>
                      <p>If the button above does not work, you can also copy and paste the following link into your browser:</p>
                      <p>http://localhost:4200/sign-in</p>
                      <p>Thank you once again for joining us!</p>
                      <p>Best regards, </p>
                      <p>Pitech Blog</p>
                    </div>
                  </body>
                </html>
            """,
            user.getUsername()
        );
    }
}
