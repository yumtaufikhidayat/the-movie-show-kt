package com.taufik.themovieshow.ui.main.about.fragment

import android.content.Intent
import android.net.Uri
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
import com.taufik.themovieshow.ui.main.about.viewmodel.AboutViewModel
import com.taufik.themovieshow.utils.showToasty
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AboutViewModel by viewModels()
    private var authorAdapter: AboutAuthorAdapter? = null
    private var applicationAdapter: AboutApplicationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setAuthorData()
        setApplicationData()
    }

    private fun setToolbar()  {
        binding.toolbarAbout.tvToolbar.text = getString(R.string.icAbout)
    }

    private fun setAuthorData()  {
        authorAdapter = AboutAuthorAdapter { position ->
            when (position) {
                0 -> showToastyBasedOnType("linkedIn")
                1 -> showToastyBasedOnType("github")
                2 -> showToastyBasedOnType("email")
            }
        }

        binding.rvAuthorAbout.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = authorAdapter
        }

        authorAdapter?.submitList(viewModel.getAboutAuthor(requireContext()))
    }

    private fun setApplicationData()  {
        applicationAdapter = AboutApplicationAdapter { position ->
            when (position) {
                0 -> {}
                1, 2 -> showToastyBasedOnType("google-play")
                3 -> showToastyBasedOnType("email")
            }
        }

        binding.rvApplicationAbout.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = applicationAdapter
        }

        applicationAdapter?.submitList(viewModel.getAboutApplication(requireContext()))
    }

    private fun showToastyBasedOnType(type: String) {
        when (type) {
            "linkedIn" -> {
                val urlLink = "https://linkedin.com/in/taufik-hidayat"
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlLink))
                    startActivity(
                        Intent.createChooser(
                            intent,
                            "Open with:"
                        )
                    )
                } catch (e: Exception) {
                    showToasty(requireContext(), "Please install browser app")
                }
            }

            "google-play" -> {
                val versionLink = "https://play.google.com/store/apps/details?id=com.taufik.themovieshow"
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(versionLink))
                    startActivity(
                        Intent.createChooser(
                            intent,
                            "Open with:"
                        )
                    )
                } catch (e: Exception) {
                    showToasty(requireContext(), "Please install browser app")
                }
            }

            "github" -> {
                val githubLink = "https://github.com/yumtaufikhidayat/the-movie-show-kt"
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubLink))
                    startActivity(
                        Intent.createChooser(
                            intent,
                            "Open with:"
                        )
                    )
                } catch (e: Exception) {
                    showToasty(requireContext(), "Please install browser app")
                }
            }

            "email" -> {
                val email = "yumtaufikhidayat@gmail.com"
                try {
                    val intentEmail = Intent(
                        Intent.ACTION_SENDTO,
                        Uri.fromParts("mailto", email, null)
                    ).apply {
                        putExtra(Intent.EXTRA_EMAIL, email)
                        putExtra(Intent.EXTRA_SUBJECT, "")
                        putExtra(Intent.EXTRA_TEXT, "")
                    }
                    startActivity(
                        Intent.createChooser(
                            intentEmail,
                            "Send email"
                        )
                    )
                } catch (e: Exception) {
                    showToasty(requireContext(), "Please install email app")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        applicationAdapter = null
        _binding = null
    }
}