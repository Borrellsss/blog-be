package it.itj.academy.blogbe.util.email.user;

import it.itj.academy.blogbe.entity.Role;
import it.itj.academy.blogbe.entity.User;
import it.itj.academy.blogbe.util.email.EmailUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Setter
@Component
public class UserPromotionOrDemotionEmailUtil implements EmailUtil<User> {
    private final JavaMailSender mailSender;
    private Boolean promotion;
    private Role oldRole;

    @Override
    public void sendEmail(String to, User user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom("no-reply@pietch.blog.com");
        if (promotion) {
            message.setSubject("Promotion Notification");
        } else {
            message.setSubject("Demotion Notification");
        }
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setContent(setEmailTemplate(user), "text/html; charset=utf-8");
        mailSender.send(message);
    }
    @Override
    public String setEmailTemplate(User user) {
        String template;
        if (promotion) {
            template = String.format("""
                    <!DOCTYPE html>
                    <html lang="en">
                      <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Promotion Notification</title>
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
                          <h1>Congratulations!</h1>
                          <p>Dear %s,</p>
                          <p>We are excited to inform you that you have been promoted from %s to %s in our community.</p>
                          <p>
                            Your hard work, dedication, and valuable contributions have not gone unnoticed,
                            and we believe that you will continue to excel in your new role.
                          </p>
                          <p>We are looking forward to seeing the positive impact you will make in this new position.</p>
                          <p>Thank you for being an essential part of our community.</p>
                          <p>Best regards,</p>
                          <p>Pitech Blog</p>
                        </div>
                      </body>
                    </html>
                """,
                user.getUsername(),
                oldRole.getAuthority()
                    .replace("ROLE", "")
                    .replaceAll("_", " ")
                    .toLowerCase(),
                user.getRole().getAuthority()
                    .replace("ROLE", "")
                    .replaceAll("_", " ")
                    .toLowerCase()
            );
        } else {
            template = String.format("""
                    <!DOCTYPE html>
                    <html lang="en">
                      <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Demotion Notification</title>
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
                          <h1>Demotion Notification</h1>
                          <p>Dear %s,</p>
                          <p>We regret to inform you that you have been demoted from %s to %s in our community.</p>
                          <p>Your contributions and efforts have been appreciated, but due to certain reasons, we have had to make this decision.</p>
                          <p>
                            We encourage you to continue participating and contributing to the community,
                            and we hope to see you grow and regain your previous position in the future.
                          </p>
                          <p>Thank you for your understanding.</p>
                          <p>Best regards,</p>
                          <p>Pitech Blog</p>
                        </div>
                      </body>
                    </html>
                """,
                user.getUsername(),
                oldRole.getAuthority()
                    .replace("ROLE", "")
                    .replaceAll("_", " ")
                    .toLowerCase(),
                user.getRole().getAuthority()
                    .replace("ROLE", "")
                    .replaceAll("_", " ")
                    .toLowerCase()
            );
        }
        return template;
    }
}
