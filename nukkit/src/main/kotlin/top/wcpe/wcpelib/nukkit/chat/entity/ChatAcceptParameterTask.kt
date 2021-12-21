package top.wcpe.wcpelib.nukkit.chat.entity

import cn.nukkit.Player

/**
 * 由 WCPE 在 2021/12/18 19:52 创建
 *
 * Created by WCPE on 2021/12/18 19:52
 *
 * Github: https://github.com/wcpe
 *
 * QQ: 1837019522
 *
 * @author WCPE
 */
data class ChatAcceptParameterTask(
    val timeStamp: Long,
    val cancelJudgeTask: (player: Player, message: String) -> Boolean,
    val cancelSuccessTask: (player: Player, message: String) -> Unit,
    val judge: (player: Player, message: String) -> Boolean,
    val judgeTrueTask: (player: Player, message: String) -> Unit,
    val judgeFalseTask: (player: Player, message: String) -> Unit
)
