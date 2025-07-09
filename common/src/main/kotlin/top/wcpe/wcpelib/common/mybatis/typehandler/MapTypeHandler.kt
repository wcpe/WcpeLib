package top.wcpe.wcpelib.common.mybatis.typehandler

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * @author : NuStar
 * Date : 2025/7/9 17:47
 * Website : <a href="https://www.nustar.top">nustar's web</a>
 * Github : <a href="https://github.com/nustarworld">nustar's github</a>
 * QQ : 3318029085
 */
class MapTypeHandler : BaseTypeHandler<Map<*, *>>() {
    private var objectMapper = ObjectMapper()
    override fun setNonNullParameter(preparedStatement: PreparedStatement?, i: Int, map: Map<*, *>?, jdbcType: JdbcType?) {
        preparedStatement?.setString(i, toJson(map))
    }

    override fun getNullableResult(resultSet: ResultSet?, s: String?): Map<*, *> {
        return toMap(resultSet?.getString(s))
    }

    override fun getNullableResult(resultSet: ResultSet?, i: Int): Map<*, *> {
        return toMap(resultSet?.getString(i))
    }

    override fun getNullableResult(callableStatement: CallableStatement?, i: Int): Map<*, *> {
        return toMap(callableStatement?.getString(i))
    }

    private fun toJson(map: Map<*, *>?): String {
        return objectMapper.writeValueAsString(map)
    }
    object StringIntMapTypeReference : TypeReference<Map<*, *>>()
    private fun toMap(json: String?): Map<*, *> {
        return objectMapper.readValue(json, StringIntMapTypeReference)
    }
}