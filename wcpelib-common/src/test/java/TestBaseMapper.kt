import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import top.wcpe.wcpelib.common.mapper.BaseSQLMapper
import top.wcpe.wcpelib.common.mybatis.Mybatis

/**
 * 由 WCPE 在 2023/2/2 15:11 创建
 * <p>
 * Created by WCPE on 2023/2/2 15:11
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v
 */

class TestBaseMapper {
    companion object {
        @JvmStatic
        lateinit var mybatis: Mybatis

        @BeforeAll
        @JvmStatic
        fun beforeClass() {
            println("start")
            mybatis = Mybatis(
                "localhost",
                3306,
                "test",
                "root",
                "testtesttest",
                "useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC",
                "stat,wall,slf4j",
                128,
                8,
                6000,
                8,
                60000,
                300000,
                "select 1",
                true,
                testOnBorrow = false,
                testOnReturn = false,
                poolPreparedStatements = false,
                maxOpenPreparedStatements = -1,
                removeAbandoned = false,
                removeAbandonedTimeout = 1800,
                logAbandoned = true,
                asyncInit = true
            )
        }

        @AfterAll
        @JvmStatic
        fun afterClass() {
            println("finish")
        }
    }

    @Test
    fun test() {
        mybatis.sqlSessionFactory.openSession(true).use { sqlSession ->
            val mapper = sqlSession.getMapper(BaseSQLMapper::class.java)
            println("existTable: ${mapper.existTable("test", "gift")}")
            println("existColumn: ${mapper.existColumn("test", "gift","player")}")
            println("existTable: ${mapper.existTable("test", "test")}")
            println("existColumn: ${mapper.existColumn("test", "test", "test")}")

        }
    }

}