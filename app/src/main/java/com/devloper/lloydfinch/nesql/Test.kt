package com.devloper.lloydfinch.nesql

import android.support.annotation.NonNull

fun main() {

    println(String::class.java.toString())
    println(String.javaClass.toString())

    val str = ""
    str.isNotEmpty().apply {
        println("str = $str")
    }


    SS::class.java.declaredFields.forEach {
        println(it.name)
        val annotation = it.getAnnotation(DbField::class.java)
        println(annotation.value)
    }
}

data class SS(
    @DbField val name: String = "name"
)