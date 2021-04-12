package com.taufik.themovieshow.ui.feature.about.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.databinding.FragmentAboutBinding
import com.taufik.themovieshow.ui.feature.about.adapter.AboutAdapter
import com.taufik.themovieshow.ui.feature.about.viewmodel.AboutViewModel

class AboutFragment : Fragment() {

    private lateinit var aboutBinding: FragmentAboutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        aboutBinding = FragmentAboutBinding.inflate(inflater, container, false)
        return aboutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()
    }

    private fun setData() {

        val viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[AboutViewModel::class.java]
        val about = viewModel.getAbouts()

        val aboutAdapter = AboutAdapter()
        aboutAdapter.setAbout(about)

        with(aboutBinding.rvAbout) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = aboutAdapter
        }
    }
}