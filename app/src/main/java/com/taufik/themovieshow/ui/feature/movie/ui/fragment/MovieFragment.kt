package com.taufik.themovieshow.ui.feature.movie.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentMovieBinding
import com.taufik.themovieshow.ui.adapter.movie.MoviePagerAdapter
import com.taufik.themovieshow.ui.feature.movie.ui.activity.discover.DiscoverMovieActivity

class MovieFragment : Fragment() {

    private lateinit var movieFragmentBinding: FragmentMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        movieFragmentBinding = FragmentMovieBinding.inflate(inflater, container, false)
        return movieFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPager()
    }

    private fun setViewPager() {
        val mainPagerAdapter = MoviePagerAdapter(requireContext(), childFragmentManager)
        movieFragmentBinding.apply {
            viewPagerMovie.adapter = mainPagerAdapter
            tabLayoutMovie.setupWithViewPager(viewPagerMovie)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_discover -> {
                val intent = Intent(requireActivity(), DiscoverMovieActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}