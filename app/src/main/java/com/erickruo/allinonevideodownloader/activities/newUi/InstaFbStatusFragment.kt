/*
 * *
 *  * Created by Syed Usama Ahmad on 3/17/23, 11:37 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/17/23, 9:19 PM
 *
 */

@file:Suppress("DEPRECATION", "NAME_SHADOWING")


package com.erickruo.allinonevideodownloader.activities.newUi

import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.content.ContentValues.TAG
import android.os.*
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.*
import android.widget.FrameLayout
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.ParsedRequestListener
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.MobileAds
import com.erickruo.allinonevideodownloader.BuildConfig
import com.erickruo.allinonevideodownloader.R
import com.erickruo.allinonevideodownloader.activities.*
import com.erickruo.allinonevideodownloader.adapters.HighlightsUsersListAdapter
import com.erickruo.allinonevideodownloader.adapters.ListAllStoriesOfUserAdapter
import com.erickruo.allinonevideodownloader.adapters.StoryUsersListAdapter
import com.erickruo.allinonevideodownloader.databinding.FragmentInstaFbBinding
import com.erickruo.allinonevideodownloader.facebookstorysaver.FacebookPrivateWebview
import com.erickruo.allinonevideodownloader.facebookstorysaver.fbadapters.FBstoryAdapter
import com.erickruo.allinonevideodownloader.facebookstorysaver.fbadapters.FBuserRecyclerAdapter
import com.erickruo.allinonevideodownloader.facebookstorysaver.fbmodels.FBStory
import com.erickruo.allinonevideodownloader.facebookstorysaver.fbmodels.FBUserData
import com.erickruo.allinonevideodownloader.facebookstorysaver.fbutils.FBhelper
import com.erickruo.allinonevideodownloader.facebookstorysaver.fbutils.Facebookprefloader
import com.erickruo.allinonevideodownloader.facebookstorysaver.fbutils.LoginWithFB
import com.erickruo.allinonevideodownloader.interfaces.HighlightsListInStoryListner
import com.erickruo.allinonevideodownloader.interfaces.UserListInStoryListner
import com.erickruo.allinonevideodownloader.models.storymodels.*
import com.erickruo.allinonevideodownloader.utils.*
import com.erickruo.allinonevideodownloader.utils.iUtils.ShowToast
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import org.json.JSONObject
import java.util.*


@Keep
class InstaFbStatusFragment : Fragment(), UserListInStoryListner, HighlightsListInStoryListner {
    private var userIDInsta: String = ""
    private var myselectedActivity: FragmentActivity? = null
    private var binding: FragmentInstaFbBinding? = null
    private var nn: String? = "nnn"
    private var fbstory_adapter: FBstoryAdapter? = null
    private var fbuserlistadapter: FBuserRecyclerAdapter? = null
    private var listAllStoriesOfUserAdapter: ListAllStoriesOfUserAdapter? = null
    private var listAllHighlightsOfUserAdapter: ListAllStoriesOfUserAdapter? = null
    private var storyUsersListAdapter: StoryUsersListAdapter? = null
    private var highlightsUsersListAdapter: HighlightsUsersListAdapter? = null
    private var NotifyID = 1001
    private var csRunning = false
    private var idCookieDialog = false
    lateinit var webViewInsta: WebView
    lateinit var progressDralogGenaratinglink: ProgressDialog
    private lateinit var prefEditor: SharedPreferences.Editor
    lateinit var pref: SharedPreferences
    var myVideoUrlIs: String? = null
    var myInstaUsername: String? = ""
    var myPhotoUrlIs: String? = null
    var fbstorieslist: List<FBStory> = ArrayList()
    var fb_dtsg = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Remember add this line
        retainInstance = true
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInstaFbBinding.inflate(inflater, container, false)

        setActivityAfterAttached()

        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null && isAdded) {

            //hide keyboard
            hideKeyboard(requireActivity())

            nn = SharedPrefsMainApp(myselectedActivity).preferencE_inappads
            configureRxJavaErrorHandler()

            progressDralogGenaratinglink = ProgressDialog(myselectedActivity!!)
            progressDralogGenaratinglink.setMessage(resources.getString(R.string.genarating_download_link))
            progressDralogGenaratinglink.setCancelable(false)




            binding!!.privatefblayout.setOnClickListener {
                startActivity(Intent(myselectedActivity, FacebookPrivateWebview::class.java))
            }

            binding!!.btnOpeninstafollowers.setOnClickListener {
                startActivity(Intent(myselectedActivity, InstagramFollowersList::class.java))
            }






            checkForLoginCheckboxInstaAndFacebook()


            binding!!.loadHighlightsBtn.setOnClickListener {
                loadHighLightsBtnClick()
            }


            binding!!.chkdownloadPrivateMedia.setOnClickListener {
                val sharedPrefsForInstagram = SharedPrefsForInstagram(myselectedActivity)
                val map = sharedPrefsForInstagram.preference
                if (map != null && map.preferencE_ISINSTAGRAMLOGEDIN != "true") {
                    val intent = Intent(
                        myselectedActivity,
                        InstagramLoginActivity::class.java
                    )
                    startActivityForResult(intent, 200)
                } else {
                    if (!myselectedActivity!!.isFinishing) {
                        val ab = AlertDialog.Builder(
                            myselectedActivity!!
                        )
                        ab.setPositiveButton(
                            resources.getString(R.string.yes)
                        ) { p0, _ ->
                            val sharedPrefsForInstagram2 =
                                SharedPrefsForInstagram(myselectedActivity)
                            val map2 =
                                sharedPrefsForInstagram2.preference
                            if (sharedPrefsForInstagram2.preference != null) {
                                sharedPrefsForInstagram2.clearSharePrefs()
                                binding!!.linlayoutInstaStories.visibility = View.GONE
                                if (map2 != null && map2.preferencE_ISINSTAGRAMLOGEDIN == "true") {
                                    binding!!.chkdownloadPrivateMedia.isChecked = true
                                } else {
                                    binding!!.chkdownloadPrivateMedia.isChecked = false
                                    binding!!.recUserList.visibility = View.GONE
                                    binding!!.recStoriesList.visibility = View.GONE
                                }
                                p0?.dismiss()
                                binding!!.chkdownloadPrivateMedia.isChecked = false
                            } else {
                                sharedPrefsForInstagram2.clearSharePrefs()
                            }
                        }
                        ab.setNegativeButton(
                            resources.getString(R.string.cancel)
                        ) { dialog, _ ->
                            dialog.cancel()
                            val asfd: Boolean = binding!!.chkdownloadPrivateMedia.isChecked
                            binding!!.chkdownloadPrivateMedia.isChecked = !asfd
                        }
                        val alert = ab.create()
                        alert.setTitle(getString(R.string.noprivatedownload))
                        alert.setMessage(getString(R.string.no_private_insta))
                        alert.show()
                    }
                }
            }


            binding!!.chkdownloadFbstories.setOnClickListener {
                val sharedPrefsForfb = Facebookprefloader(myselectedActivity)
                Log.d(TAG, "Inte 0")
                val LoadPrefString = sharedPrefsForfb.LoadPrefString()
                val logedin = LoadPrefString.getFb_pref_isloggedin()
                if (logedin != "true" && logedin != "") {
                    val intent = Intent(
                        myselectedActivity,
                        LoginWithFB::class.java
                    )
                    startActivityForResult(intent, 201)
                } else {
                    if (!myselectedActivity!!.isFinishing) {
                        val ab = AlertDialog.Builder(
                            myselectedActivity!!
                        )
                        ab.setPositiveButton(
                            resources.getString(R.string.cancel)
                        ) { p0, _ ->
                            p0?.cancel()
                            val LoadPrefString = sharedPrefsForfb.LoadPrefString()
                            val logedin = LoadPrefString.getFb_pref_isloggedin()
                            if (logedin != null && logedin != "") {

                                binding!!.chkdownloadFbstories.isChecked = logedin == "true"
                            } else {
                                sharedPrefsForfb.MakePrefEmpty()
                            }
                        }
                        ab.setNegativeButton(
                            resources.getString(R.string.yes)
                        ) { dialog, _ ->
                            dialog.cancel()

                            binding!!.chkdownloadFbstories.isChecked = false
                            binding!!.recUserFblist.visibility = View.GONE
                            binding!!.recStoriesFblist.visibility = View.GONE
                            sharedPrefsForfb.MakePrefEmpty()
                            logout()
                        }
                        val alert = ab.create()
                        alert.setTitle(getString(R.string.fb_story))
                        alert.setMessage(getString(R.string.no_fb_story))
                        alert.show()
                    }
                }
            }

            binding!!.txtSearchClick.setOnClickListener {
                try {
                    binding!!.searchStory.onActionViewExpanded()
                    binding!!.txtSearchClick.visibility = View.INVISIBLE
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


            binding!!.searchStory.setOnCloseListener(object :
                SearchView.OnCloseListener, android.widget.SearchView.OnCloseListener {
                override fun onClose(): Boolean {
                    try {
                        storyUsersListAdapter!!.filter.filter("")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return true
                }


            })

            binding!!.searchStory.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return try {
                        if (newText != null && storyUsersListAdapter != null) {
                            storyUsersListAdapter!!.filter.filter(newText)
                        }
                        true
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ShowToast(myselectedActivity, getString(R.string.error_occ))
                        true
                    }
                }

            })



            binding!!.txtSearchClickFbstory.setOnClickListener {

                binding!!.searchFbstory.onActionViewExpanded()
                binding!!.txtSearchClickFbstory.visibility = View.INVISIBLE

            }

            binding!!.searchFbstory.setOnCloseListener(object :
                SearchView.OnCloseListener, android.widget.SearchView.OnCloseListener {
                override fun onClose(): Boolean {
                    try {
                        if (fbuserlistadapter != null) {
                            fbuserlistadapter!!.filter.filter("")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return true
                }


            })

            binding!!.searchFbstory.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return try {
                        if (newText != null && fbuserlistadapter != null) {
                            fbuserlistadapter!!.filter.filter(newText)
                        }

                        true
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ShowToast(myselectedActivity, getString(R.string.error_occ))
                        true
                    }
                }
            })




            try {
                if (Constants.show_Ads && !BuildConfig.ISPRO && nn == "nnn") {


                    MobileAds.initialize(
                        requireActivity()
                    ) {

                        AdsManager.loadInterstitialAd(activity as Activity?)
                        AdsManager.loadVideoAdAdmob(activity as Activity?,
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent()

                                }
                            })

                        LoadAdTaskDownload(
                            myselectedActivity!!,
                            binding!!.flAdplaceholder
                        ).execute()

                    }


                } else {
                    binding!!.flAdplaceholder.visibility = View.GONE
                }
            } catch (_: Exception) {
            }

        }

    }

    private fun checkForLoginCheckboxInstaAndFacebook() {

        try {
            val sharedPrefsFor = SharedPrefsForInstagram(myselectedActivity)
            if (sharedPrefsFor.preference.preferencE_SESSIONID == "") {
                sharedPrefsFor.clearSharePrefs()
            }
            val map = sharedPrefsFor.preference
            if (map != null) {
                if (map.preferencE_ISINSTAGRAMLOGEDIN == "true") {
                    if(!iUtils.isCookieRefreshed){
                        iUtils.isCookieRefreshed = true
                        val intent = Intent(
                            myselectedActivity,
                            InstagramLoginActivity::class.java
                        )
                        startActivityForResult(intent, 200)
                    }
                    binding!!.chkdownloadPrivateMedia.isChecked = true
                    binding!!.linlayoutInstaStories.visibility = View.VISIBLE
                    getallstoriesapicall()
                } else {
                    binding!!.chkdownloadPrivateMedia.isChecked = false
                    binding!!.linlayoutInstaStories.visibility = View.GONE
                }
            }

            val sharedPrefsForfb = Facebookprefloader(myselectedActivity)
            if (sharedPrefsForfb.LoadPrefString().getFb_pref_key() == "") {
                sharedPrefsForfb.MakePrefEmpty()
            }
            val LoadPrefString = sharedPrefsForfb.LoadPrefString()

            val logedin = LoadPrefString.getFb_pref_isloggedin()
            if (logedin != null && logedin != "") {
                println("mydataiiii=$logedin")
                if (logedin == "true") {
                    println("meditating=$logedin")
                    binding!!.chkdownloadFbstories.isChecked = true
                    binding!!.linlayoutFbStories.visibility = View.VISIBLE
                    loadFBUserDataRxjava()
                } else {
                    println("modalities=$logedin")
                    binding!!.chkdownloadFbstories.isChecked = false
                    binding!!.linlayoutFbStories.visibility = View.GONE
                }
            } else {
                binding!!.chkdownloadFbstories.isChecked = false
                binding!!.linlayoutFbStories.visibility = View.GONE
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    class LoadAdTaskDownload(private val context: Activity, private val frameLayout: FrameLayout) :
        AsyncTask<Void, Void, Unit>() {

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void?) {
            AdsManager.loadAdmobNativeAd(context, frameLayout)
        }
    }


    fun dismissProgressInstagramAndShowMessage() {
        try {
//            ShowToast(myselectedActivity, getString(R.string.cookiesnotvalid))


            if (isAdded && !idCookieDialog) {
                idCookieDialog = true
                val ab = AlertDialog.Builder(
                    requireActivity()
                )
                ab.setPositiveButton(
                    resources.getString(R.string.yes)
                ) { p0, _ ->
                    p0.cancel()
                    val intent = Intent(
                        activity,
                        InstagramLoginActivity::class.java
                    )
                    startActivityForResult(intent, 200)
                }
                ab.setNegativeButton(
                    resources.getString(R.string.cancel)
                ) { dialog, _ ->
                    dialog.cancel()
                }
                val alert = ab.create()
                alert.setMessage(getString(R.string.cookiesnotvalid))
                alert.show()
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun dismissMyDialogFrag() {
        try {
            if (activity != null && isAdded) {
                myselectedActivity?.runOnUiThread {
                    if (!(myselectedActivity as Activity).isFinishing && progressDralogGenaratinglink != null && progressDralogGenaratinglink.isShowing) {
                        progressDralogGenaratinglink.dismiss()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun dismissMyDialogErrorToastFrag() {
        try {
            if (activity != null && isAdded) {
                myselectedActivity?.runOnUiThread {
                    if (!(myselectedActivity as Activity).isFinishing && progressDralogGenaratinglink != null && progressDralogGenaratinglink.isShowing) {
                        progressDralogGenaratinglink.dismiss()
                        myselectedActivity!!.runOnUiThread {
                            iUtils.ShowToastError(
                                myselectedActivity,
                                myselectedActivity!!.resources.getString(R.string.somthing)
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun loadFBUserDataRxjava() {
        Completable.fromAction {
            this.loadUserData()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    // it worked
                }

                override fun onError(e: Throwable) {
                    if (BuildConfig.DEBUG) {
                        Log.e(
                            "Failed to get fb data",
                            "failed to ",
                            e
                        )

                    }
                }
            })


    }


    //TODO Fix Delay IN FB
    private fun loadUserData() {

        myselectedActivity!!.runOnUiThread {
            binding!!.progressLoadingFbbar.visibility = View.VISIBLE
        }
        val cookie = CookieManager.getInstance().getCookie("https://www.facebook.com")
        if (!FBhelper.valadateCooki(cookie)) {
            Log.e("tag2", "cookie is not valid")
            requireActivity().runOnUiThread {
                ShowToast(myselectedActivity, getString(R.string.cookiesnotvalid))
            }
            return
        }
        val sharedPrefsForfb = Facebookprefloader(myselectedActivity)
        val LoadPrefStringol = sharedPrefsForfb.LoadPrefString()
        val LoadPrefString = LoadPrefStringol.getFb_pref_key()
        //     = sharedPrefsForfb.LoadPrefString( "key")
        Log.e("tag299", "cookie is not valid $LoadPrefString")
        Log.e("tag2", "cookie is:$cookie")
        Log.e("tag2", "key is:$LoadPrefString")
        Log.e("tag2", "start getting user data")

        try {

            val getnewfbdtsg: String = iUtils.GetFBfb_dtsg(requireActivity())

            if (getnewfbdtsg != null && !getnewfbdtsg.equals("")) {
                fb_dtsg = getnewfbdtsg
                println("getnewfbdtsg = " + fb_dtsg)
            } else {
                fb_dtsg = LoadPrefString
            }
        } catch (e: Exception) {
            fb_dtsg = LoadPrefString
            e.printStackTrace()
        }

        AndroidNetworking.post("https://www.facebook.com/api/graphql/")
            .addHeaders("accept-language", "en,en-US;q=0.9,fr;q=0.8,ar;q=0.7")
            .addHeaders("cookie", cookie)
            .addHeaders(
                "user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36"
            )
            .addHeaders("Content-Type", "application/json")
            .addBodyParameter(
                "fb_dtsg", fb_dtsg
            )
            .addBodyParameter(
                "variables",
                "{\"bucketsCount\":200,\"initialBucketID\":null,\"pinnedIDs\":[\"\"],\"scale\":3}"
            )
            .addBodyParameter("doc_id", "2893638314007950")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e("tag55", response.toString())
                    val parse = FBUserData.parse(response.toString())
                    if (parse != null) {
                        Log.e("tag1", "data succeed")
                        showFBSTORYData(parse)
                    }
                    myselectedActivity!!.runOnUiThread {
                        binding!!.progressLoadingFbbar.visibility = View.GONE
                    }
                }

                override fun onError(error: ANError) {
                    Log.e("tag1", "data faild$error")
                    myselectedActivity!!.runOnUiThread {
                        binding!!.progressLoadingFbbar.visibility = View.GONE
                    }
                }
            })
    }

    private fun loadFriendStories(str: String) {
        myselectedActivity!!.runOnUiThread {
            binding!!.progressLoadingFbbar.visibility = View.VISIBLE
        }
        val cookie = CookieManager.getInstance().getCookie("https://www.facebook.com")
        if (!FBhelper.valadateCooki(cookie)) {
            Log.e("tag2", "cookie is not valid")

            return
        }
        val sharedPrefsForfb = Facebookprefloader(myselectedActivity!!)
        val LoadPrefStringol = sharedPrefsForfb.LoadPrefString()
        val LoadPrefString = LoadPrefStringol.getFb_pref_key()
        Log.e("tag2", "cookie is:$cookie")
        Log.e("tag2", "key is:$LoadPrefString")
        Log.e("tag2", "start getting user data")



        AndroidNetworking.post("https://www.facebook.com/api/graphql/")
            .addHeaders("accept-language", "en,en-US;q=0.9,fr;q=0.8,ar;q=0.7")
            .addHeaders("cookie", cookie)
            .addHeaders(
                "user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36"
            )
            .addHeaders("Content-Type", "application/json")
            .addBodyParameter("fb_dtsg", fb_dtsg)
            .addBodyParameter(
                "variables",
                "{\"bucketID\":\"$str\",\"initialBucketID\":\"$str\",\"initialLoad\":false,\"scale\":5}"
            )
            .addBodyParameter("doc_id", "2558148157622405")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        Log.e("tag55", response.toString())
                        fbstorieslist = FBStory.parseBulk(response.toString())
                        fbstory_adapter = FBstoryAdapter(
                            myselectedActivity!!,
                            fbstorieslist
                        )
                        binding!!.recStoriesFblist.layoutManager =
                            GridLayoutManager(myselectedActivity, 3)
                        binding!!.recStoriesFblist.adapter = fbstory_adapter
                        binding!!.progressLoadingFbbar.visibility = View.GONE
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ShowToast(myselectedActivity!!, "Failed to load stories")
                        binding!!.progressLoadingFbbar.visibility = View.GONE
                    }
                }

                override fun onError(error: ANError) {
                    ShowToast(myselectedActivity!!, "Failed to load stories")
                    Log.e("tag1", "data faild$error")
                    binding!!.progressLoadingFbbar.visibility = View.GONE
                }
            })
    }

    override fun onResume() {
        super.onResume()

        if (activity != null && isAdded) {

            checkForLoginCheckboxInstaAndFacebook()

        }
        Log.e("Frontales", "resume")
    }


    fun showFBSTORYData(FBUserData: FBUserData) {
        try {
            binding!!.recUserFblist.layoutManager = LinearLayoutManager(
                myselectedActivity!!,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            this.fbuserlistadapter =
                FBuserRecyclerAdapter(
                    myselectedActivity!!,
                    FBUserData.friends
                ) { id: String? ->
                    loadFriendStories(id!!)
                }
            binding!!.recUserFblist.adapter = this.fbuserlistadapter
            binding!!.progressLoadingFbbar.visibility = View.GONE
        } catch (_: Exception) {
        }
    }

    private fun logout() {
        CookieManager.getInstance().removeAllCookies(null as ValueCallback<Boolean>?)
        CookieManager.getInstance().flush()
        val sharedPrefsForfb = Facebookprefloader(myselectedActivity!!)
        sharedPrefsForfb.MakePrefEmpty()
    }


    private fun setActivityAfterAttached() {
        try {
            if (activity != null && isAdded) {
                myselectedActivity = activity
            }
        } catch (e: Exception) {
            myselectedActivity = requireActivity()
            e.printStackTrace()
        }
    }


    @Keep
    private fun callHighlightsDetailApi(reelId: String) {
        try {
            binding!!.progressLoadingBar.visibility = View.VISIBLE

            val sharedPrefsFor = SharedPrefsForInstagram(myselectedActivity!!)
            val map = sharedPrefsFor.preference
            if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != "") {


                Completable.fromAction {
//                    this.getFullDetailsOfClickedHighlights(
//                        reelId,
//                        "ds_user_id=" + map.preferencE_USERID
//                            .toString() + "; sessionid=" + map.preferencE_SESSIONID
//                    )

                    this.getFullDetailsOfClickedHighlights(
                        reelId,
                        map.preferencE_COOKIES
                    )
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : DisposableCompletableObserver() {
                        override fun onComplete() {
                            // it worked
                        }

                        override fun onError(e: Throwable) {
                            if (BuildConfig.DEBUG) {
                                Log.e(
                                    "Failed to get stories",
                                    "failed to ",
                                    e
                                )

                            }
                        }
                    })


            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            dismissMyDialogFrag()
            System.err.println("workkkkkkkkk 5")
            ShowToast(myselectedActivity!!, getString(R.string.error_occ))
        }
    }


    private fun loadHighLightsBtnClick() {
        try {
            if (!TextUtils.isEmpty(this.userIDInsta)) {
                binding!!.progressLoadingBar.visibility = View.VISIBLE
                binding!!.recHighlightsStoriesList.visibility = View.GONE
                binding!!.recUserHighlightsList.visibility = View.GONE
                val sharedPrefsFor = SharedPrefsForInstagram(myselectedActivity!!)
                val map = sharedPrefsFor.preference
                if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != "") {


                    Completable.fromAction {
//                        this.getAllHighlights(
//                            userIDInsta,
//                            "ds_user_id=" + map.preferencE_USERID
//                                .toString() + "; sessionid=" + map.preferencE_SESSIONID
//                        )

                        this.getAllHighlights(
                            userIDInsta,
                            map.preferencE_COOKIES
                        )
                    }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : DisposableCompletableObserver() {
                            override fun onComplete() {
                                // it worked
                                userIDInsta = ""
                            }

                            override fun onError(e: Throwable) {
                                userIDInsta = ""
                                if (BuildConfig.DEBUG) {
                                    Log.e(
                                        "Failed to get stories",
                                        "failed to ",
                                        e
                                    )

                                }
                            }
                        })


                }

            } else {
                ShowToast(myselectedActivity!!, getString(R.string.taponuserfirst))
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            dismissMyDialogFrag()
            System.err.println("workkkkkkkkk 5")
            ShowToast(myselectedActivity!!, getString(R.string.error_occ))
        }
    }

    @Keep
    private fun callStoriesDetailApi(reelId: String, UserId: String) {
        try {
            binding!!.progressLoadingBar.visibility = View.VISIBLE
            binding!!.recHighlightsStoriesList.visibility = View.GONE
            binding!!.recUserHighlightsList.visibility = View.GONE
            val sharedPrefsFor = SharedPrefsForInstagram(myselectedActivity!!)
            val map = sharedPrefsFor.preference
            if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != "") {


                Completable.fromAction {
//                    this.getFullDetailsOfClickedFeed(
//                        reelId,
//                        "ds_user_id=" + map.preferencE_USERID
//                            .toString() + "; sessionid=" + map.preferencE_SESSIONID
//                    )
                    this.getFullDetailsOfClickedFeed(
                        reelId,
                        map.preferencE_COOKIES
                    )
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : DisposableCompletableObserver() {
                        override fun onComplete() {
                            // it worked
                        }

                        override fun onError(e: Throwable) {
                            if (BuildConfig.DEBUG) {
                                Log.e(
                                    "Failed to get stories",
                                    "failed to ",
                                    e
                                )

                            }
                        }
                    })


            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            dismissMyDialogFrag()
            System.err.println("workkkkkkkkk 5")
            ShowToast(myselectedActivity!!, getString(R.string.error_occ))
        }
    }

    private fun configureRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler { e: Throwable ->
            if (e is UndeliverableException) {
                // As UndeliverableException is a wrapper, get the cause of it to get the "real" exception
//                e = e.cause!!
            }
            if (e is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
            Log.e(
                "configureRxJavaErrorHandler",
                "Undeliverable exception received, not sure what to do",
                e
            )
        }
    }

    @Keep
    private fun getallstoriesapicall() {
        try {
            binding!!.progressLoadingBar.visibility = View.VISIBLE

            val sharedPrefsFor = SharedPrefsForInstagram(myselectedActivity!!)
            val map = sharedPrefsFor.preference
            if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != "") {


                Completable.fromAction {
//                    this.getallStories(
//                        "ds_user_id=" + map.preferencE_USERID
//                            .toString() + "; sessionid=" + map.preferencE_SESSIONID
//                    )
                    println("preferencE_COOKIES ="+ map.preferencE_COOKIES)
                    this.getallStories(
                        map.preferencE_COOKIES
                    )
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : DisposableCompletableObserver() {
                        override fun onComplete() {
                            // it worked
                        }

                        override fun onError(e: Throwable) {
                            if (BuildConfig.DEBUG) {
                                Log.e(
                                    "Failed to get stories",
                                    "failed to ",
                                    e
                                )

                            }
                        }
                    })


            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @Keep
    @SuppressLint("NotifyDataSetChanged")
    fun getallStories(Cookie: String?) {
        var Cookie = Cookie
        if (TextUtils.isEmpty(Cookie)) {
            Cookie = ""
        }
        println("mycookies are = $Cookie")

        AndroidNetworking.get("https://i.instagram.com/api/v1/feed/reels_tray/")
            .setPriority(Priority.LOW)
            .addHeaders("Cookie", Cookie)
            .addHeaders(
                "User-Agent",
                "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+"
            )
            .build()
            .getAsObject(
                InstaStoryModelClass::class.java,
                object : ParsedRequestListener<InstaStoryModelClass> {

                    override fun onResponse(response: InstaStoryModelClass) {
                        try {
                            println("response1122334455_story:  " + response.tray)
                            binding!!.recUserList.visibility = View.VISIBLE
                            binding!!.progressLoadingBar.visibility = View.GONE
                            storyUsersListAdapter = StoryUsersListAdapter(
                                myselectedActivity!!,
                                response.tray, this@InstaFbStatusFragment
                            )
                            val linearLayoutManager =
                                LinearLayoutManager(
                                    myselectedActivity,
                                    RecyclerView.HORIZONTAL,
                                    false
                                )

                            binding!!.recUserList.layoutManager = linearLayoutManager
                            binding!!.recUserList.adapter = storyUsersListAdapter
                            storyUsersListAdapter!!.notifyDataSetChanged()
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            println("response1122334455_storyERROR getallStories:  " + e.message)
                            binding!!.progressLoadingBar.visibility = View.GONE
                        }

                    }

                    override fun onError(anError: ANError) {
                        anError.printStackTrace()
                        println("response1122334455_storyERROR:getallStories1  " + anError.message)
                        binding!!.progressLoadingBar.visibility = View.GONE
                        dismissProgressInstagramAndShowMessage()
                    }
                })


    }


    @Keep
    @SuppressLint("NotifyDataSetChanged")
    fun getAllHighlights(userid: String, Cookie: String?) {
        var Cookie = Cookie
        if (TextUtils.isEmpty(Cookie)) {
            Cookie = ""
        }
        println("mycookies are = $Cookie")

        AndroidNetworking.get("https://i.instagram.com/api/v1/highlights/$userid/highlights_tray/")
            .setPriority(Priority.LOW)
            .addHeaders("Cookie", Cookie)
            .addHeaders(
                "User-Agent",
                "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+"
            )
            .build()
            .getAsObject(
                InstaHIghlightModelClass::class.java,
                object : ParsedRequestListener<InstaHIghlightModelClass> {

                    override fun onResponse(response: InstaHIghlightModelClass) {
                        try {
//                            println("response1122334455_story:  " + response.tray)
                            binding!!.recUserHighlightsList.visibility = View.VISIBLE
                            binding!!.progressLoadingBar.visibility = View.GONE
                            highlightsUsersListAdapter = HighlightsUsersListAdapter(
                                myselectedActivity!!,
                                response.highlightsTray,
                                this@InstaFbStatusFragment
                            )
                            val linearLayoutManager =
                                LinearLayoutManager(
                                    myselectedActivity,
                                    RecyclerView.HORIZONTAL,
                                    false
                                )

                            binding!!.recUserHighlightsList.layoutManager = linearLayoutManager
                            binding!!.recUserHighlightsList.adapter = highlightsUsersListAdapter
                            highlightsUsersListAdapter!!.notifyDataSetChanged()
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            println("response1122334455_storyERROR: getAllHighlights " + e.message)
                            binding!!.progressLoadingBar.visibility = View.GONE
                        }

                    }

                    override fun onError(anError: ANError) {
                        anError.printStackTrace()
                        println("response1122334455_storyERROR:getAllHighlights1  " + anError.message)
                        binding!!.progressLoadingBar.visibility = View.GONE
//                        dismissProgressInstagramAndShowMessage()
                    }
                })


    }


    @Keep
    fun getFullDetailsOfClickedFeed(reel_id: String, Cookie: String?) {
        AndroidNetworking.get("https://i.instagram.com/api/v1/feed/reels_media/?reel_ids=$reel_id")
            .setPriority(Priority.LOW)
            .addHeaders("Cookie", Cookie)
            .addHeaders(
                "User-Agent",
                "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+"
            )
            .build()
            .getAsObject(
                ReelsMedia::class.java,
                object : ParsedRequestListener<ReelsMedia> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(response: ReelsMedia) {
                        try {
                            binding!!.recUserList.visibility = View.VISIBLE
                            binding!!.progressLoadingBar.visibility = View.GONE
                            println("response1122334455_fulldetails:   ${response.status}")

                            if (response.reelsMedia[0].items.size == 0) {
                                ShowToast(
                                    myselectedActivity!!,
                                    getString(R.string.nostoryfound)
                                )
                            }

                            listAllStoriesOfUserAdapter = ListAllStoriesOfUserAdapter(
                                myselectedActivity!!,
                                response.reelsMedia[0].items
                            )
                            binding!!.recStoriesList.visibility = View.VISIBLE

                            val gridLayoutManager = GridLayoutManager(myselectedActivity, 3)

                            binding!!.recStoriesList.layoutManager = gridLayoutManager
                            binding!!.recStoriesList.isNestedScrollingEnabled = true
                            binding!!.recStoriesList.adapter = listAllStoriesOfUserAdapter
                            listAllStoriesOfUserAdapter!!.notifyDataSetChanged()
                        } catch (e: java.lang.Exception) {
                            binding!!.recStoriesList.visibility = View.GONE
                            e.printStackTrace()
                            binding!!.progressLoadingBar.visibility = View.GONE
                            ShowToast(myselectedActivity!!, getString(R.string.nostoryfound))
                        }
                    }

                    override fun onError(anError: ANError) {
                        anError.printStackTrace()
                        println("response1122334455_storyERROR:getFullDetailsOfClickedFeed  " + anError.message)
                        binding!!.progressLoadingBar.visibility = View.GONE
                        dismissProgressInstagramAndShowMessage()
                    }
                })
    }

//TODO fix issue of
//   D/View: [ANR Warning]onMeasure time too long, this =androidx.recyclerview.widget.RecyclerView{dbfd340 VFED..... ......ID 0,1515-1008,9159 #7f0a028f app:id/rec_highlights_stories_list}time =451 ms
//    D/View: [ANR Warning]onMeasure time too long, this =android.widget.LinearLayout{8aa279 V.E...... ......ID 0,660-1036,9847 #7f0a01ca app:id/linlayout_insta_stories}time =451 ms
//    D/View: [ANR Warning]onMeasure time too long, this =android.widget.LinearLayout{b1e3bbe V.E...... ......ID 0,0-1036,9847}time =451 ms
//    D/View: [ANR Warning]onMeasure time too long, this =androidx.cardview.widget.CardView{507841f V.E...... ......ID 22,1916-1058,11763}time =451 ms
//    D/View: [ANR Warning]onMeasure time too long, this =android.widget.LinearLayout{d6ee36c VFE...... ......ID 0,0-1080,14073 #7f0a00db app:id/clMain}time =451 ms
//    D/View: [ANR Warning]onMeasure time too long, this =android.widget.ScrollView{309ed35 VFED.V... ......ID 0,0-1080,1649}time =451 ms
//    D/View: [ANR Warning]onMeasure time too long, this =android.widget.FrameLayout{d7e55ca V.E...... ......ID 0,0-1080,1649 #4}time =451 ms
//    D/View: [ANR Warning]onLayout time too long, this =androidx.viewpager2.widget.ViewPager2$RecyclerViewImpl{deb4a1d VFED..... ......ID 0,0-1080,1649 #1}time =458 ms
//    D/View: [ANR Warning]onLayout time too long, this =androidx.viewpager2.widget.ViewPager2{d996cf V.E...... ......ID 0,165-1080,1814 #7f0a0397 app:id/viewpager}time =458 ms
//    D/View: [ANR Warning]onLayout time too long, this =androidx.coordinatorlayout.widget.CoordinatorLayout{d319492 V.E...... ......ID 0,0-1080,1814}time =458 ms
//    D/View: [ANR Warning]onLayout time too long, this =android.widget.RelativeLayout{44cee63 V.E...... ......ID 0,0-1080,1980}time =458 ms
//    D/View: [ANR Warning]onLayout time too long, this =androidx.appcompat.widget.ContentFrameLayout{8f5da60 V.E...... ......ID 0,154-1080,2134 #1020002 android:id/content}time =458 ms
//    D/View: [ANR Warning]onLayout time too long, this =androidx.appcompat.widget.ActionBarOverlayLayout{f776b19 V.E...... ......ID 0,0-1080,2134 #7f0a0103 app:id/decor_content_parent}time =459 ms
//    D/View: [ANR Warning]onLayout time too long, this =android.widget.FrameLayout{6d067de V.E...... ......ID 0,76-1080,2210}time =459 ms
//    D/View: [ANR Warning]onLayout time too long, this =android.widget.LinearLayout{a160dbf V.E...... ......ID 0,0-1080,2210}time =459 ms
//    D/View: [ANR Warning]onLayout time too long, this =DecorView@8468756[MainActivity]time =459 ms

    @Keep
    fun getFullDetailsOfClickedHighlights(reel_id: String, Cookie: String?) {

        val reel_id1 = reel_id.replace(":", "%3A")
        println("response1122334455_fulldetails_highlights0:$reel_id1")

        val newurl = "https://i.instagram.com/api/v1/feed/reels_media/?reel_ids=$reel_id1"

        AndroidNetworking.get(newurl)
            .setPriority(Priority.LOW)
            .addHeaders("Cookie", Cookie)
            .addHeaders(
                "User-Agent",
                "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+"
            )
            .build()
            .getAsObject(
                ReelsHighlightsMedia::class.java,
                object : ParsedRequestListener<ReelsHighlightsMedia> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(response: ReelsHighlightsMedia) {

                        try {
                            binding!!.recUserHighlightsList.visibility = View.VISIBLE
                            binding!!.progressLoadingBar.visibility = View.GONE
                            println("response1122334455_fulldetails_highlightsmm:   ${response.status}")

                            if (response.reelsMedia[0].items.size == 0) {
                                ShowToast(
                                    myselectedActivity!!,
                                    getString(R.string.nostoryfound)
                                )
                            }
                            listAllHighlightsOfUserAdapter = ListAllStoriesOfUserAdapter(
                                myselectedActivity!!,
                                response.reelsMedia[0].items
                            )
                            binding!!.recHighlightsStoriesList.visibility = View.VISIBLE

                            val gridLayoutManager = GridLayoutManager(myselectedActivity, 3)

                            binding!!.recHighlightsStoriesList.layoutManager = gridLayoutManager
                            binding!!.recHighlightsStoriesList.isNestedScrollingEnabled = true
                            binding!!.recHighlightsStoriesList.adapter =
                                listAllHighlightsOfUserAdapter
                            listAllHighlightsOfUserAdapter!!.notifyDataSetChanged()

                        } catch (e: java.lang.Exception) {
                            binding!!.recHighlightsStoriesList.visibility = View.GONE
                            e.printStackTrace()
                            binding!!.progressLoadingBar.visibility = View.GONE
                            ShowToast(myselectedActivity!!, getString(R.string.nostoryfound))
                        }
                    }

                    override fun onError(anError: ANError) {
                        anError.printStackTrace()
                        println("response1122334455_storyERROR:getFullDetailsOfClickedHighlights  " + anError.message)
                        binding!!.progressLoadingBar.visibility = View.GONE
                    }
                })


    }


    @Keep
    override fun onclickUserStoryListeItem(position: Int, modelUsrTray: ModelUsrTray?) {
        println("response1122ff334455:   $modelUsrTray$position")
        this.userIDInsta = modelUsrTray!!.user.pk.toString()
        callStoriesDetailApi(modelUsrTray.id.toString(), this.userIDInsta)
    }

    @Keep
    override fun onclickUserHighlightsListItem(
        position: Int,
        modelUsrTray: ModelHighlightsUsrTray?
    ) {
        println("response1122ff334455:   $modelUsrTray$position")
        callHighlightsDetailApi(modelUsrTray?.id.toString())
    }


    private fun hideKeyboard(activity: Activity) {
        try {
            val inputManager = activity
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocusedView = activity.currentFocus
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(
                    currentFocusedView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }
}