package it.itj.academy.blogbe.util.email.user;

import it.itj.academy.blogbe.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDeletedEmailUtil {
    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, User user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom("no-reply@pietch.blog.com");
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentUser.getId().equals(user.getId())) {
            message.setSubject("Your account has been deleted");
        } else {
            message.setSubject("Your account has been successfully deleted");
        }
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setContent(setEmailTemplate(user), "text/html; charset=utf-8");
        mailSender.send(message);
    }
    public String setEmailTemplate(User user) {
        String template;
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentUser.getId().equals(user.getId())) {
            template = String.format(
                """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                      <meta charset="UTF-8">
                      <meta name="viewport" content="width=device-width, initial-scale=1.0">
                      <title>Account Deletion Due to Policy Violations</title>
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
                        <h1>Account Deletion</h1>
                        <p>Dear %s,</p>
                        <p>We regret to inform you that your account has been permanently deleted due to violations of our platform's policies and guidelines.</p>
                        <p>
                          We take our community guidelines seriously to ensure a safe and respectful environment for all users. 
                          Unfortunately, your actions were in violation of these rules, leading to the necessary account removal.
                        </p>
                        <p>If you believe this decision was made in error or have any questions, you can reach out to our support team for further clarification.</p>
                        <p>Thank you for your understanding.</p>
                        <p>Best regards,</p>
                        <p>Pitech Blog</p>
                      </div>
                    </body>
                    </html>
                """,
                user.getUsername()
            );
        } else {
            template = String.format(
                """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                      <meta charset="UTF-8">
                      <meta name="viewport" content="width=device-width, initial-scale=1.0">
                      <title>Account Deletion Success</title>
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
                       <h1>Account Deletion Success</h1>
                       <p>Dear %s,</p>
                       <p>We are sorry to see you go, but your account has been successfully deleted from our system.</p>
                       <p>If you did not initiate this action, please contact our support team immediately.</p>
                       <p>If you change your mind and wish to rejoin us, you can always sign up again.</p>
                       <p>Thank you for being a part of our community. We hope to see you again in the future.</p>
                       <p>Best regards,</p>
                       <p>Pitech Blog</p>
                     </div>
                   </body>
                   </html>
                """,
                user.getUsername()
            );
        }
        return template;
    }
}
