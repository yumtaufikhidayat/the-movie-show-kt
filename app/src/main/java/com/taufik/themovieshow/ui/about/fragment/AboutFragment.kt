package com.taufik.themovieshow.ui.about.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.R
import com.taufik.themovieshow.base.fragment.BaseFragment
import com.taufik.themovieshow.databinding.FragmentAboutBinding
import com.taufik.themovieshow.model.language.LanguageOption
import com.taufik.themovieshow.model.response.about.AboutAction
import com.taufik.themovieshow.ui.about.adapter.AboutParentAdapter
import com.taufik.themovieshow.ui.about.viewmodel.AboutViewModel
import com.taufik.themovieshow.ui.language.bottomsheet.LanguageBottomSheetDialog
import com.taufik.themovieshow.utils.extensions.refreshActivity
import com.taufik.themovieshow.utils.extensions.showSuccessToastyIcon
import com.taufik.themovieshow.utils.language.LANGUAGE
import com.taufik.themovieshow.utils.objects.CommonConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AboutFragment : BaseFragment<FragmentAboutBinding>() {

    private val viewModel: AboutViewModel by viewModels()
    private var aboutParentAdapter: AboutParentAdapter? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAboutBinding = FragmentAboutBinding.inflate(inflater, container, false)

    override fun onFragmentReady(savedInstanceState: Bundle?) {
        setToolbar()
        setupRecyclerView()
        observeLanguageChange()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.languageFlow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect {
                    setToolbar()
                    refreshAboutData()
                }
        }
    }

    private fun setToolbar() {
        binding.toolbarAbout.tvToolbar.text = getString(R.string.icAbout)
    }

    private fun setupRecyclerView() {
        aboutParentAdapter = AboutParentAdapter { about ->
            when (about.id) {
                AboutAction.LinkedIn -> openUrl(CommonConstants.LINKEDIN_URL_LINK)
                AboutAction.GitHub -> openUrl(CommonConstants.GITHUB_URL_LINK)
                AboutAction.Email -> sendEmail()
                AboutAction.GooglePlay -> openUrl(CommonConstants.GOOGLE_PLAY_URL_LINK)
                AboutAction.LanguageSetting -> showLanguageOptions()
                AboutAction.NoOp -> Unit
            }
        }

        binding.rvParentAbout.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = aboutParentAdapter
        }

        aboutParentAdapter?.submitList(viewModel.getAboutData(requireContext()))
    }

    private fun showLanguageOptions() {
        viewLifecycleOwner.lifecycleScope.launch {
            val currentLang = viewModel.getCurrentLanguage()
            val languageList = listOf(
                LanguageOption(LANGUAGE.INDONESIA.code, getString(R.string.tvLanguageIndonesia), R.drawable.ic_indonesia_flag_round_circle, false),
                LanguageOption(LANGUAGE.ENGLISH.code, getString(R.string.tvLanguageEnglish), R.drawable.ic_usa_flag_round_circle, false)
            )

            val existingDialog = parentFragmentManager.findFragmentByTag(BOTTOM_SHEET_LANGUAGE_TAG)
            if (existingDialog == null) {
                val dialog = LanguageBottomSheetDialog.newInstance(languageList, currentLang)
                dialog.setListener {
                    requireActivity().let {
                        activity?.refreshActivity()
                    }
                }
                dialog.show(parentFragmentManager, BOTTOM_SHEET_LANGUAGE_TAG)
            }
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            startActivity(Intent.createChooser(intent, getString(R.string.tvOpenWith)))
        } catch (e: Exception) {
            requireContext().showSuccessToastyIcon(getString(R.string.tvInstallBrowser, e.message ?: ""))
        }
    }

    private fun sendEmail() {
        val email = CommonConstants.EMAIL_ADDRESS
        try {
            val intentEmail = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null)).apply {
                putExtra(Intent.EXTRA_EMAIL, email)
                putExtra(Intent.EXTRA_SUBJECT, "")
                putExtra(Intent.EXTRA_TEXT, "")
            }
            startActivity(Intent.createChooser(intentEmail, getString(R.string.tvSendTo)))
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.localizedMessage}")
            requireContext().showSuccessToastyIcon(getString(R.string.tvInstallEmailApp))
        }
    }

    private fun refreshAboutData() {
        aboutParentAdapter?.submitList(viewModel.getAboutData(requireContext()))
    }

    private fun observeLanguageChange() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.languageFlow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect {
                    setToolbar()
                    refreshAboutData()
                }
        }
    }

    override fun onDestroyView() {
        aboutParentAdapter = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        setToolbar()
        refreshAboutData()
    }

    companion object {
        private const val TAG = "AboutFragment"
        private const val BOTTOM_SHEET_LANGUAGE_TAG = "LanguageBottomSheetDialog"
    }
}