package com.shows.franmaric

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shows.franmaric.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity(){

    companion object {
        private const val EXTRA_USERNAME = "EXTRA_USERNAME"

        fun buildIntent(originActivity: Activity, userName: String): Intent {
            val intent = Intent(originActivity, WelcomeActivity::class.java)
            intent.putExtra(EXTRA_USERNAME, userName)

            return intent
        }
    }

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.extras?.getString(EXTRA_USERNAME)

        binding.textView.text = "Welcome, " + userName + "!"
    }
}