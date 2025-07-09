package top.wcpe.wcpelib.common.mybatis

import org.apache.ibatis.session.SqlSession
import java.util.function.Consumer
import java.util.function.Function


/**
 * 由 WCPE 在 2022/1/3 21:47 创建
 *
 * Created by WCPE on 2022/1/3 21:47
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.7-alpha-dev-1
 */
object Mybatis {

    fun init(mybatisInstance: MybatisInstance): Mybatis {
        this.mybatisInstance = mybatisInstance
        this.database = mybatisInstance.getDatabaseName()
        return this
    }

    @JvmStatic
    lateinit var mybatisInstance: MybatisInstance

    lateinit var database: String


    fun getDatabaseName(): String {
        return mybatisInstance.getDatabaseName()
    }

    val sqlSessionFactory by lazy {
        mybatisInstance.sqlSessionFactory
    }

    val dataSource by lazy {
        mybatisInstance.dataSource
    }


    fun useSession(callBack: Consumer<SqlSession>) {
        mybatisInstance.useSession(callBack)
    }

    fun <R> useSessionForResult(callBack: Function<SqlSession, R>): R {
        return mybatisInstance.useSessionForResult(callBack)
    }

    fun addMapper(vararg classes: Class<*>?) {
        mybatisInstance.addMapper(*classes)
    }

    fun removeMapper(vararg classes: Class<*>?) {
        mybatisInstance.removeMapper(*classes)
    }
}

