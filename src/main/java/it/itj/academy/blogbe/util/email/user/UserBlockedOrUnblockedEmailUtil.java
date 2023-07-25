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
public class UserBlockedOrUnblockedEmailUtil implements EmailUtil<User> {
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, User user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom("no-reply@pietch.blog.com");
        if (user.isBlocked()) {
            message.setSubject("Your account has been blocked");
        } else {
            message.setSubject("Your account has been unblocked");
        }
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setContent(setEmailTemplate(user), "text/html; charset=utf-8");
        mailSender.send(message);
    }
    @Override
    public String setEmailTemplate(User user) {
        String template;
        if (user.isBlocked()) {
            template = String.format(
                """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                      <meta charset="UTF-8">
                      <meta name="viewport" content="width=device-width, initial-scale=1.0">
                      <title>Account Suspension Due to Policy Violations</title>
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
                          color: #007BFF;
                        }
                        p {
                          margin-bottom: 20px;
                        }
                      </style>
                    </head>
                    <body>
                      <div class="container">
                        <h1>Account Suspension Due to Policy Violations</h1>
                        <p>Dear %s,</p>
                        <p>We regret to inform you that your account has been temporarily suspended due to violations of our platform's policies and guidelines.</p>
                        <p>
                          We take our community guidelines seriously to ensure a safe and respectful environment for all users.
                          Unfortunately, your actions were in violation of these rules, leading to the necessary account suspension.
                        </p>
                        <p>
                          During this suspension period, you will not be able to access your account and its features.
                          To appeal this decision or seek further information, please contact our support team.
                        </p>
                        <p>We encourage you to review our policies and ensure compliance before your account can be reinstated.</p>
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
                      <title>Account Unblocked</title>
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
                        <h1>Account Unblocked</h1>
                        <p>Dear %s,</p>
                        <p>We are pleased to inform you that your account has been successfully unblocked.</p>
                        <p>
                          After reviewing the situation and your appeal, we have decided to lift the previous suspension,
                          and you can now access your account and its features.
                        </p>
                        <p>
                          <a href="http://localhost:4200/sign-in">Sign in now</a>
                        </p>
                        <p>We remind you to adhere to our community guidelines and policies to maintain a safe and respectful environment for all users.</p>
                        <p>If you have any questions or concerns, feel free to reach out to our support team.</p>
                        <p>Thank you for your understanding.</p>
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
