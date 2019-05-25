package com.devloper.lloydfinch.nesql

/**
 * 用于获取字段类型的注解
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class DbField(val value: String = "")