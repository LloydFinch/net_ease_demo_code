package com.devloper.lloydfinch.nesql

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {

    private lateinit var btnInsert: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        init()
    }

    private fun init() {
        btnInsert = findViewById(R.id.btn_insert)
        btnInsert.setOnClickListener {
            val user = User("10000", "Android", "Java")
            val baseDao = BaseDaoFactory.getBaseDao(User::class.java)
            baseDao?.insert(user)
        }
    }
}
