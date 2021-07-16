package com.shows.franmaric

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shows.franmaric.data.ShowsResources
import com.shows.franmaric.databinding.ActivityShowsBinding

class ShowsActivity : AppCompatActivity()  {
    companion object {

        fun buildIntent(context: Activity): Intent {
            return Intent(context, ShowsActivity::class.java)
        }
    }

    private lateinit var binding: ActivityShowsBinding
    private var showsAdapter: ShowsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initShowsRecycler()
    }

    private fun initShowsRecycler() {
        showsAdapter = ShowsAdapter(emptyList()) { item ->
            Toast.makeText(this, item.name, Toast.LENGTH_SHORT).show()
        }

        binding.showsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.showsRecyclerView.adapter = showsAdapter

        binding.showsRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
}
