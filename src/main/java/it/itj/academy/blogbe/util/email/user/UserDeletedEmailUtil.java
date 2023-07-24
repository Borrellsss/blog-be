package it.itj.academy.blogbe.util.email.user;

import it.itj.academy.blogbe.entity.User;
import it.itj.academy.blogbe.util.email.EmailUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDeletedEmailUtil implements EmailUtil<User> {
    private final JavaMailSender mailSender;

    @Override
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
        message.setContent(setMail(user), "text/html; charset=utf-8");
        mailSender.send(message);
    }
    @Override
    public String setMail(User user) {
        String template;
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentUser.getId().equals(user.getId())) {
            template = String.format(
                """
                    <!DOCTYPE html>
                    <html>
                      <head>
                        <meta charset="UTF-8">
                        <title>Account Deletion Notice</title>
                      </head>
                      <body style="font-family: Arial, sans-serif; background-color: #f2f2f2; color: #333; margin: 0; padding: 0;">
                        <table cellpadding="0" cellspacing="0" border="0" align="center" style="max-width: 600px; margin: 20px auto; background-color: #ffffff; border: 1px solid #ccc;">
                          <tr>
                            <td style="padding: 20px;">
                              <h2 style="color: #dd3d3d; margin-bottom: 20px;">Account Deletion Notice</h2>
                              <p>Dear %s,</p>
                              <p>
                                We regret to inform you that your account on PitechBlog.com has been deleted due to a violation of our policies.
                                This decision was not taken lightly, and it is necessary to maintain a safe and respectful environment for all our users.
                              </p>
                              <p>The specific violation that led to the deletion of your account is as follows:</p>
                              <p>
                                We understand that mistakes can happen, but the violation was severe enough that account deletion was the appropriate
                                course of action. As a user of our platform, it is essential to adhere to our policies and guidelines to ensure a positive
                                experience for everyone.
                              </p>
                              <p>
                                If you have any questions or believe this action was taken in error, please feel free to contact our
                                support team at support@pitech.blog.com for further clarification.
                              </p>
                              <p>We thank you for your understanding.</p>
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
                        <title>Account Deletion Successful (Can be Restored)</title>
                      </head>
                        <body style="font-family: Arial, sans-serif; background-color: #f2f2f2; color: #333; margin: 0; padding: 0;">
                          <table cellpadding="0" cellspacing="0" border="0" align="center" style="max-width: 600px; margin: 20px auto; background-color: #ffffff; border: 1px solid #ccc;">
                            <tr>
                              <td style="padding: 20px;">
                                <h2 style="color: #3dd089; margin-bottom: 20px;">Account Deletion Successful (Can be Restored)</h2>
                                <p>Dear %s,</p>
                                <p>We want to inform you that your account on PitechBlog.com has been successfully deleted as per your request.</p>
                                <p>Please keep in mind that your account data has been temporarily archived and can be restored in the future if you decide to come back.</p>
                                <p>If you wish to restore your account, simply log in using your previous credentials, and you'll be guided through the restoration process.</p>
                                <p>
                                  However, if you didn't request this account deletion or have changed your mind, please contact our support 
                                  team immediately at support@pitech.blog.com so that we can assist you.
                                </p>
                                <p>We appreciate the time you spent with us and hope that you had a positive experience during your stay.</p>
                                <p>If you have any questions or need further assistance, feel free to reach out to us.</p>
                                <p>Thank you for being a part of our community.</p>
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
