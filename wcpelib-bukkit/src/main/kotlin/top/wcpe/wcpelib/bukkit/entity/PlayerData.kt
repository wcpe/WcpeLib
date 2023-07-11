package top.wcpe.wcpelib.bukkit.entity

import top.wcpe.wcpelib.bukkit.WcpeLib

/**
 * 由 WCPE 在 2023/7/10 15:23 创建
 * <p>
 * Created by WCPE on 2023/7/10 15:23
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.5-alpha-dev-3
 */
data class PlayerData(
    val id: Int = -1,
    val playerName: String,
    val uuid: String,
    var lastServerName: String = WcpeLib.getServerName(),
    val firstLoginTime: Long,
    var lastLoginTime: Long = firstLoginTime
)