/*
 * *
 *  * Created by Syed Usama Ahmad on 3/17/23, 11:37 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/17/23, 11:01 PM
 *
 */

@file:Suppress("DEPRECATION")

package com.erickruo.allinonevideodownloader.activities

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.android.billingclient.api.*
import com.erickruo.allinonevideodownloader.BuildConfig
import com.erickruo.allinonevideodownloader.activities.newUi.GalleryActivityNewUi
import com.erickruo.allinonevideodownloader.activities.newUi.MainActivityNewUi
import com.erickruo.allinonevideodownloader.databinding.ActivityMainBinding
import com.erickruo.allinonevideodownloader.extraFeatures.ExtraFeaturesFragment
import com.erickruo.allinonevideodownloader.fragments.DownloadMainFragment
import com.erickruo.allinonevideodownloader.inappbilling.BillingClientSetup
import com.erickruo.allinonevideodownloader.models.adminpanel.Setting
import com.erickruo.allinonevideodownloader.snapchatstorysaver.SnapChatBulkStoryDownloader
import com.erickruo.allinonevideodownloader.statussaver.StatusSaverMainFragOld
import com.erickruo.allinonevideodownloader.statussaver.StatusSaverMainFragment
import com.erickruo.allinonevideodownloader.utils.*
import com.erickruo.allinonevideodownloader.webservices.DownloadVideosMain
import com.github.javiersantos.piracychecker.PiracyChecker
import com.github.javiersantos.piracychecker.callbacks.PiracyCheckerCallback
import com.github.javiersantos.piracychecker.enums.PiracyCheckerError
import com.github.javiersantos.piracychecker.enums.PirateApp
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.ump.*
import com.onesignal.OneSignal
import com.suddenh4x.ratingdialog.AppRating
import com.suddenh4x.ratingdialog.preferences.RatingThreshold
import java.util.concurrent.Executor
import kotlin.system.exitProcess
import com.erickruo.allinonevideodownloader.R


@Keep
class MainActivity : AppCompatActivity() {


    private var mBottomSheetDialog: BottomSheetDialog? = null
    private var nn: String? = "nnn"
    private var myString: String? = ""
    private val REQUEST_PERMISSION_CODE = 1001
    private val requiredPermissions = arrayOf(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requiredPermissionsTIRAMISU = arrayOf(
        android.Manifest.permission.READ_MEDIA_IMAGES,
        android.Manifest.permission.READ_MEDIA_VIDEO,
        android.Manifest.permission.READ_MEDIA_AUDIO
    )

    private lateinit var progressDralogGenaratinglink: ProgressDialog

    private val APP_UPDATE_REQUEST_CODE = 261

    private var billingClient: BillingClient? = null

    private lateinit var fragment: Fragment
    private lateinit var binding: ActivityMainBinding
    private var biometricPrompt: BiometricPrompt? = null
    private var promptInfo: BiometricPrompt.PromptInfo? = null
    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>
    private var hasNotificationPermissionGranted = false


    private lateinit var consentInformation: ConsentInformation
    private lateinit var consentForm: ConsentForm
    var isfgOpen = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {


            binding = ActivityMainBinding.inflate(layoutInflater)
            val view = binding.root
            view.keepScreenOn = true
            setContentView(view)

        } catch (e: Throwable) {
            e.printStackTrace()
        }


        try {
            requestPermissionsLauncher =
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                    setLayout()
                    if (SDK_INT < 33) {
                        //  restartApp()
                    }

                }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (requiredPermissionsTIRAMISU.all { isPermissionGranted(it) }) {
                    setLayout()
                } else {

                    showStoragePermissionRationale()
                    runpermissioncheck()
                }
            } else {
                if (requiredPermissions.all { isPermissionGranted(it) }) {
                    setLayout()
                } else {
                    showStoragePermissionRationale()
                    runpermissioncheck()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


        try {


            if (SDK_INT >= 33) {

                val manager = getSystemService(
                    NotificationManager::class.java
                )
                val areNotificationsEnabled = manager.areNotificationsEnabled()

                if (!areNotificationsEnabled) showNotificationPermissionRationale()
            } else {
                hasNotificationPermissionGranted = true
            }

            progressDralogGenaratinglink = ProgressDialog(this@MainActivity)
            progressDralogGenaratinglink.setMessage(resources.getString(R.string.genarating_download_link))
            progressDralogGenaratinglink.setCancelable(false)

            val action = intent.action
            val type = intent.type

            if (Intent.ACTION_SEND == action && type != null) {
                if ("text/plain" == type) {
                    SplashScreen.initDlLibs(this@MainActivity, false)
                    handleSendText(intent) // Handle text being sent
                }
            }


            if (SDK_INT > 27) {
                AppRating.Builder(this)
                    .setMinimumLaunchTimes(5)
                    .setMinimumDays(5)
                    .setMinimumLaunchTimesToShowAgain(5)
                    .setMinimumDaysToShowAgain(10)
                    .setRatingThreshold(RatingThreshold.FOUR)
                    .showIfMeetsConditions()
            }








            doAllBioMetricTasks()

            initIInAppBillingAcknologement()

            binding.addRemoveFab.setOnClickListener {
                isfgOpen = true
                binding.fragmentContainer.visibility = View.VISIBLE
//                startActivity(Intent(this, ExtraFeaturesActivity::class.java))

                val fragment = ExtraFeaturesFragment() // Replace YourFragment with your actual fragment class

                // Add the fragment to the fragment container
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
                binding.addRemoveFab.visibility = View.GONE
            }


            if (!Constants.show_subscription) {
                binding.subscriptionFab.visibility = View.GONE
            } else {
                if (!BuildConfig.ISPRO) {
                    openAdsConsentDialog()
                    binding.subscriptionFab.visibility = View.VISIBLE
                    binding.subscriptionFab.setOnClickListener {
                        try {

                            if (iUtils.isSubactive) {
                                runOnUiThread {
                                    iUtils.ShowToast(
                                        this@MainActivity,
                                        resources.getString(R.string.youhavealready),
                                    )
                                }
                            } else {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        SubscriptionActivity::class.java
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    binding.subscriptionFab.visibility = View.GONE
                }
            }


            if (Constants.show_Ads && !BuildConfig.ISPRO && AdsManager.status_AdmobBanner) {

                val pp = SharedPrefsMainApp(this@MainActivity).preferencE_inappads
                if (pp.equals("nnn")) {

                    MobileAds.initialize(
                        this@MainActivity
                    ) {
                        AdsManager.loadBannerAd(
                            this@MainActivity,
                            binding.bannerContainer
                        )
                    }
                } else {
                    binding.bannerContainer.visibility = View.GONE
                }
            } else {
                binding.bannerContainer.visibility = View.GONE
            }


        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    private fun openAdsConsentDialog() {

        // Set tag for under age of consent. false means users are not under
        // age.
        val params = ConsentRequestParameters
            .Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()


//        val debugSettings = ConsentDebugSettings.Builder(this)
//            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
//            .addTestDeviceHashedId("526ADF044BBF93ED0DFF932622F387D1")//a39cc1c1f7543c75
//            .build()
//
//        val params = ConsentRequestParameters
//            .Builder()
//            .setConsentDebugSettings(debugSettings)
//            .build()


        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            {

                // The consent information state was updated.
                // You are now ready to check if a form is available.
                if (consentInformation.isConsentFormAvailable) {
                    println("workinbbb 1")
                    loadForm()
                }
                println("workinbbb 0")
            },
            {
                // Handle the error.
                println("workinbbb 4")
            })

    }

    fun runpermissioncheck() {
        try {
            PiracyChecker(this@MainActivity)
                .enableGooglePlayLicensing(getString(R.string.GOOGLE_PLAY_App_Liscencekey))
                .enableUnauthorizedAppsCheck()
                .saveResultToSharedPreferences("allvideoprefs", "allvideoprefs_valid_license")
                .callback(object : PiracyCheckerCallback() {
                    override fun allow() {}
                    override fun doNotAllow(
                        error: PiracyCheckerError,
                        app: PirateApp?
                    ) {

                    }

                    override fun onError(error: PiracyCheckerError) {}
                })
                .start()

            if (!BuildConfig.DEBUG) {
//                verifyAppInstall(iUtils.verifyInstallerId(this@MainActivity))
                iUtils.showCookiesLL(this@MainActivity);
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }


    private fun loadForm() {
        // Loads a consent form. Must be called on the main thread.
        UserMessagingPlatform.loadConsentForm(
            this,
            {
                this.consentForm = it
                if (consentInformation.consentStatus == ConsentInformation.ConsentStatus.REQUIRED) {
                    consentForm.show(
                        this
                    ) {
                        if (consentInformation.consentStatus == ConsentInformation.ConsentStatus.OBTAINED) {
                            // App can start requesting ads.
                        }

                        // Handle dismissal by reloading form.
                        loadForm()
                    }
                }
            },
            {
                // Handle the error.
            }
        )
    }


    private fun doAllBioMetricTasks() {
        try {

            val biometricManager: BiometricManager = BiometricManager.from(this@MainActivity)
            when {
                biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS -> {

                }
                biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    val prefs = getSharedPreferences("whatsapp_pref", MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putBoolean("isBio", false)
                    editor.apply()

//TODO
//                    runOnUiThread {
//                        iUtils.ShowToastError(
//                            this@MainActivity,
//                            "This device does not have a fingerprint sensor",
//                        )
//                    }
                }
                biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {

                    runOnUiThread {
                        iUtils.ShowToastError(
                            this@MainActivity,
                            "The biometric sensor is currently unavailable",
                        )
                    }
                    val prefs = getSharedPreferences("whatsapp_pref", MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putBoolean("isBio", false)
                    editor.apply()
                }
                biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {

//                    runOnUiThread {
//                        iUtils.ShowToastError(
//                            this@MainActivity,
//                            "Your device doesn't have fingerprint saved, please check your security settings",
//
//                            )
//                    }
                    val prefs = getSharedPreferences("whatsapp_pref", MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putBoolean("isBio", false)
                    editor.apply()
                }
            }


            val executor: Executor = ContextCompat.getMainExecutor(this@MainActivity)
            biometricPrompt =
                BiometricPrompt(
                    this@MainActivity,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)

                            runOnUiThread {
                                iUtils.ShowToastError(
                                    this@MainActivity,
                                    "Error in Authentication $errString"
                                )
                            }
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    GalleryActivity::class.java
                                )
                            )
                        }

                    })

            promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Verify Identity")
                .setDescription("Use your fingerprint or device credentials to Access Gallery ")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                // .setNegativeButtonText("Cancel")
                .build()





            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!isFinishing) {
                        try {
                            showBottomSheetExitDialog()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                }

            })

        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }


    private fun getBinding(): ActivityMainBinding? {
        return if (::binding.isInitialized) {
            binding
        } else {
            null;
        }
    }

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (SDK_INT >= 33) {
//                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                    showNotificationPermissionRationale()
//                    } else {
//                        showSettingDialog()
//                    }
                }
            }

        }


    private fun showNotificationPermissionRationale() {
        if (Build.VERSION.SDK_INT >= 33 && !hasNotificationPermissionGranted) {
            try {
                if (!isFinishing) {
                    val alertBuilder = androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
                    alertBuilder.setCancelable(true)
                    alertBuilder.setTitle(getString(R.string.alert))
                    alertBuilder.setMessage(getString(R.string.notificationpermission))
                    alertBuilder.setPositiveButton(R.string.yes) { _, _ ->
                        if (Build.VERSION.SDK_INT >= 33) {
                            try {
                                OneSignal.promptForPushNotifications()
                            } catch (e: Throwable) {
                                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                            }
                        } else {
                            hasNotificationPermissionGranted = true
                        }

                    }
                    alertBuilder.setNegativeButton(getString(R.string.cancel), null)
                    val alert = alertBuilder.create()
                    alert.show()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        } else {
            hasNotificationPermissionGranted = true
        }


    }


    private fun showStoragePermissionRationale() {
        try {
            if (!isFinishing) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (requiredPermissionsTIRAMISU.all { isPermissionGranted(it) }) {
                        return
                    }
                } else {
                    if (requiredPermissions.all { isPermissionGranted(it) }) {
                        return
                    }
                }

                val alertBuilder = androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
                alertBuilder.setCancelable(true)
                alertBuilder.setTitle(getString(R.string.alert))
                alertBuilder.setMessage(getString(R.string.write_neesory))
                alertBuilder.setPositiveButton(R.string.yes) { _, _ ->

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (!requiredPermissionsTIRAMISU.all { isPermissionGranted(it) }) {
                            requestPermissionsLauncher.launch(requiredPermissionsTIRAMISU)
                        }
                    } else {
                        if (!requiredPermissions.all { isPermissionGranted(it) }) {
                            requestPermissionsLauncher.launch(requiredPermissions)
                        }
                    }

                }
                alertBuilder.setNegativeButton(getString(R.string.cancel), null)
                val alert = alertBuilder.create()
                alert.show()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    fun getMyData(): String? {
        return myString
    }


    fun setmydata(mysa: String) {
        this.myString = mysa
    }

    private fun verifyAppInstall(isinatll: Boolean) {
        try {
            println("myappisvalid $isinatll")
            if (!isinatll) {
                if (!isFinishing) {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle(getString(R.string.please_install))
                        .setCancelable(false)
                        .setMessage(
                            getString(R.string.isappfromplaystore)
                        )
                        .setPositiveButton(
                            resources.getString(R.string.installapp)
                        )
                        { dialog, _ ->
                            dialog.dismiss()
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                                )
                            )

                        }
                        .setNegativeButton(
                            resources.getString(R.string.useanyway)
                        )
                        { dialog, _ ->
                            dialog.dismiss()

                        }
                        .setIcon(R.drawable.ic_appicon)
                        .show()
                }
            }
        } catch (e: java.lang.Exception) {

            println("appupdater error rrrr $e")
            e.printStackTrace()
        }
    }

    private fun handleSendText(intent: Intent) {
        try {
            this.intent = null
            var url = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (url.equals("") && iUtils.checkURL(url)) {
                iUtils.ShowToast(
                    this@MainActivity,
                    this@MainActivity.resources?.getString(R.string.enter_valid)
                )
                return
            }

            if (url?.contains("myjosh.in")!!) {
                try {
                    url = iUtils.extractUrls(url)[0]
                } catch (_: Exception) {

                }
                DownloadVideosMain.Start(this@MainActivity, url!!.trim(), false)


            } else if (url.contains("chingari")) {
                try {
                    url = iUtils.extractUrls(url)[0]
                } catch (_: Exception) {

                }
                DownloadVideosMain.Start(this@MainActivity, url!!.trim(), false)

            } else if (url.contains("snapchat.com")) {
                try {
                    url = iUtils.extractUrls(url)[0]
                    val i = Intent(
                        this@MainActivity,
                        SnapChatBulkStoryDownloader::class.java
                    )
                    i.putExtra("urlsnap", url?.trim())
                    startActivity(i)
                } catch (_: Exception) {

                }

            } else if (url.contains("bemate")) {

                try {
                    url = iUtils.extractUrls(url)[0]
                } catch (_: Exception) {

                }
                DownloadVideosMain.Start(this@MainActivity, url!!.trim(), false)

            } else if (url.contains("instagram.com")) {


                val bundle = Bundle()
                bundle.putString("myinstaurl", url)
                val fragobj = DownloadMainFragment()
                fragobj.arguments = bundle
                this.setmydata(url)

                Log.e("downloadFileName12 insta ", url)
            } else if (url.contains("sck.io") || url.contains("snackvideo")) {
                try {
                    url = iUtils.extractUrls(url)[0]
                } catch (_: Exception) {

                }
                DownloadVideosMain.Start(this@MainActivity, url!!.trim(), false)

            } else {
                try {
                    url = iUtils.extractUrls(url)[0]
                } catch (_: Exception) {

                }

                val myurl = url
                val bundle = Bundle()
                bundle.putString("myinstaurl", myurl)
                val fragobj = DownloadMainFragment()
                fragobj.arguments = bundle

                this.setmydata(myurl.toString())


            }
        } catch (_: Exception) {

        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        try {
            Log.e(
                "myhdasbdhf newintent ",
                intent?.getStringExtra(Intent.EXTRA_TEXT) + "_46237478234"
            )
            intent?.let { newIntent ->
                //  handleSendText(newIntent)
                if (fragment is DownloadMainFragment) {
                    Log.e("myhdasbdhf downlaod ", newIntent.getStringExtra(Intent.EXTRA_TEXT) + "")

                    val my: DownloadMainFragment? = fragment as DownloadMainFragment?
                    // Pass intent or its data to the fragment's method

                    my?.requireView()?.findViewById<EditText>(R.id.etURL)
                        ?.setText(newIntent.getStringExtra(Intent.EXTRA_TEXT).toString())
                    my?.DownloadVideo(newIntent.getStringExtra(Intent.EXTRA_TEXT).toString())
                } else {
                    handleSendText(newIntent)
                    Log.e(
                        "myhdasbdhf notdownload ",
                        newIntent.getStringExtra(Intent.EXTRA_TEXT) + ""
                    )

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setLayout() {
        try {
            Log.d("isNeedGrantPermission", " layout")

            setupViewPager(binding.viewpager)

            if (Constants.isNonPlayStoreApp) {
                binding.bottomNavBar.onItemSelected = {
                    Log.d("NowTest",it.toString())
                    when (it) {
                        0 -> binding.viewpager.currentItem = 0

                        1 -> binding.viewpager.currentItem = 1

                        2 -> binding.viewpager.currentItem = 2
                    }

                }
            }else{
                binding.bottomNavBarNonPlay.visibility = View.VISIBLE
                binding.bottomNavBar.visibility = View.GONE
                binding.bottomNavBarNonPlay.onItemSelected = {
                    when (it) {
                        0 -> binding.viewpager.currentItem = 0
                        1 -> binding.viewpager.currentItem = 1
                    }

                }
            }

            binding.viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (Constants.isNonPlayStoreApp) {
                        binding.bottomNavBar.itemActiveIndex = position
                    }else{
                        binding.bottomNavBarNonPlay.itemActiveIndex = position

                    }
                }
            })

            binding.viewpager.offscreenPageLimit = 1


        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    private fun setupViewPager(viewPager: ViewPager2) {
        try {
            val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
            adapter.addFragment(DownloadMainFragment(), getString(R.string.download_fragment))
            if (Constants.isNonPlayStoreApp) {
            if (SDK_INT >= 29) {
                adapter.addFragment(StatusSaverMainFragment(), getString(R.string.StatusSaver))
            } else {
                adapter.addFragment(StatusSaverMainFragOld(), getString(R.string.StatusSaver))
            }
            }
//            adapter.addFragment(ExtraFeaturesFragment(), getString(R.string.extrafeatures))
            viewPager.adapter = adapter

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission,
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        try {
            if (requestCode == REQUEST_PERMISSION_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    setLayout()
                    restartApp()
                } else {
                    iUtils.ShowToast(
                        this@MainActivity,
                        getString(R.string.info_permission_denied)
                    )
                    //TODO
                    //finish()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            iUtils.ShowToast(this@MainActivity, getString(R.string.info_permission_denied))
            finish()
        }
    }

    private class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun createFragment(position: Int): Fragment {
            // Hardcoded in this order, you'll want to use lists and make sure the titles match
            return if (position == 0) {
                mFragmentList[0]
            } else mFragmentList[position]
        }


        override fun getItemCount(): Int {
            return mFragmentList.size
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)


        val pp = SharedPrefsMainApp(this@MainActivity).preferencE_inappads

        if (pp.equals("nnn")) {

            menu.findItem(R.id.action_removeads).isVisible = true

        } else if (pp.equals("ppp")) {

            menu.findItem(R.id.action_removeads).isVisible = false

        }

        if (!Constants.show_subscription) {
            menu.findItem(R.id.action_removeads).isVisible = false
        } else {
            menu.findItem(R.id.action_removeads).isVisible = !BuildConfig.ISPRO
        }


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_privacy -> {
                try {
                    val browserIntent =
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(
                                Setting.getPrivacyPolicy(
                                    this@MainActivity,
                                    Constants.iSAdminAttached
                                )
                            )
                        )
                    startActivity(browserIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                true
            }

            R.id.action_downloadtiktok -> {
                val intent = Intent(this@MainActivity, TikTokDownloadWebview::class.java)
                startActivity(intent)
                true
            }

            R.id.action_rate -> {
                try {
                    if (!isFinishing) {
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle(getString(R.string.RateAppTitle))
                            .setMessage(getString(R.string.RateApp))
                            .setCancelable(false)
                            .setPositiveButton(
                                getString(R.string.rate_dialog)
                            ) { _, _ ->
                                val appPackageName = packageName
                                try {
                                    startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("market://details?id=$appPackageName")
                                        )
                                    )
                                } catch (anfe: android.content.ActivityNotFoundException) {
                                    startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                                        )
                                    )
                                }
                            }
                            .setNegativeButton(getString(R.string.later_btn), null).show()
                    }
                } catch (_: Exception) {
                }
                true
            }

            R.id.action_share -> {


                try {
                    iUtils.shareApp(this@MainActivity)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }


                true
            }


            R.id.ic_whatapp -> {
                val launchIntent = packageManager.getLaunchIntentForPackage("com.whatsapp")
                if (launchIntent != null) {
                    startActivity(launchIntent)
                    //  finish()
                } else {
                    iUtils.ShowToast(
                        this@MainActivity,
                        this.resources.getString(R.string.appnotinstalled)
                    )
                }
                true
            }

            R.id.action_language -> {
                if (!isFinishing) {
                    val dialog = Dialog(this@MainActivity)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(true)
                    dialog.setContentView(R.layout.dialog_change_language)

                    val l_english = dialog.findViewById(R.id.l_english) as TextView
                    l_english.setOnClickListener {
                        changeLanguage(application, View.LAYOUT_DIRECTION_RTL, "en")
                        recreate()
                        dialog.dismiss()
                    }


                    val l_french = dialog.findViewById(R.id.l_french) as TextView
                    l_french.setOnClickListener {
                        changeLanguage(application, View.LAYOUT_DIRECTION_RTL, "fr")
                        recreate()
                        dialog.dismiss()
                    }


                    val l_arabic = dialog.findViewById(R.id.l_arabic) as TextView
                    l_arabic.setOnClickListener {
                        changeLanguage(application, View.LAYOUT_DIRECTION_LTR, "ar")
                        recreate()
                        dialog.dismiss()

                    }
                    val l_urdu = dialog.findViewById(R.id.l_urdu) as TextView
                    l_urdu.setOnClickListener {
                        changeLanguage(application, View.LAYOUT_DIRECTION_LTR, "ur")
                        recreate()
                        dialog.dismiss()
                    }


                    val l_german = dialog.findViewById(R.id.l_german) as TextView
                    l_german.setOnClickListener {
                        changeLanguage(application, View.LAYOUT_DIRECTION_RTL, "de")
                        recreate()
                        dialog.dismiss()
                    }


                    val l_turkey = dialog.findViewById(R.id.l_turkey) as TextView
                    l_turkey.setOnClickListener {
                        changeLanguage(application, View.LAYOUT_DIRECTION_RTL, "tr")
                        recreate()
                        dialog.dismiss()
                    }


                    val l_portougese = dialog.findViewById(R.id.l_portougese) as TextView
                    l_portougese.setOnClickListener {
                        changeLanguage(application, View.LAYOUT_DIRECTION_RTL, "pt")
                        recreate()
                        dialog.dismiss()
                    }


                    val l_chinese = dialog.findViewById(R.id.l_chinese) as TextView
                    l_chinese.setOnClickListener {
                        changeLanguage(application, View.LAYOUT_DIRECTION_RTL, "zh")
                        recreate()
                        dialog.dismiss()
                    }


                    val l_hindi = dialog.findViewById(R.id.l_hindi) as TextView
                    l_hindi.setOnClickListener {
                        changeLanguage(application, View.LAYOUT_DIRECTION_RTL, "hi")
                        recreate()
                        dialog.dismiss()
                    }

                    dialog.show()
                }
                true
            }

            R.id.action_removeads -> {
                try {


                    if (iUtils.isSubactive) {
                        runOnUiThread {
                            iUtils.ShowToast(
                                this@MainActivity,
                                resources.getString(R.string.youhavealready),
                            )
                        }
                    } else {
                        startActivity(Intent(this@MainActivity, SubscriptionActivity::class.java))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                true
            }

            R.id.action_opengallery -> {
                try {
                    val prefs = getSharedPreferences("whatsapp_pref", MODE_PRIVATE)
                    val lang = prefs.getBoolean("isBio", false)
                    if (lang) {
                        biometricPrompt!!.authenticate(promptInfo!!)
                    } else {
                        if (iUtils.isNewUi) {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    GalleryActivityNewUi::class.java
                                )
                            )
                        } else {
                            startActivity(Intent(this@MainActivity, GalleryActivity::class.java))
                        }
                    }

                } catch (e: Throwable) {
                    e.printStackTrace()
                    if (iUtils.isNewUi) {
                        startActivity(Intent(this@MainActivity, GalleryActivityNewUi::class.java))
                    } else {
                        startActivity(Intent(this@MainActivity, GalleryActivity::class.java))
                    }

                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun changeLanguage(context: Application?, direction: Int, language: String) {
        LocaleHelper.setLocale(context, language)
        window.decorView.layoutDirection = direction
        val editor: SharedPreferences.Editor = getSharedPreferences(
            "lang_pref",
            Context.MODE_PRIVATE
        ).edit()
        editor.putString("lang", language)
        editor.apply()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
           if (isfgOpen){
               binding.fragmentContainer.visibility = View.GONE
               isfgOpen = false
               binding.addRemoveFab.visibility = View.VISIBLE
           }else{
               if (!isFinishing) {
                   try {
                       showBottomSheetExitDialog()
                   } catch (e: Exception) {
                       e.printStackTrace()
                   }

               }
           }



    }


    private fun showBottomSheetExitDialog() {
        try {
            @SuppressLint("InflateParams") val view: View =
                layoutInflater.inflate(R.layout.dialog_exit, null)
            val lytBottomSheet = view.findViewById<FrameLayout>(R.id.bottom_sheet)

            lytBottomSheet.background = ContextCompat.getDrawable(this, R.drawable.bg_rounded_light)

            val btnRate = view.findViewById<Button>(R.id.btn_rate)
            val btnShare = view.findViewById<Button>(R.id.btn_share)
            val btnExit = view.findViewById<Button>(R.id.btn_exit)
            val frameAd = view.findViewById<FrameLayout>(R.id.fl_adplaceholder)
            nn = SharedPrefsMainApp(this@MainActivity).preferencE_inappads

            if (Constants.show_Ads && !BuildConfig.ISPRO && nn == "nnn") {

                MainActivityNewUi.LoadAdTask(this@MainActivity, frameAd).execute()
            } else {
                frameAd.visibility = View.GONE
            }
// TODO load native ad or Large Banner on exit dialog

            btnRate.setOnClickListener {
                iUtils.rateApp(this)
                mBottomSheetDialog?.dismiss()
            }
            btnShare.setOnClickListener {
                iUtils.shareApp(this)
                mBottomSheetDialog?.dismiss()
            }
            btnExit.setOnClickListener {

                try {
                    mBottomSheetDialog?.dismiss()
                    finishAndRemoveTask()
                    exitProcess(0)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }

            }

            mBottomSheetDialog = BottomSheetDialog(this, R.style.SheetDialogLight)

            mBottomSheetDialog!!.setContentView(view)
            mBottomSheetDialog!!.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val bottomSheetBehavior: BottomSheetBehavior<*> = mBottomSheetDialog!!.behavior
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {}
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
            mBottomSheetDialog!!.show()
            mBottomSheetDialog!!.setOnDismissListener {
                mBottomSheetDialog = null
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @SuppressLint("StaticFieldLeak")
    class LoadAdTask(private val context: Activity, private val frameLayout: FrameLayout) :
        AsyncTask<Void, Void, Unit>() {

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void?) {
            AdsManager.loadAdmobNativeAd(context, frameLayout)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("proddddd11111222 $resultCode __$data")
        if (requestCode == 200 && resultCode == RESULT_OK) {
            println("proddddd11111 $resultCode __$data")
        }
        if (requestCode == APP_UPDATE_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                runOnUiThread {
                    iUtils.ShowToastError(
                        this@MainActivity,
                        resources.getString(R.string.updatefailed),
                    )
                }
            }
        }


    }


    private fun initIInAppBillingAcknologement() {
        try {
            println("mypurchase12 2 = ")
            billingClient = BillingClientSetup.getInstance(this@MainActivity) { _, list ->
                if (list != null) {
                    for (purchase in list) handlesAlreadyPurchased(purchase)
                }
            }
            billingClient!!.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    try {

                        println("mypurchase 2 = ")
                        loadAllSubscribePackage()
                        billingClient!!.queryPurchasesAsync(
                            BillingClient.SkuType.SUBS
                        ) { _: BillingResult, purchases: List<Purchase> ->
                            println("mypurchase 6.5 = ")
                            if (purchases.size > 0) {
                                iUtils.isSubactive = true
                                runOnUiThread {
                                    binding.subscriptionFab.visibility = View.GONE
                                }

                                for (purchase in purchases) handlesAlreadyPurchased(purchase)
                            } else {
                                iUtils.isSubactive = false
                                runOnUiThread {
                                    if (!Constants.show_subscription) {
                                        binding.subscriptionFab.visibility = View.GONE
                                    } else {
                                        if (!BuildConfig.ISPRO) {
                                            binding.subscriptionFab.visibility = View.VISIBLE
                                        } else {
                                            binding.subscriptionFab.visibility = View.GONE
                                        }

                                    }
                                }

//                            println("mypurchase 4 = " + billingResult1.responseCode)
                                SharedPrefsMainApp(this@MainActivity).preferencE_inappads =
                                    "nnn"

//                            println("mypurchase 9 nnndd= " + purchases[0].skus)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onBillingServiceDisconnected() {

//                    runOnUiThread {
//                        iUtils.ShowToast(this@MainActivity,
//                            "You are disconnected from Billing Service",
//                        )
//                    }

                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadAllSubscribePackage() {
        try {
            if (billingClient!!.isReady) {


                val productList =
                    listOf(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(getString(R.string.playstoresubscription_premium1month))
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build(),
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(getString(R.string.playstoresubscription_premium3month))
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build(),
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(getString(R.string.playstoresubscription_premium6months))
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build(),
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(getString(R.string.playstoresubscription_premium12months))
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build()
                    )




                billingClient!!.queryProductDetailsAsync(
                    QueryProductDetailsParams.newBuilder().setProductList(productList).build()
                ) { _,
                    productDetailsList ->


                    println("mypurchase 1 = ")
                    try {
                        iUtils.SkuDetailsList = productDetailsList
//                        println("mypurchase 0 = " + productDetailsList)
//                        println("mypurchase 0 = " + productDetailsList[0].subscriptionOfferDetails!![1].pricingPhases.pricingPhaseList[0].formattedPrice)

                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }

                }

            } else {
//todo
//                runOnUiThread {
//                    iUtils.ShowToastError(
//                        this@MainActivity,
//                        resources.getString(R.string.billingnotready),
//                    )
//                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handlesAlreadyPurchased(purchases: Purchase) {
        if (purchases.purchaseState == Purchase.PurchaseState.PURCHASED) {
            iUtils.isSubactive = true
            runOnUiThread {
                binding.subscriptionFab.visibility = View.GONE
                binding.bannerContainer.visibility = View.GONE
            }

            SharedPrefsMainApp(this@MainActivity).preferencE_inappads = "ppp"
        }
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }

    private fun restartApp() {
        try {
            val i = packageManager.getLaunchIntentForPackage(
                packageName
            )
            i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AdsManager.destroyAdview()
    }


    override fun onResume() {
        super.onResume()
        println(">>>>>>>>>>>> main resumed")
        try {
            if (Constants.iSAdminAttached) {
                SplashScreen.getExistingAdminPanelData(this@MainActivity)
            }
            doAllBioMetricTasks()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


}