package com.taufik.themovieshow.ui.feature.about.fragment

import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentAboutBinding
import com.taufik.themovieshow.ui.feature.about.adapter.AboutApplicationAdapter
import com.taufik.themovieshow.ui.feature.about.adapter.AboutAuthorAdapter
import com.taufik.themovieshow.ui.feature.about.viewmodel.AboutViewModel
import es.dmoral.toasty.Toasty

class AboutFragment : Fragment() {

    private lateinit var aboutBinding: FragmentAboutBinding
    private lateinit var viewModel: AboutViewModel
    private lateinit var authorAdapter: AboutAuthorAdapter
    private lateinit var applicationAdapter: AboutApplicationAdapter

//    val RC_APP_UPDATES = 1001

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setHasOptionsMenu(true)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        aboutBinding = FragmentAboutBinding.inflate(inflater, container, false)
        return aboutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModel()

        setAuthorData()

        setApplicationData()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[AboutViewModel::class.java]
    }

    private fun setAuthorData() {

        authorAdapter = AboutAuthorAdapter()
        authorAdapter.setAbout(viewModel.getAboutAuthor())

        with(aboutBinding.rvAuthorAbout) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = authorAdapter
        }
    }

    private fun setApplicationData() {

        applicationAdapter = AboutApplicationAdapter()
        applicationAdapter.setAbout(viewModel.getAboutApplication())

        with(aboutBinding.rvApplicationAbout) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = applicationAdapter
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//
//        inflater.inflate(R.menu.about_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        when (item.itemId) {
//            R.id.nav_update -> {
//                checkUpdates()
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    private fun checkUpdates() {
//
//        val appUpdateManager = AppUpdateManagerFactory.create(requireActivity())
//
//        // Returns an intent object that you use to check for an update.
//        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
//
//        // Checks that the platform will allow the specified type of update.
//        Log.d("checkingForUpdates", "Checking for updates")
//        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                // This example applies a flexible update. To apply an immediate update
//                // instead, pass in AppUpdateType.IMMEDIATE
//                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
//                // Request the update.
//                Log.d("updateAvailable", "Update available")
//                try {
//                    appUpdateManager.startUpdateFlowForResult(
//                        // Pass the intent that is returned by 'getAppUpdateInfo()'.
//                        appUpdateInfo,
//                        // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
//                        AppUpdateType.FLEXIBLE,
//                        // The current activity making the update request.
//                        requireActivity(),
//                        // Include a request code to later monitor this update request.
//                        RC_APP_UPDATES)
//                } catch (exception: IntentSender.SendIntentException) {
//                    exception.localizedMessage
//                }
//            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
//
//            } else {
//                Log.d("noUpdatesAvailable", "No Update available")
//                Toasty.success(
//                    requireActivity(),
//                    "Please install browser.",
//                    Toast.LENGTH_SHORT, true
//                ).show()
//            }
//        }
//    }
}