package top.wcpe.wcpelib.common.mail

import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.*

/**
 * 由 WCPE 在 2024/3/17 14:38 创建
 * <p>
 * Created by WCPE on 2024/3/17 14:38
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.6.0-SNAPSHOT
 */
class Mail(private val config: MailConfig) {
    fun sendMail(mails: List<MailMessage>) {
        val session = Session.getInstance(config.properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(config.user, config.password)
            }
        })

        for (mail in mails) {
            val mimeMessage = MimeMessage(session)
            mimeMessage.setFrom(InternetAddress(config.user))
            mimeMessage.setRecipients(
                Message.RecipientType.TO,
                mail.recipients.map { InternetAddress(it) }.toTypedArray()
            )
            mimeMessage.subject = mail.subject
            mimeMessage.sentDate = Date()
            mimeMessage.setText(mail.message)
            Transport.send(mimeMessage)
        }
    }
}