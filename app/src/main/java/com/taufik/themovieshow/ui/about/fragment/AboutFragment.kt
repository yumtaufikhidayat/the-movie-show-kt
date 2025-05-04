package com.taufik.themovieshow.ui.about.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentAboutBinding
import com.taufik.themovieshow.model.response.about.AboutAction
import com.taufik.themovieshow.ui.about.adapter.AboutParentAdapter
import com.taufik.themovieshow.ui.about.viewmodel.AboutViewModel
import com.taufik.themovieshow.utils.CommonConstants
import com.taufik.themovieshow.utils.extensions.showSuccessToastyIcon
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AboutViewModel by viewModels()
    private var aboutParentAdapter: AboutParentAdapter? = null

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
    }

    private fun setToolbar() {
        binding.toolbarAbout.tvToolbar.text = getString(R.string.icAbout)
    }

    private fun setAuthorData() {
        aboutParentAdapter = AboutParentAdapter { about ->
            when (about.id) {
                AboutAction.LinkedIn -> showToastyBasedOnType(CommonConstants.LINKEDIN)
                AboutAction.GitHub -> showToastyBasedOnType(CommonConstants.GITHUB)
                AboutAction.Email -> showToastyBasedOnType(CommonConstants.EMAIL)
                AboutAction.GooglePlay -> showToastyBasedOnType(CommonConstants.GOOGLE_PLAY)
                AboutAction.LanguageSetting -> {
                    // TODO: open language setting
                }
                AboutAction.NoOp -> {
                    // No-op
                }
            }
        }

        binding.rvParentAbout.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = aboutParentAdapter
        }

        aboutParentAdapter?.submitList(viewModel.getAboutData())
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
        aboutParentAdapter = null
    }

    companion object {
        private const val TAG = "AboutFragment"
    }
}