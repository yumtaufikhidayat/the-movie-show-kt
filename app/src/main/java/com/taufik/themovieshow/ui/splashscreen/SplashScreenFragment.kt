package com.taufik.themovieshow.ui.splashscreen

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentSplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateToMovie()
        setAppVersion()
    }

    private fun navigateToMovie() {
        lifecycleScope.launch {
            delay(2.seconds)
            findNavController().apply {
                popBackStack()
                navigate(R.id.movieFragment)
            }
        }
    }

    private fun setAppVersion() {
        try {
            val pInfo = activity?.packageManager?.getPackageInfo(activity?.packageName.toString(), 0)
            val appVersion = pInfo?.versionName
            binding.tvAppVersion.text = StringBuilder("v").append(appVersion)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}