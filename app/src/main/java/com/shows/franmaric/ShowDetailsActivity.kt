package com.shows.franmaric

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shows.franmaric.databinding.ActivityLoginBinding
import com.shows.franmaric.databinding.ActivityShowDetailsBinding

class ShowDetailsActivity : AppCompatActivity() {
    companion object {
        private const val SHOW_INDEX = "SHOW_INDEX"

        fun buildIntent(originActivity: Activity,showIndex: Int): Intent {
            val intent = Intent(originActivity, ShowDetailsActivity::class.java)
            intent.putExtra(SHOW_INDEX, showIndex)

            return intent
        }
    }

    private lateinit var binding: ActivityShowDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}