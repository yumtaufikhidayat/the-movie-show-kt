package com.taufik.themovieshow.ui.main.favorite.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.local.entity.FavoriteTvShow
import com.taufik.themovieshow.data.main.tvshow.popularairingtoday.TvShowsMainResult
import com.taufik.themovieshow.data.viewmodel.tvshow.FavoriteTvShowViewModel
import com.taufik.themovieshow.databinding.FragmentFavoriteTvShowsBinding
import com.taufik.themovieshow.ui.main.tvshow.adapter.TvShowsAdapter

class FavoriteTvShowsFragment : Fragment() {

    private var _binding: FragmentFavoriteTvShowsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteTvShowViewModel by viewModels()
    private val tvShowsAdapter by lazy { TvShowsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteTvShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        getFavoriteTvShow()
        searchData()
    }

    private fun setAdapter() = with(binding) {
        rvDiscoverFavoriteTvShow.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowsAdapter
        }
    }

    private fun getFavoriteTvShow() {
        viewModel.getFavoriteTvShow()?.observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                tvShowsAdapter.setData(mapList(it))
                showNoFavorite(false)
            } else {
                showNoFavorite(true)
            }
        }
    }

    private fun searchData() {
        binding.etSearch.apply {
            setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard()
                    return@OnEditorActionListener true
                }
                false
            })

            addTextChangedListener(afterTextChanged = { p0 ->
                tvShowsAdapter.filter.filter(p0.toString())
            })
        }
    }

    private fun mapList(tvShows: List<FavoriteTvShow>): ArrayList<TvShowsMainResult> {
        val listTvShow = ArrayList<TvShowsMainResult>()
        tvShows.forEach { tvShow ->
            val tvShowMapped = TvShowsMainResult(
                tvShow.tvShowFirstAirDate,
                tvShow.tvShowId,
                tvShow.tvShowTitle,
                tvShow.tvShowPoster,
                tvShow.tvShowRating
            )
            listTvShow.add(tvShowMapped)
        }

        return listTvShow
    }

    private fun hideKeyboard() = with(binding) {
        etSearch.clearFocus()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
    }

    private fun showNoFavorite(isShow: Boolean) = with(binding) {
        if (isShow) {
            layoutNoFavorite.apply {
                root.isVisible = true
                imgError.setImageResource(R.drawable.ic_outline_no_favorite)
                tvError.text = getString(R.string.tvNoFavorite)
            }
        } else {
            layoutNoFavorite.root.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}