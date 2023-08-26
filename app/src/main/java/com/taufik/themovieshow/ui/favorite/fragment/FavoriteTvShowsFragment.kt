package com.taufik.themovieshow.ui.favorite.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.databinding.FragmentFavoriteTvShowsBinding
import com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResult
import com.taufik.themovieshow.ui.favorite.adapter.FavoriteTvShowsAdapter
import com.taufik.themovieshow.ui.favorite.viewmodel.FavoriteTvShowViewModel
import com.taufik.themovieshow.utils.navigateToDetailTvShow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteTvShowsFragment : Fragment() {

    private var _binding: FragmentFavoriteTvShowsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteTvShowViewModel by viewModels()
    private var favoriteTvShowsAdapter: FavoriteTvShowsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTvShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        getFavoriteTvShow()
        searchData()
    }

    private fun setAdapter() {
        favoriteTvShowsAdapter = FavoriteTvShowsAdapter {
            navigateToDetailTvShow(it.id, it.name)
        }

        binding.rvDiscoverFavoriteTvShow.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = favoriteTvShowsAdapter
        }
    }

    private fun getFavoriteTvShow() {
        viewModel.getFavoriteTvShow().observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                favoriteTvShowsAdapter?.setData(mapList(it))
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
                favoriteTvShowsAdapter?.filter?.filter(p0.toString())
            })
        }
    }

    private fun mapList(tvShows: List<FavoriteTvShowEntity>): ArrayList<TvShowsMainResult> {
        val listTvShow = arrayListOf<TvShowsMainResult>()
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

    private fun hideKeyboard() {
        binding.apply {
            etSearch.clearFocus()
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
        }
    }

    private fun showNoFavorite(isShow: Boolean) {
        binding.apply {
            if (isShow) {
                layoutNoFavorite.apply {
                    root.isVisible = true
                    tvErrorTitle.apply {
                        isVisible = true
                        text = getString(R.string.tvNoFavoriteData)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOrange))
                    }
                    tvErrorDesc.apply {
                        isVisible = true
                        text = getString(R.string.tvSaveFavoriteTvShows)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOrange))
                    }
                }
            } else {
                layoutNoFavorite.root.isVisible = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}