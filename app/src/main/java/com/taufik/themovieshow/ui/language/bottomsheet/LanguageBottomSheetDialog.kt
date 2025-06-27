package com.taufik.themovieshow.ui.language.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.BottomSheetLanguageBinding
import com.taufik.themovieshow.model.language.LanguageOption
import com.taufik.themovieshow.ui.about.viewmodel.AboutViewModel
import com.taufik.themovieshow.ui.language.adapter.LanguageListAdapter
import com.taufik.themovieshow.utils.language.LanguageCache
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LanguageBottomSheetDialog : BottomSheetDialogFragment() {

    private val viewModel: AboutViewModel by activityViewModels()
    private var _binding: BottomSheetLanguageBinding? = null
    private val binding get() = _binding!!

    private var onLanguageSelected: ((LanguageOption) -> Unit)? = null

    private val languageList: List<LanguageOption> by lazy {
        arguments?.getParcelableArrayList(ARG_LANGUAGE_LIST) ?: emptyList()
    }

    private val selectedLangCode: String by lazy {
        arguments?.getString(ARG_SELECTED_LANG_CODE).orEmpty()
    }

    fun setListener(callback: (LanguageOption) -> Unit) {
        this.onLanguageSelected = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val list = languageList.map {
            it.copy(isSelected = it.code == selectedLangCode)
        }

        val languageAdapter = LanguageListAdapter { selected ->
            if (selected.code != selectedLangCode) {
                lifecycleScope.launch {
                    viewModel.setLanguage(selected.code)
                    LanguageCache.save(requireContext(), selected.code)
                    onLanguageSelected?.invoke(selected)
                    dismiss()
                }
            }
        }

        binding.recyclerViewLanguage.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = languageAdapter
        }
        languageAdapter.submitList(list)
    }

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val SUCCESS_CHANGE_LANGUAGE = "success_change_language"
        private const val ARG_LANGUAGE_LIST = "arg_language_list"
        private const val ARG_SELECTED_LANG_CODE = "arg_selected_lang"

        fun newInstance(
            languageList: List<LanguageOption>,
            selectedLangCode: String
        ): LanguageBottomSheetDialog {
            return LanguageBottomSheetDialog().apply {
                arguments = bundleOf(
                    ARG_LANGUAGE_LIST to ArrayList(languageList),
                    ARG_SELECTED_LANG_CODE to selectedLangCode
                )
            }
        }
    }
}
