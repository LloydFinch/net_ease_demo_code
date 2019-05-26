package com.devloper.lloydfinch.nesql

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.lang.reflect.Field
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 底层实现类
 */
open class BaseDao<T> : IBaseDao<T> {

    val TAG = "BaseDao"
    /**
     * native数据库引用
     */
    private lateinit var sqLiteDatabase: SQLiteDatabase

    /**
     * 表名
     */
    private lateinit var tableName: String

    private lateinit var beanClass: Class<T>

    /**
     * 牺牲内存提升效率
     */
    private lateinit var cacheMap: HashMap<String, Field>

    /**
     * 是否已经初始化
     */
    private var isInit: AtomicBoolean = AtomicBoolean(false)

    open fun init(sqLiteDatabase: SQLiteDatabase, beanClass: Class<T>): Boolean {
        this.sqLiteDatabase = sqLiteDatabase
        this.beanClass = beanClass

        if (!isInit.get()) {
            //检查1
            if (!sqLiteDatabase.isOpen) {
                throw IllegalStateException("you should open SQLiteDatabase firstly!")
            }

            //获取表名字，如果没有就返回一个空串
            tableName = this.beanClass.getAnnotation(DbTable::class.java)?.value ?: ""

            //检查2
            if (tableName.isEmpty()) {
                throw IllegalStateException("the class has not inject by DbTable annotation, please check your code")
            }

            //创建table
            sqLiteDatabase.execSQL(getCreateTableSql())

            cacheMap = HashMap()
            initCacheMap()

            isInit.set(true)
        }

        return false
    }

    /**
     * 对成成员进行缓存
     * key-注解 value-field
     */
    private fun initCacheMap() {
        beanClass.declaredFields.forEach {
            cacheMap[it.getAnnotation(DbField::class.java).value] = it
        }
    }

    /**
     * 返回创建数据表语句
     */
    private fun getCreateTableSql(): String {
        //根据反射
        val fs = beanClass.declaredFields.fold(StringBuffer()) { acc, field ->
            when (field.type) {
                String::class.java -> {
                    acc.append("${field.name} TEXT,")
                }
                Int::class.java -> {
                    acc.append("${field.name} INTEGER,")
                }
                Long::class.java -> {
                    acc.append("${field.name} LONG,")
                }
                else -> {
                    //不支持的类型，跳过不处理
                    acc
                }
            }
        }
        Log.e(TAG, "before handle, fs = $fs")
        //移除最后一个逗号
        if (fs.isNotEmpty()) {
            fs.deleteCharAt(fs.length - 1)
        }
        Log.e(TAG, "after handle, fs = $fs")

        val create = "create table if not exists $tableName ($fs)"
        Log.e(TAG, "after handle, create = $create")

        return create
    }

    override fun insert(bean: T): Long {
        val map = getValue(bean)
        val contentValues = getContentValues(map)
        val row = sqLiteDatabase.insert(tableName, null, contentValues)
        Log.e(TAG, "insert successfully $row")
        return row
    }

    private fun getContentValues(map: Map<String, String>): ContentValues {
        val contentValues = ContentValues()
        for ((k, v) in map) {
            contentValues.put(k, v)
        }
        return contentValues
    }

    private fun getValue(bean: T): Map<String, String> {
        val map: MutableMap<String, String> = HashMap()
        cacheMap.values.forEach {
            it.isAccessible = true //使得非public的可以访问
            val obj = it.get(bean)
            obj?.apply {
                val key = it.getAnnotation(DbField::class.java).value
                val value = obj.toString()
                if (key.isNotEmpty() && value.isNotEmpty()) {
                    map[key] = value
                }
            }
        }
        Log.e(TAG, "---------------------------------------------------")
        return map
    }
}