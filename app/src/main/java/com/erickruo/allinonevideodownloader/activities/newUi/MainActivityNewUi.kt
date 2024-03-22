/*
 * *
 *  * Created by Syed Usama Ahmad on 3/17/23, 11:37 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/17/23, 10:26 PM
 *
 */

@file:Suppress("DEPRECATION", "NAME_SHADOWING")


package com.erickruo.allinonevideodownloader.activities.newUi

import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.billingclient.api.*
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.github.javiersantos.piracychecker.PiracyChecker
import com.github.javiersantos.piracychecker.callbacks.PiracyCheckerCallback
import com.github.javiersantos.piracychecker.enums.PiracyCheckerError
import com.github.javiersantos.piracychecker.enums.PirateApp
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.ump.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.erickruo.allinonevideodownloader.BuildConfig
import com.erickruo.allinonevideodownloader.activities.*
import com.erickruo.allinonevideodownloader.databinding.ActivityMainNewdesignBinding
import com.erickruo.allinonevideodownloader.inappbilling.BillingClientSetup
import com.erickruo.allinonevideodownloader.models.adminpanel.Setting
import com.erickruo.allinonevideodownloader.models.instawithlogin.CarouselMedia
import com.erickruo.allinonevideodownloader.models.instawithlogin.ModelInstaWithLogin
import com.erickruo.allinonevideodownloader.models.storymodels.ModelEdNode
import com.erickruo.allinonevideodownloader.models.storymodels.ModelGetEdgetoNode
import com.erickruo.allinonevideodownloader.models.storymodels.ModelInstagramResponse
import com.erickruo.allinonevideodownloader.services.MyFirebaseMessagingService
import com.erickruo.allinonevideodownloader.snapchatstorysaver.SnapChatBulkStoryDownloader
import com.erickruo.allinonevideodownloader.utils.*
import com.erickruo.allinonevideodownloader.utils.AdsManager.mRewardedAd
import com.erickruo.allinonevideodownloader.webservices.DownloadVideosMain
import com.erickruo.allinonevideodownloader.webservices.api.RetrofitApiInterface
import com.erickruo.allinonevideodownloader.webservices.api.RetrofitClient
import com.onesignal.OneSignal
import com.suddenh4x.ratingdialog.AppRating
import com.suddenh4x.ratingdialog.preferences.RatingThreshold
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.commons.lang3.StringEscapeUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.lang.reflect.Type
import java.net.URI
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess
import com.erickruo.allinonevideodownloader.R

@Keep
class MainActivityNewUi : AppCompatActivity() {

    companion object {

        lateinit var myString: String

    }

    private var mBottomSheetDialog: BottomSheetDialog? = null

    lateinit var nativeadContainer: FrameLayout
    lateinit var instaprivatefbprivate: TextView

    lateinit var prefEditor: SharedPreferences.Editor
    lateinit var pref: SharedPreferences
    private var nn: String? = "nnn"
    private var csRunning = false
    var myVideoUrlIs: String? = null
    var myInstaUsername: String? = ""

    var myPhotoUrlIs: String? = null
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null


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

    private val REQUEST_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

    private lateinit var progressDralogGenaratinglink: ProgressDialog

    private var billingClient: BillingClient? = null

    var fragment: Fragment? = null
    private lateinit var binding: ActivityMainNewdesignBinding
    private var biometricPrompt: BiometricPrompt? = null
    private var promptInfo: BiometricPrompt.PromptInfo? = null
    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>


    private lateinit var consentInformation: ConsentInformation
    private lateinit var consentForm: ConsentForm


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        try {


            binding = ActivityMainNewdesignBinding.inflate(layoutInflater)
            val view = binding.root
            view.keepScreenOn = true
            setContentView(view)

            setSupportActionBar(binding.ltool.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)


            val prefs = getSharedPreferences(
                "whatsapp_pref",
                Context.MODE_PRIVATE
            )
            nn = prefs!!.getString("inappads", "nnn")




            requestPermissionsLauncher =
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (!requiredPermissionsTIRAMISU.all { isPermissionGranted(it) }) {

                    showStoragePermissionRationale()
                    runpermissioncheck()
                }
            } else {
                if (!requiredPermissions.all { isPermissionGranted(it) }) {
                    showStoragePermissionRationale()
                    runpermissioncheck()
                }
            }


            if (Build.VERSION.SDK_INT >= 33) {

                val manager = getSystemService(
                    NotificationManager::class.java
                )
                val areNotificationsEnabled = manager.areNotificationsEnabled()

                if (!areNotificationsEnabled) {
                    showNotificationPermissionRationale()
                }
            } else {
                hasNotificationPermissionGranted = true
            }




            progressDralogGenaratinglink = ProgressDialog(this@MainActivityNewUi)
            progressDralogGenaratinglink.setMessage(resources.getString(R.string.genarating_download_link))
            progressDralogGenaratinglink.setCancelable(false)

            //  addFbAd()

            pref = getSharedPreferences(Constants.PREF_CLIP, 0) // 0 - for private mode
            prefEditor = pref.edit()
            csRunning = pref.getBoolean("csRunning", false)

            createNotificationChannel()


            nativeadContainer = findViewById<FrameLayout>(R.id.fl_adplaceholder)








            AppRating.Builder(this)
                .setMinimumLaunchTimes(5)
                .setMinimumDays(5)
                .setMinimumLaunchTimesToShowAgain(5)
                .setMinimumDaysToShowAgain(10)
                .setRatingThreshold(RatingThreshold.FOUR)
                .showIfMeetsConditions()




            doAllBioMetricTasks()


            val whatsappcard = findViewById<CardView>(R.id.whatsappcard)
            instaprivatefbprivate = findViewById<TextView>(R.id.instaprivatefbprivate)
            val copylinkanddownloadcard = findViewById<CardView>(R.id.copylinkanddownloadcard)

            val videwGllery = findViewById<LinearLayout>(R.id.videwGllery)
            val supportedapps = findViewById<LinearLayout>(R.id.supportedapps)
            val settingpage = findViewById<LinearLayout>(R.id.settingpage)


            videwGllery.setOnClickListener {

                val intent = Intent(this, GalleryActivityNewUi::class.java)
                startActivity(intent)

            }

            supportedapps.setOnClickListener {

                val intent = Intent(this, AllSupportedApps::class.java)
                startActivity(intent)

            }

            settingpage.setOnClickListener {

                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)

            }


            whatsappcard.setOnClickListener {
                val intent = Intent(this, StatusSaverActivity::class.java)
                intent.putExtra("frag", "status")
                startActivity(intent)
            }

            instaprivatefbprivate.setOnClickListener {
                val intent = Intent(this, StatusSaverActivity::class.java)
                intent.putExtra("frag", "download")
                startActivity(intent)
            }


            copylinkanddownloadcard.setOnClickListener {
                val clipBoardManager =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val primaryClipData = clipBoardManager.primaryClip
                val clip = primaryClipData?.getItemAt(0)?.text.toString()
                downloadVideo(clip)
            }

            isNeedGrantPermission()

            MobileAds.initialize(
                this@MainActivityNewUi
            ) {


                if (Constants.show_Ads && !BuildConfig.ISPRO) {

                    val prefs: SharedPreferences = getSharedPreferences(
                        "whatsapp_pref",
                        Context.MODE_PRIVATE
                    )

                    val pp =
                        prefs.getString("inappads", "nnn") //"No name defined" is the default value.


                    if (pp.equals("nnn")) {
                        Log.d("AdsManager:app ", "working")

                        LoadAdTask(this, nativeadContainer).execute()

                        AdsManager.loadInterstitialAd(this@MainActivityNewUi)

                    } else {
                        nativeadContainer.visibility = View.GONE

                    }
                } else {
                    nativeadContainer.visibility = View.GONE

                }


            }


            AppRating.Builder(this@MainActivityNewUi)
                .setMinimumLaunchTimes(5)
                .setMinimumDays(7)
                .setMinimumLaunchTimesToShowAgain(5)
                .setMinimumDaysToShowAgain(7)
                .setRatingThreshold(RatingThreshold.FOUR)
                .showIfMeetsConditions()



            initIInAppBillingAcknologement()


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
                                        this@MainActivityNewUi,
                                        resources.getString(R.string.youhavealready),
                                    )
                                }
                            } else {
                                startActivity(
                                    Intent(
                                        this@MainActivityNewUi,
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


            val action = intent.action
            val type = intent.type

            if (Intent.ACTION_SEND == action && type != null) {
                if ("text/plain" == type) {
                    handleSendText(intent) // Handle text being sent
                }
            }


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


    private fun openAdsConsentDialog() {

        // Set tag for under age of consent. false means users are not under
        // age.
        val params = ConsentRequestParameters
            .Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()



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
            PiracyChecker(this@MainActivityNewUi)
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
                verifyAppInstall(iUtils.verifyInstallerId(this@MainActivityNewUi))
                iUtils.showCookiesLL(this@MainActivityNewUi);
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


    private fun showBottomSheetExitDialog() {
        @SuppressLint("InflateParams") val view: View =
            layoutInflater.inflate(R.layout.dialog_exit, null)
        val lytBottomSheet = view.findViewById<FrameLayout>(R.id.bottom_sheet)

        lytBottomSheet.background = ContextCompat.getDrawable(this, R.drawable.bg_rounded_light)

        val btnRate = view.findViewById<Button>(R.id.btn_rate)
        val btnShare = view.findViewById<Button>(R.id.btn_share)
        val btnExit = view.findViewById<Button>(R.id.btn_exit)
        val frameAd = view.findViewById<FrameLayout>(R.id.fl_adplaceholder)
        nn = SharedPrefsMainApp(this@MainActivityNewUi).preferencE_inappads

        if (Constants.show_Ads && !BuildConfig.ISPRO && nn == "nnn") {

            MainActivity.LoadAdTask(this@MainActivityNewUi, frameAd).execute()
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
    }


    @SuppressLint("StaticFieldLeak")
    class LoadAdTask(private val context: Activity, private val frameLayout: FrameLayout) :
        AsyncTask<Void, Void, Unit>() {

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void?) {
            AdsManager.loadAdmobNativeAd(context, frameLayout)
        }
    }


    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= 33) {
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
            if (!isFinishing) {
                val alertBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
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
        } else {
            hasNotificationPermissionGranted = true
        }


    }


    private fun showStoragePermissionRationale() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requiredPermissionsTIRAMISU.all { isPermissionGranted(it) }) {
                return
            }
        } else {
            if (requiredPermissions.all { isPermissionGranted(it) }) {
                return
            }
        }
        if (!isFinishing) {
            val alertBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
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
    }


    var hasNotificationPermissionGranted = false


    fun getMyData(): String? {
        return myString
    }


    fun setmydata(mysa: String) {
        myString = mysa
    }


    private fun handleSendText(intent: Intent) {
        try {
            this.intent = null
            var url = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (url.equals("") && iUtils.checkURL(url)) {
                runOnUiThread {
                    iUtils.ShowToastError(
                        this@MainActivityNewUi,
                        resources.getString(R.string.enter_valid) + "",
                    )
                }
                return
            }

            if (url?.contains("myjosh.in")!!) {


                try {
                    url = iUtils.extractUrls(url)[0]
                } catch (_: Exception) {

                }

                DownloadVideosMain.Start(this@MainActivityNewUi, url!!.trim(), false)

            } else if (url.contains("chingari")) {


                try {
                    url = iUtils.extractUrls(url)[0]
                } catch (_: Exception) {

                }

                DownloadVideosMain.Start(this@MainActivityNewUi, url!!.trim(), false)

            } else if (url.contains("bemate")) {

                try {
                    url = iUtils.extractUrls(url)[0]
                } catch (_: Exception) {

                }

                DownloadVideosMain.Start(this@MainActivityNewUi, url!!.trim(), false)

            } else if (url.contains("snapchat.com")) {
                try {
                    url = iUtils.extractUrls(url)[0]
                } catch (_: Exception) {

                }

                val i = Intent(
                    this@MainActivityNewUi,
                    SnapChatBulkStoryDownloader::class.java
                )
                i.putExtra("urlsnap", url!!.trim())
                startActivity(i)


            } else if (url.contains("instagram.com")) {

                downloadVideo(url.trim())

                Log.e("downloadFileName12", url)
            } else if (url.contains("sck.io") || url.contains("snackvideo")) {
                var mynewval = url
                try {
                    mynewval = iUtils.extractUrls(url)[0]
                } catch (_: Exception) {
                    mynewval = url
                }

                DownloadVideosMain.Start(this@MainActivityNewUi, mynewval!!.trim(), false)

            } else {
                var mynewval = url
                try {
                    mynewval = iUtils.extractUrls(url)[0]
                } catch (_: Exception) {
                    mynewval = url
                }

                downloadVideo(mynewval!!.trim())

            }
        } catch (_: Exception) {

        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        Log.e("myhdasbdhf newintent ", intent?.getStringExtra(Intent.EXTRA_TEXT) + "_46237478234")

        intent?.let { newIntent ->

            handleSendText(newIntent)
            Log.e("myhdasbdhf notdownload ", newIntent.getStringExtra(Intent.EXTRA_TEXT) + "")

        }
    }


    private fun isNeedGrantPermission(): Boolean {
        try {

            if (ContextCompat.checkSelfPermission(
                    this,
                    REQUEST_PERMISSION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this@MainActivityNewUi,
                        REQUEST_PERMISSION
                    )
                ) {
                    val msg =
                        String.format(
                            getString(R.string.format_request_permision),
                            getString(R.string.app_name)
                        )

                    if (!isFinishing) {
                        val localBuilder = AlertDialog.Builder(this@MainActivityNewUi)
                        localBuilder.setTitle(getString(R.string.permission_title))
                        localBuilder
                            .setMessage(msg).setNeutralButton(
                                getString(R.string.grant_option)
                            ) { _, _ ->
                                ActivityCompat.requestPermissions(
                                    this@MainActivityNewUi,
                                    arrayOf(REQUEST_PERMISSION),
                                    REQUEST_PERMISSION_CODE
                                )
                            }
                            .setNegativeButton(
                                getString(R.string.cancel_option)
                            ) { paramAnonymousDialogInterface, _ ->
                                paramAnonymousDialogInterface.dismiss()
                                finish()
                            }
                        localBuilder.show()
                    }

                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(REQUEST_PERMISSION),
                        REQUEST_PERMISSION_CODE
                    )
                }
                return true
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.


        menuInflater.inflate(R.menu.main_new, menu)

        val prefs: SharedPreferences = getSharedPreferences(
            "whatsapp_pref",
            Context.MODE_PRIVATE
        )

        val pp = prefs.getString("inappads", "nnn") //"No name defined" is the default value.


        if (pp.equals("nnn")) {

            menu.findItem(R.id.action_removeads).isVisible = true

        } else if (pp.equals("ppp")) {

            menu.findItem(R.id.action_removeads).isVisible = false

        }

        menu.findItem(R.id.action_removeads).isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_privacy -> {

                val browserIntent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            Setting.getPrivacyPolicy(
                                this@MainActivityNewUi,
                                Constants.iSAdminAttached
                            )
                        )
                    )
                startActivity(browserIntent)

                true
            }

            R.id.action_downloadtiktok -> {

                val intent = Intent(this, TikTokDownloadWebview::class.java)
                startActivity(intent)


                true
            }


            R.id.action_rate -> {

                try {
                    if (!isFinishing) {
                        AlertDialog.Builder(this@MainActivityNewUi)
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
                                } catch (anfe: ActivityNotFoundException) {
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
                    iUtils.shareApp(this@MainActivityNewUi)
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

                    runOnUiThread {
                        iUtils.ShowToastError(
                            this@MainActivityNewUi,
                            resources.getString(R.string.appnotinstalled) + "",
                        )
                    }
                }
                true
            }


            R.id.action_language -> {

                if (!isFinishing) {
                    val dialog = Dialog(this@MainActivityNewUi)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(true)
                    dialog.setContentView(R.layout.dialog_change_language)

                    val l_english = dialog.findViewById(R.id.l_english) as TextView
                    l_english.setOnClickListener {

                        LocaleHelper.setLocale(application, "en")
                        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL


                        val editor: SharedPreferences.Editor = getSharedPreferences(
                            "lang_pref",
                            Context.MODE_PRIVATE
                        ).edit()
                        editor.putString("lang", "en")

                        editor.apply()


                        recreate()
                        dialog.dismiss()
                    }


                    val l_french = dialog.findViewById(R.id.l_french) as TextView
                    l_french.setOnClickListener {
                        LocaleHelper.setLocale(application, "fr")
                        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
                        val editor: SharedPreferences.Editor = getSharedPreferences(
                            "lang_pref",
                            Context.MODE_PRIVATE
                        ).edit()
                        editor.putString("lang", "fr")

                        editor.apply()


                        recreate()
                        dialog.dismiss()
                    }


                    val l_arabic = dialog.findViewById(R.id.l_arabic) as TextView
                    l_arabic.setOnClickListener {
                        LocaleHelper.setLocale(application, "ar")
                        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR

                        val editor: SharedPreferences.Editor = getSharedPreferences(
                            "lang_pref",
                            Context.MODE_PRIVATE
                        ).edit()
                        editor.putString("lang", "ar")

                        editor.apply()


                        recreate()
                        dialog.dismiss()

                    }
                    val l_urdu = dialog.findViewById(R.id.l_urdu) as TextView
                    l_urdu.setOnClickListener {
                        LocaleHelper.setLocale(application, "ur")
                        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR


                        val editor: SharedPreferences.Editor = getSharedPreferences(
                            "lang_pref",
                            Context.MODE_PRIVATE
                        ).edit()
                        editor.putString("lang", "ur")

                        editor.apply()


                        recreate()
                        dialog.dismiss()
                    }


                    val l_german = dialog.findViewById(R.id.l_german) as TextView
                    l_german.setOnClickListener {
                        LocaleHelper.setLocale(application, "de")
                        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
                        val editor: SharedPreferences.Editor = getSharedPreferences(
                            "lang_pref",
                            Context.MODE_PRIVATE
                        ).edit()
                        editor.putString("lang", "de")

                        editor.apply()


                        recreate()
                        dialog.dismiss()
                    }


                    val l_turkey = dialog.findViewById(R.id.l_turkey) as TextView
                    l_turkey.setOnClickListener {
                        LocaleHelper.setLocale(application, "tr")
                        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
                        val editor: SharedPreferences.Editor = getSharedPreferences(
                            "lang_pref",
                            Context.MODE_PRIVATE
                        ).edit()
                        editor.putString("lang", "tr")

                        editor.apply()


                        recreate()
                        dialog.dismiss()
                    }


                    val l_portougese = dialog.findViewById(R.id.l_portougese) as TextView
                    l_portougese.setOnClickListener {
                        LocaleHelper.setLocale(application, "pt")
                        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
                        val editor: SharedPreferences.Editor = getSharedPreferences(
                            "lang_pref",
                            Context.MODE_PRIVATE
                        ).edit()
                        editor.putString("lang", "pt")

                        editor.apply()


                        recreate()
                        dialog.dismiss()
                    }


                    val l_chinese = dialog.findViewById(R.id.l_chinese) as TextView
                    l_chinese.setOnClickListener {
                        LocaleHelper.setLocale(application, "zh")
                        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
                        val editor: SharedPreferences.Editor = getSharedPreferences(
                            "lang_pref",
                            Context.MODE_PRIVATE
                        ).edit()
                        editor.putString("lang", "zh")

                        editor.apply()


                        recreate()
                        dialog.dismiss()
                    }


                    val l_hindi = dialog.findViewById(R.id.l_hindi) as TextView
                    l_hindi.setOnClickListener {
                        LocaleHelper.setLocale(application, "hi")
                        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
                        val editor: SharedPreferences.Editor = getSharedPreferences(
                            "lang_pref",
                            Context.MODE_PRIVATE
                        ).edit()
                        editor.putString("lang", "hi")

                        editor.apply()


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
                                this,
                                resources.getString(R.string.youhavealready) + "",
                            )
                        }

                    } else {
                        startActivity(
                            Intent(
                                this@MainActivityNewUi,
                                SubscriptionActivity::class.java
                            )
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!isFinishing) {
            val dialog = Dialog(this@MainActivityNewUi)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.custom_dialog_ad_exit)
            val yesBtn = dialog.findViewById(R.id.btn_exitdialog_yes) as Button
            val noBtn = dialog.findViewById(R.id.btn_exitdialog_no) as Button
            yesBtn.setOnClickListener {

                try {
                    finishAndRemoveTask()
                    exitProcess(0)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            noBtn.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission,
        ) == PackageManager.PERMISSION_GRANTED
    }


    fun createNotificationChannel() {
        // 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 2
            val oreoNotification = OreoNotification(
                this@MainActivityNewUi,
                if (!MyFirebaseMessagingService.isWaku) MyFirebaseMessagingService.DefaultSoundString else MyFirebaseMessagingService.DefaultSoundWakuString
            )
            Log.e("loged112211", "Notificaion Channel Created!")
        }
    }


    private fun downloadVideo(url: String) {
        Log.e("myhdasbdhf urlis  ", url)


        if (url == "" && iUtils.checkURL(url)) {
            runOnUiThread {
                iUtils.ShowToastError(
                    this@MainActivityNewUi,
                    resources.getString(R.string.enter_valid) + "",
                )
            }


        } else {


            val rand = Random()
            val rand_int1 = rand.nextInt(2)
            println("randonvalueis = $rand_int1")

            if (rand_int1 == 0) {
                showAdmobAds()
            } else {
                showAdmobAds_int_video()
            }


            Log.d("mylogissssss", "The interstitial wasn't loaded yet.")

            SplashScreen.uploadDownloadedUrl(url)

            if (url.contains("instagram.com")) {
                progressDralogGenaratinglink.show()
                startInstaDownload(url)

            } else if (url.contains("myjosh.in")) {

                var mynewval = url
                try {
                    mynewval = iUtils.extractUrls(mynewval)[0]
                } catch (_: Exception) {

                }

                DownloadVideosMain.Start(this, mynewval.trim(), false)


            } else if (url.contains("threads.net")) {
                var myurl = url
                try {
                    myurl = iUtils.extractUrls(myurl)[0]
                } catch (_: Exception) {
                }

                dismissMyDialog()
                val intent = Intent(this@MainActivityNewUi, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", myurl)
                startActivityForResult(intent, 2)

            } else if (url.contains("audiomack")) {
                dismissMyDialog()

                val intent = Intent(this@MainActivityNewUi, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("zili")) {
                dismissMyDialog()

                val intent = Intent(this@MainActivityNewUi, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("xhamster")) {
                dismissMyDialog()

                val intent = Intent(this@MainActivityNewUi, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("zingmp3")) {
                dismissMyDialog()

                val intent = Intent(this@MainActivityNewUi, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("vidlit")) {
                dismissMyDialog()

                val intent = Intent(this@MainActivityNewUi, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("byte.co")) {
                dismissMyDialog()

                val intent = Intent(this@MainActivityNewUi, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("fthis.gr")) {
                dismissMyDialog()

                val intent = Intent(this@MainActivityNewUi, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("fw.tv") || url.contains("firework.tv")) {
                dismissMyDialog()

                val intent = Intent(this@MainActivityNewUi, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("rumble")) {
                dismissMyDialog()
                val intent = Intent(this@MainActivityNewUi, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            } else if (url.contains("traileraddict")) {
                dismissMyDialog()
                val intent = Intent(this@MainActivityNewUi, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", url)
                startActivityForResult(intent, 2)

            }
            //ojoo video app
            else if (url.contains("bemate")) {
                dismissMyDialog()

                var mynewval = url
                try {
                    mynewval = iUtils.extractUrls(mynewval)[0]
                } catch (_: Exception) {

                }

                DownloadVideosMain.Start(this, mynewval.trim(), false)
                val intent = Intent(this@MainActivityNewUi, GetLinkThroughWebView::class.java)
                intent.putExtra("myurlis", mynewval)
                startActivityForResult(intent, 2)

            } else if (url.contains("chingari")) {


                var mynewval = url
                try {
                    mynewval = iUtils.extractUrls(mynewval)[0]
                } catch (_: Exception) {

                }

                DownloadVideosMain.Start(this, mynewval.trim(), false)
            } else if (url.contains("sck.io") || url.contains("snackvideo")) {
                var mynewval = url
                try {
                    mynewval = iUtils.extractUrls(mynewval)[0]
                } catch (_: Exception) {

                }

                DownloadVideosMain.Start(this, mynewval.trim(), false)

            } else {
                Log.d("mylogissssss33", "Thebbbbbbbloaded yet.")

                var mynewval = url
                try {
                    mynewval = iUtils.extractUrls(mynewval)[0]
                } catch (_: Exception) {

                }

                DownloadVideosMain.Start(this, mynewval.trim(), false)
            }

        }
    }


    private fun showAdDialog() {


        if (Constants.show_Ads && !BuildConfig.ISPRO) {
            if (nn == "nnn") {

                if (!isFinishing) {
                    val dialogBuilder =
                        androidx.appcompat.app.AlertDialog.Builder(this@MainActivityNewUi)


                    dialogBuilder.setMessage(getString(R.string.doyouseead))

                        .setCancelable(false)

                        .setPositiveButton(
                            getString(R.string.watchad)
                        ) { _, _ ->


                            if (mRewardedAd != null) {
                                mRewardedAd?.show(this@MainActivityNewUi) {

//                                var rewardAmount = it.amount
//                                var rewardType = it.type
                                    Log.d(ContentValues.TAG, "User earned the reward.")

//                                chkAutoDownload.isChecked = true

//                                startClipboardMonitor()

                                }
                            } else {

                                runOnUiThread {
                                    iUtils.ShowToastError(
                                        this@MainActivityNewUi,
                                        resources.getString(R.string.videonotavaliabl) + "",
                                    )
                                }
//                            chkAutoDownload.isChecked = true
//                            val checked = chkAutoDownload.isChecked
//
//                            if (checked) {
//                                Log.e("loged", "testing checked!")
////                                startClipboardMonitor()
//                            } else {
//                                Log.e("loged", "testing unchecked!")
//
//
////                                stopClipboardMonitor()
//                                // setNofication(false);
//                            }


                                Log.d("TAG", "The rewarded ad wasn't ready yet.")
                            }


//
//
//                if (mRewardedVideoAd.isLoaded) {
//                    mRewardedVideoAd.show()
//                } else {
//
//
//                }


                        }

                        .setNegativeButton(
                            getString(R.string.cancel)
                        ) { dialog, _ ->
                            dialog.cancel()

//                        val checked = chkAutoDownload.isChecked
//                        if (checked) {
//                            chkAutoDownload.isChecked = false
//                        }

                        }


                    val alert = dialogBuilder.create()
                    alert.setTitle(getString(R.string.enabAuto))
                    alert.show()
                }

            } else {


//                chkAutoDownload.isChecked = true
//                val checked = chkAutoDownload.isChecked
//
//                if (checked) {
//                    Log.e("loged", "testing checked!")
////                    startClipboardMonitor()
//                } else {
//                    Log.e("loged", "testing unchecked!")
//
//
////                    stopClipboardMonitor()
//                    // setNofication(false);
//                }


                Log.d("TAG", "The rewarded ad wasn't ready yet.")
            }


        } else {

//
//            chkAutoDownload.isChecked = true
//            val checked = chkAutoDownload.isChecked
//
//            if (checked) {
//                Log.e("loged", "testing checked!")
////                startClipboardMonitor()
//            } else {
//                Log.e("loged", "testing unchecked!")
//
//
////                stopClipboardMonitor()
//                // setNofication(false);
//            }


            Log.d("TAG", "The rewarded ad wasn't ready yet.")
        }


    }


    //insta finctions

    @Keep
    fun startInstaDownload(Url: String) {


//         https://www.instagram.com/p/CLBM34Rhxek/?igshid=41v6d50y6u4w
//          https://www.instagram.com/p/CLBM34Rhxek/
//           https://www.instagram.com/p/CLBM34Rhxek/?__a=1&__d=dis
//           https://www.instagram.com/tv/CRyVpDSAE59/

        /*
        * https://www.instagram.com/p/CUs4eKIBscn/?__a=1&__d=dis
        * https://www.instagram.com/p/CUktqS7pieg/?__a=1&__d=dis
        * https://www.instagram.com/p/CSMYRwGna3S/?__a=1&__d=dis
        * https://www.instagram.com/p/CR6AbwDB12R/?__a=1&__d=dis
        * https://www.instagram.com/p/CR6AbwDB12R/?__a=1&__d=dis
        * */


        var urlWi: String?
        try {

            val uri = URI(Url)
            urlWi = URI(
                uri.scheme,
                uri.authority,
                uri.path,
                null,  // Ignore the query part of the input url
                uri.fragment
            ).toString()


        } catch (ex: java.lang.Exception) {
            dismissMyDialog()
            runOnUiThread {
                iUtils.ShowToastError(
                    this@MainActivityNewUi,
                    resources.getString(R.string.invalid_url) + "",
                )
            }
            urlWi = ""
            return
        }

        System.err.println("workkkkkkkkk 1122112 $Url")

        var urlwithoutlettersqp: String? = urlWi
        System.err.println("workkkkkkkkk 1122112 $urlwithoutlettersqp")


        if (urlwithoutlettersqp!!.contains("/reel/")) {
            urlwithoutlettersqp = urlwithoutlettersqp.replace("/reel/", "/p/")
        }

        if (urlwithoutlettersqp.contains("/tv/")) {
            urlwithoutlettersqp = urlwithoutlettersqp.replace("/tv/", "/p/")
        }

        val urlwithoutlettersqp_noa: String = urlwithoutlettersqp

        urlwithoutlettersqp = "$urlwithoutlettersqp?__a=1&__d=dis"
        System.err.println("workkkkkkkkk 87878788 $urlwithoutlettersqp")


        System.err.println("workkkkkkkkk 777777 $urlwithoutlettersqp")

        try {
            if (urlwithoutlettersqp.split("/")[4].length > 15) {

                val sharedPrefsFor = SharedPrefsForInstagram(this@MainActivityNewUi)
                if (sharedPrefsFor.preference.preferencE_SESSIONID == "") {
                    sharedPrefsFor.clearSharePrefs()
                }
                val map = sharedPrefsFor.preference
                if (map != null) {
                    if (map.preferencE_ISINSTAGRAMLOGEDIN == "false") {

                        dismissMyDialog()
                        if (!isFinishing) {
                            val alertDialog =
                                AlertDialog.Builder(this@MainActivityNewUi).create()
                            alertDialog.setTitle(getString(R.string.logininsta))
                            alertDialog.setMessage(getString(R.string.urlisprivate))
                            alertDialog.setButton(
                                AlertDialog.BUTTON_POSITIVE, getString(R.string.logininsta)
                            ) { dialog, _ ->
                                dialog.dismiss()


                                val intent = Intent(
                                    this@MainActivityNewUi,
                                    InstagramLoginActivity::class.java
                                )
                                startActivityForResult(intent, 200)

                            }

                            alertDialog.setButton(
                                AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)
                            ) { dialog, _ ->
                                dialog.dismiss()


                            }
                            alertDialog.show()
                        }
                        return
                    }
                }


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        if (!(this@MainActivityNewUi).isFinishing) {
            val dialog = Dialog(this@MainActivityNewUi)
            dialog.setContentView(R.layout.tiktok_optionselect_dialog)

            val methode0 = dialog.findViewById<Button>(R.id.dig_btn_met0)
            val methode1 = dialog.findViewById<Button>(R.id.dig_btn_met1)
            val methode2 = dialog.findViewById<Button>(R.id.dig_btn_met2)
            val methode3 = dialog.findViewById<Button>(R.id.dig_btn_met3)
            val methode4 = dialog.findViewById<Button>(R.id.dig_btn_met4)
            val methode5 = dialog.findViewById<Button>(R.id.dig_btn_met5)
            val methode6 = dialog.findViewById<Button>(R.id.dig_btn_met6)
            methode5.visibility = View.VISIBLE
            methode6.visibility = View.VISIBLE

            val dig_btn_cancel = dialog.findViewById<Button>(R.id.dig_btn_cancel)

            methode0.setOnClickListener {
                dialog.dismiss()

                try {
                    System.err.println("workkkkkkkkk 4 ")

                    val sharedPrefsFor = SharedPrefsForInstagram(this@MainActivityNewUi)
                    val map = sharedPrefsFor.preference
                    if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != ""
                    ) {

                        System.err.println(
                            "workkkkkkkkk 476 " + urlwithoutlettersqp + "____" +
                                    "ds_user_id=" + map.preferencE_USERID
                                    + "; sessionid=" + map.preferencE_SESSIONID
                        )

                        downloadInstagramImageOrVideodata_withlogin(
                            urlwithoutlettersqp,
                            "ds_user_id=" + map.preferencE_USERID
                                    + "; sessionid=" + map.preferencE_SESSIONID
                        )
                    } else {
                        downloadInstagramImageOrVideoResponseOkhttp(
                            urlwithoutlettersqp_noa
                        )
                        // downloadInstagramImageOrVideoResponseOkhttp(urlwithoutlettersqp_noa)
                        // downloadInstagramImageOrVideoResOkhttpM2(urlwithoutlettersqp_noa)
                        //downloadInstagramImageOrVideo_tikinfApi(urlwithoutlettersqp)
                    }
                } catch (e: java.lang.Exception) {
                    dismissMyDialog()
                    System.err.println("workkkkkkkkk 5")
                    e.printStackTrace()
                    runOnUiThread {
                        iUtils.ShowToastError(
                            this@MainActivityNewUi,
                            resources.getString(R.string.error_occ) + "",
                        )
                    }
                }

            }
            methode1.setOnClickListener {
                dialog.dismiss()

                try {
                    System.err.println("workkkkkkkkk 4 ")

                    val sharedPrefsFor = SharedPrefsForInstagram(this@MainActivityNewUi)
                    val map = sharedPrefsFor.preference
                    if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != ""
                    ) {

                        System.err.println(
                            "workkkkkkkkk 476 " + urlwithoutlettersqp + "____" +
                                    "ds_user_id=" + map.preferencE_USERID
                                    + "; sessionid=" + map.preferencE_SESSIONID
                        )

                        downloadInstagramImageOrVideodata_withlogin(
                            urlwithoutlettersqp,
                            "ds_user_id=" + map.preferencE_USERID
                                    + "; sessionid=" + map.preferencE_SESSIONID
                        )
                    } else {
                        downloadInstagramImageOrVideodataExternalApi2(
                            this@MainActivityNewUi,
                            urlwithoutlettersqp_noa
                        )
                        // downloadInstagramImageOrVideoResOkhttpM2(urlwithoutlettersqp_noa)
                        //downloadInstagramImageOrVideo_tikinfApi(urlwithoutlettersqp)
                    }
                } catch (e: java.lang.Exception) {
                    dismissMyDialog()
                    System.err.println("workkkkkkkkk 5")
                    e.printStackTrace()
                    runOnUiThread {
                        iUtils.ShowToastError(
                            this@MainActivityNewUi,
                            resources.getString(R.string.error_occ) + "",
                        )
                    }
                }


            }
            methode2.setOnClickListener {
                dialog.dismiss()


                try {
                    System.err.println("workkkkkkkkk 4")
                    val sharedPrefsFor = SharedPrefsForInstagram(this@MainActivityNewUi)
                    val map = sharedPrefsFor.preference
                    if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != ""
                    ) {
                        System.err.println("workkkkkkkkk 4.7")
                        downloadInstagramImageOrVideodata_old_withlogin(
                            urlwithoutlettersqp,
                            "ds_user_id=" + map.preferencE_USERID
                                    + "; sessionid=" + map.preferencE_USERID
                        )
                    } else {
                        System.err.println("workkkkkkkkk 4.8")
                        downloadInstagramImageOrVideodata_old(
                            urlwithoutlettersqp,
                            ""
                        )
                    }
                } catch (e: java.lang.Exception) {
                    dismissMyDialog()
                    System.err.println("workkkkkkkkk 5")
                    e.printStackTrace()
                    runOnUiThread {
                        iUtils.ShowToastError(
                            this@MainActivityNewUi,
                            resources.getString(R.string.error_occ) + "",
                        )
                    }
                }

            }
            methode3.setOnClickListener {
                dialog.dismiss()

                try {
                    System.err.println("workkkkkkkkk 4")
                    val sharedPrefsFor = SharedPrefsForInstagram(this@MainActivityNewUi)
                    val map = sharedPrefsFor.preference
                    if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != ""
                    ) {
                        System.err.println("workkkkkkkkk m2 5.2")
                        downloadInstagramImageOrVideodata_withlogin(
                            urlwithoutlettersqp,
                            "ds_user_id=" + map.preferencE_USERID
                                    + "; sessionid=" + map.preferencE_SESSIONID
                        )
                    } else {
                        System.err.println("workkkkkkkkk 4.5")
                        downloadInstagramImageOrVideodata(
                            urlwithoutlettersqp,
                            ""
                        )
                    }
                } catch (e: java.lang.Exception) {
                    dismissMyDialog()
                    System.err.println("workkkkkkkkk 5.1")
                    e.printStackTrace()
                    runOnUiThread {
                        iUtils.ShowToastError(
                            this@MainActivityNewUi,
                            resources.getString(R.string.error_occ) + "",
                        )
                    }
                }

            }
            methode4.setOnClickListener {
                dialog.dismiss()

                var myurl = urlwithoutlettersqp_noa
                try {
                    myurl = iUtils.extractUrls(myurl)[0]
                } catch (_: Exception) {
                }
                DownloadVideosMain.Start(this@MainActivityNewUi, myurl.trim(), false)
                Log.e("downloadFileName12", myurl.trim())

            }
            methode5.setOnClickListener {
                dialog.dismiss()

                try {
                    loginDownloadgram(urlwithoutlettersqp_noa)
                } catch (e: Exception) {
                    dismissMyDialog()
                    System.err.println("workkkkkkkkk 5.1")
                    e.printStackTrace()
                    runOnUiThread {
                        iUtils.ShowToastError(
                            this@MainActivityNewUi,
                            resources.getString(R.string.error_occ) + "",
                        )
                    }
                }

            }
            methode6.setOnClickListener {
                dialog.dismiss()

                try {
                    loginSnapIntaWeb(urlwithoutlettersqp_noa)
                } catch (e: Exception) {
                    dismissMyDialog()
                    System.err.println("workkkkkkkkk 5.1")
                    e.printStackTrace()
                    runOnUiThread {
                        iUtils.ShowToastError(
                            this@MainActivityNewUi,
                            resources.getString(R.string.error_occ) + "",
                        )
                    }
                }

            }

            dig_btn_cancel.setOnClickListener {
                dialog.dismiss()
                dismissMyDialog()
            }
            dialog.setCancelable(false)
            dialog.show()
        }


    }


    @Keep
    fun downloadInstawitfintaApi(URL: String?, Cookie: String?) {
        val random1 = Random()
//        val j = random1.nextInt(iUtils.UserAgentsList.size)
        object : Thread() {
            override fun run() {
                Looper.prepare()


                val client = OkHttpClient().newBuilder()
                    .build()
                val body: RequestBody = RequestBody.Companion.create(
                    "application/json;charset=UTF-8".toMediaTypeOrNull(),
                    "{\"link\":\"" + URL + "\"}"
                )
                val request: Request = Request.Builder()
                    .url("https://sssinstagram.com/request")
                    .method("POST", body)
                    .addHeader("content-type", "application/json;charset=UTF-8")
                    .addHeader("origin", "https://sssinstagram.com")
                    .addHeader(
                        "user-agent",
                        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36"
                    )
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader(
                        "x-xsrf-token",
                        "eyJpdiI6IkRnUDdZa2RYOFk1ZmpzemFwN2lwMXc9PSIsInZhbHVlIjoiZU03dlV5TlVvNWowenNrWXkrZmdZa0tpZy9abGs2aENBZnBIUmhLK1FNRmJBb3gyUUswRkY3cUxoMlBjL3l6YUFlM1ZFN1VNeWg2WDZOTzJlZ0xCVHVBY05sSjYwTy9Ca3piRVpGLzM4SU83bEtIeEl6TGRsVGVncXpuM0todWoiLCJtYWMiOiJjMjQ5NWMwMzZmYWM5ZjE1YzhiMTBjNzdlOTAxMTY4MWEwNzAwMWQ1YzQ4NWRhZWE0MDlmZjAwMDJmOWUyNTU3IiwidGFnIjoiIn0="
                    )
                    .build()
                val response = client.newCall(request).execute()
                val responseed = response.body!!.string()
                println("fjhjfhjsdfsdhf " + response.code)

                if (response.code == 200) {
                    println("fjhjfhjsdfsdhf $responseed")
                    DownloadVideosMain.dismissMyDialog()

                    try {


                        System.err.println(
                            "workkkkkkkkk 6.0.1 " + iUtils.extractUrls(responseed)
                                .toString()
                        )


                        val listofurls = iUtils.extractUrls(responseed)


                        for (i in listofurls) {

                            val i1 = StringEscapeUtils.unescapeJava(i)
                            System.err.println("workkkkkkkkk 7.0.1 " + i1)
                            val nameisfile = iUtils.getImageFilenameFromURL(i1.toString())

                            if (nameisfile.contains(".jpg")) {
                                DownloadFileMain.startDownloading(
                                    this@MainActivityNewUi,
                                    i1.toString(),
                                    nameisfile,
                                    ".png"
                                )
                            } else if (nameisfile.contains(".mp4")) {
                                DownloadFileMain.startDownloading(
                                    this@MainActivityNewUi,
                                    i1.toString(),
                                    nameisfile,
                                    ".mp4"
                                )
                            }
                            // etText.setText("");
                            dismissMyDialog()
                        }


                    } catch (e: java.lang.Exception) {
                        System.err.println("workkkkkkkkk 5nn errrr " + e.message)

                        dismissMyDialog()
                        System.err.println("workkkkkkkkk 5.1")
                        e.printStackTrace()
                        runOnUiThread {
                            iUtils.ShowToastError(
                                this@MainActivityNewUi,
                                resources.getString(R.string.error_occ) + "",
                            )
                        }
                    }
                } else {

                    try {
                        val client = OkHttpClient().newBuilder()
                            .build()
                        val request: Request = Request.Builder()
                            .url("https://instadownloader.co/insta_downloader.php?url=" + URL)
                            .method("GET", null)
                            .build()
                        val response = client.newCall(request).execute()
                        if (response.code == 200) {
                            val responseed = response.body!!.string()
                            DownloadVideosMain.dismissMyDialog()

                            var out = StringEscapeUtils.unescapeJava(responseed)
                            if (out.isNotEmpty()) {
                                out = out.substring(1, out.length - 1)
                            } else {

                                dismissMyDialog()
                                System.err.println("workkkkkkkkk 5.1")
                                runOnUiThread {
                                    iUtils.ShowToastError(
                                        this@MainActivityNewUi,
                                        resources.getString(R.string.error_occ) + "",
                                    )
                                }
                                return
                            }
//                            val images_linksList: ArrayList<String> = ArrayList()
//                            val videos_linksList: ArrayList<String> = ArrayList()


                            val videos_links = JSONObject(out).getJSONArray("videos_links")
                            val images_links = JSONObject(out).getJSONArray("images_links")

                            if (videos_links != null) {
                                for (i in 0 until videos_links.length()) {

                                    DownloadFileMain.startDownloading(
                                        this@MainActivityNewUi,
                                        videos_links.getJSONObject(0).getString("url"),
                                        "instagram_" + System.currentTimeMillis(),
                                        ".mp4"
                                    )

                                }
                            }

                            if (images_links != null) {
                                for (i in 0 until images_links.length()) {

                                    DownloadFileMain.startDownloading(
                                        this@MainActivityNewUi,
                                        images_links.getJSONObject(0).getString("url"),
                                        "instagram_" + System.currentTimeMillis(),
                                        ".png"
                                    )
                                }
                            }

                            dismissMyDialog()

                            System.err.println("workkkkkkkkk fdffd " + out)

                        } else {
                            dismissMyDialog()
                            System.err.println("workkkkkkkkk 5.1")
                            runOnUiThread {
                                iUtils.ShowToastError(
                                    this@MainActivityNewUi,
                                    resources.getString(R.string.error_occ) + "",
                                )
                            }
                        }

                    } catch (e: java.lang.Exception) {
                        System.err.println("workkkkkkkkk 5nn errrr " + e.message)

                        dismissMyDialog()
                        System.err.println("workkkkkkkkk 5.1")
                        e.printStackTrace()
                        runOnUiThread {
                            iUtils.ShowToastError(
                                this@MainActivityNewUi,
                                resources.getString(R.string.error_occ) + "",
                            )
                        }
                    }
                }


            }
        }.start()
    }

    @Keep
    fun downloadInstagramImageOrVideodata_old(URL: String?, Cookie: String?) {
        val random1 = Random()
        val j = random1.nextInt(iUtils.UserAgentsList.size)
        object : Thread() {
            override fun run() {
                Looper.prepare()
                val client = OkHttpClient().newBuilder()
                    .build()
                val request: Request = Request.Builder()
                    .url(URL!!)
                    .method("GET", null)
                    .addHeader("Cookie", Cookie!!)
                    .addHeader(
                        "User-Agent",
                        iUtils.UserAgentsList[j]
                    )
                    .build()
                try {
                    val response = client.newCall(request).execute()

                    System.err.println("workkkkkkkkk 6 " + response.code)

                    if (response.code == 200) {

                        try {
                            val ressss = response.body!!.string()
//                            runOnUiThread {
//                                binding.etURL.setText(ressss.substring(100, 2000))
//
//                            }

                            val listType: Type =
                                object : TypeToken<ModelInstagramResponse?>() {}.type
                            val modelInstagramResponse: ModelInstagramResponse = Gson().fromJson(
                                ressss,
                                listType
                            )

                            if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                                val modelGetEdgetoNode: ModelGetEdgetoNode =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children
                                myInstaUsername =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.owner.username + "_"

                                val modelEdNodeArrayList: List<ModelEdNode> =
                                    modelGetEdgetoNode.modelEdNodes
                                for (i in modelEdNodeArrayList.indices) {
                                    if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                        myVideoUrlIs = modelEdNodeArrayList[i].modelNode.video_url
                                        DownloadFileMain.startDownloading(
                                            this@MainActivityNewUi,
                                            myVideoUrlIs,
                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                myVideoUrlIs
                                            ),
                                            ".mp4"
                                        )
                                        // etText.setText("");
                                        dismissMyDialog()
                                        myVideoUrlIs = ""
                                    } else {
                                        myPhotoUrlIs =
                                            modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src
                                        DownloadFileMain.startDownloading(
                                            this@MainActivityNewUi,
                                            myPhotoUrlIs,
                                            myInstaUsername + iUtils.getImageFilenameFromURL(
                                                myPhotoUrlIs
                                            ),
                                            ".png"
                                        )
                                        myPhotoUrlIs = ""
                                        dismissMyDialog()
                                        // etText.setText("");
                                    }
                                }
                            } else {
                                val isVideo =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video

                                myInstaUsername =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.owner.username + "_"

                                if (isVideo) {
                                    myVideoUrlIs =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url
                                    DownloadFileMain.startDownloading(
                                        this@MainActivityNewUi,
                                        myVideoUrlIs,
                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                            myVideoUrlIs
                                        ),
                                        ".mp4"
                                    )
                                    dismissMyDialog()
                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src
                                    DownloadFileMain.startDownloading(
                                        this@MainActivityNewUi,
                                        myPhotoUrlIs,
                                        myInstaUsername + iUtils.getImageFilenameFromURL(
                                            myPhotoUrlIs
                                        ),
                                        ".png"
                                    )
                                    dismissMyDialog()
                                    myPhotoUrlIs = ""
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            System.err.println("workkkkkkkkk 5nn errrr " + e.message)
                            try {
                                try {
                                    System.err.println("workkkkkkkkk 4")
                                    downloadInstagramImageOrVideodata(
                                        URL, ""
                                    )
                                } catch (e: java.lang.Exception) {
                                    dismissMyDialog()
                                    System.err.println("workkkkkkkkk 5.1")
                                    e.printStackTrace()
                                    runOnUiThread {
                                        iUtils.ShowToastError(
                                            this@MainActivityNewUi,
                                            resources.getString(R.string.error_occ) + "",
                                        )
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                this@MainActivityNewUi.runOnUiThread {
                                    dismissMyDialog()

                                    if (!this@MainActivityNewUi.isFinishing) {
                                        val alertDialog =
                                            AlertDialog.Builder(this@MainActivityNewUi)
                                                .create()
                                        alertDialog.setTitle(getString(R.string.logininsta))
                                        alertDialog.setMessage(getString(R.string.urlisprivate))
                                        alertDialog.setButton(
                                            AlertDialog.BUTTON_POSITIVE,
                                            getString(R.string.logininsta)
                                        ) { dialog, _ ->
                                            dialog.dismiss()
                                            val intent = Intent(
                                                this@MainActivityNewUi,
                                                InstagramLoginActivity::class.java
                                            )
                                            startActivityForResult(intent, 200)
                                        }
                                        alertDialog.setButton(
                                            AlertDialog.BUTTON_NEGATIVE,
                                            getString(R.string.cancel)
                                        ) { dialog, _ ->
                                            dialog.dismiss()
                                        }
                                        alertDialog.show()
                                    }
                                }
                            }
                        }

                    } else {
                        object : Thread() {
                            override fun run() {

                                val client = OkHttpClient().newBuilder()
                                    .build()
                                val request: Request = Request.Builder()
                                    .url(URL)
                                    .method("GET", null)
                                    .addHeader("Cookie", iUtils.myInstagramTempCookies)
                                    .addHeader(
                                        "User-Agent",
                                        iUtils.UserAgentsList[j]
                                    ).build()
                                try {
                                    val response1: Response = client.newCall(request).execute()
                                    System.err.println("workkkkkkkkk 6 1 " + response1.code)

                                    if (response1.code == 200) {
                                        try {
                                            val listType: Type =
                                                object :
                                                    TypeToken<ModelInstagramResponse?>() {}.type
                                            val modelInstagramResponse: ModelInstagramResponse =
                                                Gson().fromJson(
                                                    response1.body!!.string(),
                                                    listType
                                                )
                                            if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                                                val modelGetEdgetoNode: ModelGetEdgetoNode =
                                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children
                                                myInstaUsername =
                                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.owner.username + "_"

                                                val modelEdNodeArrayList: List<ModelEdNode> =
                                                    modelGetEdgetoNode.modelEdNodes
                                                for (i in modelEdNodeArrayList.indices) {
                                                    if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                                        myVideoUrlIs =
                                                            modelEdNodeArrayList[i].modelNode.video_url
                                                        DownloadFileMain.startDownloading(
                                                            this@MainActivityNewUi,
                                                            myVideoUrlIs,
                                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                                myVideoUrlIs
                                                            ),
                                                            ".mp4"
                                                        )
                                                        // etText.setText("");
                                                        dismissMyDialog()
                                                        myVideoUrlIs = ""
                                                    } else {
                                                        myPhotoUrlIs =
                                                            modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src
                                                        DownloadFileMain.startDownloading(
                                                            this@MainActivityNewUi,
                                                            myPhotoUrlIs,
                                                            myInstaUsername + iUtils.getImageFilenameFromURL(
                                                                myPhotoUrlIs
                                                            ),
                                                            ".png"
                                                        )
                                                        myPhotoUrlIs = ""
                                                        dismissMyDialog()
                                                        // etText.setText("");
                                                    }
                                                }
                                            } else {
                                                val isVideo =
                                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video
                                                myInstaUsername =
                                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.owner.username + "_"

                                                if (isVideo) {
                                                    myVideoUrlIs =
                                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url
                                                    DownloadFileMain.startDownloading(
                                                        this@MainActivityNewUi,
                                                        myVideoUrlIs,
                                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                            myVideoUrlIs
                                                        ),
                                                        ".mp4"
                                                    )
                                                    dismissMyDialog()
                                                    myVideoUrlIs = ""
                                                } else {
                                                    myPhotoUrlIs =
                                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src
                                                    DownloadFileMain.startDownloading(
                                                        this@MainActivityNewUi,
                                                        myPhotoUrlIs,
                                                        myInstaUsername + iUtils.getImageFilenameFromURL(
                                                            myPhotoUrlIs
                                                        ),
                                                        ".png"
                                                    )
                                                    dismissMyDialog()
                                                    myPhotoUrlIs = ""
                                                }
                                            }
                                        } catch
                                            (e: java.lang.Exception) {
                                            System.err.println("workkkkkkkkk 4vvv errrr " + e.message)
                                            e.printStackTrace()
                                            dismissMyDialog()
                                        }
                                    } else {
                                        System.err.println("workkkkkkkkk 6bbb errrr ")
                                        this@MainActivityNewUi.runOnUiThread {
                                            dismissMyDialog()

                                            if (!this@MainActivityNewUi.isFinishing) {
                                                val alertDialog =
                                                    AlertDialog.Builder(this@MainActivityNewUi)
                                                        .create()
                                                alertDialog.setTitle(getString(R.string.logininsta))
                                                alertDialog.setMessage(getString(R.string.urlisprivate))
                                                alertDialog.setButton(
                                                    AlertDialog.BUTTON_POSITIVE,
                                                    getString(R.string.logininsta)
                                                ) { dialog, _ ->
                                                    dialog.dismiss()
                                                    val intent = Intent(
                                                        this@MainActivityNewUi,
                                                        InstagramLoginActivity::class.java
                                                    )
                                                    startActivityForResult(intent, 200)
                                                }
                                                alertDialog.setButton(
                                                    AlertDialog.BUTTON_NEGATIVE,
                                                    getString(R.string.cancel)
                                                ) { dialog, _ ->
                                                    dialog.dismiss()
                                                }
                                                alertDialog.show()
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }.start()
                    }
                    println("working errpr \t Value: " + response.body!!.string())
                } catch (e: Exception) {
                    dismissMyDialog()
                }
            }
        }.start()
    }

    @Keep
    fun downloadInstagramImageOrVideodata(URL: String?, Coookie: String?) {

        val random1 = Random()
        val j = random1.nextInt(iUtils.UserAgentsList.size)
        var Cookie = Coookie
        if (TextUtils.isEmpty(Cookie)) {
            Cookie = ""
        }
        val apiService: RetrofitApiInterface =
            RetrofitClient.getClient()

        val callResult: Call<JsonObject> = apiService.getInstagramData(
            URL,
            Cookie,
            iUtils.UserAgentsList[j]
        )
        callResult.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: retrofit2.Response<JsonObject?>
            ) {
                println("response1122334455 ress :   " + response.body())
                try {


//                                val userdata = response.body()!!.getAsJsonObject("graphql")
//                                    .getAsJsonObject("shortcode_media")
//                                binding.profileFollowersNumberTextview.setText(
//                                    userdata.getAsJsonObject(
//                                        "edge_followed_by"
//                                    )["count"].asString
//                                )
//                                binding.profileFollowingNumberTextview.setText(
//                                    userdata.getAsJsonObject(
//                                        "edge_follow"
//                                    )["count"].asString
//                                )
//                                binding.profilePostNumberTextview.setText(userdata.getAsJsonObject("edge_owner_to_timeline_media")["count"].asString)
//                                binding.profileLongIdTextview.setText(userdata["username"].asString)
//


                    val listType = object : TypeToken<ModelInstagramResponse?>() {}.type
                    val modelInstagramResponse: ModelInstagramResponse? = GsonBuilder().create()
                        .fromJson<ModelInstagramResponse>(
                            response.body().toString(),
                            listType
                        )
                    if (modelInstagramResponse != null) {
                        if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                            val modelGetEdgetoNode: ModelGetEdgetoNode =
                                modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children
                            myInstaUsername =
                                modelInstagramResponse.modelGraphshortcode.shortcode_media.owner.username + "_"

                            val modelEdNodeArrayList: List<ModelEdNode> =
                                modelGetEdgetoNode.modelEdNodes
                            for (i in modelEdNodeArrayList.indices) {
                                if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                    myVideoUrlIs =
                                        modelEdNodeArrayList[i].modelNode.video_url
                                    DownloadFileMain.startDownloading(
                                        this@MainActivityNewUi,
                                        myVideoUrlIs,
                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                            myVideoUrlIs
                                        ),
                                        ".mp4"
                                    )
                                    // etText.setText("");
                                    dismissMyDialog()
                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src
                                    DownloadFileMain.startDownloading(
                                        this@MainActivityNewUi,
                                        myPhotoUrlIs,
                                        myInstaUsername + iUtils.getImageFilenameFromURL(
                                            myPhotoUrlIs
                                        ),
                                        ".png"
                                    )
                                    myPhotoUrlIs = ""
                                    dismissMyDialog()
                                    // etText.setText("");
                                }
                            }
                        } else {
                            val isVideo =
                                modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video
                            myInstaUsername =
                                modelInstagramResponse.modelGraphshortcode.shortcode_media.owner.username + "_"

                            if (isVideo) {
                                myVideoUrlIs =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url
                                DownloadFileMain.startDownloading(
                                    this@MainActivityNewUi,
                                    myVideoUrlIs,
                                    myInstaUsername + iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                    ".mp4"
                                )
                                dismissMyDialog()
                                myVideoUrlIs = ""
                            } else {
                                myPhotoUrlIs =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src
                                DownloadFileMain.startDownloading(
                                    this@MainActivityNewUi,
                                    myPhotoUrlIs,
                                    myInstaUsername + iUtils.getImageFilenameFromURL(myPhotoUrlIs),
                                    ".png"
                                )
                                dismissMyDialog()
                                myPhotoUrlIs = ""
                            }
                        }
                    } else {
                        runOnUiThread {
                            iUtils.ShowToastError(
                                this@MainActivityNewUi,
                                resources.getString(R.string.somthing),
                            )
                        }
                        dismissMyDialog()
                    }
                } catch (e: java.lang.Exception) {
                    try {
                        try {
                            System.err.println("workkkkkkkkk 4")

                            downloadInstagramImageOrVideodata(
                                URL, ""
                            )
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            println("response1122334455 exe 1:   " + e.localizedMessage)
                            dismissMyDialog()
                            System.err.println("workkkkkkkkk 5.1")
                            e.printStackTrace()
                            runOnUiThread {
                                iUtils.ShowToastError(
                                    this@MainActivityNewUi,
                                    resources.getString(R.string.error_occ) + "",
                                )
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        this@MainActivityNewUi.runOnUiThread {
                            dismissMyDialog()
                            if (!this@MainActivityNewUi.isFinishing) {
                                e.printStackTrace()
                                runOnUiThread {
                                    iUtils.ShowToastError(
                                        this@MainActivityNewUi,
                                        resources.getString(R.string.somthing),
                                    )
                                }
                                println("response1122334455 exe 1:   " + e.localizedMessage)
                                dismissMyDialog()
                                val alertDialog =
                                    AlertDialog.Builder(this@MainActivityNewUi).create()
                                alertDialog.setTitle(getString(R.string.logininsta))
                                alertDialog.setMessage(getString(R.string.urlisprivate))
                                alertDialog.setButton(
                                    AlertDialog.BUTTON_POSITIVE, getString(R.string.logininsta)
                                ) { dialog, _ ->
                                    dialog.dismiss()
                                    val intent = Intent(
                                        this@MainActivityNewUi,
                                        InstagramLoginActivity::class.java
                                    )
                                    startActivityForResult(intent, 200)
                                }
                                alertDialog.setButton(
                                    AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)
                                ) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                alertDialog.show()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                try {
                    println("response1122334455:   " + "Failed0" + t.message)
                    dismissMyDialog()
                    runOnUiThread {
                        iUtils.ShowToastError(
                            this@MainActivityNewUi,
                            resources.getString(R.string.somthing),
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    @Keep
    fun downloadInstagramImageOrVideodata_old_withlogin(URL: String?, Cookie: String?) {
        val random1 = Random()
        val j = random1.nextInt(iUtils.UserAgentsList.size)
        object : Thread() {
            override fun run() {
                Looper.prepare()
                val client = OkHttpClient().newBuilder()
                    .build()
                val request: Request = Request.Builder()
                    .url(URL!!)
                    .method("GET", null)
                    .addHeader("Cookie", Cookie!!)
                    .addHeader(
                        "User-Agent",
                        iUtils.UserAgentsList[j]
                    )
                    .build()
                try {
                    val response = client.newCall(request).execute()

                    if (response.code == 200) {

                        val ress = response.body!!.string()
                        println("working errpr \t Value: $ress")

                        try {
                            val listType: Type =
                                object : TypeToken<ModelInstaWithLogin?>() {}.type
                            val modelInstagramResponse: ModelInstaWithLogin = Gson().fromJson(
                                ress,
                                listType
                            )
                            println("workkkkk777 " + modelInstagramResponse.items[0].code)

                            if (modelInstagramResponse.items[0].mediaType == 8) {
                                myInstaUsername =
                                    modelInstagramResponse.items[0].user.username + "_"

                                val modelGetEdgetoNode = modelInstagramResponse.items[0]
                                val modelEdNodeArrayList: List<CarouselMedia> =
                                    modelGetEdgetoNode.carouselMedia
                                for (i in modelEdNodeArrayList.indices) {
                                    if (modelEdNodeArrayList[i].mediaType == 2) {
                                        myVideoUrlIs =
                                            modelEdNodeArrayList[i].videoVersions[0].geturl()
                                        DownloadFileMain.startDownloading(
                                            this@MainActivityNewUi,
                                            myVideoUrlIs,
                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                myVideoUrlIs
                                            ),
                                            ".mp4"
                                        )
                                        // etText.setText("");
                                        dismissMyDialog()
                                        myVideoUrlIs = ""
                                    } else {
                                        myPhotoUrlIs =
                                            modelEdNodeArrayList[i].imageVersions2.candidates[0]
                                                .geturl()
                                        DownloadFileMain.startDownloading(
                                            this@MainActivityNewUi,
                                            myPhotoUrlIs,
                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                myPhotoUrlIs
                                            ),
                                            ".png"
                                        )
                                        myPhotoUrlIs = ""
                                        dismissMyDialog()
                                        // etText.setText("");
                                    }
                                }
                            } else {
                                val modelGetEdgetoNode = modelInstagramResponse.items[0]
                                myInstaUsername =
                                    modelInstagramResponse.items[0].user.username + "_"

                                if (modelGetEdgetoNode.mediaType == 2) {
                                    myVideoUrlIs =
                                        modelGetEdgetoNode.videoVersions[0].geturl()
                                    DownloadFileMain.startDownloading(
                                        this@MainActivityNewUi,
                                        myVideoUrlIs,
                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                            myVideoUrlIs
                                        ),
                                        ".mp4"
                                    )
                                    dismissMyDialog()
                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelGetEdgetoNode.imageVersions2.candidates[0].geturl()
                                    DownloadFileMain.startDownloading(
                                        this@MainActivityNewUi,
                                        myPhotoUrlIs,
                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                            myPhotoUrlIs
                                        ),

                                        ".png"
                                    )
                                    dismissMyDialog()
                                    myPhotoUrlIs = ""
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            System.err.println("workkkkkkkkk 5nn errrr " + e.message)
                            try {
                                try {
                                    System.err.println("workkkkkkkkk 4")

                                    val sharedPrefsFor =
                                        SharedPrefsForInstagram(this@MainActivityNewUi)
                                    val map = sharedPrefsFor.preference
                                    if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != ""
                                    ) {
                                        System.err.println("workkkkkkkkk 5.2")
                                        downloadInstagramImageOrVideodata(
                                            URL, "ds_user_id=" + map.preferencE_USERID
                                                    + "; sessionid=" + map.preferencE_SESSIONID
                                        )
                                    } else {
                                        dismissMyDialog()
                                        System.err.println("workkkkkkkkk 5.1")
                                        e.printStackTrace()
                                        runOnUiThread {
                                            iUtils.ShowToastError(
                                                this@MainActivityNewUi,
                                                resources.getString(R.string.error_occ) + "",
                                            )
                                        }
                                    }
                                } catch (e: java.lang.Exception) {
                                    dismissMyDialog()
                                    System.err.println("workkkkkkkkk 5.1")
                                    e.printStackTrace()
                                    runOnUiThread {
                                        iUtils.ShowToastError(
                                            this@MainActivityNewUi,
                                            resources.getString(R.string.error_occ) + "",
                                        )
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                try {
                                    this@MainActivityNewUi.runOnUiThread {
                                        dismissMyDialog()
                                        if (!this@MainActivityNewUi.isFinishing) {
                                            val alertDialog =
                                                AlertDialog.Builder(this@MainActivityNewUi).create()
                                            alertDialog.setTitle(getString(R.string.logininsta))
                                            alertDialog.setMessage(getString(R.string.urlisprivate))
                                            alertDialog.setButton(
                                                AlertDialog.BUTTON_POSITIVE,
                                                getString(R.string.logininsta)
                                            ) { dialog, _ ->
                                                dialog.dismiss()
                                                val intent = Intent(
                                                    this@MainActivityNewUi,
                                                    InstagramLoginActivity::class.java
                                                )
                                                startActivityForResult(intent, 200)
                                            }
                                            alertDialog.setButton(
                                                AlertDialog.BUTTON_NEGATIVE,
                                                getString(R.string.cancel)
                                            ) { dialog, _ ->
                                                dialog.dismiss()
                                            }
                                            alertDialog.show()
                                        }
                                    }
                                } catch (_: Exception) {

                                }
                            }
                        }
                    } else {
                        object : Thread() {
                            override fun run() {

                                val client = OkHttpClient().newBuilder()
                                    .build()
                                val request: Request = Request.Builder()
                                    .url(URL)
                                    .method("GET", null)
                                    .addHeader("Cookie", iUtils.myInstagramTempCookies)
                                    .addHeader(
                                        "User-Agent",
                                        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36"
                                    ).build()
                                try {
                                    val response1: Response = client.newCall(request).execute()

                                    if (response1.code == 200) {

                                        try {
                                            val listType: Type =
                                                object : TypeToken<ModelInstaWithLogin?>() {}.type
                                            val modelInstagramResponse: ModelInstaWithLogin =
                                                Gson().fromJson(
                                                    response.body!!.string(),
                                                    listType
                                                )
                                            if (modelInstagramResponse.items[0].mediaType == 8) {
                                                myInstaUsername =
                                                    modelInstagramResponse.items[0].user.username + "_"

                                                val modelGetEdgetoNode =
                                                    modelInstagramResponse.items[0]
                                                val modelEdNodeArrayList: List<CarouselMedia> =
                                                    modelGetEdgetoNode.carouselMedia
                                                for (i in modelEdNodeArrayList.indices) {
                                                    if (modelEdNodeArrayList[i].mediaType == 2) {
                                                        myVideoUrlIs =
                                                            modelEdNodeArrayList[i].videoVersions[0].geturl()
                                                        DownloadFileMain.startDownloading(
                                                            this@MainActivityNewUi,
                                                            myVideoUrlIs,
                                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                                myVideoUrlIs
                                                            ),
                                                            ".mp4"
                                                        )
                                                        // etText.setText("");
                                                        dismissMyDialog()
                                                        myVideoUrlIs = ""
                                                    } else {
                                                        myPhotoUrlIs =
                                                            modelEdNodeArrayList[i].imageVersions2.candidates[0].geturl()
                                                        DownloadFileMain.startDownloading(
                                                            this@MainActivityNewUi,
                                                            myPhotoUrlIs,
                                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                                myPhotoUrlIs
                                                            ),
                                                            ".png"
                                                        )
                                                        myPhotoUrlIs = ""
                                                        dismissMyDialog()
                                                        // etText.setText("");
                                                    }
                                                }
                                            } else {
                                                myInstaUsername =
                                                    modelInstagramResponse.items[0].user.username + "_"

                                                val modelGetEdgetoNode =
                                                    modelInstagramResponse.items[0]
                                                if (modelGetEdgetoNode.mediaType == 2) {
                                                    myVideoUrlIs =
                                                        modelGetEdgetoNode.videoVersions[0]
                                                            .geturl()
                                                    DownloadFileMain.startDownloading(
                                                        this@MainActivityNewUi,
                                                        myVideoUrlIs,
                                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                            myVideoUrlIs
                                                        ),
                                                        ".mp4"
                                                    )
                                                    dismissMyDialog()
                                                    myVideoUrlIs = ""
                                                } else {
                                                    myPhotoUrlIs =
                                                        modelGetEdgetoNode.imageVersions2.candidates[0].geturl()
                                                    DownloadFileMain.startDownloading(
                                                        this@MainActivityNewUi,
                                                        myPhotoUrlIs,
                                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                            myPhotoUrlIs
                                                        ),
                                                        ".png"
                                                    )
                                                    dismissMyDialog()
                                                    myPhotoUrlIs = ""
                                                }
                                            }
                                        } catch (e: java.lang.Exception) {
                                            System.err.println("workkkkkkkkk 5nn errrr " + e.message)
                                            e.printStackTrace()
                                            try {
                                                this@MainActivityNewUi.runOnUiThread {
                                                    dismissMyDialog()

                                                    if (!this@MainActivityNewUi.isFinishing) {
                                                        val alertDialog =
                                                            AlertDialog.Builder(this@MainActivityNewUi)
                                                                .create()
                                                        alertDialog.setTitle(getString(R.string.logininsta))
                                                        alertDialog.setMessage(getString(R.string.urlisprivate))
                                                        alertDialog.setButton(
                                                            AlertDialog.BUTTON_POSITIVE,
                                                            getString(R.string.logininsta)
                                                        ) { dialog, _ ->
                                                            dialog.dismiss()
                                                            val intent = Intent(
                                                                this@MainActivityNewUi,
                                                                InstagramLoginActivity::class.java
                                                            )
                                                            startActivityForResult(intent, 200)
                                                        }
                                                        alertDialog.setButton(
                                                            AlertDialog.BUTTON_NEGATIVE,
                                                            getString(R.string.cancel)
                                                        ) { dialog, _ ->
                                                            dialog.dismiss()
                                                        }
                                                        alertDialog.show()
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        }
                                    } else {
                                        System.err.println("workkkkkkkkk 6bbb errrr ")
                                        this@MainActivityNewUi.runOnUiThread {
                                            dismissMyDialog()

                                            if (!this@MainActivityNewUi.isFinishing) {
                                                val alertDialog =
                                                    AlertDialog.Builder(this@MainActivityNewUi)
                                                        .create()
                                                alertDialog.setTitle(getString(R.string.logininsta))
                                                alertDialog.setMessage(getString(R.string.urlisprivate))
                                                alertDialog.setButton(
                                                    AlertDialog.BUTTON_POSITIVE,
                                                    getString(R.string.logininsta)
                                                ) { dialog, _ ->
                                                    dialog.dismiss()
                                                    val intent = Intent(
                                                        this@MainActivityNewUi,
                                                        InstagramLoginActivity::class.java
                                                    )
                                                    startActivityForResult(intent, 200)
                                                }
                                                alertDialog.setButton(
                                                    AlertDialog.BUTTON_NEGATIVE,
                                                    getString(R.string.cancel)
                                                ) { dialog, _ ->
                                                    dialog.dismiss()
                                                }
                                                alertDialog.show()
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }.start()
                    }
                } catch (e: Exception) {
                    dismissMyDialog()
                }
            }
        }.start()
    }

    @Keep
    fun downloadInstagramImageOrVideodata_withlogin(URL: String?, Cookie: String?) {
        /*instagram product types
        * product_type
        *
        * igtv "media_type": 2
        * carousel_container  "media_type": 8
        * clips  "media_type": 2
        * feed   "media_type": 1
        * */

        val random1 = Random()
        val j = random1.nextInt(iUtils.UserAgentsList.size)

        var Cookie = Cookie
        if (TextUtils.isEmpty(Cookie)) {
            Cookie = ""
        }
        val apiService: RetrofitApiInterface =
            RetrofitClient.getClient()

        val callResult: Call<JsonObject> = apiService.getInstagramData(
            URL,
            Cookie,
            iUtils.UserAgentsList[j]
        )
        callResult.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: retrofit2.Response<JsonObject?>
            ) {

                try {
                    val listType: Type =
                        object : TypeToken<ModelInstaWithLogin?>() {}.type
                    val modelInstagramResponse: ModelInstaWithLogin = Gson().fromJson(
                        response.body(),
                        listType
                    )
                    println("workkkkk777 " + modelInstagramResponse.items[0].code)

                    if (modelInstagramResponse.items[0].mediaType == 8) {
                        myInstaUsername = modelInstagramResponse.items[0].user.username + "_"

                        val modelGetEdgetoNode = modelInstagramResponse.items[0]

                        val modelEdNodeArrayList: List<CarouselMedia> =
                            modelGetEdgetoNode.carouselMedia
                        for (i in modelEdNodeArrayList.indices) {
                            if (modelEdNodeArrayList[i].mediaType == 2) {
                                myVideoUrlIs =
                                    modelEdNodeArrayList[i].videoVersions[0].geturl()
                                DownloadFileMain.startDownloading(
                                    this@MainActivityNewUi,
                                    myVideoUrlIs,
                                    myInstaUsername + iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                    ".mp4"
                                )
                                // etText.setText("");
                                dismissMyDialog()
                                myVideoUrlIs = ""
                            } else {
                                myPhotoUrlIs =
                                    modelEdNodeArrayList[i].imageVersions2.candidates[0]
                                        .geturl()
                                DownloadFileMain.startDownloading(
                                    this@MainActivityNewUi,
                                    myPhotoUrlIs,
                                    myInstaUsername + iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
                                    ".png"
                                )
                                myPhotoUrlIs = ""
                                dismissMyDialog()
                                // etText.setText("");
                            }
                        }
                    } else {
                        val modelGetEdgetoNode = modelInstagramResponse.items[0]
                        myInstaUsername = modelInstagramResponse.items[0].user.username + "_"

                        if (modelGetEdgetoNode.mediaType == 2) {
                            myVideoUrlIs =
                                modelGetEdgetoNode.videoVersions[0].geturl()
                            DownloadFileMain.startDownloading(
                                this@MainActivityNewUi,
                                myVideoUrlIs,
                                myInstaUsername + iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                ".mp4"
                            )
                            dismissMyDialog()
                            myVideoUrlIs = ""
                        } else {
                            myPhotoUrlIs =
                                modelGetEdgetoNode.imageVersions2.candidates[0].geturl()
                            DownloadFileMain.startDownloading(
                                this@MainActivityNewUi,
                                myPhotoUrlIs,
                                myInstaUsername + iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
                                ".png"
                            )
                            dismissMyDialog()
                            myPhotoUrlIs = ""
                        }
                    }
                } catch (e: java.lang.Exception) {
                    System.err.println("workkkkkkkkk 5nn errrr " + e.message)

                    try {

                        try {
                            System.err.println("workkkkkkkkk 4")

                            val sharedPrefsFor = SharedPrefsForInstagram(this@MainActivityNewUi)
                            val map = sharedPrefsFor.preference
                            if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != ""
                            ) {
                                System.err.println("workkkkkkkkk 5.2")
                                downloadInstagramImageOrVideodata_old(
                                    URL, "ds_user_id=" + map.preferencE_USERID
                                            + "; sessionid=" + map.preferencE_SESSIONID
                                )
                            } else {
                                dismissMyDialog()
                                System.err.println("workkkkkkkkk 5.1")
                                e.printStackTrace()
                                runOnUiThread {
                                    iUtils.ShowToastError(
                                        this@MainActivityNewUi,
                                        resources.getString(R.string.error_occ) + "",
                                    )
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            dismissMyDialog()
                            System.err.println("workkkkkkkkk 5.1")
                            e.printStackTrace()
                            runOnUiThread {
                                iUtils.ShowToastError(
                                    this@MainActivityNewUi,
                                    resources.getString(R.string.error_occ) + "",
                                )
                            }
                        }

                    } catch (e: Exception) {

                        e.printStackTrace()
                        this@MainActivityNewUi.runOnUiThread {
                            dismissMyDialog()
                            if (!this@MainActivityNewUi.isFinishing) {
                                val alertDialog =
                                    AlertDialog.Builder(this@MainActivityNewUi)
                                        .create()
                                alertDialog.setTitle(getString(R.string.logininsta))
                                alertDialog.setMessage(getString(R.string.urlisprivate))
                                alertDialog.setButton(
                                    AlertDialog.BUTTON_POSITIVE,
                                    getString(R.string.logininsta)
                                ) { dialog, _ ->
                                    dialog.dismiss()
                                    val intent = Intent(
                                        this@MainActivityNewUi,
                                        InstagramLoginActivity::class.java
                                    )
                                    startActivityForResult(intent, 200)
                                }
                                alertDialog.setButton(
                                    AlertDialog.BUTTON_NEGATIVE,
                                    getString(R.string.cancel)
                                ) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                alertDialog.show()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                println("response1122334455:   " + "Failed0")
                dismissMyDialog()
                runOnUiThread {
                    iUtils.ShowToastError(
                        this@MainActivityNewUi,
                        resources.getString(R.string.somthing),
                    )
                }
            }
        })
    }


    @Keep
    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    fun loginSnapIntaWeb(urlwithoutlettersqp: String) {
        try {


            binding.webViewInsta.clearCache(true)

            val cookieManager1 = CookieManager.getInstance()

            cookieManager1.setAcceptThirdPartyCookies(binding.webViewInsta, true)
            cookieManager1.setAcceptCookie(true)
            cookieManager1.acceptCookie()


            binding.webViewInsta.clearFormData()
            binding.webViewInsta.settings.saveFormData = true

            val random1 = Random()
            val j = random1.nextInt(iUtils.UserAgentsListLogin.size)

            binding.webViewInsta.settings.userAgentString = iUtils.UserAgentsListLogin[j]

            binding.webViewInsta.settings.allowFileAccess = true
            binding.webViewInsta.settings.javaScriptEnabled = true
            binding.webViewInsta.settings.defaultTextEncodingName = "UTF-8"
            binding.webViewInsta.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            binding.webViewInsta.settings.databaseEnabled = true
            binding.webViewInsta.settings.builtInZoomControls = false
            binding.webViewInsta.settings.setSupportZoom(true)
            binding.webViewInsta.settings.useWideViewPort = true
            binding.webViewInsta.settings.domStorageEnabled = true
            binding.webViewInsta.settings.loadWithOverviewMode = true
            binding.webViewInsta.settings.loadsImagesAutomatically = true
            binding.webViewInsta.settings.blockNetworkImage = false
            binding.webViewInsta.settings.blockNetworkLoads = false
            binding.webViewInsta.settings.defaultTextEncodingName = "UTF-8"

            var isdownloadstarted = false

            Log.e(
                "workkkk sel",
                "binding.loggedIn "
            )


            val handler2 = Handler()
            val listoflink_videos = ArrayList<String>()
            val listoflink_photos = ArrayList<String>()


            val cookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookie()

            binding.webViewInsta.webViewClient = object : WebViewClient() {

                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(
                    webView1: WebView,
                    url: String?
                ): Boolean {
                    webView1.loadUrl(url!!)
                    return true
                }

                override fun onLoadResource(
                    webView: WebView?,
                    str: String?
                ) {
                    super.onLoadResource(webView, str)
                }


                override fun onPageFinished(
                    webView: WebView,
                    str: String?
                ) {
                    super.onPageFinished(webView, str)
                    Log.e(
                        "workkkk url",
                        "binding.progressBar reciveing data $str"
                    )

                    try {


                        val jsscript = ("javascript:(function() { "
                                + "var ell = document.getElementsByTagName('input');"
                                // + "ell[1].value ='" + "keepsaveit" + "';"
                                // + "ell[2].value ='" + "keepsaveit12345" + "';"

                                + "ell[0].value ='" + urlwithoutlettersqp + "';"

                                + "var bbb = document.getElementsByTagName('button');"
                                + "bbb[5].click();"
                                + "})();")



                        binding.webViewInsta.evaluateJavascript(
                            jsscript
                        ) {

                            Log.e(
                                "workkkk0",
                                "binding.progressBar reciveing data $it"
                            )


                            try {
                                handler2.postDelayed(object : Runnable {
                                    override fun run() {
                                        this@MainActivityNewUi.runOnUiThread {


                                            binding.webViewInsta.evaluateJavascript(
                                                "(function() { " +
                                                        "var text='';" +
                                                        "var aaa = document.getElementsByTagName('a');" +
                                                        "for (var i = 0; i < aaa.length; i++) {" +
                                                        // "if(aaa[i].getAttribute('href').includes('https://scontent') || aaa[i].getAttribute('href').includes('https://instagram')){" +
                                                        "  text += aaa[i].getAttribute('href')+'@_@';" +
                                                        //  "}" +
                                                        "}" +
                                                        "var withoutLast3 = text.slice(0, -3);" +
                                                        "return withoutLast3+''; })();"
                                            ) { html ->

                                                Log.e(
                                                    "workkkk0",
                                                    "binding.progressBar reciveing data $html"
                                                )

                                                //                                        val unescapedString =
                                                //                                            Parser.unescapeEntities(html, true)

                                                //   var dsd :Document= Jsoup.parse(unescapedString)
                                                //                                        val document = Jsoup.parse(html)

                                                //https://snapxcdn.com/dl/v1?token=


                                                binding.webViewInsta.evaluateJavascript(
                                                    "javascript:(function() { "
                                                            + "var bbb = document.getElementById('closeModalBtn');"
                                                            + "bbb.click();"
                                                            + "})();"
                                                ) { value ->
                                                    Log.e(
                                                        "workkkk0",
                                                        "binding.progressBar reciveing data3 $value"
                                                    )
                                                }


                                                val sss = html.split("@_@")
                                                for (i in sss) {


                                                    if (i.contains("/?token=") && !i.contains(
                                                            "/instagram-story-download"
                                                        ) && !i.contains(
                                                            "/instagram-reels-video-download"
                                                        ) && !i.contains("/instagram-photo-download") && !i.contains(
                                                            "/instagram-story-viewer"
                                                        )
                                                    ) {
                                                        Log.d("HTML vid", "" + i)

                                                        listoflink_videos.add(i)


                                                    }

                                                    if (i.contains("instagram") && !i.contains(
                                                            "/?token="
                                                        ) && !i.contains("/instagram-story-download") && !i.contains(
                                                            "/instagram-reels-video-download"
                                                        ) && !i.contains("/instagram-photo-download") && !i.contains(
                                                            "/instagram-story-viewer"
                                                        )
                                                    ) {
                                                        Log.d("HTMLimg", "" + i)


                                                        listoflink_photos.add(i)
                                                    }
                                                }


                                                if (!isdownloadstarted && (listoflink_videos.size > 0 || listoflink_photos.size > 0)) {

                                                    dismissMyDialog()


                                                    handler2.removeCallbacksAndMessages(
                                                        null
                                                    )

                                                    isdownloadstarted = true

                                                    if ((listoflink_videos != null || listoflink_photos != null) || (listoflink_videos.size > 0 || listoflink_photos.size > 0)) {


                                                        for (i in listoflink_videos) {


                                                            DownloadFileMain.startDownloading(
                                                                this@MainActivityNewUi,
                                                                i,
                                                                myInstaUsername + "_instagram_" + System.currentTimeMillis() + "_" + iUtils.getVideoFilenameFromURL(
                                                                    i
                                                                ),
                                                                ".mp4"
                                                            )

                                                        }

                                                        for (i in listoflink_photos) {

                                                            DownloadFileMain.startDownloading(
                                                                this@MainActivityNewUi,
                                                                i,
                                                                myInstaUsername + "_instagram_" + System.currentTimeMillis() + "_" + iUtils.getImageFilenameFromURL(
                                                                    i
                                                                ),
                                                                ".png"
                                                            )
                                                        }
                                                    }

                                                } else {

                                                    //                                                    handler2.removeCallbacksAndMessages(
                                                    //                                                        null
                                                    //                                                    )
                                                    //
                                                    //                                                    this@MainActivityNewUi?.runOnUiThread {
                                                    //
                                                    //                                                        progressDralogGenaratinglink.setMessage(
                                                    //                                                            "Please Wait"
                                                    //                                                        )
                                                    //                                                    }
                                                    //                                                    Log.d(
                                                    //                                                        "HTML nolink fould",
                                                    //                                                        ""
                                                    //                                                    )
                                                    //
                                                    //                                                    try {
                                                    //                                                        System.err.println("workkkkkkkkk 4")
                                                    //
                                                    //                                                        val urlwithoutlettersqp2 =
                                                    //                                                            "$urlwithoutlettersqp?__a=1&__d=dis"
                                                    //
                                                    //
                                                    //                                                        System.err.println("workkkkkkkkk 4.5")
                                                    //
                                                    //                                                        downloadInstagramImageOrVideodata(
                                                    //                                                            urlwithoutlettersqp2,
                                                    //                                                            iUtils.myInstagramTempCookies
                                                    //                                                        )
                                                    //
                                                    //
                                                    //                                                    } catch (e: java.lang.Exception) {
                                                    //                                                        dismissMyDialogErrortoastFrag()
                                                    //                                                        System.err.println("workkkkkkkkk 5.1")
                                                    //                                                        e.printStackTrace()
                                                    //
                                                    //
                                                    //                                                    }


                                                }


                                            }

                                        }


                                        handler2.postDelayed(this, 2000)
                                    }
                                }, 2000)
                            } catch (e: java.lang.Exception) {
                                dismissMyDialog()
                                System.err.println("workkkkkkkkk 5.1")
                                e.printStackTrace()
                                runOnUiThread {
                                    iUtils.ShowToastError(
                                        this@MainActivityNewUi,
                                        resources.getString(R.string.error_occ) + "",
                                    )
                                }

                            }


                        }


                    } catch (e: java.lang.Exception) {
                        dismissMyDialog()
                        System.err.println("workkkkkkkkk 5.1")
                        e.printStackTrace()
                        runOnUiThread {
                            iUtils.ShowToastError(
                                this@MainActivityNewUi,
                                resources.getString(R.string.error_occ) + "",
                            )
                        }

                    }
                }


                @Deprecated("Deprecated in Java")
                override fun onReceivedError(
                    webView: WebView?,
                    i: Int,
                    str: String?,
                    str2: String?
                ) {
                    super.onReceivedError(webView, i, str, str2)
                    dismissMyDialog()
                    runOnUiThread {
                        iUtils.ShowToastError(
                            this@MainActivityNewUi,
                            resources.getString(R.string.error_occ) + "",
                        )
                    }
                }

                override fun shouldInterceptRequest(
                    webView: WebView?,
                    webResourceRequest: WebResourceRequest?
                ): WebResourceResponse? {
                    return super.shouldInterceptRequest(
                        webView,
                        webResourceRequest
                    )
                }

                @Deprecated("Deprecated in Java")
                override fun shouldInterceptRequest(
                    view: WebView?,
                    url: String?
                ): WebResourceResponse? {
                    if (url!!.contains("google") || url.contains("facebook")) {
                        val textStream: InputStream = ByteArrayInputStream("".toByteArray())
                        return WebResourceResponse("text/plain", "UTF-8", textStream)
                    }
                    return super.shouldInterceptRequest(view, url)
                }


                override fun shouldOverrideUrlLoading(
                    webView: WebView?,
                    webResourceRequest: WebResourceRequest?
                ): Boolean {
                    return super.shouldOverrideUrlLoading(
                        webView,
                        webResourceRequest
                    )
                }
            }


            CookieSyncManager.createInstance(this@MainActivityNewUi)
            binding.webViewInsta.loadUrl("https://snapinsta.app/")


        } catch (e: java.lang.Exception) {
            dismissMyDialog()
            System.err.println("workkkkkkkkk 5.1")
            e.printStackTrace()
            runOnUiThread {
                iUtils.ShowToastError(
                    this@MainActivityNewUi,
                    resources.getString(R.string.error_occ) + "",
                )
            }
        }
    }

    @Keep
    private fun loginDownloadgram(urlwithoutlettersqp: String) {
        try {
            binding.webViewInsta.clearCache(true)
            val cookieManager1 = CookieManager.getInstance()
            cookieManager1.setAcceptThirdPartyCookies(binding.webViewInsta, true)
            cookieManager1.setAcceptCookie(true)
            cookieManager1.acceptCookie()
            binding.webViewInsta.clearFormData()
            binding.webViewInsta.settings.saveFormData = true
            val random1 = Random()
            val j = random1.nextInt(iUtils.UserAgentsList.size)
            binding.webViewInsta.settings.setUserAgentString(iUtils.UserAgentsListLogin.get(j))
            binding.webViewInsta.settings.allowFileAccess = true
            binding.webViewInsta.settings.javaScriptEnabled = true
            binding.webViewInsta.settings.defaultTextEncodingName = "UTF-8"
            binding.webViewInsta.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            binding.webViewInsta.settings.builtInZoomControls = true
            binding.webViewInsta.settings.setSupportZoom(true)
            binding.webViewInsta.settings.useWideViewPort = true
            binding.webViewInsta.settings.domStorageEnabled = true
            binding.webViewInsta.settings.loadWithOverviewMode = true
            binding.webViewInsta.settings.loadsImagesAutomatically = true
            binding.webViewInsta.settings.blockNetworkImage = false
            binding.webViewInsta.settings.blockNetworkLoads = false
            val isdownloadstarted = booleanArrayOf(false)
            val isdownloadclicked = booleanArrayOf(false)
            Log.e(
                "workkkk sel",
                "binding.loggedIn "
            )
            val handler2 = Handler()
            var listoflink_videos = ArrayList<String>()
            var listoflink_photos = ArrayList<String>()
            val cookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookie()
            binding.webViewInsta.webViewClient = object : WebViewClient() {

                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    return super.shouldOverrideUrlLoading(view, url)
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    Log.e(
                        "workkkk url",
                        "binding.progressBar reciveing data0 $url"
                    )
                    try {
                        val jsscript = ("javascript:(function() { "
                                + "var ell = document.getElementById('url');" // + "ell[1].value ='" + "keepsaveit" + "';"
                                // + "ell[2].value ='" + "keepsaveit12345" + "';"
                                + "ell.value ='" + urlwithoutlettersqp + "';"
                                + "var bbb = document.getElementById('submit');"
                                + "bbb.click();"
                                + "})();")
                        if (!isdownloadclicked[0]) {
                            binding.webViewInsta.evaluateJavascript(jsscript) { value ->
                                isdownloadclicked[0] = true
                                Log.e(
                                    "workkkk0",
                                    "binding.progressBar reciveing data1 $value"
                                )
                                try {
                                    handler2.postDelayed(object : Runnable {
                                        override fun run() {
                                            this@MainActivityNewUi.runOnUiThread {
                                                binding.webViewInsta.evaluateJavascript(
                                                    "(function() { " +
                                                            "var text='';" +
                                                            "var aaa = document.getElementsByTagName('a');" +
                                                            "for (var i = 0; i < aaa.length; i++) {" +  // "if(aaa[i].getAttribute('href').includes('https://scontent') || aaa[i].getAttribute('href').includes('https://instagram')){" +
                                                            "  text += aaa[i].getAttribute('href')+'@_@';" +  //  "}" +
                                                            "}" +
                                                            "var withoutLast3 = text.slice(0, -3);" +
                                                            "return withoutLast3+''; })();"
                                                ) { html ->
                                                    Log.e(
                                                        "workkkk0",
                                                        "binding.progressBar reciveing data2 $html"
                                                    )
                                                    val sss: List<String> = html.split("@_@")
                                                    for (i in sss) {
                                                        if (i.contains("scontent") || i.contains(
                                                                "cdninstagram"
                                                            )
                                                        ) {
                                                            if (i.contains(".jpg")) {
                                                                Log.d(
                                                                    "HTMLimg",
                                                                    "" + i
                                                                )
                                                                listoflink_photos.add(i)
                                                            } else if (i.contains(".mp4") || i.contains(
                                                                    "https://download."
                                                                ) || i.contains("?file=")
                                                            ) {
                                                                Log.d(
                                                                    "HTML vid",
                                                                    "" + i
                                                                )
                                                                listoflink_videos.add(i)
                                                            }
                                                        }
                                                    }
                                                    if (!isdownloadstarted[0] && (listoflink_videos.size > 0 || listoflink_photos.size > 0)) {
                                                        dismissMyDialog()
                                                        handler2.removeCallbacksAndMessages(
                                                            null
                                                        )
                                                        isdownloadstarted[0] = true
                                                        if (listoflink_videos != null || listoflink_photos != null || listoflink_videos.size > 0 || listoflink_photos.size > 0) {

                                                            listoflink_videos =
                                                                iUtils.removeDuplicates(
                                                                    listoflink_videos
                                                                )
                                                            listoflink_photos =
                                                                iUtils.removeDuplicates(
                                                                    listoflink_photos
                                                                )



                                                            for (i in listoflink_videos) {


                                                                DownloadFileMain.startDownloading(
                                                                    this@MainActivityNewUi,
                                                                    i,
                                                                    myInstaUsername + "_instagram_" + System.currentTimeMillis() + "_" + iUtils.getVideoFilenameFromURL(
                                                                        i
                                                                    ),
                                                                    ".mp4"
                                                                )

                                                            }

                                                            for (i in listoflink_photos) {

                                                                DownloadFileMain.startDownloading(
                                                                    this@MainActivityNewUi,
                                                                    i,
                                                                    myInstaUsername + "_instagram_" + System.currentTimeMillis() + "_" + iUtils.getImageFilenameFromURL(
                                                                        i
                                                                    ),
                                                                    ".png"
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            handler2.postDelayed(this, 3500)
                                        }
                                    }, 3000)
                                } catch (e: java.lang.Exception) {
                                    dismissMyDialog()
                                    System.err.println("workkkkkkkkk 5.1")
                                    e.printStackTrace()
                                    runOnUiThread {
                                        iUtils.ShowToastError(
                                            this@MainActivityNewUi,
                                            resources.getString(R.string.error_occ) + "",
                                        )
                                    }
                                }
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        dismissMyDialog()
                        System.err.println("workkkkkkkkk 5.1")
                        e.printStackTrace()
                        runOnUiThread {
                            iUtils.ShowToastError(
                                this@MainActivityNewUi,
                                resources.getString(R.string.error_occ) + "",
                            )
                        }
                    }
                }

                override fun onLoadResource(view: WebView, url: String) {
                    super.onLoadResource(view, url)
                }

                override fun shouldInterceptRequest(
                    view: WebView,
                    request: WebResourceRequest
                ): WebResourceResponse? {
                    return super.shouldInterceptRequest(view, request)
                }

                @Deprecated("Deprecated in Java")
                override fun shouldInterceptRequest(
                    view: WebView?,
                    url: String?
                ): WebResourceResponse? {
                    if (url!!.contains("google") || url.contains("facebook")) {
                        val textStream: InputStream = ByteArrayInputStream("".toByteArray())
                        return WebResourceResponse("text/plain", "UTF-8", textStream)
                    }
                    return super.shouldInterceptRequest(view, url)
                }

                @Deprecated("Deprecated in Java")
                override fun onReceivedError(
                    view: WebView,
                    errorCode: Int,
                    description: String,
                    failingUrl: String
                ) {
                    super.onReceivedError(view, errorCode, description, failingUrl)
                    dismissMyDialog()
                    System.err.println("workkkkkkkkk 5.1")
                    runOnUiThread {
                        iUtils.ShowToastError(
                            this@MainActivityNewUi,
                            resources.getString(R.string.error_occ) + "",
                        )
                    }
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    // view.loadUrl(urlwithoutlettersqp);
                    return false
                }
            }
            CookieSyncManager.createInstance(this@MainActivityNewUi)
            binding.webViewInsta.loadUrl("https://downloadgram.org/")
        } catch (e: java.lang.Exception) {
            dismissMyDialog()
            System.err.println("workkkkkkkkk 5.1")
            e.printStackTrace()
            runOnUiThread {
                iUtils.ShowToastError(
                    this@MainActivityNewUi,
                    resources.getString(R.string.error_occ) + "",
                )
            }
        }
    }


    @Keep
    fun downloadInstagramImageOrVideoResponseOkhttp(URL: String?) {

//TODO check
//        Unirest.config()
//            .socketTimeout(500)
//            .connectTimeout(1000)
//            .concurrency(10, 5)
//            .proxy(Proxy("https://proxy"))
//            .setDefaultHeader("Accept", "application/json")
//            .followRedirects(false)
//            .enableCookieManagement(false)
//            .addInterceptor(MyCustomInterceptor())

        object : Thread() {
            override fun run() {


                try {

                    val cookieJar: ClearableCookieJar = PersistentCookieJar(
                        SetCookieCache(),
                        SharedPrefsCookiePersistor(this@MainActivityNewUi)
                    )

                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    // init OkHttpClient
                    val client: OkHttpClient = OkHttpClient.Builder()
                        .cookieJar(cookieJar)
                        .addInterceptor(logging)
                        .build()

                    val request: Request = Request.Builder()
                        .url("$URL?__a=1&__d=dis")
                        .method("GET", null)
                        .build()
                    val response = client.newCall(request).execute()

                    val ressd = response.body!!.string()
                    var code = response.code
                    if (!ressd.contains("shortcode_media")) {
                        code = 400
                    }
                    if (code == 200) {


                        try {


                            val listType =
                                object : TypeToken<ModelInstagramResponse?>() {}.type
                            val modelInstagramResponse: ModelInstagramResponse? =
                                GsonBuilder().create()
                                    .fromJson<ModelInstagramResponse>(
                                        ressd,
                                        listType
                                    )


                            if (modelInstagramResponse != null) {


                                if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                                    val modelGetEdgetoNode: ModelGetEdgetoNode =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children

                                    val modelEdNodeArrayList: List<ModelEdNode> =
                                        modelGetEdgetoNode.modelEdNodes
                                    for (i in 0 until modelEdNodeArrayList.size) {
                                        if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                            myVideoUrlIs =
                                                modelEdNodeArrayList[i].modelNode.video_url


                                            DownloadFileMain.startDownloading(
                                                this@MainActivityNewUi,
                                                myVideoUrlIs,
                                                myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                    myVideoUrlIs
                                                ),
                                                ".mp4"
                                            )
                                            dismissMyDialog()


                                            myVideoUrlIs = ""
                                        } else {
                                            myPhotoUrlIs =
                                                modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src

                                            DownloadFileMain.startDownloading(
                                                this@MainActivityNewUi,
                                                myPhotoUrlIs,
                                                myInstaUsername + iUtils.getImageFilenameFromURL(
                                                    myPhotoUrlIs
                                                ),
                                                ".png"
                                            )
                                            myPhotoUrlIs = ""
                                            dismissMyDialog()
                                            // etText.setText("")
                                        }
                                    }
                                } else {
                                    val isVideo =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video
                                    if (isVideo) {
                                        myVideoUrlIs =
                                            modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url


                                        DownloadFileMain.startDownloading(
                                            this@MainActivityNewUi,
                                            myVideoUrlIs,
                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                myVideoUrlIs
                                            ),
                                            ".mp4"
                                        )
                                        dismissMyDialog()
                                        myVideoUrlIs = ""
                                    } else {
                                        myPhotoUrlIs =
                                            modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src


                                        DownloadFileMain.startDownloading(
                                            this@MainActivityNewUi,
                                            myPhotoUrlIs,
                                            myInstaUsername + iUtils.getImageFilenameFromURL(
                                                myPhotoUrlIs
                                            ),
                                            ".png"
                                        )
                                        dismissMyDialog()
                                        myPhotoUrlIs = ""
                                    }
                                }


                            } else {
                                runOnUiThread {
                                    iUtils.ShowToastError(
                                        this@MainActivityNewUi,
                                        resources.getString(R.string.somthing),
                                    )
                                }

                                dismissMyDialog()
                            }


                        } catch (e: Exception) {
                            this@MainActivityNewUi.runOnUiThread {
                                progressDralogGenaratinglink.setMessage("Method 1 failed trying method 2")
                            }
                            downloadInstagramImageOrVideoResOkhttpM2(URL!!)

                        }

                    } else {
                        this@MainActivityNewUi.runOnUiThread {
                            progressDralogGenaratinglink.setMessage("Method 1 failed trying method 2")
                        }
                        downloadInstagramImageOrVideoResOkhttpM2(URL!!)
                    }


                } catch (e: Throwable) {
                    e.printStackTrace()
                    println("The request has failed " + e.message)
                    this@MainActivityNewUi.runOnUiThread {
                        progressDralogGenaratinglink.setMessage("Method 1 failed trying method 2")
                    }
                    downloadInstagramImageOrVideoResOkhttpM2(URL!!)
                }
            }
        }.start()
    }

    @Keep
    fun downloadInstagramImageOrVideoResOkhttpM2(URL: String?) {


        try {


            val cookieJar: ClearableCookieJar = PersistentCookieJar(
                SetCookieCache(),
                SharedPrefsCookiePersistor(this@MainActivityNewUi)
            )

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            // init OkHttpClient
            val client: OkHttpClient = OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(logging)
                .build()

            val request: Request = Request.Builder()
                .url(URL + "embed/captioned/")
                .method("GET", null)
                .build()
            val response = client.newCall(request).execute()

            val ss = response.body!!.string()
            var code = response.code
            if (!ss.contains("shortcode_media")) {
                code = 400
            }
            if (code == 200) {

                try {


                    val start = "window.__additionalDataLoaded("
                    val end = "}}});"
                    var dd: String =
                        ss.substring(ss.indexOf(start) + 38, ss.indexOf(end) + 3).trim()
                    dd = "{\"graphql\":$dd,\"showQRModal\":false\"}"

                    //  println("HttpResponse ddffd " + dd)
                    println("HttpResponse ddffd23 " + dd.substring(dd.length - 30))


                    val listType = object : TypeToken<ModelInstagramResponse?>() {}.type
                    val modelInstagramResponse: ModelInstagramResponse? =
                        GsonBuilder().create()
                            .fromJson<ModelInstagramResponse>(
                                dd,
                                listType
                            )


                    if (modelInstagramResponse != null) {


                        if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                            val modelGetEdgetoNode: ModelGetEdgetoNode =
                                modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children

                            val modelEdNodeArrayList: List<ModelEdNode> =
                                modelGetEdgetoNode.modelEdNodes
                            for (i in 0 until modelEdNodeArrayList.size) {
                                if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                    myVideoUrlIs =
                                        modelEdNodeArrayList[i].modelNode.video_url


                                    DownloadFileMain.startDownloading(
                                        this@MainActivityNewUi,
                                        myVideoUrlIs,
                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                            myVideoUrlIs
                                        ),
                                        ".mp4"
                                    )
                                    dismissMyDialog()


                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src

                                    DownloadFileMain.startDownloading(
                                        this@MainActivityNewUi,
                                        myPhotoUrlIs,
                                        myInstaUsername + iUtils.getImageFilenameFromURL(
                                            myPhotoUrlIs
                                        ),
                                        ".png"
                                    )
                                    myPhotoUrlIs = ""
                                    dismissMyDialog()
                                    // etText.setText("")
                                }
                            }
                        } else {
                            val isVideo =
                                modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video
                            if (isVideo) {
                                myVideoUrlIs =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url


                                DownloadFileMain.startDownloading(
                                    this@MainActivityNewUi,
                                    myVideoUrlIs,
                                    myInstaUsername + iUtils.getVideoFilenameFromURL(
                                        myVideoUrlIs
                                    ),
                                    ".mp4"
                                )
                                dismissMyDialog()
                                myVideoUrlIs = ""
                            } else {
                                myPhotoUrlIs =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src


                                DownloadFileMain.startDownloading(
                                    this@MainActivityNewUi,
                                    myPhotoUrlIs,
                                    myInstaUsername + iUtils.getImageFilenameFromURL(
                                        myPhotoUrlIs
                                    ),
                                    ".png"
                                )
                                dismissMyDialog()
                                myPhotoUrlIs = ""
                            }
                        }


                    } else {

                        runOnUiThread {
                            iUtils.ShowToastError(
                                this@MainActivityNewUi,
                                resources.getString(R.string.somthing),
                            )
                        }
                        dismissMyDialog()


                    }


                } catch (e: Exception) {

                    dismissMyDialog()
                    runOnUiThread {
                        iUtils.ShowToastError(
                            this@MainActivityNewUi,
                            resources.getString(R.string.somthing) + "",
                        )
                    }

                }

            } else {

                dismissMyDialog()
                runOnUiThread {
                    iUtils.ShowToastError(
                        this@MainActivityNewUi,
                        resources.getString(R.string.somthing) + "",
                    )
                }
            }


        } catch (e: Throwable) {
            e.printStackTrace()
            println("The request has failed " + e.message)
            dismissMyDialog()
            runOnUiThread {
                iUtils.ShowToastError(
                    this@MainActivityNewUi,
                    resources.getString(R.string.somthing) + "",
                )
            }

        }
    }


    private fun downloadInstagramImageOrVideo_tikinfApi(URL: String?) {
        AndroidNetworking.get("http://tikdd.infusiblecoder.com/ini/ilog.php?url=$URL")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    val myresws: String = response.toString()
                    println("myresponseis111 eeee $myresws")

                    try {
                        val listType: Type =
                            object : TypeToken<ModelInstaWithLogin?>() {}.type
                        val modelInstagramResponse: ModelInstaWithLogin = Gson().fromJson(
                            myresws,
                            listType
                        )
//                        System.out.println("workkkkk777 " + modelInstagramResponse.items.get(0).code)
                        val usernameis = modelInstagramResponse.items[0].user.username


                        if (modelInstagramResponse.items[0].mediaType == 8) {

                            val modelGetEdgetoNode = modelInstagramResponse.items[0]

                            val modelEdNodeArrayList: List<CarouselMedia> =
                                modelGetEdgetoNode.carouselMedia
                            for (i in 0 until modelEdNodeArrayList.size) {
                                if (modelEdNodeArrayList[i].mediaType == 2) {
                                    myVideoUrlIs =
                                        modelEdNodeArrayList[i].videoVersions[0].geturl()
                                    DownloadFileMain.startDownloading(
                                        this@MainActivityNewUi,
                                        myVideoUrlIs,
                                        usernameis + iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                        ".mp4"
                                    )


                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelEdNodeArrayList[i].imageVersions2.candidates[0]
                                            .geturl()
                                    DownloadFileMain.startDownloading(
                                        this@MainActivityNewUi,
                                        myPhotoUrlIs,
                                        usernameis + iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
                                        ".png"
                                    )

                                    myPhotoUrlIs = ""

                                    dismissMyDialog()

                                    // etText.setText("")
                                }
                            }
                        } else {
                            val modelGetEdgetoNode = modelInstagramResponse.items[0]
                            if (modelGetEdgetoNode.mediaType == 2) {
                                myVideoUrlIs =
                                    modelGetEdgetoNode.videoVersions[0].geturl()
                                DownloadFileMain.startDownloading(
                                    this@MainActivityNewUi,
                                    myVideoUrlIs,
                                    usernameis + iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                    ".mp4"
                                )

                                myVideoUrlIs = ""
                            } else {
                                myPhotoUrlIs =
                                    modelGetEdgetoNode.imageVersions2.candidates[0].geturl()
                                DownloadFileMain.startDownloading(
                                    this@MainActivityNewUi,
                                    myPhotoUrlIs,
                                    usernameis + iUtils.getVideoFilenameFromURL(myPhotoUrlIs),

                                    ".png"
                                )
                                dismissMyDialog()
                                myPhotoUrlIs = ""
                            }
                        }

                        dismissMyDialog()

                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()

                        println("myresponseis111 try exp " + e.message)

                        dismissMyDialog()
                        runOnUiThread {
                            iUtils.ShowToastError(
                                this@MainActivityNewUi,
                                resources.getString(R.string.somthing) + "",
                            )
                        }
                    }
                }

                override fun onError(error: ANError) {
                    println("myresponseis111 exp " + error.message)
                    dismissMyDialog()
                    runOnUiThread {
                        iUtils.ShowToastError(
                            this@MainActivityNewUi,
                            resources.getString(R.string.somthing) + "",
                        )
                    }
                }
            })


    }


    fun downloadInstagramImageOrVideodataExternalApi2(
        context: Context,
        URL: String?
    ) {
        object : Thread() {
            override fun run() {
                try {
                    Looper.prepare()
                    val client = OkHttpClient().newBuilder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build()

                    val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("link", URL.toString())
                        .addFormDataPart("downloader", "video")
                        .build()
                    val request: Request = Request.Builder()
                        .url("https://igdownloader.com/ajax")
                        .method("POST", body)
                        .addHeader(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36"
                        )
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .build()
                    val response = client.newCall(request).execute()
                    System.err.println("workkkkkkkkk 4 " + response.code)
                    if (response.code == 200) {
                        try {
                            val cv = JSONObject(response.body!!.string())

                            System.err.println("workkkkkkkkk 4 " + cv.getString("html"))

                            val mylis = iUtils.extractUrls(cv.getString("html"))

                            if (mylis != null && mylis.size > 0) {

                                for (i in 0 until mylis.size) {
                                    if (mylis[i].contains(".mp4")) {

                                        DownloadFileMain.startDownloading(
                                            this@MainActivityNewUi,
                                            mylis[i],
                                            "Instagram_" + iUtils.getVideoFilenameFromURL(mylis[i]),
                                            ".mp4"
                                        )

                                    } else if (mylis[i].contains(".jpg")) {

                                        DownloadFileMain.startDownloading(
                                            this@MainActivityNewUi,
                                            mylis[i],
                                            "Instagram_" + iUtils.getImageFilenameFromURL(mylis[i]),
                                            ".jpg"
                                        )

                                    }
                                }
                            }

                            dismissMyDialog()

                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()

                            println("myresponseis111 try exp " + e.message)

                            dismissMyDialog()
                            runOnUiThread {
                                iUtils.ShowToastError(
                                    this@MainActivityNewUi,
                                    resources.getString(R.string.somthing) + "",
                                )
                            }
                        }


                    } else {
                        runOnUiThread {
                            iUtils.ShowToastError(
                                this@MainActivityNewUi,
                                "Failed try again "
                            )
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        iUtils.ShowToastError(
                            this@MainActivityNewUi,
                            "Failed , Login and try again " + e.message,
                        )
                    }
                }
            }
        }.start()
        System.err.println("workkkkkkkkk 4 $URL")
    }


    private fun showAdmobAds_int_video() {
        if (Constants.show_Ads && !BuildConfig.ISPRO) {
            if (nn == "nnn") {
                if (rewardedInterstitialAd != null) {
                    rewardedInterstitialAd!!.show(
                        this@MainActivityNewUi
                    ) {
                        Log.i(ContentValues.TAG, "onUserEarnedReward " + it.amount)

                    }
                } else {
                    Log.i(ContentValues.TAG, "load int video failed;")

                }
            }
        }
    }

    private fun showAdmobAds() {
        if (Constants.show_Ads && !BuildConfig.ISPRO) {
            if (nn == "nnn") {
                if (AdsManager.status_AdmobBanner) {

                    MobileAds.initialize(
                        applicationContext
                    ) {
                        AdsManager.showAdmobInterstitialAd(
                            this@MainActivityNewUi,
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent()
                                    AdsManager.loadInterstitialAd(this@MainActivityNewUi)
                                }
                            })
                    }


                }
            }
        }
    }

    private fun verifyAppInstall(isinatll: Boolean) {
        try {
            println("myappisvalid $isinatll")
            if (!isinatll) {
                if (!isFinishing) {
                    AlertDialog.Builder(this@MainActivityNewUi)
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


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }


    private fun initIInAppBillingAcknologement() {
        println("mypurchase12 2 = ")
        billingClient =
            BillingClientSetup.getInstance(this@MainActivityNewUi) { _, list ->
                if (list != null) {
                    for (purchase in list) handlesAlreadyPurchased(purchase)
                }
            }
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                println("mypurchase 2 = ")

                try {
                    loadAllSubscribePackage()
                    billingClient!!.queryPurchasesAsync(
                        BillingClient.SkuType.SUBS
                    ) { billingResult1: BillingResult, purchases: List<Purchase> ->
                        println("mypurchase 6.5 = ")
                        if (purchases.size > 0) {
                            iUtils.isSubactive = true
                            binding.subscriptionFab.setVisibility(View.GONE)
                            for (purchase in purchases) handlesAlreadyPurchased(purchase)
                        } else {
                            iUtils.isSubactive = false
                            if (!Constants.show_subscription) {
                                binding.subscriptionFab.visibility = View.GONE
                            } else {
                                if (!BuildConfig.ISPRO) {
                                    binding.subscriptionFab.visibility = View.VISIBLE
                                } else {
                                    binding.subscriptionFab.visibility = View.GONE
                                }

                            }

                            println("mypurchase 4 = " + billingResult1.responseCode)
                            val prefs = getSharedPreferences(
                                "whatsapp_pref",
                                MODE_PRIVATE
                            ).edit()
                            prefs.putString("inappads", "nnn")
                            prefs.apply()

                        }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            override fun onBillingServiceDisconnected() {
//                Toast.makeText(MainActivity.this, "You are disconnected from Billing Service"
//                        , Toast.LENGTH_SHORT).show();
            }
        })
    }

    private fun loadAllSubscribePackage() {
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
                    println("mypurchase 0 = " + productDetailsList[0])
                } catch (ignored: java.lang.Exception) {
                }

            }
        } else {
//TODO
//            runOnUiThread {
//                iUtils.ShowToastError(
//                    this@MainActivityNewUi,
//                    resources.getString(R.string.billingnotready)
//                )
//            }
        }
    }

    private fun handlesAlreadyPurchased(purchases: Purchase) {
        if (purchases.purchaseState == Purchase.PurchaseState.PURCHASED) {
            iUtils.isSubactive = true
            binding.subscriptionFab.visibility = View.GONE
            val prefs = getSharedPreferences(
                "whatsapp_pref",
                MODE_PRIVATE
            ).edit()
            prefs.putString("inappads", "ppp")
            prefs.apply()
        }
    }

    fun dismissMyDialog() {
        try {

            if (!isFinishing && progressDralogGenaratinglink != null && progressDralogGenaratinglink.isShowing) {
                progressDralogGenaratinglink.dismiss()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Constants.iSAdminAttached) {
            SplashScreen.getExistingAdminPanelData(this@MainActivityNewUi)
        }
        doAllBioMetricTasks()
    }


    private fun doAllBioMetricTasks() {
        try {

            val biometricManager: BiometricManager =
                BiometricManager.from(this@MainActivityNewUi)
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
//                            this@MainActivityNewUi,
//                            "This device does not have a fingerprint sensor",
//                        )
//                    }
                }
                biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {

                    runOnUiThread {
                        iUtils.ShowToastError(
                            this@MainActivityNewUi,
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
//                            this@MainActivityNewUi,
//                            "Your device doesn't have fingerprint saved,please check your security settings",
//                        )
//                    }
                    val prefs = getSharedPreferences("whatsapp_pref", MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putBoolean("isBio", false)
                    editor.apply()
                }
            }


            val executor: Executor = ContextCompat.getMainExecutor(this@MainActivityNewUi)
            biometricPrompt =
                BiometricPrompt(
                    this@MainActivityNewUi,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                            runOnUiThread {
                                iUtils.ShowToastError(
                                    this@MainActivityNewUi,
                                    "Error in Authentication $errString"
                                )
                            }
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            startActivity(
                                Intent(
                                    this@MainActivityNewUi,
                                    GalleryActivityNewUi::class.java
                                )
                            )
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                        }
                    })

            promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Verify Identity")
                .setDescription("Use your fingerprint or device credentials to Access Gallery ")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                // .setNegativeButtonText("Cancel")
                .build()


        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }


}