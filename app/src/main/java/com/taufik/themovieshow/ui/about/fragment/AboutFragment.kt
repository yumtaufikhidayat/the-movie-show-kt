package com.taufik.themovieshow.ui.about.fragment

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
import com.taufik.themovieshow.ui.about.adapter.AboutApplicationAdapter
import com.taufik.themovieshow.ui.about.adapter.AboutAuthorAdapter
import com.taufik.themovieshow.ui.about.viewmodel.AboutViewModel
import com.taufik.themovieshow.utils.CommonConstants
import com.taufik.themovieshow.utils.showSuccessToastyIcon
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

    private fun setToolbar() {
        binding.toolbarAbout.tvToolbar.text = getString(R.string.icAbout)
    }

    private fun setAuthorData() {
        authorAdapter = AboutAuthorAdapter { position ->
            when (position) {
                0 -> showToastyBasedOnType(CommonConstants.LINKEDIN)
                1 -> showToastyBasedOnType(CommonConstants.GITHUB)
                2 -> showToastyBasedOnType(CommonConstants.EMAIL)
            }
        }

        binding.rvAuthorAbout.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = authorAdapter
        }

        authorAdapter?.submitList(viewModel.getAboutAuthor(requireContext()))
    }

    private fun setApplicationData() {
        applicationAdapter = AboutApplicationAdapter { position ->
                when (position) {
                    0 -> {
                        // no reaction while given clicked action
                    }
                    1, 2 -> showToastyBasedOnType(CommonConstants.GOOGLE_PLAY)
                    3 -> showToastyBasedOnType(CommonConstants.EMAIL)
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
            CommonConstants.LINKEDIN -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(CommonConstants.LINKEDIN_URL_LINK))
                    startActivity(
                        Intent.createChooser(
                            intent,
                            getString(R.string.tvOpenWith)
                        )
                    )
                } catch (e: Exception) {
                    requireContext().showSuccessToastyIcon(getString(R.string.tvInstallBrowser, e.printStackTrace()))
                }
            }

            CommonConstants.GOOGLE_PLAY -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(CommonConstants.GOOGLE_PLAY_URL_LINK))
                    startActivity(
                        Intent.createChooser(
                            intent,
                            "Open with:"
                        )
                    )
                } catch (e: Exception) {
                    requireContext().showSuccessToastyIcon("Please install browser app")
                }
            }

            CommonConstants.GITHUB -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(CommonConstants.GITHUB_URL_LINK))
                    startActivity(
                        Intent.createChooser(
                            intent,
                            "Open with:"
                        )
                    )
                } catch (e: Exception) {
                    requireContext().showSuccessToastyIcon("Please install browser app")
                }
            }

            CommonConstants.EMAIL -> {
                val email = CommonConstants.EMAIL_ADDRESS
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
                    requireContext().showSuccessToastyIcon("Please install email app")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        applicationAdapter = null
    }
}