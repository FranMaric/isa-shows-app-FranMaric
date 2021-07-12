package com.shows.franmaric

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shows.franmaric.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity(){
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent: Intent = getIntent()

        binding.textView.text = "Welcome, " + intent.getStringExtra("name") + "!"
    }
}