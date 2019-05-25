package com.devloper.lloydfinch.nesql

import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.lang.Exception

/**
 * 单例工厂类
 */
object BaseDaoFactory {

    private lateinit var sqLiteDatabase: SQLiteDatabase
    /**
     * 数据库存放位置，存放到在包目录下
     * 但是不建议这么些，适配性不好，建议使用系统目录(Environment)
     */
    private val path: String = "data/data/com.devloper.lloydfinch.nesql/ne.db"

    init {
        //检查目录并创建
        val file = File(path)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }

        //创建数据路
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(path, null)
    }

    fun <T> getBaseDao(beanClass: Class<T>): BaseDao<T>? {
        try {
            val baseDao: BaseDao<T> = BaseDao::class.java.newInstance() as BaseDao<T>
            baseDao.init(sqLiteDatabase, beanClass)
            return baseDao
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

}