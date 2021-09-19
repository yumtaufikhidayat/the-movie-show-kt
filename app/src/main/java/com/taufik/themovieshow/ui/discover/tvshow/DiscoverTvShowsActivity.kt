package com.taufik.themovieshow.ui.discover.tvshow

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.ActivityDiscoverTvShowsBinding
import com.taufik.themovieshow.ui.tvshow.adapter.DiscoverTvShowsAdapter
import com.taufik.themovieshow.ui.tvshow.viewmodel.TvShowsViewModel
import es.dmoral.toasty.Toasty

class DiscoverTvShowsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiscoverTvShowsBinding
    private lateinit var viewModel: TvShowsViewModel
    private lateinit var discoverTvShowsAdapter: DiscoverTvShowsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscoverTvShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActionBar()

        setAdapter()

        setViewModel()

        setData()
    }

    private fun initActionBar() {

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Discover TV Shows"
        supportActionBar?.elevation = 0F
    }

    private fun setAdapter() {
        discoverTvShowsAdapter = DiscoverTvShowsAdapter()
        discoverTvShowsAdapter.notifyDataSetChanged()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[TvShowsViewModel::class.java]
    }

    private fun setData() {
        with(binding.rvDiscoverTvShows) {
            layoutManager = LinearLayoutManager(this@DiscoverTvShowsActivity)
            setHasFixedSize(true)
            adapter = discoverTvShowsAdapter
        }
    }

    private fun showLoading(state: Boolean) {

        binding.apply {
            if (state) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        showLoading(true)

        val inflater = menuInflater
        inflater.inflate(R.menu.discover_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.tvDiscoverTvShows)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                when {
                    query.isNotEmpty() -> {
                        setDiscoverMovies(query)
                        searchView.clearFocus()
                    }

                    query.isEmpty() -> {
                        Toasty.warning(this@DiscoverTvShowsActivity, "Please fill discover form", Toast.LENGTH_SHORT, true).show()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        return true
    }

    private fun setDiscoverMovies(query: String) {

        viewModel.setDiscoverTvShows(BuildConfig.API_KEY, query)
        viewModel.getDiscoverTvShows().observe(this, {
            if (it != null) {
                discoverTvShowsAdapter.setTvShows(it)
                showLoading(false)
            }
        })
    }
}