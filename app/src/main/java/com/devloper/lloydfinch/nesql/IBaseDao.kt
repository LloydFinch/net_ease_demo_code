package com.devloper.lloydfinch.nesql

/**
 * 高层接口
 */
interface IBaseDao<T> {

    /**
     * 插入数据
     */
    fun insert(bean: T): Long
}