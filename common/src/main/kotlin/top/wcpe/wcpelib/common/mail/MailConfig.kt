package top.wcpe.wcpelib.common.mail

import top.wcpe.wcpelib.common.adapter.ConfigAdapter
import top.wcpe.wcpelib.common.adapter.SectionAdapter
import java.util.*

/**
 * 由 WCPE 在 2024/3/22 2:18 创建
 * <p>
 * Created by WCPE on 2024/3/22 2:18
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.6.0-SNAPSHOT
 */
data class MailConfig(
    val user: String,
    val password: String,
    val smtp: SmtpConfig,
) {

    companion object {
        @JvmStatic
        fun load(configAdapter: SectionAdapter): MailConfig? {

            val user = configAdapter.getString("user")
            val password = configAdapter.getString("password")
            val section = configAdapter.getSection("smtp") ?: return null
            val smtpConfig = SmtpConfig.load(section) ?: return null

            return MailConfig(user, password, smtpConfig)
        }
    }

    val properties = Properties()

    init {
        properties["mail.smtp.auth"] = smtp.auth
        properties["mail.smtp.host"] = smtp.host
        properties["mail.smtp.port"] = smtp.port
        properties["mail.smtp.starttls.enable"] = smtp.starttls.enable
        properties["mail.smtp.ssl.enable"] = smtp.ssl.enable
    }
}