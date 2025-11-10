package com.easyjob.easyjobapi.utils.mailer;

import com.easyjob.easyjobapi.core.authAccount.models.AuthAccount;
import com.easyjob.easyjobapi.core.passwordReset.management.PasswordResetEmailSendingException;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.model.message.Message;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordResetEmailService {
    private final MailgunMessagesApi mailgunMessagesApi;

    @Value("${mailgun.api.client_domain}")
    private String CLIENT_DOMAIN;

    @Value("${mailgun.api.from}")
    private String SENDER;

    public void sendPasswordRecoveryEmail(AuthAccount user, String code) throws PasswordResetEmailSendingException {
        log.info("Sending password recovery email to user: {}", user.getEmail());
        Message message = Message.builder()
                                 .from(SENDER)
                                 .to(user.getEmail())
                                 .subject("Kod do zresetowania hasła w aplikacji ")
                                 .html("""
                                               <div>
                                                   Kod do zresetowania hasła w aplikacji :
                                                   <br>
                                                   <br>
                                                   <b>%s</b>
                                               </div>
                                               """.formatted(code))
                                 .build();

        try (Response feignResponse = mailgunMessagesApi.sendMessageFeignResponse(CLIENT_DOMAIN, message)) {
            if (feignResponse.status() != 200) {
                log.error("Mail not send!");
                throw new PasswordResetEmailSendingException("Mail not send!");
            }
            log.info("Sent password recovery email to user: {}", user.getEmail());
        }
    }
}

