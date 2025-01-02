package top.wcpe.wcpelib.nukkit.server

/**
 * 由 WCPE 在 2022/6/3 1:53 创建
 *
 * Created by WCPE on 2022/6/3 1:53
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.12-alpha-dev-5
 */
data class RegisterEntityInfo(
    val registerEntityKey: String,
    val hasSpawnEgg: Boolean,
    val summonAble: Boolean,
    val id: String,
    val bid: String,
    val rid: Int,
)