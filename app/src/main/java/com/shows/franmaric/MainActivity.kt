package com.shows.franmaric

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shows.franmaric.databinding.ActivityMainBinding
import com.shows.franmaric.networking.ApiModule

class MainActivity: AppCompatActivity()  {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        ApiModule.initRetrofit(getPreferences(Context.MODE_PRIVATE))
    }
}