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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentAboutBinding
import com.taufik.themovieshow.model.language.LanguageOption
import com.taufik.themovieshow.model.response.about.AboutAction
import com.taufik.themovieshow.ui.MainActivity
import com.taufik.themovieshow.ui.about.adapter.AboutParentAdapter
import com.taufik.themovieshow.ui.about.viewmodel.AboutViewModel
import com.taufik.themovieshow.ui.language.bottomsheet.LanguageBottomSheetDialog
import com.taufik.themovieshow.utils.CommonConstants
import com.taufik.themovieshow.utils.extensions.restartAppWithLanguageChange
import com.taufik.themovieshow.utils.extensions.showSuccessToasty
import com.taufik.themovieshow.utils.extensions.showSuccessToastyIcon
import com.taufik.themovieshow.utils.language.LANGUAGE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        setupRecyclerView()
        observeLanguageChange()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.languageFlow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect {
                    setToolbar() // update title toolbar
                    refreshAboutData() // refresh list About
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
                AboutAction.Email -> sendEmail(CommonConstants.EMAIL_ADDRESS)
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

            val existingDialog = parentFragmentManager.findFragmentByTag("LanguageBottomSheetDialog")
            if (existingDialog == null) {
                val dialog = LanguageBottomSheetDialog.newInstance(languageList, currentLang)
                dialog.setListener {
                    requireActivity().let {
                        it.showSuccessToasty(getString(R.string.tvSuccesfullyChangedLanguage))
                        if (it is MainActivity) {
                            it.restartAppWithLanguageChange(requireContext(), MainActivity::class.java)
                        }
                    }
                }
                dialog.show(parentFragmentManager, "LanguageBottomSheetDialog")
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

    private fun sendEmail(email: String) {
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
        _binding = null
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
    }
}