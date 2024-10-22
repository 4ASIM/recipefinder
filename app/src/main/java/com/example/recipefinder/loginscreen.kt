package com.example.recipefinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class loginscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginscreen)

        val intent = Intent(this,loginscreen::class.java)
    }
}