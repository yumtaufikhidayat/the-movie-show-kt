package com.taufik.themovieshow.ui.about.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.taufik.themovieshow.utils.extensions.showSuccessToastyIcon
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.net.toUri

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

        authorAdapter?.submitList(viewModel.getAboutAuthor())
    }

    private fun setApplicationData() {
        applicationAdapter = AboutApplicationAdapter { position ->
                when (position) {
                    0 -> {
                        // TODO: Open setting fragment
                    }
                    1 -> showToastyBasedOnType(CommonConstants.EMAIL)
                    2 -> showToastyBasedOnType(CommonConstants.GOOGLE_PLAY)
                    3, 4 -> { /* Nothing to click */}
                }
            }

        binding.rvApplicationAbout.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = applicationAdapter
        }

        applicationAdapter?.submitList(viewModel.getAboutApplication())
    }

    private fun showToastyBasedOnType(type: String) {
        when (type) {
            CommonConstants.LINKEDIN -> goToIntent(Intent.ACTION_VIEW, CommonConstants.LINKEDIN_URL_LINK)
            CommonConstants.GOOGLE_PLAY -> goToIntent(Intent.ACTION_VIEW, CommonConstants.GOOGLE_PLAY_URL_LINK)
            CommonConstants.GITHUB -> goToIntent(Intent.ACTION_VIEW, CommonConstants.GITHUB_URL_LINK)
            CommonConstants.EMAIL -> goToIntent(
                Intent.ACTION_SENDTO,
                CommonConstants.GITHUB_URL_LINK,
                CommonConstants.EMAIL_ADDRESS
            )
        }
    }

    private fun goToIntent(
        action: String,
        urlString: String,
        email: String = ""
    ) {
        if (action == Intent.ACTION_SENDTO) {
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
                        getString(R.string.tvSendTo)
                    )
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.localizedMessage}")
                requireContext().showSuccessToastyIcon(getString(R.string.tvInstallEmailApp))
            }
        } else {
            try {
                val intent = Intent(action, urlString.toUri())
                startActivity(
                    Intent.createChooser(
                        intent,
                        getString(R.string.tvOpenWith)
                    )
                )
            } catch (e: Exception) {
                requireContext().showSuccessToastyIcon(getString(
                    R.string.tvInstallBrowser,
                    e.printStackTrace())
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        applicationAdapter = null
    }

    companion object {
        private const val TAG = "AboutFragment"
    }
}