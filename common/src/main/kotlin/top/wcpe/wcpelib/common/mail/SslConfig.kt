package top.wcpe.wcpelib.common.mail

import top.wcpe.wcpelib.common.adapter.ConfigAdapter
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
data class SslConfig(
    val enable: Boolean,
    val trust: String
){
    companion object {
        @JvmStatic
        fun load(configAdapter: SectionAdapter): SslConfig {
            val enable = configAdapter.getBoolean("enable")
            val trust = configAdapter.getString("trust")
            return SslConfig(enable, trust)
        }
    }
}