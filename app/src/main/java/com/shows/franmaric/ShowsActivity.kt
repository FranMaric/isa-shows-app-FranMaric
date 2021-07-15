package com.shows.franmaric

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shows.franmaric.databinding.ActivityShowsBinding

class ShowsActivity : AppCompatActivity()  {
    private lateinit var binding: ActivityShowsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
