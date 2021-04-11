package com.taufik.themovieshow.ui.feature.movie.ui.activity.discover

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
import com.taufik.themovieshow.databinding.ActivityDiscoverMovieBinding
import com.taufik.themovieshow.ui.feature.movie.ui.adapter.DiscoverMovieAdapter
import com.taufik.themovieshow.ui.feature.movie.viewmodel.MovieViewModel
import es.dmoral.toasty.Toasty

class DiscoverMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiscoverMovieBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var discoverMovieAdapter: DiscoverMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscoverMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActionBar()

        setAdapter()

        setViewModel()

        setData()
    }

    private fun initActionBar() {

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Discover Movies"
        supportActionBar?.elevation = 0F
    }

    private fun setAdapter() {
        discoverMovieAdapter = DiscoverMovieAdapter()
        discoverMovieAdapter.notifyDataSetChanged()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MovieViewModel::class.java]
    }

    private fun setData() {
        with(binding.rvDiscoverMovies) {
            layoutManager = LinearLayoutManager(this@DiscoverMovieActivity)
            setHasFixedSize(true)
            adapter = discoverMovieAdapter
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
        searchView.queryHint = resources.getString(R.string.tvDiscoverMovie)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                setDiscoverMovies(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        return true
    }

    private fun setDiscoverMovies(query: String) {

        if (query.isEmpty()) {
            Toasty.warning(this, "Please fill discover form", Toast.LENGTH_SHORT, true).show()
        }

        viewModel.setDiscoverMovie(BuildConfig.API_KEY, query)
        viewModel.getDiscoverMovie().observe(this, {
            if (it != null) {
                discoverMovieAdapter.setMovies(it)
                showLoading(false)
            }
        })
    }
}