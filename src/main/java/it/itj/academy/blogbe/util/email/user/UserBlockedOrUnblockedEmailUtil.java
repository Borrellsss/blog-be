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
        message.setContent(setMail(user), "text/html; charset=utf-8");
        mailSender.send(message);
    }
    @Override
    public String setMail(User user) {
        String template;
        if (user.isBlocked()) {
            template = String.format(
                """
                    <!DOCTYPE html>
                    <html>
                      <head>
                        <meta charset="UTF-8">
                        <title>Account Blocked Due to Policy Violations</title>
                      </head>
                      <body style="font-family: Arial, sans-serif; background-color: #f2f2f2; color: #333; margin: 0; padding: 0;">
                        <table cellpadding="0" cellspacing="0" border="0" align="center" style="max-width: 600px; margin: 20px auto; background-color: #ffffff; border: 1px solid #ccc;">
                          <tr>
                            <td style="padding: 20px;">
                              <h2 style="color: #dd3d3d; margin-bottom: 20px;">Account Blocked Due to Policy Violations</h2>
                              <p>Dear %s,</p>
                              <p>
                                We regret to inform you that your account on Pitech Blog has been blocked due to multiple violations of our policies.
                                This decision was made to ensure the safety and integrity of our platform and its community.
                              </p>
                              <p>We take our policies very seriously, and unfortunately, your account's behavior has consistently violated our guidelines, prompting this action.</p>
                              <p>
                                If you believe that this action was taken in error or have any questions regarding the violations,
                                please contact our support team at support@pitech.blog.com for further clarification.
                              </p>
                              <p>Please note that you will no longer be able to access your account and all associated services until the issue is resolved.</p>
                              <p>We appreciate your understanding and adherence to our policies as we strive to create a safe and respectful environment for all users.</p>
                              <p>Best regards,</p>
                              <p>Pitech Blog</p>
                            </td>
                          </tr>
                        </table>
                      </body>
                    </html>
                    """,
                user.getUsername()
            );
        } else {
            template = String.format(
                """
                    <!DOCTYPE html>
                    <html>
                      <head>
                        <meta charset="UTF-8">
                        <title>Account Unblocked</title>
                      </head>
                      <body style="font-family: Arial, sans-serif; background-color: #f2f2f2; color: #333; margin: 0; padding: 0;">
                        <table cellpadding="0" cellspacing="0" border="0" align="center" style="max-width: 600px; margin: 20px auto; background-color: #ffffff; border: 1px solid #ccc;">
                          <tr>
                            <td style="padding: 20px;">
                              <h2 style="color: #3dd089; margin-bottom: 20px;">Account Unblocked</h2>
                              <p>Dear %s,</p>
                              <p>We are pleased to inform you that your account on Pitech Blog has been successfully unblocked.</p>
                              <p>
                                After reviewing the situation, we found that the issue leading to the blocking of your account
                                has been resolved or deemed an isolated incident. As a result, we have lifted the restrictions on your account,
                                and you can now access all features and services as before.
                              </p>
                              <p>We apologize for any inconvenience caused during the block period and appreciate your understanding throughout the process.</p>
                              <p>If you have any questions or need further assistance, please don't hesitate to contact our support team at support@pitech.blog.</p>
                              <p>Thank you for being a valued member of our community.</p>
                              <p>Best regards,</p>
                              <p>Pitech Blog</p>
                            </td>
                          </tr>
                        </table>
                      </body>
                    </html>
                """,
                user.getUsername()
            );
        }
        return template;
    }
}
