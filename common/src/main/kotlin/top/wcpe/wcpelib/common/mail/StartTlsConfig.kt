package top.wcpe.wcpelib.common.mail

import top.wcpe.wcpelib.common.adapter.ConfigAdapter
import top.wcpe.wcpelib.common.adapter.SectionAdapter

/**
 * 由 WCPE 在 2024/3/22 2:19 创建
 * <p>
 * Created by WCPE on 2024/3/22 2:19
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.6.0-SNAPSHOT
 */
data class StartTlsConfig(
    val enable: Boolean,
) {
    companion object {
        @JvmStatic
        fun load(configAdapter: SectionAdapter): StartTlsConfig {
            val enable = configAdapter.getBoolean("enable")
            return StartTlsConfig(enable)
        }
    }
}