package com.taufik.themovieshow.ui.feature.about.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.databinding.FragmentAboutBinding
import com.taufik.themovieshow.ui.feature.about.adapter.AboutApplicationAdapter
import com.taufik.themovieshow.ui.feature.about.adapter.AboutAuthorAdapter
import com.taufik.themovieshow.ui.feature.about.viewmodel.AboutViewModel

class AboutFragment : Fragment() {

    private lateinit var aboutBinding: FragmentAboutBinding
    private lateinit var viewModel: AboutViewModel
    private lateinit var authorAdapter: AboutAuthorAdapter
    private lateinit var applicationAdapter: AboutApplicationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        aboutBinding = FragmentAboutBinding.inflate(inflater, container, false)
        return aboutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModel()

        setAuthorData()

        setApplicationData()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[AboutViewModel::class.java]
    }

    private fun setAuthorData() {

        authorAdapter = AboutAuthorAdapter()
        authorAdapter.setAbout(viewModel.getAboutAuthor())

        with(aboutBinding.rvAuthorAbout) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = authorAdapter
        }
    }

    private fun setApplicationData() {

        applicationAdapter = AboutApplicationAdapter()
        applicationAdapter.setAbout(viewModel.getAboutApplication())

        with(aboutBinding.rvApplicationAbout) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = applicationAdapter
        }
    }
}