package com.taufik.themovieshow.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.databinding.FragmentTvShowsBinding
import com.taufik.themovieshow.main.adapter.MovieShowAdapter
import com.taufik.themovieshow.main.viewmodel.MovieShowViewModel

class TVShowsFragment : Fragment() {

    private lateinit var tvShowFragmentBinding: FragmentTvShowsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        tvShowFragmentBinding = FragmentTvShowsBinding.inflate(inflater, container, false)
        return tvShowFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()
    }

    private fun setData() {
        val viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[MovieShowViewModel::class.java]
        val tvShows = viewModel.getTvShows()

        val tvShowAdapter = MovieShowAdapter()
        tvShowAdapter.setMovie(tvShows)

        with(tvShowFragmentBinding.rvTvShow) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowAdapter
        }
    }
}