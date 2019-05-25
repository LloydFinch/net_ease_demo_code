package com.devloper.lloydfinch.nesql

/**
 * 实体类
 */
@DbTable("tb_user")
data class User(@DbField(value = "id") val id: String, @DbField(value = "userName") val userName: String, @DbField(value = "psw") val psw: String)