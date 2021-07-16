package com.shows.franmaric

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.shows.franmaric.data.ShowsResources
import com.shows.franmaric.databinding.ActivityShowsBinding

class ShowsActivity : AppCompatActivity()  {
    private lateinit var binding: ActivityShowsBinding
    private var showsAdapter: ShowsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSuperherosRecycler()
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
