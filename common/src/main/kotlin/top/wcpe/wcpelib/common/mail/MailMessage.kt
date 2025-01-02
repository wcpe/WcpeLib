package top.wcpe.wcpelib.common.mail

/**
 * 由 WCPE 在 2024/3/22 2:29 创建
 * <p>
 * Created by WCPE on 2024/3/22 2:29
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.6.0-SNAPSHOT
 */
// 定义用于发送邮件的数据类
data class MailMessage(
    val recipients: List<String>,
    val subject: String,
    val message: String,
)