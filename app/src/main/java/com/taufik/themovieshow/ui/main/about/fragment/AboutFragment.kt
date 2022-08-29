package com.taufik.themovieshow.ui.main.about.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentAboutBinding
import com.taufik.themovieshow.ui.main.about.adapter.AboutApplicationAdapter
import com.taufik.themovieshow.ui.main.about.adapter.AboutAuthorAdapter
import com.taufik.themovieshow.data.viewmodel.about.AboutViewModel

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AboutViewModel by viewModels()
    private lateinit var authorAdapter: AboutAuthorAdapter
    private lateinit var applicationAdapter: AboutApplicationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setAuthorData()
        setApplicationData()
    }

    private fun setToolbar() = with(binding) {
        toolbarAbout.tvToolbar.text = getString(R.string.icAbout)
    }

    private fun setAuthorData() = with(binding) {
        authorAdapter = AboutAuthorAdapter()
        authorAdapter.setAbout(viewModel.getAboutAuthor())

        rvAuthorAbout.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = authorAdapter
        }
    }

    private fun setApplicationData() = with(binding){
        applicationAdapter = AboutApplicationAdapter()
        applicationAdapter.setAbout(viewModel.getAboutApplication())

        rvApplicationAbout.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = applicationAdapter
        }
    }
}