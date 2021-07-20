package com.shows.franmaric

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
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

        supportActionBar?.hide()

        initShowsRecycler()

        initToggleListStateButton()
    }

    private fun initToggleListStateButton() {
        binding.toggleListStateButton.setOnClickListener{
            if(binding.toggleListStateButton.isChecked){
                showsAdapter?.setItems(emptyList())
                binding.toggleListStateButton.text = " ADD SHOWS"
                Snackbar.make(binding.root, "Removed all shows :(", Snackbar.LENGTH_SHORT)
                    .show()

            } else {
                showsAdapter?.setItems(ShowsResources.shows)
                binding.toggleListStateButton.text = " REMOVE SHOWS"
                Snackbar.make(binding.root, "Added latest and greatest from Krv nije voda just for you.", Snackbar.LENGTH_SHORT)
                    .show()
            }

            if(showsAdapter?.getItemCount() == 0){
                binding.showsRecyclerView.visibility = View.GONE
            } else {
                binding.showsRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun initShowsRecycler() {
        showsAdapter = ShowsAdapter(ShowsResources.shows) { show ->
            val intent = ShowDetailsActivity.buildIntent(
                originActivity = this,
                showIndex = ShowsResources.shows.indexOf(show)
            )

            println(ShowsResources.shows.indexOf(show))
            startActivity(intent)
        }

        if(showsAdapter?.getItemCount() == 0){
            binding.showsRecyclerView.visibility = GONE
        }

        binding.showsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.showsRecyclerView.adapter = showsAdapter

        binding.showsRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
}
