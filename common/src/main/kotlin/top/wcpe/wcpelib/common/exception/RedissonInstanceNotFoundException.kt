package top.wcpe.wcpelib.common.exception

/**
 * 由 WCPE 在 2024/12/13 18:39 创建
 * <p>
 * Created by WCPE on 2024/12/13 18:39
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 */
data class RedissonInstanceNotFoundException(override val message: String) : Exception(message)