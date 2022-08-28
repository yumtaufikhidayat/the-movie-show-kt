package com.taufik.themovieshow.ui.tvshow.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.taufik.themovieshow.databinding.FragmentDetailTvShowBinding
import com.taufik.themovieshow.ui.tvshow.adapter.TvShowsCastAdapter
import com.taufik.themovieshow.ui.tvshow.model.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.ui.tvshow.model.discover.DiscoverTvShowsResult
import com.taufik.themovieshow.ui.tvshow.viewmodel.DetailTvShowViewModel

class DetailTvShowFragment : Fragment() {

    private var _binding: FragmentDetailTvShowBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailTvShowViewModel by viewModels()

    private var idTvShow = 0
    private var title = ""
    private lateinit var data: TvShowsPopularDetailResponse
    private lateinit var tvShowResult: DiscoverTvShowsResult
    private lateinit var castAdapter: TvShowsCastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = com.taufik.themovieshow.databinding.FragmentDetailTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundleData()
//        setData()
//        setVideo()
//        setCastAdapter()
//        setRecyclerView()
//        setCast()
//        setReadMore()
    }

    private fun getBundleData() {
        idTvShow = arguments?.getInt(EXTRA_DETAIL_TV_ID) ?: 0
        title = arguments?.getString(EXTRA_DETAIL_TV_TITLE) ?: ""
        Log.i("TAG", "getBundleData: $idTvShow $title")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_DETAIL_TV_ID = "com.taufik.themovieshow.ui.tvshow.fragment.EXTRA_DETAIL_TV_ID"
        const val EXTRA_DETAIL_TV_TITLE = "com.taufik.themovieshow.ui.tvshow.fragment.EXTRA_DETAIL_TV_ID"
    }
}