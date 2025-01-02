package top.wcpe.wcpelib.common.mail

import top.wcpe.wcpelib.common.adapter.SectionAdapter

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

data class SmtpConfig(
    val host: String,
    val port: Int,
    val auth: Boolean,
    val ssl: SslConfig,
    val starttls: StartTlsConfig,
) {
    companion object {
        @JvmStatic
        fun load(configAdapter: SectionAdapter): SmtpConfig? {
            val host = configAdapter.getString("host")
            val port = configAdapter.getInt("port")
            val auth = configAdapter.getBoolean("auth")
            val sslSection = configAdapter.getSection("ssl") ?: return null
            val ssl = SslConfig.load(sslSection)
            val starttlsSection = configAdapter.getSection("starttls") ?: return null
            val starttls = StartTlsConfig.load(starttlsSection)

            return SmtpConfig(host, port, auth, ssl, starttls)
        }
    }
}