package top.wcpe.wcpelib.common.mybatis.typehandler

import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.UUID

/**
 * @author : NuStar
 * Date : 2025/7/9 17:42
 * Website : <a href="https://www.nustar.top">nustar's web</a>
 * Github : <a href="https://github.com/nustarworld">nustar's github</a>
 * QQ : 3318029085
 */
class UUIDTypeHandler : BaseTypeHandler<UUID>() {
    override fun setNonNullParameter(preparedStatement: PreparedStatement?, i: Int, uuid: UUID?, jdbcType: JdbcType?) {
        preparedStatement?.setString(i, uuid.toString())
    }

    override fun getNullableResult(resultSet: ResultSet?, s: String?): UUID {
        return UUID.fromString(resultSet?.getString(s))
    }

    override fun getNullableResult(resultSet: ResultSet?, i: Int): UUID {
        return UUID.fromString(resultSet?.getString(i))
    }

    override fun getNullableResult(callableStatement: CallableStatement?, i: Int): UUID {
        return UUID.fromString(callableStatement?.getString(i))
    }
}