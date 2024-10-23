package com.example.recipefinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.recipefinder.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        Handler().postDelayed({
            val intent = if (currentUser != null) {
                Intent(this, Dashboard::class.java)
            } else {
                Intent(this, loginscreen::class.java)
            }
            startActivity(intent)
            finish()
        }, 3000)
    }
}
