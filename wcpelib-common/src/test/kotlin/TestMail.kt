import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.*


/**
 * 由 WCPE 在 2024/3/16 16:15 创建
 * <p>
 * Created by WCPE on 2024/3/16 16:15
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.6.0-SNAPSHOT
 */
fun main() {
    val username = "test@mail.exa.cn"
    val password = "test"
    val recipients = listOf("test@mail.example.com")

    val properties = Properties()
    properties["mail.smtp.host"] = "smtp.example.com"
    properties["mail.smtp.port"] = 465
    properties["mail.smtp.ssl.enable"] = true

    val session = Session.getInstance(properties, null)

    try {
        val message = MimeMessage(session)
        message.setRecipients(
            Message.RecipientType.TO,
            recipients.map { InternetAddress(it) }.toTypedArray()
        )
        message.subject = "Test Email"
        message.sentDate = Date()
        // 使用 HTML 创建漂亮的文本内容
        val htmlContent = """
            <html>
            <body>
            <h2 style="color: #008CBA;">This is a styled email</h2>
            <p style="font-size: 18px;">Hello, <strong>recipient</strong>! This is a <em>styled</em> email content.</p>
            </body>
            </html>
        """.trimIndent()

        message.setContent(htmlContent, "text/html")

        Transport.send(message)
    } catch (e: MessagingException) {
        e.printStackTrace()
    }
}