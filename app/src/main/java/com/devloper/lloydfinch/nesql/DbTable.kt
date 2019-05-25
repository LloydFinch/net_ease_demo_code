package com.devloper.lloydfinch.nesql

/**
 * 用于指定数据表名的注解
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class DbTable(val value: String = "")

